package com.sk89q.worldedit.fabric;

import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.*;
import com.sk89q.worldedit.util.command.Dispatcher;
import com.sk89q.worldedit.world.World;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FabricPlatform extends AbstractPlatform implements MultiUserPlatform {

    private final FabricWorldEdit mod;

    FabricPlatform(FabricWorldEdit mod) {
        this.mod = mod;
    }

    @Override
    public Collection<Actor> getConnectedUsers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int resolveItem(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isValidMobType(String type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Player matchPlayer(Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public World matchWorld(World world) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerCommands(Dispatcher dispatcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerGameHooks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalConfiguration getConfiguration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public String getPlatformVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Capability, Preference> getCapabilities() {
        throw new UnsupportedOperationException();
    }

}
