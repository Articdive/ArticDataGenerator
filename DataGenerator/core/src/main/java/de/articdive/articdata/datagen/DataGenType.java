package de.articdive.articdata.datagen;

public enum DataGenType {
    ATTRIBUTES("attributes", "Attributes", "AttributeGenerator"),
    BIOMES("biomes", "Biomes", "BiomeGenerator"),
    BLOCKS("blocks", "Blocks", "BlockGenerator"),
    BLOCK_ENTITIES("block_entities", "Block Entities", "BlockEntityGenerator"),
    BLOCK_PROPERTIES("block_properties", "Block Properties", "BlockPropertyGenerator"),
    COMMANDS("commands", "Commands", "CommandGenerator"),
    CUSTOM_STATISTICS("custom_statistics", "Custom Statistics", "CustomStatisticGenerator"),
    DIMENSION_TYPES("dimension_types", "Dimension Types", "DimensionTypeGenerator"),
    DYE_COLORS("dye_colors", "Dye Colors", "DyeColorGenerator"),
    ENCHANTMENTS("enchantments", "Enchantments", "EnchantmentGenerator"),
    ENTITIES("entities", "Entities", "EntityGenerator"),
    ENTITY_DATA_SERIALIZERS("entity_data_serializers", "Entity Data Serializers", "EntityDataSerializerGenerator"),
    FLUIDS("fluids", "Fluids", "FluidGenerator"),
    FLUID_PROPERTIES("fluid_properties", "Fluid Properties", "FluidPropertyGenerator"),
    GAME_EVENTS("game_events", "Game Events", "GameEventGenerator"), // Added in 1.17
    MAP_COLORS("map_colors", "Map Colors", "MapColorGenerator"),
    MATERIALS("items", "Items", "MaterialGenerator"),
    MOB_EFFECTS("potion_effects", "Potion Effects", "MobEffectGenerator"),
    PACKETS("packets", "Packets", "PacketGenerator"),
    PARTICLES("particles", "Particles", "ParticleGenerator"),
    POTIONS("potions", "Potions", "PotionGenerator"),
    RECIPES("recipes", "Recipes", "RecipeGenerator"),
    SOUNDS("sounds", "Sounds", "SoundGenerator"),
    SOUND_SOURCES("sound_sources", "Sound Sources", "SoundSourceGenerator"),
    VILLAGER_PROFESSIONS("villager_professions", "Villager Professions", "VillagerProfessionGenerator"),
    VILLAGER_TYPES("villager_types", "Villager Types", "VillagerTypeGenerator"),

    BLOCK_TAGS("tags/block_tags", "Block Tags", "tags.BlockTagGenerator"),
    ENTITY_TYPE_TAGS("tags/entity_type_tags", "Entity Type Tags", "tags.EntityTypeTagGenerator"),
    FLUID_TAGS("tags/fluid_tags", "Fluid Tags", "tags.FluidTagGenerator"),
    GAMEEVENT_TAGS("tags/gameplay_tags", "Gameplay Tags", "tags.GameEventTagGenerator"), // Added in 1.17
    ITEM_TAGS("tags/item_tags", "Item Tags", "tags.ItemTagGenerator"),
    WORLDGEN_TAGS("tags/worldgen_tags", "World Generation Tags", "tags.WorldgenTagGenerator"), // Added in 1.18,2
    BANNER_PATTERN_TAGS("tags/banner_pattern_tags", "Banner Pattern Tags", "tags.BannerPatternTagGenerator"), // Added in 1.19
    CAT_VARIANT_TAGS("tags/cat_variant_tags", "Cat Variant Tags", "tags.CatVariantTagGenerator"), // Added in 1.19
    INSTRUMENT_TAGS("tags/instrument_tags", "Instrument Tags", "tags.InstrumentTagGenerator"), // Added in 1.19
    PAINTING_VARIANT_TAGS("tags/painting_variant_tags", "Painting Variant Tags", "tags.PaintingVariantTagGenerator"), // Added in 1.19
    POI_TYPE_TAGS("tags/poi_type_tags", "Point of Interest Type Tags", "tags.POITypeTagGenerator"), // Added in 1.19

    BLOCK_LOOT_TABLES("loot_tables/block_loot_tables", "Block Loot Tables", "loot_tables.BlockLootTableGenerator"),
    CHEST_LOOT_TABLES("loot_tables/chest_loot_tables", "Chest Loot Tables", "loot_tables.ChestLootTableGenerator"),
    ENTITY_LOOT_TABLES("loot_tables/entity_loot_tables", "Entity Loot Tables", "loot_tables.EntityLootTableGenerator"),
    GAMEPLAY_LOOT_TABLES("loot_tables/gameplay_loot_tables", "Gameplay Loot Tables", "loot_tables.GameplayLootTableGenerator");

    private final String fileName;
    private final String header; // For the docuemntation
    private final String className;

    DataGenType(String fileName, String header, String className) {
        this.fileName = fileName;
        this.header = header;
        this.className = className;
    }

    public String getFileName() {
        return fileName;
    }

    public String getHeader() {
        return header;
    }

    public String getClassName() {
        return className;
    }
}
