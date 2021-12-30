package de.articdive.articdata.datagen;

public enum DataGenType {
    ATTRIBUTES("attributes", "Attributes"),
    BIOMES("biomes", "Biomes"),
    BLOCKS("blocks", "Blocks"),
    BLOCK_ENTITIES("block_entities", "Block Entities"),
    BLOCK_PROPERTIES("block_properties", "Block Properties"),
    CUSTOM_STATISTICS("custom_statistics", "Custom Statistics"),
    DIMENSION_TYPES("dimension_types", "Dimension Types"),
    DYE_COLORS("dye_colors", "Dye Colors"),
    ENCHANTMENTS("enchantments", "Enchantments"),
    ENTITIES("entities", "Entities"),
    ENTITY_DATA_SERIALIZERS("entity_data_serializers", "Entity Data Serializers"),
    FLUIDS("fluids", "Fluids"),
    FLUID_PROPERTIES("fluid_properties", "Fluid Properties"),
    GAME_EVENTS("game_events", "Game Events"), // Added in 1.17
    MAP_COLORS("map_colors", "Map Colors"),
    MATERIALS("items", "Items"),
    MOB_EFFECTS("potion_effects", "Potion Effects"),
    PARTICLES("particles", "Particles"),
    POTIONS("potions", "Potions"),
    SOUNDS("sounds", "Sounds"),
    SOUND_SOURCES("sound_sources", "Sound Sources"),
    VILLAGER_PROFESSIONS("villager_professions", "Villager Professions"),
    VILLAGER_TYPES("villager_types", "Villager Types"),

    BLOCK_TAGS("tags/block_tags", "Block Tags"),
    ENTITY_TYPE_TAGS("tags/entity_type_tags", "Entity Type Tags"),
    FLUID_TAGS("tags/fluid_tags", "Fluid Tags"),
    GAMEEVENT_TAGS("tags/gameplay_tags", "Gameplay Tags"), // Added in 1.17
    ITEM_TAGS("tags/item_tags", "Item Tags"),

    BLOCK_LOOT_TABLES("loot_tables/block_loot_tables", "Block Loot Tables"),
    CHEST_LOOT_TABLES("loot_tables/chest_loot_tables", "Chest Loot Tables"),
    ENTITY_LOOT_TABLES("loot_tables/entity_loot_tables", "Entity Loot Tables"),
    GAMEPLAY_LOOT_TABLES("loot_tables/gameplay_loot_tables", "Gameplay Loot Tables");

    private final String fileName;
    private final String header; // For the docuemntation

    DataGenType(String fileName, String header) {
        this.fileName = fileName;
        this.header = header;
    }

    public String getFileName() {
        return fileName;
    }

    public String getHeader() {
        return header;
    }
}
