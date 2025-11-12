package net.michaelsebero.noworldgen5you.world.gen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.structure.MapGenEndCity;

public class MapGenEndCityEmpty extends MapGenEndCity {
   public MapGenEndCityEmpty() {
      super((ChunkGeneratorEnd)null);
   }

   public void func_186125_a(World worldIn, int x, int z, ChunkPrimer primer) {
   }

   public boolean func_75047_a(int chunkX, int chunkZ) {
      return false;
   }
}
