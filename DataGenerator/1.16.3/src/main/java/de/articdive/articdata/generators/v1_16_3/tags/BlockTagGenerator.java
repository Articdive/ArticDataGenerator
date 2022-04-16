package de.articdive.articdata.generators.v1_16_3.tags;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import de.articdive.articdata.datagen.annotations.NoGeneratorEntries;
import de.articdive.articdata.generators.v1_16_3.common.DataGenerator;
import de.articdive.articdata.datagen.FileOutputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoGeneratorEntries
public final class BlockTagGenerator extends DataGenerator<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockTagGenerator.class);

    @Override
    public void generateNames() {
        // Not required for block tags
    }

    @Override
    public JsonObject generate() {
        File tagFolder = new File(dataFolder, "tags");
        File blockTagsFolder = new File(tagFolder, "blocks");

        File[] listedFiles = blockTagsFolder.listFiles();
        if (listedFiles != null) {
            List<File> children = new ArrayList<>(Arrays.asList(listedFiles));
            JsonObject blockTags = new JsonObject();
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
                JsonObject blockTag;
                try {
                    blockTag = FileOutputHandler.GSON.fromJson(new JsonReader(new FileReader(file)), JsonObject.class);
                } catch (FileNotFoundException e) {
                    LOGGER.error("Failed to read block tag located at '" + file + "'.", e);
                    continue;
                }
                String fileName = file.getAbsolutePath().substring(blockTagsFolder.getAbsolutePath().length() + 1);
                // Make sure we use the correct slashes.
                fileName = fileName.replace("\\", "/");
                // Remove .json (substring of 5)
                String tagName = fileName.substring(0, fileName.length() - 5);
                blockTags.add("minecraft:" + tagName, blockTag);
            }
            return blockTags;
        } else {
            LOGGER.error("Failed to find block tags in data folder.");
            return new JsonObject();
        }
    }
}
