package de.articdive.articdata.generators.v1_16_3.loot_tables;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.FileOutputHandler;
import de.articdive.articdata.datagen.annotations.NoGeneratorEntries;
import de.articdive.articdata.generators.v1_16_3.common.Initializer;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoGeneratorEntries
public final class BlockLootTableGenerator extends DataGenerator<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockLootTableGenerator.class);

    @Override
    public void generateNames() {
        // Not required for block loot tables
    }

    @Override
    public JsonObject generate() {
        File lootTablesFolder = new File(Initializer.DATA_FOLDER_1_16_3, "loot_tables");
        File blockTables = new File(lootTablesFolder, "blocks");

        File[] listedFiles = blockTables.listFiles();
        if (listedFiles != null) {
            List<File> children = new ArrayList<>(Arrays.asList(listedFiles));
            JsonObject blockLootTables = new JsonObject();
            for (int i = 0; i < children.size(); i++) {
                File file = children.get(i);
                // Add subdirectories files to the for-loop.
                if (file.isDirectory()) {
                    File[] subChildren = file.listFiles();
                    if (subChildren != null) {
                        children.addAll(Arrays.asList(subChildren));
                    }
                    continue;
                }
                JsonObject blockLootTable;
                try (FileReader reader = new FileReader(file)){
                    blockLootTable = FileOutputHandler.GSON.fromJson(reader, JsonObject.class);
                } catch (IOException e) {
                    LOGGER.error("Failed to read block loot table located at '" + file + "'.", e);
                    continue;
                }
                String fileName = file.getAbsolutePath().substring(blockTables.getAbsolutePath().length() + 1);
                // Make sure we use the correct slashes.
                fileName = fileName.replace("\\", "/");
                // Remove .json by removing last 5 chars of the name.
                String tableName = fileName.substring(0, fileName.length() - 5);
                blockLootTables.add("minecraft:" + tableName, blockLootTable);
            }
            return blockLootTables;
        } else {
            LOGGER.error("Failed to find block loot tables in data folder.");
            return new JsonObject();
        }
    }
}
