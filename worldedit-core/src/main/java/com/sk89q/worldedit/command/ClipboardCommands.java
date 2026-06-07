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

package com.sk89q.worldedit.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.Logging;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.block.BlockReplace;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.internal.annotation.Direction;
import com.sk89q.worldedit.internal.annotation.Selection;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.regions.selector.ExtendingCuboidRegionSelector;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.command.binding.Switch;
import com.sk89q.worldedit.util.command.parametric.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.sk89q.minecraft.util.commands.Logging.LogMode.PLACEMENT;
import static com.sk89q.minecraft.util.commands.Logging.LogMode.REGION;

/**
 * Clipboard commands.
 */
public class ClipboardCommands {

    private final WorldEdit worldEdit;

    /**
     * Create a new instance.
     *
     * @param worldEdit reference to WorldEdit
     */
    public ClipboardCommands(WorldEdit worldEdit) {
        checkNotNull(worldEdit);
        this.worldEdit = worldEdit;
    }

    @Command(
        aliases = { "/copy" },
        flags = "ebm",
        desc = "Copy the selection to the clipboard",
        help = "Copy the selection to the clipboard\n" +
                "Flags:\n" +
                "  -e will also copy entities\n" +
                "  -b will also copy biomes\n" +
                "  -m sets a source mask so that excluded blocks become air\n" +
                "WARNING: Pasting entities cannot yet be undone!",
        max = 0
    )
    @CommandPermissions("worldedit.clipboard.copy")
    public void copy(Player player, LocalSession session, EditSession editSession,
                     @Selection Region region, @Switch('e') boolean copyEntities,
                     @Switch('b') boolean copyBiomes, @Switch('m') Mask mask) throws WorldEditException {

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        clipboard.setOrigin(session.getPlacementPosition(player));
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
        copy.setCopyingEntities(copyEntities);
        copy.setCopyingBiomes(copyBiomes);
        if (mask != null) {
            copy.setSourceMask(mask);
        }
        Operations.completeLegacy(copy);
        session.setClipboard(new ClipboardHolder(clipboard, editSession.getWorld().getWorldData()));

        player.print(copy.getAffectedBlocks() + " block(s) were copied.");
        if (copy.getAffectedBiomeCols() > 0) {
            player.print(copy.getAffectedBiomeCols() + " biomes(s) were copied.");
        }
        if (copy.getAffectedEntities() > 0) {
            player.print(copy.getAffectedEntities() + " entities(s) were copied.");
        }
    }

    @Command(
        aliases = { "/cut" },
        flags = "ebm",
        usage = "[leave-id]",
        desc = "Cut the selection to the clipboard",
        help = "Copy the selection to the clipboard\n" +
                "Flags:\n" +
                "  -e will also cut entities\n" +
                "  -b will also copy biomes, source biomes are unaffected\n" +
                "  -m sets a source mask so that excluded blocks become air\n" +
                "WARNING: Cutting / pasting entities & biomes cannot yet be undone!",
        max = 1
    )
    @CommandPermissions("worldedit.clipboard.cut")
    @Logging(REGION)
    public void cut(Player player, LocalSession session, EditSession editSession,
                    @Selection Region region,
                    @Optional("air") Pattern leavePattern,
                    @Switch('e') boolean copyEntities,
                    @Switch('b') boolean copyBiomes,
                    @Switch('m') Mask mask) throws WorldEditException {

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        clipboard.setOrigin(session.getPlacementPosition(player));
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
        copy.setSourceFunction(new BlockReplace(editSession, leavePattern));
        copy.setCopyingEntities(copyEntities);
        copy.setCopyingBiomes(copyBiomes);
        copy.setRemovingEntities(true);
        if (mask != null) {
            copy.setSourceMask(mask);
        }
        Operations.completeLegacy(copy);
        session.setClipboard(new ClipboardHolder(clipboard, editSession.getWorld().getWorldData()));

        player.print(copy.getAffectedBlocks() + " block(s) were cut.");
        if (copy.getAffectedBiomeCols() > 0) {
            player.print(copy.getAffectedBiomeCols() + " biomes(s) were cut.");
        }
        if (copy.getAffectedEntities() > 0) {
            player.print(copy.getAffectedEntities() + " entities(s) were cut.");
        }
    }

