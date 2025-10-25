package net.michaelsebero.noworldgen5you.world.gen;

import net.michaelsebero.noworldgen5you.WorldgenConfig;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;

public class MapGenScatteredFeaturesEmpty extends MapGenScatteredFeature {

    public enum Type {
        WITCH_HUT,
        IGLOO,
        DESERT_PYRAMID,
        JUNGLE_TEMPLE;
    }

    @Override
    public boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        // Safety check
        if (this.world == null || this.world.getBiomeProvider() == null) {
            return false;
        }

        final Biome biome = this.world.getBiomeProvider().getBiome(
            new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));

        // Check if biome is null (can happen in some modded dimensions)
        if (biome == null) {
            return super.canSpawnStructureAtCoords(chunkX, chunkZ);
        }

        // Witch hut check
        if (WorldgenConfig.isScateredStructureDisabled(Type.WITCH_HUT.name().toLowerCase()) 
            && biome == Biomes.SWAMPLAND) {
            return false;
        }

        // Desert pyramid check
        if (WorldgenConfig.isScateredStructureDisabled(Type.DESERT_PYRAMID.name().toLowerCase()) 
            && (biome == Biomes.DESERT || biome == Biomes.DESERT_HILLS)) {
            return false;
        }

        // Igloo check
        if (WorldgenConfig.isScateredStructureDisabled(Type.IGLOO.name().toLowerCase()) 
            && (biome == Biomes.ICE_PLAINS || biome == Biomes.COLD_TAIGA)) {
            return false;
        }

        // Jungle temple check
        if (WorldgenConfig.isScateredStructureDisabled(Type.JUNGLE_TEMPLE.name().toLowerCase()) 
            && (biome == Biomes.JUNGLE || biome == Biomes.JUNGLE_HILLS)) {
            return false;
        }

        return super.canSpawnStructureAtCoords(chunkX, chunkZ);
    }
}
