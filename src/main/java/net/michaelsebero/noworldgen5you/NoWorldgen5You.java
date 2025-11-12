package net.michaelsebero.noworldgen5you;

import net.michaelsebero.noworldgen5you.world.gen.MapGenCaveEmpty;
import net.michaelsebero.noworldgen5you.world.gen.MapGenEmpty;
import net.michaelsebero.noworldgen5you.world.gen.MapGenEndCityEmpty;
import net.michaelsebero.noworldgen5you.world.gen.MapGenFortressEmpty;
import net.michaelsebero.noworldgen5you.world.gen.MapGenMineshaftEmpty;
import net.michaelsebero.noworldgen5you.world.gen.MapGenOceanMonumentEmpty;
import net.michaelsebero.noworldgen5you.world.gen.MapGenScatteredFeaturesEmpty;
import net.michaelsebero.noworldgen5you.world.gen.MapGenStrongholdEmpty;
import net.michaelsebero.noworldgen5you.world.gen.MapGenVillageEmpty;
import net.michaelsebero.noworldgen5you.world.gen.MapGenWoodlandMansion;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
   modid = "noworldgen5you",
   name = "No Worldgen 5 You",
   version = "1.0.0",
   certificateFingerprint = "@FINGERPRINT@",
   acceptedMinecraftVersions = "[1.12.2]"
)
public class NoWorldgen5You {
   private static final Logger LOG = LogManager.getLogger("No Worldgen 5 You");
   private static MapGenScatteredFeaturesEmpty SCATTERED_GEN;
   private static int lastDimensionLoaded = Integer.MIN_VALUE;
   private static int dimensionLoadCounter = 0;
   private static boolean firstCaveGenOfWorld = true;

   @EventHandler
   public void onPreInit(FMLPreInitializationEvent event) {
      WorldgenConfig.initConfig(event.getSuggestedConfigurationFile());
      MinecraftForge.TERRAIN_GEN_BUS.register(this);
      MinecraftForge.EVENT_BUS.register(this);
      SCATTERED_GEN = new MapGenScatteredFeaturesEmpty();
   }

   @SubscribeEvent
   public void onWorldLoad(Load loadEvent) {
      if (!loadEvent.getWorld().isRemote) {
         int dimension = loadEvent.getWorld().provider.getDimension();
         
         // When dimension 0 loads, mark that the next cave gen should reset the counter
         if (dimension == 0) {
            firstCaveGenOfWorld = true;
            LOG.info("Overworld loading - Next cave generation will reset counter");
         }
         
         lastDimensionLoaded = dimension;
         LOG.info("World loaded - Dimension ID: {}, Name: {}", lastDimensionLoaded, loadEvent.getWorld().provider.getDimensionType().getName());
      }
   }

   @SubscribeEvent
   public void onChunkPopulated(Populate event) {
      if (WorldgenConfig.isPopulateDisabled(event.getType().name().toLowerCase())) {
         event.setResult(Result.DENY);
      }
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public void onMapGen(InitMapGenEvent event) {
      LOG.info("InitMapGenEvent fired - Type: {}, Original: {}, NewGen: {}", event.getType(), event.getOriginalGen().getClass().getName(), event.getNewGen().getClass().getName());
      if (WorldgenConfig.isStructureDisabled(event.getType().name().toLowerCase())) {
         LOG.info("Replacing {} generator", event.getType());
         switch(event.getType()) {
         case CAVE:
            boolean isVanillaCaves = event.getNewGen() instanceof MapGenCaves;
            String packageName = event.getNewGen().getClass().getPackage().getName();
            boolean isMinecraftPackage = packageName.startsWith("net.minecraft");
            boolean isOurEmpty = event.getNewGen() instanceof MapGenCaveEmpty;
            boolean isBetterCaves = packageName.contains("bettercaves");
            
            // Reset counter on first cave gen after dimension 0 load
            if (firstCaveGenOfWorld) {
               dimensionLoadCounter = 0;
               firstCaveGenOfWorld = false;
               LOG.info("First cave generation after overworld load - Resetting dimension load counter");
            }
            
            ++dimensionLoadCounter;
            
            if (isVanillaCaves && isMinecraftPackage && !isOurEmpty) {
               LOG.info("Vanilla caves detected, replacing with empty generator");
               event.setNewGen(new MapGenCaveEmpty());
            } else if (isBetterCaves) {
               if (dimensionLoadCounter <= 3) {
                  LOG.info("Better Caves in primary dimensions, keeping it (load count: {})", dimensionLoadCounter);
               } else {
                  LOG.info("Better Caves in OTG secondary dimension, disabling (load count: {})", dimensionLoadCounter);
                  event.setNewGen(new MapGenCaveEmpty());
               }
            } else if (isOurEmpty) {
               LOG.info("Already using empty cave generator");
            } else {
               LOG.info("Other cave generator detected ({}), leaving it alone", event.getNewGen().getClass().getName());
            }
            break;
         case CUSTOM:
            LOG.info("Attempting to replace {} with an empty map generator. If this causes a ClassCastException report it to the author of that mod and not the author of NoWorldgen5You");
            event.setNewGen(new MapGenEmpty());
            break;
         case MINESHAFT:
            event.setNewGen(new MapGenMineshaftEmpty());
            break;
         case NETHER_BRIDGE:
            event.setNewGen(new MapGenFortressEmpty());
            break;
         case NETHER_CAVE:
            event.setNewGen(new MapGenEmpty());
            break;
         case OCEAN_MONUMENT:
            event.setNewGen(new MapGenOceanMonumentEmpty());
            break;
         case RAVINE:
            event.setNewGen(new MapGenEmpty());
            break;
         case SCATTERED_FEATURE:
            event.setNewGen(SCATTERED_GEN);
            break;
         case STRONGHOLD:
            event.setNewGen(new MapGenStrongholdEmpty());
            break;
         case VILLAGE:
            event.setNewGen(new MapGenVillageEmpty());
            break;
         case END_CITY:
            event.setNewGen(new MapGenEndCityEmpty());
            break;
         case WOODLAND_MANSION:
            event.setNewGen(new MapGenWoodlandMansion());
         }
      }
   }

   @EventHandler
   public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
      LOG.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
   }
}
