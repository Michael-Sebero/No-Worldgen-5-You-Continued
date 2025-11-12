package net.michaelsebero.noworldgen5you;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.michaelsebero.noworldgen5you.world.gen.MapGenScatteredFeaturesEmpty;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;

public class WorldgenConfig {
   private static Configuration config;
   public static final Map<String, Boolean> settings = new HashMap();

   public static Configuration initConfig(File file) {
      config = new Configuration(file);
      config.setCategoryComment("map_structures", "Allows for various types of map generators to be disabled.");
      EventType[] var1 = EventType.values();
      int var2 = var1.length;

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         EventType type = var1[var3];
         if (type != EventType.CUSTOM) {
            isStructureDisabled(type.name().toLowerCase());
         }
      }

      config.setCategoryComment("scattered_structures", "This category requires the scattered map generator from the map_structures category to be disabled.");
      MapGenScatteredFeaturesEmpty.Type[] var5 = MapGenScatteredFeaturesEmpty.Type.values();
      var2 = var5.length;

      for(var3 = 0; var3 < var2; ++var3) {
         MapGenScatteredFeaturesEmpty.Type type = var5[var3];
         isScateredStructureDisabled(type.name().toLowerCase());
      }

      config.setCategoryComment("map_populates", "Allows for various types of chunk populators to be disabled.");
      net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType[] var6 = net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.values();
      var2 = var6.length;

      for(var3 = 0; var3 < var2; ++var3) {
         net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType type = var6[var3];
         if (type != net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.CUSTOM) {
            isPopulateDisabled(type.name().toLowerCase());
         }
      }

      syncConfigData();
      return config;
   }

   public static void syncConfigData() {
      if (config.hasChanged()) {
         config.save();
      }

   }

   public static boolean isStructureDisabled(String type) {
      boolean result = config.getBoolean("disable_" + type, "map_structures", false, "Should " + type + " generation be disabled?");
      return result;
   }

   public static boolean isScateredStructureDisabled(String type) {
      boolean result = config.getBoolean("disable_" + type, "scattered_structures", false, "Should " + type + " generation be disabled?");
      return result;
   }

   public static boolean isPopulateDisabled(String type) {
      boolean result = config.getBoolean("disable_" + type, "map_populates", false, "Should " + type + " generation be disabled?");
      return result;
   }
}
