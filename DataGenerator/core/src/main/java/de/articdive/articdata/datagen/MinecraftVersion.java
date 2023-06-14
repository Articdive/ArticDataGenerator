package de.articdive.articdata.datagen;

import java.util.Arrays;

public enum MinecraftVersion {
    V1_16_3(new DataGenType[]{
            DataGenType.ATTRIBUTES,
            DataGenType.BIOMES,
            DataGenType.BLOCKS,
            DataGenType.BLOCK_ENTITIES,
            DataGenType.BLOCK_PROPERTIES,
            DataGenType.COMMANDS,
            DataGenType.CUSTOM_STATISTICS,
            DataGenType.DIMENSION_TYPES,
            DataGenType.DYE_COLORS,
            DataGenType.ENCHANTMENTS,
            DataGenType.ENTITIES,
            DataGenType.ENTITY_DATA_SERIALIZERS,
            DataGenType.FLUIDS,
            DataGenType.FLUID_PROPERTIES,
            DataGenType.MAP_COLORS,
            DataGenType.MATERIALS,
            DataGenType.MOB_EFFECTS,
            DataGenType.PACKETS,
            DataGenType.PARTICLES,
            DataGenType.POTIONS,
            DataGenType.RECIPES,
            DataGenType.SOUNDS,
            DataGenType.SOUND_SOURCES,
            DataGenType.VILLAGER_PROFESSIONS,
            DataGenType.VILLAGER_TYPES,

            DataGenType.BLOCK_TAGS,
            DataGenType.ENTITY_TYPE_TAGS,
            DataGenType.FLUID_TAGS,
            DataGenType.ITEM_TAGS,

            DataGenType.BLOCK_LOOT_TABLES,
            DataGenType.CHEST_LOOT_TABLES,
            DataGenType.ENTITY_LOOT_TABLES,
            DataGenType.GAMEPLAY_LOOT_TABLES,
    }),
    V1_16_4(V1_16_3.supportedDataGenerators),
    V1_16_5(V1_16_4.supportedDataGenerators),
    V1_17(copyAppend(V1_16_5.supportedDataGenerators, new DataGenType[]{DataGenType.GAMEEVENT_TAGS, DataGenType.GAME_EVENTS})),
    V1_17_1(V1_17.supportedDataGenerators),
    V1_18(V1_17_1.supportedDataGenerators),
    V1_18_1(V1_18.supportedDataGenerators),
    V1_18_2(copyAppend(V1_18_1.supportedDataGenerators, new DataGenType[]{DataGenType.BIOME_TAGS})),
    V1_19(copyAppend(V1_18_2.supportedDataGenerators, new DataGenType[]{
            DataGenType.BANNER_PATTERN_TAGS,
            DataGenType.CAT_VARIANT_TAGS,
            DataGenType.FLAT_LEVEL_GENERATOR_PRESET_TAGS,
            DataGenType.INSTRUMENT_TAGS,
            DataGenType.PAINTING_VARIANT_TAGS,
            DataGenType.POI_TYPE_TAGS,
            DataGenType.STRUCTURE_TAGS,
            DataGenType.WORLD_PRESET_TAGS
    })),
    V1_19_1(V1_19.supportedDataGenerators),
    V1_19_2(V1_19_1.supportedDataGenerators),
    V1_19_3(V1_19_2.supportedDataGenerators),
    V1_19_4(V1_19_3.supportedDataGenerators),
    V1_20(V1_19_4.supportedDataGenerators),
    V1_20_1(V1_20.supportedDataGenerators);

    private final DataGenType[] supportedDataGenerators;

    MinecraftVersion(DataGenType[] supportedDataGenerators) {
        this.supportedDataGenerators = supportedDataGenerators;
    }

    public DataGenType[] getSupportedDataGenerators() {
        return supportedDataGenerators;
    }

    public String getPackageName() {
        return "v" + name().substring(1); // A bit faster than toLowerCase() I believe.
    }

    public MinecraftVersion[] lessAndEqualVersions() {
        return Arrays.copyOf(values(), ordinal() + 1);
    }

    private static <T> T[] copyAppend(T[] root, T[] toAdd) {
        T[] output = Arrays.copyOf(root, root.length + toAdd.length);
        System.arraycopy(toAdd, 0, output, root.length, toAdd.length);
        return output;
    }
}
