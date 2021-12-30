package de.articdive.articdata.datagen;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DataGen {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenHolder.class);

    private DataGen() {

    }

    public static void main(String[] args) {
        if (args.length == 0) {
            LOGGER.info("You must specify a version to generate data for.");
            return;
        }
        String version = args[0].toLowerCase(Locale.ROOT).replace('_', '.');
        if (!version.matches("\\d+\\.\\d+") && !version.matches("\\d+\\.\\d+\\.\\d+")) {
            LOGGER.error("The version specified is not explicitly defined.");
            LOGGER.error("The generator will fallback to 1.16.5 and attempt to use its generators.");
            version = "1.16.5";
        }
        String versionPrefix = version.replace('.', '_') + "_";
        // Try to ensure the format X.X.X.
        switch (args[0].toLowerCase(Locale.ROOT).replace('_', '.')) {
            case "1.18", "1.18.1" -> {
                // Run 1.18
                try {
                    Class<?> dgCommon1_18 = Class.forName("de.articdive.articdata.generators.common.DataGenerator_1_18");
                    Method prepareMethod1_18 = dgCommon1_18.getDeclaredMethod("prepare");
                    prepareMethod1_18.invoke(null);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                DataGenHolder.addGenerator(DataGenType.ATTRIBUTES, "AttributeGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.BIOMES, "BiomeGenerator_1_18");
                DataGenHolder.addGenerator(DataGenType.BLOCKS, "BlockGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.BLOCK_ENTITIES, "BlockEntityGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.BLOCK_PROPERTIES, "BlockPropertyGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.CUSTOM_STATISTICS, "CustomStatisticGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.DIMENSION_TYPES, "DimensionTypeGenerator_1_17");
                DataGenHolder.addGenerator(DataGenType.ENCHANTMENTS, "EnchantmentGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENTITIES, "EntityGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENTITY_DATA_SERIALIZERS, "EntityDataSerializerGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.FLUIDS, "FluidGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.FLUID_PROPERTIES, "FluidPropertyGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.GAME_EVENTS, "GameEventGenerator_1_17");
                DataGenHolder.addGenerator(DataGenType.MAP_COLORS, "MapColorGenerator_1_18");
                DataGenHolder.addGenerator(DataGenType.MATERIALS, "MaterialGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.MOB_EFFECTS, "MobEffectGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.PARTICLES, "ParticleGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.POTIONS, "PotionGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.SOUNDS, "SoundGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.SOUND_SOURCES, "SoundSourceGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.VILLAGER_PROFESSIONS, "VillagerProfessionGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.VILLAGER_TYPES, "VillagerTypeGenerator_1_16_5");

                DataGenHolder.addGenerator(DataGenType.BLOCK_TAGS, "tags.BlockTagGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENTITY_TYPE_TAGS, "tags.EntityTypeTagGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.FLUID_TAGS, "tags.FluidTagGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ITEM_TAGS, "tags.ItemTagGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.GAMEEVENT_TAGS, "tags.GameEventTagGenerator_1_17");

                DataGenHolder.addGenerator(DataGenType.BLOCK_LOOT_TABLES, "loot_tables.BlockLootTableGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.CHEST_LOOT_TABLES, "loot_tables.ChestLootTableGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENTITY_LOOT_TABLES, "loot_tables.EntityLootTableGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.GAMEPLAY_LOOT_TABLES, "loot_tables.GameplayLootTableGenerator_1_16_5");
            }
            case "1.17", "1.17.1" -> {
                // Run 1.17
                try {
                    Class<?> dgCommon1_17 = Class.forName("de.articdive.articdata.generators.common.DataGenerator_1_17");
                    Method prepareMethod1_17 = dgCommon1_17.getDeclaredMethod("prepare");
                    prepareMethod1_17.invoke(null);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                DataGenHolder.addGenerator(DataGenType.ATTRIBUTES, "AttributeGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.BIOMES, "BiomeGenerator_1_17");
                DataGenHolder.addGenerator(DataGenType.BLOCKS, "BlockGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.BLOCK_ENTITIES, "BlockEntityGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.BLOCK_PROPERTIES, "BlockPropertyGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.CUSTOM_STATISTICS, "CustomStatisticGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.DIMENSION_TYPES, "DimensionTypeGenerator_1_17");
                DataGenHolder.addGenerator(DataGenType.ENCHANTMENTS, "EnchantmentGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENTITIES, "EntityGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENTITY_DATA_SERIALIZERS, "EntityDataSerializerGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.FLUIDS, "FluidGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.FLUID_PROPERTIES, "FluidPropertyGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.GAME_EVENTS, "GameEventGenerator_1_17");
                DataGenHolder.addGenerator(DataGenType.MAP_COLORS, "MapColorGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.MATERIALS, "MaterialGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.MOB_EFFECTS, "MobEffectGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.PARTICLES, "ParticleGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.POTIONS, "PotionGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.SOUNDS, "SoundGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.SOUND_SOURCES, "SoundSourceGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.VILLAGER_PROFESSIONS, "VillagerProfessionGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.VILLAGER_TYPES, "VillagerTypeGenerator_1_16_5");

                DataGenHolder.addGenerator(DataGenType.BLOCK_TAGS, "tags.BlockTagGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENTITY_TYPE_TAGS, "tags.EntityTypeTagGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.FLUID_TAGS, "tags.FluidTagGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ITEM_TAGS, "tags.ItemTagGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.GAMEEVENT_TAGS, "tags.GameEventTagGenerator_1_17");

                DataGenHolder.addGenerator(DataGenType.BLOCK_LOOT_TABLES, "loot_tables.BlockLootTableGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.CHEST_LOOT_TABLES, "loot_tables.ChestLootTableGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENTITY_LOOT_TABLES, "loot_tables.EntityLootTableGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.GAMEPLAY_LOOT_TABLES, "loot_tables.GameplayLootTableGenerator_1_16_5");
            }
            case "1.16", "1.16.1", "1.16.2", "1.16.3", "1.16.4", "1.16.5" -> {
                // Run 1.16_5
                try {
                    Class<?> dgCommon1_16_5 = Class.forName("de.articdive.articdata.generators.common.DataGenerator_1_16_5");
                    Method prepareMethod1_16_5 = dgCommon1_16_5.getDeclaredMethod("prepare");
                    prepareMethod1_16_5.invoke(null);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                DataGenHolder.addGenerator(DataGenType.ATTRIBUTES, "AttributeGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.BIOMES, "BiomeGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.BLOCK_ENTITIES, "BlockEntityGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.BLOCK_PROPERTIES, "BlockPropertyGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.BLOCKS, "BlockGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.CUSTOM_STATISTICS, "CustomStatisticGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.DIMENSION_TYPES, "DimensionTypeGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENCHANTMENTS, "EnchantmentGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENTITIES, "EntityGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENTITY_DATA_SERIALIZERS, "EntityDataSerializerGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.FLUIDS, "FluidGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.FLUID_PROPERTIES, "FluidPropertyGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.MATERIALS, "MaterialGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.MAP_COLORS, "MapColorGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.PARTICLES, "ParticleGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.MOB_EFFECTS, "MobEffectGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.POTIONS, "PotionGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.SOUND_SOURCES, "SoundSourceGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.SOUNDS, "SoundGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.VILLAGER_PROFESSIONS, "VillagerProfessionGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.VILLAGER_TYPES, "VillagerTypeGenerator_1_16_5");

                DataGenHolder.addGenerator(DataGenType.BLOCK_TAGS, "tags.BlockTagGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENTITY_TYPE_TAGS, "tags.EntityTypeTagGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.FLUID_TAGS, "tags.FluidTagGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ITEM_TAGS, "tags.ItemTagGenerator_1_16_5");

                DataGenHolder.addGenerator(DataGenType.BLOCK_LOOT_TABLES, "loot_tables.BlockLootTableGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.CHEST_LOOT_TABLES, "loot_tables.ChestLootTableGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.ENTITY_LOOT_TABLES, "loot_tables.EntityLootTableGenerator_1_16_5");
                DataGenHolder.addGenerator(DataGenType.GAMEPLAY_LOOT_TABLES, "loot_tables.GameplayLootTableGenerator_1_16_5");
            }
        }

        // Folder for the output.
        // Remove a character at the end since the prefix includes an _ at the end
        File outputFolder = new File("../ArticData/" + versionPrefix.substring(0, versionPrefix.length() - 1) + "/");
        if (args.length >= 2) {
            outputFolder = new File(args[1]);
        }
        DataGenHolder.runGenerators(new FileOutputHandler(versionPrefix, outputFolder));

        LOGGER.info("Output data in: " + outputFolder.getAbsolutePath());
    }
}
