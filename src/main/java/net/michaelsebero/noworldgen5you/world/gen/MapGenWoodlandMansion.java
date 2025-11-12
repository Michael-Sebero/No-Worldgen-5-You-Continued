package net.michaelsebero.noworldgen5you.world.gen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.structure.WoodlandMansion;

public class MapGenWoodlandMansion extends WoodlandMansion {
   public MapGenWoodlandMansion() {
      super((ChunkGeneratorOverworld)null);
   }

   public void func_186125_a(World worldIn, int x, int z, ChunkPrimer primer) {
   }

   public boolean func_75047_a(int chunkX, int chunkZ) {
      return false;
   }
}
