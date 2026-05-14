package com.sk89q.worldedit.fabric;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.extension.platform.AbstractPlayerActor;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.session.SessionKey;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;

import java.util.UUID;

public class FabricPlayer extends AbstractPlayerActor {

    @Override
    public World getWorld() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getItemInHand() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void giveItem(int type, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BlockBag getInventoryBlockBag() {
        throw new UnsupportedOperationException();
    }

    @Override
    public WorldVector getPosition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getPitch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getYaw() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPosition(Vector pos, float pitch, float yaw) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BaseEntity getState() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Location getLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printRaw(String msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printDebug(String msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(String msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printError(String msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SessionKey getSessionKey() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getFacet(Class<? extends T> cls) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UUID getUniqueId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getGroups() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPermission(String permission) {
        throw new UnsupportedOperationException();
    }

}
