package com.sk89q.worldedit.fabric;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.world.AbstractWorld;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.registry.WorldData;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;
import java.util.List;

public class FabricWorld extends AbstractWorld {

    private final WeakReference<World> worldRef;

    public FabricWorld(World world) {
        this.worldRef = new WeakReference<>(world);
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
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBlockLightLevel(Vector position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean clearContainerBlockContents(Vector position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dropItem(Vector position, BaseItemStack item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean regenerate(Region region, EditSession editSession) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean generateTree(TreeGenerator.TreeType type, EditSession editSession, Vector position) throws MaxChangedBlocksException {
        throw new UnsupportedOperationException();
    }

    @Override
    public WorldData getWorldData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Entity> getEntities(Region region) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Entity> getEntities() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entity createEntity(Location location, BaseEntity entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BaseBlock getBlock(Vector position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BaseBlock getLazyBlock(Vector position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BaseBiome getBiome(Vector2D position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setBiome(Vector2D position, BaseBiome biome) {
        throw new UnsupportedOperationException();
    }

}
