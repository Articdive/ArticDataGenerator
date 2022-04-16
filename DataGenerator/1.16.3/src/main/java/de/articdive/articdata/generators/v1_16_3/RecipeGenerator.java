package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import de.articdive.articdata.datagen.FileOutputHandler;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_16_3.common.DataGenerator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Mojang ID", supported = true)
@GeneratorEntry(name = "Recipe Type", supported = true)
@GeneratorEntry(name = "Layout", supported = true)
@GeneratorEntry(name = "Recipe", supported = true)
@GeneratorEntry(name = "Result", supported = true)
public class RecipeGenerator extends DataGenerator<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeGenerator.class);
    @Override
    public void generateNames() {

    }

    @Override
    public JsonElement generate() {
        File recipesFolder = new File(dataFolder, "recipes");

        File[] listedFiles = recipesFolder.listFiles();
        if (listedFiles != null) {
            List<File> children = new ArrayList<>(Arrays.asList(listedFiles));
            JsonObject recipes = new JsonObject();
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
                JsonObject recipe;
                try {
                    recipe = FileOutputHandler.GSON.fromJson(new JsonReader(new FileReader(file)), JsonObject.class);
                } catch (FileNotFoundException e) {
                    LOGGER.error("Failed to read recipe located at '" + file + "'.", e);
                    continue;
                }
                String fileName = file.getAbsolutePath().substring(recipesFolder.getAbsolutePath().length() + 1);
                // Make sure we use the correct slashes.
                fileName = fileName.replace("\\", "/");
                // Remove .json by removing last 5 chars of the name.
                String tableName = fileName.substring(0, fileName.length() - 5);
                recipes.add("minecraft:" + tableName, recipe);
            }
            return recipes;
        } else {
            LOGGER.error("Failed to find recipes in data folder.");
            return new JsonObject();
        }
    }
}
