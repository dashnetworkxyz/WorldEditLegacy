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

package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.biome.BiomeData;
import com.sk89q.worldedit.world.registry.BiomeRegistry;

import java.util.ArrayList;
import java.util.List;
//#if MC>10809
import net.minecraft.world.biome.Biome;
//#else
//$$import com.google.common.collect.HashBiMap;
//$$import net.minecraft.world.biome.BiomeGenBase;
//$$import java.util.Collections;
//$$import java.util.HashMap;
//$$import java.util.Map;
//#endif

/**
 * Provides access to biome data in Forge.
 */
class ForgeBiomeRegistry implements BiomeRegistry {

    //#if MC<=10809
    //$$private static Map<Integer, BiomeGenBase> biomes = Collections.emptyMap();
    //$$private static Map<Integer, BiomeData> biomeData = Collections.emptyMap();
    //#endif

    @Override
    public BaseBiome createFromId(int id) {
        return new BaseBiome(id);
    }

    @Override
    public List<BaseBiome> getBiomes() {
        List<BaseBiome> list = new ArrayList<>();

        //#if MC>10809
        for (Biome biome : Biome.REGISTRY) {
            list.add(new BaseBiome(Biome.getIdForBiome(biome)));
        //#else
        //$$for (int biome : biomes.keySet()) {
        //$$    list.add(new BaseBiome(biome));
        //#endif
        }

        return list;
    }

    @Override
    public BiomeData getData(BaseBiome biome) {
        //#if MC>10809
        return new ForgeBiomeData(Biome.getBiome(biome.getId()));
        //#else
        //$$return biomeData.get(biome.getId());
        //#endif
    }

    //#if MC<=10809
    /**
     * Populate the internal static list of biomes.
     *
     * <p>If called repeatedly, the last call will overwrite all previous
     * calls.</p>
     */
    static void populate() {
    //$$    Map<Integer, BiomeGenBase> biomes = HashBiMap.create();
    //$$    Map<Integer, BiomeData> biomeData = new HashMap<>();

    //$$    for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
    //$$        if ((biome == null) || (biomes.containsValue(biome))) {
    //$$            continue;
    //$$        }

    //$$        biomes.put(biome.biomeID, biome);
    //$$        biomeData.put(biome.biomeID, new ForgeBiomeData(biome));
    //$$    }

    //$$    ForgeBiomeRegistry.biomes = biomes;
    //$$    ForgeBiomeRegistry.biomeData = biomeData;
    }
    //#endif

    /**
     * Cached biome data information.
     */
    private static class ForgeBiomeData implements BiomeData {

        //#if MC>10809
        private final Biome biome;
        //#else
        //$$private final BiomeGenBase biome;
        //#endif

        /**
         * Create a new instance.
         *
         * @param biome the base biome
         */
        //#if MC>10809
        private ForgeBiomeData(Biome biome) {
        //#else
        //$$private ForgeBiomeData(BiomeGenBase biome) {
        //#endif
            this.biome = biome;
        }

        @Override
        public String getName() {
            return biome.biomeName;
        }
    }

}