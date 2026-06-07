/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.session;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.transform.BlockTransformExtent;
import com.sk89q.worldedit.function.mask.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.registry.WorldData;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an operation to paste the contents of a clipboard.
 */
public class PasteBuilder {

    private final Clipboard clipboard;
    private final Transform transform;
    private final Extent targetExtent;
    private final WorldData targetWorldData;

    private Mask sourceMask = Masks.alwaysTrue();

    private Vector to = Vector.ZERO;
    private boolean ignoreAirBlocks;
    private boolean ignoreStructureVoidBlocks;
    private boolean copyEntities = true;
    private boolean copyBiomes;
    private Region copyRegion;

    /**
     * Create a new instance.
     *
     * @param holder the clipboard holder
     * @param targetExtent an extent
     * @param targetWorldData world data of the target
     */
    PasteBuilder(ClipboardHolder holder, Extent targetExtent, WorldData targetWorldData) {
        checkNotNull(holder);
        checkNotNull(targetExtent);
        checkNotNull(targetWorldData);
        this.clipboard = holder.getClipboard();
        this.transform = holder.getTransform();
        this.targetExtent = targetExtent;
        this.targetWorldData = targetWorldData;
        this.copyRegion = this.clipboard.getRegion();
    }

    /**
     * Set the target location.
     *
     * @param to the target location
     * @return this builder instance
     */
    public PasteBuilder to(Vector to) {
        this.to = to;
        return this;
    }

    /**
     * Set a custom mask of blocks to ignore from the source.
     * This provides a more flexible alternative to {@link #ignoreAirBlocks(boolean)}, for example
     * one might want to ignore structure void if copying a Minecraft Structure, etc.
     *
     * @param sourceMask the mask for the source
     * @return this builder instance
     */
    public PasteBuilder maskSource(Mask sourceMask) {
        this.sourceMask = sourceMask == null ? Masks.alwaysTrue() : sourceMask;
        return this;
    }

    /**
     * Set whether air blocks in the source are skipped over when pasting.
     *
     * @return this builder instance
     */
    public PasteBuilder ignoreAirBlocks(boolean ignoreAirBlocks) {
        this.ignoreAirBlocks = ignoreAirBlocks;
        return this;
    }

    /**
     * Set whether structure void blocks in the source are skipped over when pasting.
     *
     * @param ignoreStructureVoidBlocks value to set it to
     * @return This builder instance
     */
    public PasteBuilder ignoreStructureVoidBlocks(boolean ignoreStructureVoidBlocks) {
        this.ignoreStructureVoidBlocks = ignoreStructureVoidBlocks;
        return this;
    }

    /**
     * Set whether the copy should include source entities.
     * Note that this is true by default for legacy reasons.
     *
     * @param copyEntities if entities should be copied
     * @return this builder instance
     */
    public PasteBuilder copyEntities(boolean copyEntities) {
        this.copyEntities = copyEntities;
        return this;
    }

    /**
     * Set whether the copy should include source biomes (if available).
     *
     * @param copyBiomes if biomes should be copied
     * @return this builder instance
     */
    public PasteBuilder copyBiomes(boolean copyBiomes) {
        this.copyBiomes = copyBiomes;
        return this;
    }

    /**
     * Set the region to copy from the clipboard. By default, this uses the region stored in the clipboard.
     *
     * @param copyRegion the region to copy from the clipboard
     * @return this builder instance
     */
    public PasteBuilder copyRegion(Region copyRegion) {
        this.copyRegion = copyRegion;
        return this;
    }

    /**
     * Build the operation.
     *
     * @return the operation
     */
    public Operation build() {
        BlockTransformExtent extent = new BlockTransformExtent(clipboard, transform, targetWorldData.getBlockRegistry());
        ForwardExtentCopy copy = new ForwardExtentCopy(extent, copyRegion, clipboard.getOrigin(), targetExtent, to);
        copy.setTransform(transform);

        Mask combinedMask = sourceMask;
        if (ignoreAirBlocks) {
            combinedMask = combinedMask == Masks.alwaysTrue() ? new ExistingBlockMask(clipboard)
                    : new MaskIntersection(combinedMask, new ExistingBlockMask(clipboard));
        }
        if (ignoreStructureVoidBlocks) {
            Mask structureVoidMask = Masks.negate(new BlockMask(clipboard, new BaseBlock(217)));
            combinedMask = combinedMask == Masks.alwaysTrue() ? structureVoidMask
                    : new MaskIntersection(combinedMask, structureVoidMask);
        }

        copy.setSourceMask(combinedMask);
        copy.setCopyingEntities(copyEntities);
        copy.setCopyingBiomes(copyBiomes && clipboard.hasBiomes());
        return copy;
    }

}
