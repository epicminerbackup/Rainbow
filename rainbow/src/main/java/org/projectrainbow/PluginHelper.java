package org.projectrainbow;

import PluginReference.*;
import com.google.common.base.MoreObjects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameType;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class PluginHelper {
    public static BiMap<EnumFacing, MC_DirectionNESWUD> directionMap = HashBiMap.create();
    public static BiMap<MC_GameRuleType, String> gameRuleMap = HashBiMap.create();
    public static BiMap<GameType, MC_GameMode> gamemodeMap = HashBiMap.create();
    public static BiMap<Class<? extends Entity>, MC_EntityType> entityMap = HashBiMap.create();
    public static BiMap<Potion, MC_PotionEffectType> potionMap = HashBiMap.create();
    public static BiMap<Enchantment, MC_EnchantmentType> enchantmentMap = HashBiMap.create();
    public static BiMap<Biome, MC_WorldBiomeType> biomeMap = HashBiMap.create();
    public static BiMap<EnumHand, MC_Hand> handMap = HashBiMap.create();
    public static BiMap<MC_AttributeType, IAttribute> attributeMap = HashBiMap.create();
    public static BiMap<MC_AttributeModifier.Operator, Integer> operatorMap = HashBiMap.create();
    public static BiMap<Item, Integer> legacyItemIdMap = HashBiMap.create();

    public static MC_EntityType getEntityType(Class<? extends Entity> clazz) {
        if (EntityPlayer.class.isAssignableFrom(clazz)) {
            return MC_EntityType.PLAYER;
        } else {
            return MoreObjects.firstNonNull(entityMap.get(clazz), MC_EntityType.UNSPECIFIED);
        }
    }

    public static List<MC_ItemStack> copyInvList(List<ItemStack> items) {
        int size = items.size();
        List<MC_ItemStack> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ItemStack itemStack = items.get(i);
            list.add((MC_ItemStack) (Object) itemStack);
        }
        return list;
    }

    public static ItemStack getItemStack(MC_ItemStack itemStack) {
        return (ItemStack) (itemStack == null ? ItemStack.EMPTY : itemStack);
    }

    public static void updateInv(List<ItemStack> inv, List<MC_ItemStack> items) {
        int size = inv.size();
        for (int i = 0; i < size; i++) {
            inv.set(i, getItemStack(items.get(i)));
        }
    }

    public static MC_PotionEffect wrap(PotionEffect potionEffect) {
        MC_PotionEffectType type = potionMap.get(potionEffect.getPotion());
        type = MoreObjects.firstNonNull(type, MC_PotionEffectType.UNSPECIFIED);
        MC_PotionEffect mc_potionEffect = new MC_PotionEffect(type, potionEffect.getDuration(), potionEffect.getAmplifier());
        mc_potionEffect.ambient = potionEffect.getIsAmbient();
        mc_potionEffect.showsParticles = potionEffect.doesShowParticles();
        return mc_potionEffect;
    }

    public static PotionEffect unwrap(MC_PotionEffect potionEffect) {
        return new PotionEffect(potionMap.inverse().get(potionEffect.type), potionEffect.duration, potionEffect.amplifier, potionEffect.ambient, potionEffect.showsParticles);
    }

    static {
        directionMap.put(EnumFacing.DOWN, MC_DirectionNESWUD.DOWN);
        directionMap.put(EnumFacing.UP, MC_DirectionNESWUD.UP);
        directionMap.put(EnumFacing.NORTH, MC_DirectionNESWUD.NORTH);
        directionMap.put(EnumFacing.SOUTH, MC_DirectionNESWUD.SOUTH);
        directionMap.put(EnumFacing.WEST, MC_DirectionNESWUD.WEST);
        directionMap.put(EnumFacing.EAST, MC_DirectionNESWUD.EAST);

        gameRuleMap.put(MC_GameRuleType.DO_FIRE_TICK, "doFireTick");
        gameRuleMap.put(MC_GameRuleType.MOB_GRIEFING, "mobGriefing");
        gameRuleMap.put(MC_GameRuleType.KEEP_INVENTORY, "keepInventory");
        gameRuleMap.put(MC_GameRuleType.DO_MOB_SPAWNING, "doMobSpawning");
        gameRuleMap.put(MC_GameRuleType.DO_MOB_LOOT, "doMobLoot");
        gameRuleMap.put(MC_GameRuleType.DO_TILE_DROPS, "doTileDrops");
        gameRuleMap.put(MC_GameRuleType.DO_ENTITY_DROPS, "doEntityDrops");
        gameRuleMap.put(MC_GameRuleType.COMMAND_BLOCK_OUTPUT, "commandBlockOutput");
        gameRuleMap.put(MC_GameRuleType.NATURAL_REGENERATION, "naturalRegeneration");
        gameRuleMap.put(MC_GameRuleType.DO_DAYLIGHT_CYCLE, "doDaylightCycle");
        gameRuleMap.put(MC_GameRuleType.LOG_ADMIN_COMMANDS, "logAdminCommands");
        gameRuleMap.put(MC_GameRuleType.SHOW_DEATH_MESSAGES, "showDeathMessages");
        gameRuleMap.put(MC_GameRuleType.SEND_COMMAND_FEEDBACK, "sendCommandFeedback");
        gameRuleMap.put(MC_GameRuleType.REDUCED_DEBUG_INFO, "reducedDebugInfo");
        gameRuleMap.put(MC_GameRuleType.SPECTATORS_GENERATE_CHUNKS, "spectatorsGenerateChunks");
        gameRuleMap.put(MC_GameRuleType.DISABLE_ELYTRA_MOVEMENT_CHECK, "disableElytraMovementCheck");
        gameRuleMap.put(MC_GameRuleType.DO_WEATHER_CYCLE, "doWeatherCycle");
        gameRuleMap.put(MC_GameRuleType.DO_LIMITED_CRAFTING, "doLimitedCrafting");
        gameRuleMap.put(MC_GameRuleType.ANNOUNCE_ADVANCEMENTS, "announceAdvancements");

        gamemodeMap.put(GameType.NOT_SET, MC_GameMode.UNKNOWN);
        gamemodeMap.put(GameType.SURVIVAL, MC_GameMode.SURVIVAL);
        gamemodeMap.put(GameType.CREATIVE, MC_GameMode.CREATIVE);
        gamemodeMap.put(GameType.ADVENTURE, MC_GameMode.ADVENTURE);
        gamemodeMap.put(GameType.SPECTATOR, MC_GameMode.SPECTATOR);

        entityMap.put(EntityItem.class, MC_EntityType.ITEM);
        entityMap.put(EntityXPOrb.class, MC_EntityType.XP_ORB);
        entityMap.put(EntityAreaEffectCloud.class, MC_EntityType.AREA_EFFECT_CLOUD);
        entityMap.put(EntityEgg.class, MC_EntityType.THROWN_EGG);
        entityMap.put(EntityTippedArrow.class, MC_EntityType.ARROW);
        entityMap.put(EntitySnowball.class, MC_EntityType.SNOWBALL);
        entityMap.put(EntityLargeFireball.class, MC_EntityType.FIREBALL);
        entityMap.put(EntitySmallFireball.class, MC_EntityType.SMALL_FIREBALL);
        entityMap.put(EntityEnderPearl.class, MC_EntityType.THROWN_ENDERPEARL);
        entityMap.put(EntityEnderEye.class, MC_EntityType.EYE_OF_ENDER_SIGNAL);
        entityMap.put(EntityPotion.class, MC_EntityType.THROWN_POTION);
        entityMap.put(EntityExpBottle.class, MC_EntityType.THROWN_EXP_BOTTLE);
        entityMap.put(EntityWitherSkull.class, MC_EntityType.WITHER_SKULL);
        entityMap.put(EntityTNTPrimed.class, MC_EntityType.PRIMED_TNT);
        entityMap.put(EntityFallingBlock.class, MC_EntityType.FALLING_SAND);
        entityMap.put(EntityFireworkRocket.class, MC_EntityType.FIREWORK);
        entityMap.put(EntitySpectralArrow.class, MC_EntityType.SPECTRAL_ARROW);
        entityMap.put(EntityShulkerBullet.class, MC_EntityType.SHULKER_BULLET);
        entityMap.put(EntityDragonFireball.class, MC_EntityType.DRAGON_FIREBALL);
        entityMap.put(EntityArmorStand.class, MC_EntityType.ARMOR_STAND);
        entityMap.put(EntityBoat.class, MC_EntityType.BOAT);
        entityMap.put(EntityCreeper.class, MC_EntityType.CREEPER);
        entityMap.put(EntitySkeleton.class, MC_EntityType.SKELETON);
        entityMap.put(EntityWitherSkeleton.class, MC_EntityType.WHITHER_SKELETON);
        entityMap.put(EntityStray.class, MC_EntityType.STRAY);
        entityMap.put(EntitySpider.class, MC_EntityType.SPIDER);
        entityMap.put(EntityGiantZombie.class, MC_EntityType.GIANT);
        entityMap.put(EntityZombie.class, MC_EntityType.ZOMBIE);
        entityMap.put(EntityHusk.class, MC_EntityType.HUSK);
        entityMap.put(EntityZombieVillager.class, MC_EntityType.ZOMBIE_VILLAGER);
        entityMap.put(EntitySlime.class, MC_EntityType.SLIME);
        entityMap.put(EntityGhast.class, MC_EntityType.GHAST);
        entityMap.put(EntityPigZombie.class, MC_EntityType.PIG_ZOMBIE);
        entityMap.put(EntityEnderman.class, MC_EntityType.ENDERMAN);
        entityMap.put(EntityCaveSpider.class, MC_EntityType.CAVE_SPIDER);
        entityMap.put(EntitySilverfish.class, MC_EntityType.SILVERFISH);
        entityMap.put(EntityBlaze.class, MC_EntityType.BLAZE);
        entityMap.put(EntityMagmaCube.class, MC_EntityType.LAVA_SLIME);
        entityMap.put(EntityDragon.class, MC_EntityType.ENDERDRAGON);
        entityMap.put(EntityWither.class, MC_EntityType.WITHERBOSS);
        entityMap.put(EntityBat.class, MC_EntityType.BAT);
        entityMap.put(EntityWitch.class, MC_EntityType.WITCH);
        entityMap.put(EntityEndermite.class, MC_EntityType.ENDERMITE);
        entityMap.put(EntityGuardian.class, MC_EntityType.GUARDIAN);
        entityMap.put(EntityElderGuardian.class, MC_EntityType.ELDER_GUARDIAN);
        entityMap.put(EntityShulker.class, MC_EntityType.SHULKER);
        entityMap.put(EntityPig.class, MC_EntityType.PIG);
        entityMap.put(EntitySheep.class, MC_EntityType.SHEEP);
        entityMap.put(EntityCow.class, MC_EntityType.COW);
        entityMap.put(EntityChicken.class, MC_EntityType.CHICKEN);
        entityMap.put(EntitySquid.class, MC_EntityType.SQUID);
        entityMap.put(EntityWolf.class, MC_EntityType.WOLF);
        entityMap.put(EntityMooshroom.class, MC_EntityType.MUSHROOM_COW);
        entityMap.put(EntitySnowman.class, MC_EntityType.SNOWMAN);
        entityMap.put(EntityOcelot.class, MC_EntityType.OCELOT);
        entityMap.put(EntityIronGolem.class, MC_EntityType.VILLAGER_GOLEM);
        entityMap.put(EntityHorse.class, MC_EntityType.HORSE);
        entityMap.put(EntityZombieHorse.class, MC_EntityType.ZOMBIE_HORSE);
        entityMap.put(EntitySkeletonHorse.class, MC_EntityType.SKELETON_HORSE);
        entityMap.put(EntityDonkey.class, MC_EntityType.DONKEY);
        entityMap.put(EntityMule.class, MC_EntityType.MULE);
        entityMap.put(EntityLlama.class, MC_EntityType.LLAMA);
        entityMap.put(EntityRabbit.class, MC_EntityType.RABBIT);
        entityMap.put(EntityVillager.class, MC_EntityType.VILLAGER);
        entityMap.put(EntityEnderCrystal.class, MC_EntityType.ENDER_CRYSTAL);
        entityMap.put(EntityFishHook.class, MC_EntityType.FISHING_HOOK);
        entityMap.put(EntityItemFrame.class, MC_EntityType.ITEM_FRAME);
        entityMap.put(EntityPainting.class, MC_EntityType.PAINTING);
        entityMap.put(EntityLeashKnot.class, MC_EntityType.LEASH_KNOT);
        entityMap.put(EntityLightningBolt.class, MC_EntityType.LIGHTNING_BOLT);
        entityMap.put(EntityPolarBear.class, MC_EntityType.POLAR_BEAR);
        entityMap.put(EntityEvokerFangs.class, MC_EntityType.EVOCATION_FANGS);
        entityMap.put(EntityEvoker.class, MC_EntityType.EVOKER);
        entityMap.put(EntityVex.class, MC_EntityType.VEX);
        entityMap.put(EntityVindicator.class, MC_EntityType.VINDICATOR);
        entityMap.put(EntityMinecartEmpty.class, MC_EntityType.MINECART);
        entityMap.put(EntityMinecartChest.class, MC_EntityType.MINECART_CHEST);
        entityMap.put(EntityMinecartFurnace.class, MC_EntityType.MINECART_FURNACE);
        entityMap.put(EntityMinecartTNT.class, MC_EntityType.MINECART_TNT);
        entityMap.put(EntityMinecartHopper.class, MC_EntityType.MINECART_HOPPER);
        entityMap.put(EntityMinecartMobSpawner.class, MC_EntityType.MINECART_SPAWNER);
        entityMap.put(EntityParrot.class, MC_EntityType.PARROT);
        entityMap.put(EntityIllusionIllager.class, MC_EntityType.ILLUSIONER);
        entityMap.put(EntityCod.class, MC_EntityType.COD);
        entityMap.put(EntityDolphin.class, MC_EntityType.DOLPHIN);
        entityMap.put(EntityDrowned.class, MC_EntityType.DROWNED);
        entityMap.put(EntityLlamaSpit.class, MC_EntityType.LLAMA_SPIT);
        entityMap.put(EntityMinecartCommandBlock.class, MC_EntityType.MINECART_COMMAND_BLOCK);
        entityMap.put(EntityPufferFish.class, MC_EntityType.PUFFER_FISH);
        entityMap.put(EntitySalmon.class, MC_EntityType.SALMON);
        entityMap.put(EntityTropicalFish.class, MC_EntityType.TROPICAL_FISH);
        entityMap.put(EntityTurtle.class, MC_EntityType.TURTLE);
        entityMap.put(EntityPhantom.class, MC_EntityType.PHANTOM);
        entityMap.put(EntityTrident.class, MC_EntityType.TRIDENT);

        potionMap.put(MobEffects.SPEED, MC_PotionEffectType.SPEED);
        potionMap.put(MobEffects.SLOWNESS, MC_PotionEffectType.SLOWNESS);
        potionMap.put(MobEffects.HASTE, MC_PotionEffectType.HASTE);
        potionMap.put(MobEffects.MINING_FATIGUE, MC_PotionEffectType.MINING_FATIGUE);
        potionMap.put(MobEffects.STRENGTH, MC_PotionEffectType.STRENGTH);
        potionMap.put(MobEffects.INSTANT_HEALTH, MC_PotionEffectType.INSTANT_HEALTH);
        potionMap.put(MobEffects.INSTANT_DAMAGE, MC_PotionEffectType.INSTANT_DAMAGE);
        potionMap.put(MobEffects.JUMP_BOOST, MC_PotionEffectType.JUMP_BOOST);
        potionMap.put(MobEffects.NAUSEA, MC_PotionEffectType.NAUSEA);
        potionMap.put(MobEffects.REGENERATION, MC_PotionEffectType.REGENERATION);
        potionMap.put(MobEffects.RESISTANCE, MC_PotionEffectType.RESISTANCE);
        potionMap.put(MobEffects.FIRE_RESISTANCE, MC_PotionEffectType.FIRE_RESISTANCE);
        potionMap.put(MobEffects.WATER_BREATHING, MC_PotionEffectType.WATER_BREATHING);
        potionMap.put(MobEffects.INVISIBILITY, MC_PotionEffectType.INVISIBILITY);
        potionMap.put(MobEffects.BLINDNESS, MC_PotionEffectType.BLINDNESS);
        potionMap.put(MobEffects.NIGHT_VISION, MC_PotionEffectType.NIGHT_VISION);
        potionMap.put(MobEffects.HUNGER, MC_PotionEffectType.HUNGER);
        potionMap.put(MobEffects.WEAKNESS, MC_PotionEffectType.WEAKNESS);
        potionMap.put(MobEffects.POISON, MC_PotionEffectType.POISON);
        potionMap.put(MobEffects.WITHER, MC_PotionEffectType.WITHER);
        potionMap.put(MobEffects.HEALTH_BOOST, MC_PotionEffectType.HEALTH_BOOST);
        potionMap.put(MobEffects.ABSORPTION, MC_PotionEffectType.ABSORPTION);
        potionMap.put(MobEffects.SATURATION, MC_PotionEffectType.SATURATION);
        potionMap.put(MobEffects.GLOWING, MC_PotionEffectType.GLOWING);
        potionMap.put(MobEffects.LEVITATION, MC_PotionEffectType.LEVITATION);
        potionMap.put(MobEffects.LUCK, MC_PotionEffectType.LUCK);
        potionMap.put(MobEffects.UNLUCK, MC_PotionEffectType.UNLUCK);

        enchantmentMap.put(Enchantments.PROTECTION, MC_EnchantmentType.PROTECTION);
        enchantmentMap.put(Enchantments.FIRE_PROTECTION, MC_EnchantmentType.FIRE_PROTECTION);
        enchantmentMap.put(Enchantments.FEATHER_FALLING, MC_EnchantmentType.FEATHER_FALLING);
        enchantmentMap.put(Enchantments.BLAST_PROTECTION, MC_EnchantmentType.BLAST_PROTECTION);
        enchantmentMap.put(Enchantments.PROJECTILE_PROTECTION, MC_EnchantmentType.PROJECTILE_PROTECTION);
        enchantmentMap.put(Enchantments.RESPIRATION, MC_EnchantmentType.RESPIRATION);
        enchantmentMap.put(Enchantments.AQUA_AFFINITY, MC_EnchantmentType.AQUA_AFFINITY);
        enchantmentMap.put(Enchantments.THORNS, MC_EnchantmentType.THORNS);
        enchantmentMap.put(Enchantments.DEPTH_STRIDER, MC_EnchantmentType.DEPTH_STRIDER);
        enchantmentMap.put(Enchantments.FROST_WALKER, MC_EnchantmentType.FROST_WALKER);
        enchantmentMap.put(Enchantments.BINDING_CURSE, MC_EnchantmentType.CURSE_OF_BINDING);
        enchantmentMap.put(Enchantments.SHARPNESS, MC_EnchantmentType.SHARPNESS);
        enchantmentMap.put(Enchantments.SMITE, MC_EnchantmentType.SMITE);
        enchantmentMap.put(Enchantments.BANE_OF_ARTHROPODS, MC_EnchantmentType.BANE_OF_ARTHROPODS);
        enchantmentMap.put(Enchantments.KNOCKBACK, MC_EnchantmentType.KNOCKBACK);
        enchantmentMap.put(Enchantments.FIRE_ASPECT, MC_EnchantmentType.FIRE_ASPECT);
        enchantmentMap.put(Enchantments.LOOTING, MC_EnchantmentType.LOOTING);
        enchantmentMap.put(Enchantments.SWEEPING, MC_EnchantmentType.SWEEPING);
        enchantmentMap.put(Enchantments.EFFICIENCY, MC_EnchantmentType.EFFICIENCY);
        enchantmentMap.put(Enchantments.SILK_TOUCH, MC_EnchantmentType.SILK_TOUCH);
        enchantmentMap.put(Enchantments.UNBREAKING, MC_EnchantmentType.UNBREAKING);
        enchantmentMap.put(Enchantments.FORTUNE, MC_EnchantmentType.FORTUNE);
        enchantmentMap.put(Enchantments.POWER, MC_EnchantmentType.POWER);
        enchantmentMap.put(Enchantments.PUNCH, MC_EnchantmentType.PUNCH);
        enchantmentMap.put(Enchantments.FLAME, MC_EnchantmentType.FLAME);
        enchantmentMap.put(Enchantments.INFINITY, MC_EnchantmentType.INFINITY);
        enchantmentMap.put(Enchantments.LUCK_OF_THE_SEA, MC_EnchantmentType.LUCK_OF_THE_SEA);
        enchantmentMap.put(Enchantments.LURE, MC_EnchantmentType.LURE);
        enchantmentMap.put(Enchantments.MENDING, MC_EnchantmentType.MENDING);
        enchantmentMap.put(Enchantments.VANISHING_CURSE, MC_EnchantmentType.CURSE_OF_VANISHING);

        biomeMap.put(Biome.getBiome(0), MC_WorldBiomeType.OCEAN);
        biomeMap.put(Biome.getBiome(1), MC_WorldBiomeType.PLAINS);
        biomeMap.put(Biome.getBiome(2), MC_WorldBiomeType.DESERT);
        biomeMap.put(Biome.getBiome(3), MC_WorldBiomeType.EXTREME_HILLS);
        biomeMap.put(Biome.getBiome(4), MC_WorldBiomeType.FOREST);
        biomeMap.put(Biome.getBiome(5), MC_WorldBiomeType.TAIGA);
        biomeMap.put(Biome.getBiome(6), MC_WorldBiomeType.SWAMPLAND);
        biomeMap.put(Biome.getBiome(7), MC_WorldBiomeType.RIVER);
        biomeMap.put(Biome.getBiome(8), MC_WorldBiomeType.HELL);
        biomeMap.put(Biome.getBiome(9), MC_WorldBiomeType.THE_END);
        biomeMap.put(Biome.getBiome(10), MC_WorldBiomeType.FROZEN_OCEAN);
        biomeMap.put(Biome.getBiome(11), MC_WorldBiomeType.FROZEN_RIVER);
        biomeMap.put(Biome.getBiome(12), MC_WorldBiomeType.ICE_PLAINS);
        biomeMap.put(Biome.getBiome(13), MC_WorldBiomeType.ICE_MOUNTAINS);
        biomeMap.put(Biome.getBiome(14), MC_WorldBiomeType.MUSHROOM_ISLAND);
        biomeMap.put(Biome.getBiome(15), MC_WorldBiomeType.MUSHROOM_ISLAND_SHORE);
        biomeMap.put(Biome.getBiome(16), MC_WorldBiomeType.BEACH);
        biomeMap.put(Biome.getBiome(17), MC_WorldBiomeType.DESERT_HILLS);
        biomeMap.put(Biome.getBiome(18), MC_WorldBiomeType.FOREST_HILLS);
        biomeMap.put(Biome.getBiome(19), MC_WorldBiomeType.TAIGA_HILLS);
        biomeMap.put(Biome.getBiome(20), MC_WorldBiomeType.EXTREME_HILLS_EDGE);
        biomeMap.put(Biome.getBiome(21), MC_WorldBiomeType.JUNGLE);
        biomeMap.put(Biome.getBiome(22), MC_WorldBiomeType.JUNGLE_HILLS);
        biomeMap.put(Biome.getBiome(23), MC_WorldBiomeType.JUNGLE_EDGE);
        biomeMap.put(Biome.getBiome(24), MC_WorldBiomeType.DEEP_OCEAN);
        biomeMap.put(Biome.getBiome(25), MC_WorldBiomeType.STONE_BEACH);
        biomeMap.put(Biome.getBiome(26), MC_WorldBiomeType.COLD_BEACH);
        biomeMap.put(Biome.getBiome(27), MC_WorldBiomeType.BIRCH_FOREST);
        biomeMap.put(Biome.getBiome(28), MC_WorldBiomeType.BIRCH_FOREST_HILLS);
        biomeMap.put(Biome.getBiome(29), MC_WorldBiomeType.ROOFED_FOREST);
        biomeMap.put(Biome.getBiome(30), MC_WorldBiomeType.COLD_TAIGA);
        biomeMap.put(Biome.getBiome(31), MC_WorldBiomeType.COLD_TAIGA_HILLS);
        biomeMap.put(Biome.getBiome(32), MC_WorldBiomeType.MEGA_TAIGA);
        biomeMap.put(Biome.getBiome(33), MC_WorldBiomeType.MEGA_TAIGA_HILLS);
        biomeMap.put(Biome.getBiome(34), MC_WorldBiomeType.EXTREME_HILLS_PLUS);
        biomeMap.put(Biome.getBiome(35), MC_WorldBiomeType.SAVANNA);
        biomeMap.put(Biome.getBiome(36), MC_WorldBiomeType.SAVANNA_PLATEAU);
        biomeMap.put(Biome.getBiome(37), MC_WorldBiomeType.MESA);
        biomeMap.put(Biome.getBiome(38), MC_WorldBiomeType.MESA_PLATEAU_F);
        biomeMap.put(Biome.getBiome(39), MC_WorldBiomeType.MESA_PLATEAU);
        biomeMap.put(Biome.getBiome(40), MC_WorldBiomeType.END_SMALL_ISLANDS);
        biomeMap.put(Biome.getBiome(41), MC_WorldBiomeType.END_MIDLANDS);
        biomeMap.put(Biome.getBiome(42), MC_WorldBiomeType.END_HIGHLANDS);
        biomeMap.put(Biome.getBiome(43), MC_WorldBiomeType.END_BARRENS);
        biomeMap.put(Biome.getBiome(44), MC_WorldBiomeType.WARM_OCEAN);
        biomeMap.put(Biome.getBiome(45), MC_WorldBiomeType.LUKEWARM_OCEAN);
        biomeMap.put(Biome.getBiome(46), MC_WorldBiomeType.COLD_OCEAN);
        biomeMap.put(Biome.getBiome(47), MC_WorldBiomeType.DEEP_WARM_OCEAN);
        biomeMap.put(Biome.getBiome(48), MC_WorldBiomeType.DEEP_LUKEWARM_OCEAN);
        biomeMap.put(Biome.getBiome(49), MC_WorldBiomeType.DEEP_COLD_OCEAN);
        biomeMap.put(Biome.getBiome(50), MC_WorldBiomeType.DEEP_FROZEN_OCEAN);
        biomeMap.put(Biome.getBiome(127), MC_WorldBiomeType.VOID);
        biomeMap.put(Biome.getBiome(129), MC_WorldBiomeType.SUNFLOWER_PLAINS);
        biomeMap.put(Biome.getBiome(130), MC_WorldBiomeType.DESERT_M);
        biomeMap.put(Biome.getBiome(131), MC_WorldBiomeType.EXTREME_HILLS_M);
        biomeMap.put(Biome.getBiome(132), MC_WorldBiomeType.FLOWER_FOREST);
        biomeMap.put(Biome.getBiome(133), MC_WorldBiomeType.TAIGA_M);
        biomeMap.put(Biome.getBiome(134), MC_WorldBiomeType.SWAMPLAND_M);
        biomeMap.put(Biome.getBiome(140), MC_WorldBiomeType.ICE_PLAINS_SPIKES);
        biomeMap.put(Biome.getBiome(149), MC_WorldBiomeType.JUNGLE_M);
        biomeMap.put(Biome.getBiome(151), MC_WorldBiomeType.JUNGLE_EDGE_M);
        biomeMap.put(Biome.getBiome(155), MC_WorldBiomeType.BIRCH_FOREST_M);
        biomeMap.put(Biome.getBiome(156), MC_WorldBiomeType.BIRCH_FOREST_HILLS_M);
        biomeMap.put(Biome.getBiome(157), MC_WorldBiomeType.ROOFED_FOREST_M);
        biomeMap.put(Biome.getBiome(158), MC_WorldBiomeType.COLD_TAIGA_M);
        biomeMap.put(Biome.getBiome(160), MC_WorldBiomeType.MEGA_SPRUCE_TAIGA);
        biomeMap.put(Biome.getBiome(161), MC_WorldBiomeType.REDWOOD_TAIGA_HILLS_M);
        biomeMap.put(Biome.getBiome(162), MC_WorldBiomeType.EXTREME_HILLS_PLUS_M);
        biomeMap.put(Biome.getBiome(163), MC_WorldBiomeType.SAVANNA_M);
        biomeMap.put(Biome.getBiome(164), MC_WorldBiomeType.SAVANNA_PLATEAU_M);
        biomeMap.put(Biome.getBiome(165), MC_WorldBiomeType.MESA_M);
        biomeMap.put(Biome.getBiome(166), MC_WorldBiomeType.MESA_PLATEAU_F_M);
        biomeMap.put(Biome.getBiome(167), MC_WorldBiomeType.MESA_PLATEAU_M);

        handMap.put(EnumHand.MAIN_HAND, MC_Hand.MAIN_HAND);
        handMap.put(EnumHand.OFF_HAND, MC_Hand.OFF_HAND);

        attributeMap.put(MC_AttributeType.ARMOR, SharedMonsterAttributes.ARMOR);
        attributeMap.put(MC_AttributeType.ARMOR_TOUGHNESS, SharedMonsterAttributes.ARMOR_TOUGHNESS);
        attributeMap.put(MC_AttributeType.ATTACK_DAMAGE, SharedMonsterAttributes.ATTACK_DAMAGE);
        attributeMap.put(MC_AttributeType.FOLLOW_RANGE, SharedMonsterAttributes.FOLLOW_RANGE);
        attributeMap.put(MC_AttributeType.KNOCKBACK_RESISTANCE, SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        attributeMap.put(MC_AttributeType.MAX_HEALTH, SharedMonsterAttributes.MAX_HEALTH);
        attributeMap.put(MC_AttributeType.MOVEMENT_SPEED, SharedMonsterAttributes.MOVEMENT_SPEED);
        attributeMap.put(MC_AttributeType.PLAYER_ATTACK_SPEED, SharedMonsterAttributes.ATTACK_SPEED);
        attributeMap.put(MC_AttributeType.PLAYER_LUCK, SharedMonsterAttributes.LUCK);

        operatorMap.put(MC_AttributeModifier.Operator.ADD_CONSTANT, 0);
        operatorMap.put(MC_AttributeModifier.Operator.ADD_SCALAR_BASE, 1);
        operatorMap.put(MC_AttributeModifier.Operator.ADD_SCALAR, 2);

        legacyItemIdMap.put(Items.AIR, 0);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STONE), 1);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GRASS), 2);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DIRT), 3);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COBBLESTONE), 4);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196662_n), 5); // oak planks
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196674_t), 6); // oak sapling
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BEDROCK), 7);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WATER), 8);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WATER), 9);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LAVA), 10);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LAVA), 11);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SAND), 12);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GRAVEL), 13);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GOLD_ORE), 14);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.IRON_ORE), 15);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COAL_ORE), 16);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196617_K), 17); // oak log
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196642_W), 18); // oak leaves
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SPONGE), 19);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GLASS), 20);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LAPIS_ORE), 21);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LAPIS_BLOCK), 22);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DISPENSER), 23);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SANDSTONE), 24);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196586_al), 25); // note block
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196552_aC), 27); // powered rail
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DETECTOR_RAIL), 28);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STICKY_PISTON), 29);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196553_aF), 30); // web
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196804_gh), 31); // tall grass
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196555_aI), 32); // dead bush
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PISTON), 33);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PISTON_HEAD), 34);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196556_aL), 35); // white wool
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196605_bc), 37); // dandelion / yellow flower
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196606_bd), 38); // poppy / red flower
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BROWN_MUSHROOM), 39);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RED_MUSHROOM), 40);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GOLD_BLOCK), 41);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.IRON_BLOCK), 42);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STONE_SLAB), 44);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196584_bK), 45); // bricks
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.TNT), 46);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BOOKSHELF), 47);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.MOSSY_COBBLESTONE), 48);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OBSIDIAN), 49);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.TORCH), 50);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.FIRE), 51);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.MOB_SPAWNER), 52);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_STAIRS), 53);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CHEST), 54);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_WIRE), 55);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DIAMOND_ORE), 56);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DIAMOND_BLOCK), 57);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CRAFTING_TABLE), 58);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.FARMLAND), 60);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.FURNACE), 62);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196649_cc), 63); // sign
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_DOOR), 64);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LADDER), 65);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RAIL), 66);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196659_cl), 67); // cobblestone stairs
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WALL_SIGN), 68);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LEVER), 69);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STONE_PRESSURE_PLATE), 70);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196663_cq), 72); // oak pressure plate
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_ORE), 74);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_TORCH), 76);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STONE_BUTTON), 77);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SNOW), 78);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ICE), 79);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196604_cC), 80); // snow block
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CACTUS), 81);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CLAY), 82);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.JUKEBOX), 84);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_FENCE), 85);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PUMPKIN), 86);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.NETHERRACK), 87);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SOUL_SAND), 88);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GLOWSTONE), 89);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PORTAL), 90);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196628_cT), 91); // lit pumpkin
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196633_cV), 94); // repeater
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196807_gj), 95); // white stained glass
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196636_cW), 96); // oak trapdoor
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196686_dc), 97); // infested stone
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196696_di), 98); // stone bricks
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BROWN_MUSHROOM_BLOCK), 99);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RED_MUSHROOM_BLOCK), 100);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.IRON_BARS), 101);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GLASS_PANE), 102);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.MELON_BLOCK), 103);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PUMPKIN_STEM), 104);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.MELON_STEM), 105);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.VINE), 106);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_FENCE_GATE), 107);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BRICK_STAIRS), 108);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STONE_BRICK_STAIRS), 109);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.MYCELIUM), 110);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196651_dG), 111); // lily pad
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196653_dH), 112); // nether bricks
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.NETHER_BRICK_FENCE), 113);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.NETHER_BRICK_STAIRS), 114);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ENCHANTING_TABLE), 116);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.END_PORTAL), 119);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.END_PORTAL_FRAME), 120);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.END_STONE), 121);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DRAGON_EGG), 122);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_LAMP), 124);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196622_bq), 126); // oak slab
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COCOA), 127);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SANDSTONE_STAIRS), 128);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.EMERALD_ORE), 129);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ENDER_CHEST), 130);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.TRIPWIRE_HOOK), 131);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.TRIPWIRE), 132);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.EMERALD_BLOCK), 133);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_STAIRS), 134);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_STAIRS), 135);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_STAIRS), 136);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COMMAND_BLOCK), 137);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BEACON), 138);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COBBLESTONE_WALL), 139);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CARROTS), 141);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.POTATOES), 142);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196689_eF), 143); // oak button
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196703_eM), 144); // skeleton skull
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ANVIL), 145);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.TRAPPED_CHEST), 146);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), 147);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), 148);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196762_fd), 150);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DAYLIGHT_DETECTOR), 151);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REDSTONE_BLOCK), 152);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196766_fg), 153); // nether quartz ore
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.HOPPER), 154);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.QUARTZ_BLOCK), 155);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.QUARTZ_STAIRS), 156);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ACTIVATOR_RAIL), 157);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DROPPER), 158);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196777_fo), 159); // white terracotta
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196825_gz), 160); // white stained glass pane
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196574_ab), 161); // dark oak leaves
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196623_P), 162); // dark oak log
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_STAIRS), 163);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_STAIRS), 164);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SLIME_BLOCK), 165);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BARRIER), 166);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.IRON_TRAPDOOR), 167);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PRISMARINE), 168);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SEA_LANTERN), 169);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.HAY_BLOCK), 170);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196724_fH), 171); // white carpet
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.HARDENED_CLAY), 172);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.COAL_BLOCK), 173);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PACKED_ICE), 174);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196784_gT), 176); // white banner
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196843_hj), 177); // white wall banner
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DAYLIGHT_DETECTOR), 178);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RED_SANDSTONE), 179);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RED_SANDSTONE_STAIRS), 180);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_FENCE_GATE), 183);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_FENCE_GATE), 184);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_FENCE_GATE), 185);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_FENCE_GATE), 186);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_FENCE_GATE), 187);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_FENCE), 188);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_FENCE), 189);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_FENCE), 190);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_FENCE), 191);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_FENCE), 192);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.END_ROD), 198);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CHORUS_PLANT), 199);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CHORUS_FLOWER), 200);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PURPUR_BLOCK), 201);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PURPUR_PILLAR), 202);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PURPUR_STAIRS), 203);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PURPUR_SLAB), 205);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196806_hJ), 206); // end stone bricks
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GRASS_PATH), 208);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.END_GATEWAY), 209);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.REPEATING_COMMAND_BLOCK), 210);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CHAIN_COMMAND_BLOCK), 211);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.FROSTED_ICE), 212);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196814_hQ), 213); // magma block
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.NETHER_WART_BLOCK), 214);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196817_hS), 215); // red nether brick
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BONE_BLOCK), 216);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STRUCTURE_VOID), 217);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OBSERVER), 218);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.WHITE_SHULKER_BOX), 219);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ORANGE_SHULKER_BOX), 220);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.MAGENTA_SHULKER_BOX), 221);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LIGHT_BLUE_SHULKER_BOX), 222);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.YELLOW_SHULKER_BOX), 223);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.LIME_SHULKER_BOX), 224);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PINK_SHULKER_BOX), 225);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GRAY_SHULKER_BOX), 226);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196875_ie), 227); // light gray shulker box
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CYAN_SHULKER_BOX), 228);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.PURPLE_SHULKER_BOX), 229);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BLUE_SHULKER_BOX), 230);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BROWN_SHULKER_BOX), 231);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.GREEN_SHULKER_BOX), 232);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.RED_SHULKER_BOX), 233);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BLACK_SHULKER_BOX), 234);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.STRUCTURE_BLOCK), 255);
        legacyItemIdMap.put(Items.IRON_SHOVEL, 256);
        legacyItemIdMap.put(Items.IRON_PICKAXE, 257);
        legacyItemIdMap.put(Items.IRON_AXE, 258);
        legacyItemIdMap.put(Items.FLINT_AND_STEEL, 259);
        legacyItemIdMap.put(Items.APPLE, 260);
        legacyItemIdMap.put(Items.BOW, 261);
        legacyItemIdMap.put(Items.ARROW, 262);
        legacyItemIdMap.put(Items.COAL, 263);
        legacyItemIdMap.put(Items.DIAMOND, 264);
        legacyItemIdMap.put(Items.IRON_INGOT, 265);
        legacyItemIdMap.put(Items.GOLD_INGOT, 266);
        legacyItemIdMap.put(Items.IRON_SWORD, 267);
        legacyItemIdMap.put(Items.WOODEN_SWORD, 268);
        legacyItemIdMap.put(Items.WOODEN_SHOVEL, 269);
        legacyItemIdMap.put(Items.WOODEN_PICKAXE, 270);
        legacyItemIdMap.put(Items.WOODEN_AXE, 271);
        legacyItemIdMap.put(Items.STONE_SWORD, 272);
        legacyItemIdMap.put(Items.STONE_SHOVEL, 273);
        legacyItemIdMap.put(Items.STONE_PICKAXE, 274);
        legacyItemIdMap.put(Items.STONE_AXE, 275);
        legacyItemIdMap.put(Items.DIAMOND_SWORD, 276);
        legacyItemIdMap.put(Items.DIAMOND_SHOVEL, 277);
        legacyItemIdMap.put(Items.DIAMOND_PICKAXE, 278);
        legacyItemIdMap.put(Items.DIAMOND_AXE, 279);
        legacyItemIdMap.put(Items.STICK, 280);
        legacyItemIdMap.put(Items.BOWL, 281);
        legacyItemIdMap.put(Items.MUSHROOM_STEW, 282);
        legacyItemIdMap.put(Items.GOLDEN_SWORD, 283);
        legacyItemIdMap.put(Items.GOLDEN_SHOVEL, 284);
        legacyItemIdMap.put(Items.GOLDEN_PICKAXE, 285);
        legacyItemIdMap.put(Items.GOLDEN_AXE, 286);
        legacyItemIdMap.put(Items.STRING, 287);
        legacyItemIdMap.put(Items.FEATHER, 288);
        legacyItemIdMap.put(Items.GUNPOWDER, 289);
        legacyItemIdMap.put(Items.WOODEN_HOE, 290);
        legacyItemIdMap.put(Items.STONE_HOE, 291);
        legacyItemIdMap.put(Items.IRON_HOE, 292);
        legacyItemIdMap.put(Items.DIAMOND_HOE, 293);
        legacyItemIdMap.put(Items.GOLDEN_HOE, 294);
        legacyItemIdMap.put(Items.WHEAT_SEEDS, 295);
        legacyItemIdMap.put(Items.WHEAT, 296);
        legacyItemIdMap.put(Items.BREAD, 297);
        legacyItemIdMap.put(Items.LEATHER_HELMET, 298);
        legacyItemIdMap.put(Items.LEATHER_CHESTPLATE, 299);
        legacyItemIdMap.put(Items.LEATHER_LEGGINGS, 300);
        legacyItemIdMap.put(Items.LEATHER_BOOTS, 301);
        legacyItemIdMap.put(Items.CHAINMAIL_HELMET, 302);
        legacyItemIdMap.put(Items.CHAINMAIL_CHESTPLATE, 303);
        legacyItemIdMap.put(Items.CHAINMAIL_LEGGINGS, 304);
        legacyItemIdMap.put(Items.CHAINMAIL_BOOTS, 305);
        legacyItemIdMap.put(Items.IRON_HELMET, 306);
        legacyItemIdMap.put(Items.IRON_CHESTPLATE, 307);
        legacyItemIdMap.put(Items.IRON_LEGGINGS, 308);
        legacyItemIdMap.put(Items.IRON_BOOTS, 309);
        legacyItemIdMap.put(Items.DIAMOND_HELMET, 310);
        legacyItemIdMap.put(Items.DIAMOND_CHESTPLATE, 311);
        legacyItemIdMap.put(Items.DIAMOND_LEGGINGS, 312);
        legacyItemIdMap.put(Items.DIAMOND_BOOTS, 313);
        legacyItemIdMap.put(Items.GOLDEN_HELMET, 314);
        legacyItemIdMap.put(Items.GOLDEN_CHESTPLATE, 315);
        legacyItemIdMap.put(Items.GOLDEN_LEGGINGS, 316);
        legacyItemIdMap.put(Items.GOLDEN_BOOTS, 317);
        legacyItemIdMap.put(Items.FLINT, 318);
        legacyItemIdMap.put(Items.PORKCHOP, 319);
        legacyItemIdMap.put(Items.COOKED_PORKCHOP, 320);
        legacyItemIdMap.put(Items.PAINTING, 321);
        legacyItemIdMap.put(Items.GOLDEN_APPLE, 322);
        legacyItemIdMap.put(Items.SIGN, 323);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.OAK_DOOR), 324);
        legacyItemIdMap.put(Items.BUCKET, 325);
        legacyItemIdMap.put(Items.WATER_BUCKET, 326);
        legacyItemIdMap.put(Items.LAVA_BUCKET, 327);
        legacyItemIdMap.put(Items.MINECART, 328);
        legacyItemIdMap.put(Items.SADDLE, 329);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.IRON_DOOR), 330);
        legacyItemIdMap.put(Items.REDSTONE, 331);
        legacyItemIdMap.put(Items.SNOWBALL, 332);
        legacyItemIdMap.put(Items.BOAT, 333);
        legacyItemIdMap.put(Items.LEATHER, 334);
        legacyItemIdMap.put(Items.MILK_BUCKET, 335);
        legacyItemIdMap.put(Items.BRICK, 336);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196608_cF), 338); // sugar cane
        legacyItemIdMap.put(Items.PAPER, 339);
        legacyItemIdMap.put(Items.BOOK, 340);
        legacyItemIdMap.put(Items.SLIME_BALL, 341);
        legacyItemIdMap.put(Items.CHEST_MINECART, 342);
        legacyItemIdMap.put(Items.FURNACE_MINECART, 343);
        legacyItemIdMap.put(Items.EGG, 344);
        legacyItemIdMap.put(Items.COMPASS, 345);
        legacyItemIdMap.put(Items.FISHING_ROD, 346);
        legacyItemIdMap.put(Items.CLOCK, 347);
        legacyItemIdMap.put(Items.GLOWSTONE_DUST, 348);
        legacyItemIdMap.put(Items.field_196086_aW, 349); // cod
        legacyItemIdMap.put(Items.field_196102_ba, 350); // cooked cod
        legacyItemIdMap.put(Items.field_196136_br, 351); // ink sac
        legacyItemIdMap.put(Items.BONE, 352);
        legacyItemIdMap.put(Items.SUGAR, 353);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CAKE), 354);
        legacyItemIdMap.put(Items.field_196140_bu, 355); // bed
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196633_cV), 356); // repeater
        legacyItemIdMap.put(Items.COOKIE, 357);
        legacyItemIdMap.put(Items.MAP, 358);
        legacyItemIdMap.put(Items.SHEARS, 359);
        legacyItemIdMap.put(Items.PUMPKIN_SEEDS, 361);
        legacyItemIdMap.put(Items.MELON_SEEDS, 362);
        legacyItemIdMap.put(Items.BEEF, 363);
        legacyItemIdMap.put(Items.COOKED_BEEF, 364);
        legacyItemIdMap.put(Items.CHICKEN, 365);
        legacyItemIdMap.put(Items.COOKED_CHICKEN, 366);
        legacyItemIdMap.put(Items.ROTTEN_FLESH, 367);
        legacyItemIdMap.put(Items.ENDER_PEARL, 368);
        legacyItemIdMap.put(Items.BLAZE_ROD, 369);
        legacyItemIdMap.put(Items.GHAST_TEAR, 370);
        legacyItemIdMap.put(Items.GOLD_NUGGET, 371);
        legacyItemIdMap.put(Items.NETHER_WART, 372);
        legacyItemIdMap.put(Items.GLASS_BOTTLE, 374);
        legacyItemIdMap.put(Items.SPIDER_EYE, 375);
        legacyItemIdMap.put(Items.FERMENTED_SPIDER_EYE, 376);
        legacyItemIdMap.put(Items.BLAZE_POWDER, 377);
        legacyItemIdMap.put(Items.MAGMA_CREAM, 378);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BREWING_STAND), 379);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.CAULDRON), 380);
        legacyItemIdMap.put(Items.ENDER_EYE, 381);
        legacyItemIdMap.put(Items.SPECKLED_MELON, 382);
        legacyItemIdMap.put(Items.field_196127_cN, 383); // pig spawn egg
        legacyItemIdMap.put(Items.EXPERIENCE_BOTTLE, 384);
        legacyItemIdMap.put(Items.FIRE_CHARGE, 385);
        legacyItemIdMap.put(Items.WRITABLE_BOOK, 386);
        legacyItemIdMap.put(Items.WRITTEN_BOOK, 387);
        legacyItemIdMap.put(Items.EMERALD, 388);
        legacyItemIdMap.put(Items.ITEM_FRAME, 389);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.FLOWER_POT), 390);
        legacyItemIdMap.put(Items.CARROT, 391);
        legacyItemIdMap.put(Items.POTATO, 392);
        legacyItemIdMap.put(Items.BAKED_POTATO, 393);
        legacyItemIdMap.put(Items.POISONOUS_POTATO, 394);
        legacyItemIdMap.put(Items.MAP, 395);
        legacyItemIdMap.put(Items.GOLDEN_CARROT, 396);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196703_eM), 397); // skeleton skull
        legacyItemIdMap.put(Items.CARROT_ON_A_STICK, 398);
        legacyItemIdMap.put(Items.NETHER_STAR, 399);
        legacyItemIdMap.put(Items.PUMPKIN_PIE, 400);
        legacyItemIdMap.put(Items.field_196152_dE, 401); // firework rocket
        legacyItemIdMap.put(Items.field_196153_dF, 402); // firework star
        legacyItemIdMap.put(Items.ENCHANTED_BOOK, 403);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.field_196762_fd), 404); // comparator
        legacyItemIdMap.put(Items.QUARTZ, 406);
        legacyItemIdMap.put(Items.TNT_MINECART, 407);
        legacyItemIdMap.put(Items.HOPPER_MINECART, 408);
        legacyItemIdMap.put(Items.PRISMARINE_SHARD, 409);
        legacyItemIdMap.put(Items.PRISMARINE_CRYSTALS, 410);
        legacyItemIdMap.put(Items.RABBIT, 411);
        legacyItemIdMap.put(Items.COOKED_RABBIT, 412);
        legacyItemIdMap.put(Items.RABBIT_STEW, 413);
        legacyItemIdMap.put(Items.RABBIT_FOOT, 414);
        legacyItemIdMap.put(Items.RABBIT_HIDE, 415);
        legacyItemIdMap.put(Items.ARMOR_STAND, 416);
        legacyItemIdMap.put(Items.IRON_HORSE_ARMOR, 417);
        legacyItemIdMap.put(Items.GOLDEN_HORSE_ARMOR, 418);
        legacyItemIdMap.put(Items.DIAMOND_HORSE_ARMOR, 419);
        legacyItemIdMap.put(Items.LEAD, 420);
        legacyItemIdMap.put(Items.NAME_TAG, 421);
        legacyItemIdMap.put(Items.COMMAND_BLOCK_MINECART, 422);
        legacyItemIdMap.put(Items.MUTTON, 423);
        legacyItemIdMap.put(Items.COOKED_MUTTON, 424);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.SPRUCE_DOOR), 427);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.BIRCH_DOOR), 428);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.JUNGLE_DOOR), 429);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.ACACIA_DOOR), 430);
        legacyItemIdMap.put(Item.BLOCK_TO_ITEM.get(Blocks.DARK_OAK_DOOR), 431);
        legacyItemIdMap.put(Items.CHORUS_FRUIT, 432);
        legacyItemIdMap.put(Items.CHORUS_FRUIT_POPPED, 433);
        legacyItemIdMap.put(Items.BEETROOT, 434);
        legacyItemIdMap.put(Items.BEETROOT_SEEDS, 435);
        legacyItemIdMap.put(Items.BEETROOT_SOUP, 436);
        legacyItemIdMap.put(Items.DRAGON_BREATH, 437);
        legacyItemIdMap.put(Items.SPLASH_POTION, 438);
        legacyItemIdMap.put(Items.SPECTRAL_ARROW, 439);
        legacyItemIdMap.put(Items.TIPPED_ARROW, 440);
        legacyItemIdMap.put(Items.LINGERING_POTION, 441);
        legacyItemIdMap.put(Items.SHIELD, 442);
        legacyItemIdMap.put(Items.ELYTRA, 443);
        legacyItemIdMap.put(Items.SPRUCE_BOAT, 444);
        legacyItemIdMap.put(Items.BIRCH_BOAT, 445);
        legacyItemIdMap.put(Items.JUNGLE_BOAT, 446);
        legacyItemIdMap.put(Items.ACACIA_BOAT, 447);
        legacyItemIdMap.put(Items.DARK_OAK_BOAT, 448);
        legacyItemIdMap.put(Items.TOTEM_OF_UNDYING, 449);
        legacyItemIdMap.put(Items.SHULKER_SHELL, 450);
        legacyItemIdMap.put(Items.IRON_NUGGET, 452);
        legacyItemIdMap.put(Items.KNOWLEDGE_BOOK, 453);
        legacyItemIdMap.put(Items.field_196156_dS, 2256); // record 13
        legacyItemIdMap.put(Items.field_196158_dT, 2257); // record cat
        legacyItemIdMap.put(Items.field_196160_dU, 2258); // record blocks
        legacyItemIdMap.put(Items.field_196162_dV, 2259); // record chirp
        legacyItemIdMap.put(Items.field_196164_dW, 2260); // record far
        legacyItemIdMap.put(Items.field_196166_dX, 2261); // record mall
        legacyItemIdMap.put(Items.field_196168_dY, 2262); // record mellohi
        legacyItemIdMap.put(Items.field_196170_dZ, 2263); // record stal
        legacyItemIdMap.put(Items.field_196187_ea, 2264); // record strad
        legacyItemIdMap.put(Items.field_196188_eb, 2265); // record ward
        legacyItemIdMap.put(Items.field_196189_ec, 2266); // record 11
        legacyItemIdMap.put(Items.field_196190_ed, 2267); // record wait
    }

    public static MC_Block getBlockFromName(String blockName) {
        if (blockName == null) {
            return null;
        } else if (blockName.length() <= 0) {
            return null;
        } else {
            if (blockName.equalsIgnoreCase("white")) {
                blockName = "wool";
            }

            Block bo = Block.getBlockFromName(blockName.toLowerCase());

            if (bo != null) {
                return new BlockWrapper(bo.getDefaultState());
            }
            return null;
        }
    }

    public static MC_DamageType wrap(DamageSource damageSource) {
        if (damageSource == null) {
            return MC_DamageType.UNSPECIFIED;
        } else if (damageSource == DamageSource.ANVIL) {
            return MC_DamageType.ANVIL;
        } else if (damageSource == DamageSource.CACTUS) {
            return MC_DamageType.CACTUS;
        } else if (damageSource == DamageSource.DROWN) {
            return MC_DamageType.DROWN;
        } else if (damageSource == DamageSource.FALL) {
            return MC_DamageType.FALL;
        } else if (damageSource == DamageSource.FALLING_BLOCK) {
            return MC_DamageType.FALLING_BLOCK;
        } else if (damageSource == DamageSource.GENERIC) {
            return MC_DamageType.GENERIC;
        } else if (damageSource == DamageSource.IN_FIRE) {
            return MC_DamageType.IN_FIRE;
        } else if (damageSource == DamageSource.IN_WALL) {
            return MC_DamageType.IN_WALL;
        } else if (damageSource == DamageSource.LAVA) {
            return MC_DamageType.LAVA;
        } else if (damageSource == DamageSource.LIGHTNING_BOLT) {
            return MC_DamageType.LIGHTING_BOLT;
        } else if (damageSource == DamageSource.MAGIC) {
            return MC_DamageType.MAGIC;
        } else if (damageSource == DamageSource.ON_FIRE) {
            return MC_DamageType.ON_FIRE;
        } else if (damageSource == DamageSource.OUT_OF_WORLD) {
            return MC_DamageType.OUT_OF_WORLD;
        } else if (damageSource == DamageSource.STARVE) {
            return MC_DamageType.STARVE;
        } else if (damageSource == DamageSource.WITHER) {
            return MC_DamageType.WITHER;
        } else if (damageSource == DamageSource.DRAGON_BREATH) {
            return MC_DamageType.DRAGON_BREATH;
        } else if (damageSource == DamageSource.HOT_FLOOR) {
            return MC_DamageType.HOT_FLOOR;
        } else if (damageSource == DamageSource.CRAMMING) {
            return MC_DamageType.CRAMMING;
        } else if (damageSource == DamageSource.FLY_INTO_WALL) {
            return MC_DamageType.FLY_INTO_WALL;
        } else if (damageSource == DamageSource.FIREWORKS) {
            return MC_DamageType.FIREWORKS;
        } else {
            if (damageSource.getDamageType() != null) {
                if (damageSource.getDamageType().equalsIgnoreCase("mob")) {
                    return MC_DamageType.MOB;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("player")) {
                    return MC_DamageType.PLAYER;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("arrow")) {
                    return MC_DamageType.ARROW;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("onFire")) {
                    return MC_DamageType.ON_FIRE;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("fireball")) {
                    return MC_DamageType.FIREBALL;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("thrown")) {
                    return MC_DamageType.THROWN;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("indirectMagic")) {
                    return MC_DamageType.INDIRECT_MAGIC;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("thorns")) {
                    return MC_DamageType.THORNS;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("explosion.player")) {
                    return MC_DamageType.EXPLOSION_PLAYER;
                }

                if (damageSource.getDamageType().equalsIgnoreCase("explosion")) {
                    return MC_DamageType.EXPLOSION;
                }
            }

            return MC_DamageType.UNSPECIFIED;
        }
    }

    public static DamageSource unwrap(MC_DamageType cause) {
        if (cause == null) return DamageSource.GENERIC;
        else if (cause == MC_DamageType.ANVIL) return DamageSource.ANVIL;
        else if (cause == MC_DamageType.CACTUS) return DamageSource.CACTUS;
        else if (cause == MC_DamageType.DROWN) return DamageSource.DROWN;
        else if (cause == MC_DamageType.FALL) return DamageSource.FALL;
        else if (cause == MC_DamageType.FALLING_BLOCK) return DamageSource.FALLING_BLOCK;
        else if (cause == MC_DamageType.GENERIC) return DamageSource.GENERIC;
        else if (cause == MC_DamageType.IN_FIRE) return DamageSource.IN_FIRE;
        else if (cause == MC_DamageType.IN_WALL) return DamageSource.IN_WALL;
        else if (cause == MC_DamageType.LAVA) return DamageSource.LAVA;
        else if (cause == MC_DamageType.LIGHTING_BOLT) return DamageSource.LIGHTNING_BOLT;
        else if (cause == MC_DamageType.MAGIC) return DamageSource.MAGIC;
        else if (cause == MC_DamageType.ON_FIRE) return DamageSource.ON_FIRE;
        else if (cause == MC_DamageType.OUT_OF_WORLD) return DamageSource.OUT_OF_WORLD;
        else if (cause == MC_DamageType.STARVE) return DamageSource.STARVE;
        else if (cause == MC_DamageType.WITHER) return DamageSource.WITHER;
        else if (cause == MC_DamageType.DRAGON_BREATH) return DamageSource.DRAGON_BREATH;
        else if (cause == MC_DamageType.HOT_FLOOR) return DamageSource.HOT_FLOOR;
        else if (cause == MC_DamageType.CRAMMING) return DamageSource.CRAMMING;
        else if (cause == MC_DamageType.FLY_INTO_WALL) return DamageSource.FLY_INTO_WALL;
        else if (cause == MC_DamageType.FIREWORKS) return DamageSource.FIREWORKS;
        else if (cause == MC_DamageType.MOB) return DamageSource.causeMobDamage(null);
        else if (cause == MC_DamageType.PLAYER) return DamageSource.causePlayerDamage(null);
        else if (cause == MC_DamageType.ARROW) return DamageSource.causeArrowDamage(null, null);
        else if (cause == MC_DamageType.FIREBALL) return DamageSource.causeFireballDamage(null, null);
        else if (cause == MC_DamageType.THROWN) return DamageSource.causeThrownDamage(null, null);
        else if (cause == MC_DamageType.INDIRECT_MAGIC) return DamageSource.causeIndirectMagicDamage(null, null);
        else if (cause == MC_DamageType.THORNS) return DamageSource.causeThornsDamage(null);
        else if (cause == MC_DamageType.EXPLOSION) return DamageSource.causeExplosionDamage((EntityLivingBase) null);
        else if (cause == MC_DamageType.EXPLOSION_PLAYER) return DamageSource.causeExplosionDamage((Explosion) null);
        else return DamageSource.GENERIC;

    }
    
    public static Item getItemFromLegacyId(int id) {
        return legacyItemIdMap.inverse().getOrDefault(id, Items.AIR);
    }

    public static int getLegacyItemId(Item item) {
        return legacyItemIdMap.getOrDefault(item, 547);
    }
}