    @Command(
        aliases = { "/paste" },
        flags = "avosn",
        desc = "Paste the clipboard's contents",
        help =
            "Pastes the clipboard's contents.\n" +
            "Flags:\n" +
            "  -a skips air blocks\n" +
            "  -v include structure void blocks\n" +
            "  -o pastes at the original position\n" +
            "  -s selects the region after pasting\n" +
            "  -n no paste, select only. (implies -s)",
        max = 0
    )
    @CommandPermissions("worldedit.clipboard.paste")
    @Logging(PLACEMENT)
    public void paste(Player player, LocalSession session, EditSession editSession,
                      @Switch('a') boolean ignoreAirBlocks,
                      @Switch('v') boolean pasteStructureVoid,
                      @Switch('o') boolean atOrigin,
                      @Switch('s') boolean selectPasted,
                      @Switch('n') boolean onlySelect,
                      @Switch('e') boolean pasteEntities,
                      @Switch('b') boolean pasteBiomes,
                      @Switch('m') Mask sourceMask) throws WorldEditException {

        ClipboardHolder holder = session.getClipboard();
        Clipboard clipboard = holder.getClipboard();
        Region region = clipboard.getRegion();

        Vector to = atOrigin ? clipboard.getOrigin() : session.getPlacementPosition(player);
        if (!onlySelect) {
            Operation operation = holder
                    .createPaste(editSession, editSession.getWorld().getWorldData())
                    .to(to)
                    .ignoreAirBlocks(ignoreAirBlocks)
                    .ignoreStructureVoidBlocks(!pasteStructureVoid)
                    .copyBiomes(pasteBiomes)
                    .copyEntities(pasteEntities)
                    .maskSource(sourceMask)
                    .build();
            Operations.completeLegacy(operation);
        }

        if (selectPasted || onlySelect) {
            Vector clipboardOffset = clipboard.getRegion().getMinimumPoint().subtract(clipboard.getOrigin());
            Vector realTo = to.add(holder.getTransform().apply(clipboardOffset));
            Vector max = realTo.add(holder.getTransform().apply(region.getMaximumPoint().subtract(region.getMinimumPoint())));
            RegionSelector selector;
            if (session.getRegionSelector(player.getWorld()) instanceof ExtendingCuboidRegionSelector) {
                selector = new ExtendingCuboidRegionSelector(player.getWorld(), realTo, max);
            } else {
                selector = new CuboidRegionSelector(player.getWorld(), realTo, max);
            }
            session.setRegionSelector(player.getWorld(), selector);
            selector.learnChanges();
            selector.explainRegionAdjust(player, session);
        }

        if (onlySelect) {
            player.print("Selected clipboard paste region.");
        } else {
            player.print("The clipboard has been pasted at " + to + ".");
        }
    }

    @Command(
        aliases = { "/rotate" },
        usage = "<y-axis> [<x-axis>] [<z-axis>]",
        desc = "Rotate the contents of the clipboard",
        help = "Non-destructively rotate the contents of the clipboard.\n" +
               "Angles are provided in degrees and a positive angle will result in a clockwise rotation. " +
               "Multiple rotations can be stacked. Interpolation is not performed so angles should be a multiple of 90 degrees.\n"
    )
    @CommandPermissions("worldedit.clipboard.rotate")
    public void rotate(Player player, LocalSession session, Double yRotate, @Optional Double xRotate, @Optional Double zRotate) throws WorldEditException {
        if ((yRotate != null && Math.abs(yRotate % 90) > 0.001) ||
                xRotate != null && Math.abs(xRotate % 90) > 0.001 ||
                zRotate != null && Math.abs(zRotate % 90) > 0.001) {
            player.printDebug("Note: Interpolation is not supported, so angles that are multiples of 90 is recommended.");
        }

        ClipboardHolder holder = session.getClipboard();
        AffineTransform transform = new AffineTransform();
        transform = transform.rotateY(-(yRotate != null ? yRotate : 0));
        transform = transform.rotateX(-(xRotate != null ? xRotate : 0));
        transform = transform.rotateZ(-(zRotate != null ? zRotate : 0));
        holder.setTransform(holder.getTransform().combine(transform));
        player.print("The clipboard copy has been rotated.");
    }

    @Command(
        aliases = { "/flip" },
        usage = "[<direction>]",
        desc = "Flip the contents of the clipboard",
        help =
            "Flips the contents of the clipboard across the point from which the copy was made.\n",
        max = 1
    )
    @CommandPermissions("worldedit.clipboard.flip")
    public void flip(Player player, LocalSession session, EditSession editSession,
                     @Optional(Direction.AIM) @Direction Vector direction) throws WorldEditException {
        ClipboardHolder holder = session.getClipboard();
        AffineTransform transform = new AffineTransform();
        transform = transform.scale(direction.positive().multiply(-2).add(1, 1, 1));
        holder.setTransform(holder.getTransform().combine(transform));
        player.print("The clipboard copy has been flipped.");
    }

    @Command(
        aliases = { "clearclipboard" },
        desc = "Clear your clipboard",
        max = 0
    )
    @CommandPermissions("worldedit.clipboard.clear")
    public void clearClipboard(Player player, LocalSession session, EditSession editSession) throws WorldEditException {
        session.setClipboard(null);
        player.print("Clipboard cleared.");
    }
}
