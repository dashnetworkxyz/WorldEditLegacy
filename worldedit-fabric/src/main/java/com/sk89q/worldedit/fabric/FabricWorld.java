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

package com.sk89q.worldedit.fabric;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.AbstractWorld;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.InventoryBlockEntity;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

import java.lang.ref.WeakReference;

import static com.google.common.base.Preconditions.checkNotNull;

public class FabricWorld extends AbstractWorld {

    private final WeakReference<World> worldRef;

    FabricWorld(World world) {
        checkNotNull(world);
        worldRef = new WeakReference<>(world);
    }

    /**
     * Get the underlying handle to the world.
     *
     * @return the world
     * @throws WorldEditException thrown if a reference to the world was lost (i.e. world was unloaded)
     */
    public World getWorldChecked() throws WorldEditException {
        World world = worldRef.get();

        if (world != null) {
            return world;
        } else {
            throw new WorldReferenceLostException("The reference to the world was lost (i.e. the world may have been unloaded)");
        }
    }

    /**
     * Get the underlying handle to the world.
     *
     * @return the world
     * @throws RuntimeException thrown if a reference to the world was lost (i.e. world was unloaded)
     */
    public World getWorld() {
        World world = worldRef.get();

        if (world != null) {
            return world;
        } else {
            throw new RuntimeException("The reference to the world was lost (i.e. the world may have been unloaded)");
        }
    }

    @Override
    public String getName() {
        return getWorld().getData().getName();
    }

    @Override
    public int getMaxY() {
        return getWorld().getHeight() - 1;
    }

    @Override
    public boolean setBlock(Vector position, BaseBlock block, boolean notifyAndLight) throws WorldEditException {
        checkNotNull(position);
        checkNotNull(block);

        World world = getWorldChecked();
        int x = position.getBlockX();
        int y = position.getBlockY();
        int z = position.getBlockZ();

        // First set the block
        WorldChunk chunk = world.getChunkAt(x >> 4, z >> 4);
        BlockPos pos = new BlockPos(x, y, z);
        BlockState old = chunk.getBlockState(pos);
        BlockState newState = Block.byRawId(block.getId()).getStateFromMetadata(block.getData());
        BlockState successState = chunk.setBlockState(pos, newState);
        boolean successful = successState != null || old == newState;

        // Create the TileEntity
        if (successful) {
            if (block.hasNbtData()) {
                // Kill the old TileEntity
                world.removeBlockEntity(pos);
                NbtCompound nativeTag = NbtConverter.toNative(block.getNbtData());
                nativeTag.putString("id", block.getNbtId());
                TileEntityUtils.setTileEntity(world, position, nativeTag);
            }
        }

        if (notifyAndLight) {
            if (!successful) {
                newState = old;
            }

            world.checkLight(pos);

            if (!world.isClient) {
                Block stateBlock = newState.getBlock();

                if (chunk.isPopulated()) {
                    world.onBlockChanged(pos, stateBlock);
                }

                if (stateBlock.hasAnalogOutput()) {
                    world.updateComparators(pos, stateBlock);
                }
            }
        }

        return successful;
    }

    @Override
    public int getBlockLightLevel(Vector position) {
        checkNotNull(position);
        return getWorld().getLight(new BlockPos(position.getBlockX(), position.getBlockY(), position.getBlockZ()));
    }

    @Override
    public boolean clearContainerBlockContents(Vector position) {
        checkNotNull(position);
        BlockEntity tile = getWorld().getBlockEntity(new BlockPos(position.getBlockX(), position.getBlockY(), position.getBlockZ()));

        if (tile instanceof InventoryBlockEntity) {
            ((InventoryBlockEntity) tile).clear();
            return true;
        }

        return false;
    }

    /**
     * Thrown when the reference to the world is lost.
     */
    private static class WorldReferenceLostException extends WorldEditException {
        private WorldReferenceLostException(String message) {
            super(message);
        }
    }

}
