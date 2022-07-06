package de.articdive.articdata.generators.v1_16_3.tags;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.FileOutputHandler;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardTagGenerator extends DataGenerator<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StandardTagGenerator.class);
    private final File tagFolder;

    public StandardTagGenerator(File tagFolder) {
        this.tagFolder = tagFolder;
    }

    @Override
    public final void generateNames() {
        // Not required for item tags
    }


    @Override
    public final JsonObject generate() {
        JsonObject tags = new JsonObject();
        generateInto(tags, tagFolder);
        return tags;
    }

    private static void generateInto(JsonObject tagsJSON, File root) {
        generateInto(tagsJSON, root, "");
    }

    private static void generateInto(JsonObject tagsJSON, File root, String tagPrefix) {
        File[] listedFiles = root.listFiles();
        if (listedFiles != null) {
            List<File> children = new ArrayList<>(Arrays.asList(listedFiles));
            for (File child : children) {
                if (child.isDirectory()) {

                    String currentPrefix = child.getName();
                    if (tagPrefix != null && !tagPrefix.isEmpty()) {
                        currentPrefix = tagPrefix + "/" + child.getName();
                    }

                    generateInto(tagsJSON, child, currentPrefix);
                    continue;
                }
                JsonObject tag;
                try (FileReader reader = new FileReader(child)) {
                    tag = FileOutputHandler.GSON.fromJson(reader, JsonObject.class);
                } catch (IOException e) {
                    LOGGER.error("Failed to read tag located at '" + child + "'.", e);
                    continue;
                }

                String fileName = child.getName();
                // Remove .json (substring of 5)
                String tagName = fileName.substring(0, fileName.length() - 5);

                if (tagPrefix != null && !tagPrefix.isEmpty()) {
                    tagName = tagPrefix + "/" + tagName;
                }

                tagsJSON.add("minecraft:" + tagName, tag);
            }


        } else {
            LOGGER.error("Failed to find children of folder {}.", root.getAbsolutePath());
        }
    }
}
