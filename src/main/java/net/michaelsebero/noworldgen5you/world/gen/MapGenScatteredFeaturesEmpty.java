package net.michaelsebero.noworldgen5you.world.gen;

import net.michaelsebero.noworldgen5you.WorldgenConfig;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;

public class MapGenScatteredFeaturesEmpty extends MapGenScatteredFeature {
   public boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
      if (this.world != null && this.world.getBiomeProvider() != null) {
         Biome biome = this.world.getBiomeProvider().getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));
         if (biome == null) {
            return super.canSpawnStructureAtCoords(chunkX, chunkZ);
         } else if (WorldgenConfig.isScateredStructureDisabled(MapGenScatteredFeaturesEmpty.Type.WITCH_HUT.name().toLowerCase()) && biome == Biomes.SWAMPLAND) {
            return false;
         } else if (WorldgenConfig.isScateredStructureDisabled(MapGenScatteredFeaturesEmpty.Type.DESERT_PYRAMID.name().toLowerCase()) && (biome == Biomes.DESERT || biome == Biomes.DESERT_HILLS)) {
            return false;
         } else if (!WorldgenConfig.isScateredStructureDisabled(MapGenScatteredFeaturesEmpty.Type.IGLOO.name().toLowerCase()) || biome != Biomes.ICE_PLAINS && biome != Biomes.COLD_TAIGA) {
            return !WorldgenConfig.isScateredStructureDisabled(MapGenScatteredFeaturesEmpty.Type.JUNGLE_TEMPLE.name().toLowerCase()) || biome != Biomes.JUNGLE && biome != Biomes.JUNGLE_HILLS ? super.canSpawnStructureAtCoords(chunkX, chunkZ) : false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static enum Type {
      WITCH_HUT,
      IGLOO,
      DESERT_PYRAMID,
      JUNGLE_TEMPLE;
   }
}
