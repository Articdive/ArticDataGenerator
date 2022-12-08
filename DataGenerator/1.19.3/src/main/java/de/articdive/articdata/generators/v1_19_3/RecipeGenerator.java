package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.FileOutputHandler;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_19_3.common.Initializer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Mojang ID", supported = true)
@GeneratorEntry(name = "Recipe Type", supported = true)
@GeneratorEntry(name = "Layout", supported = true)
@GeneratorEntry(name = "Recipe", supported = true)
@GeneratorEntry(name = "Result", supported = true)
public final class RecipeGenerator extends DataGenerator<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeGenerator.class);

    @Override
    public void generateNames() {

    }

    @Override
    public JsonElement generate() {
        File recipesFolder = new File(Initializer.DATA_FOLDER_1_19_3, "recipes");

        List<File> children = new ArrayList<>(Arrays.stream(Objects.requireNonNull(recipesFolder.listFiles())).sorted(Comparator.comparing(File::getName)).toList());
        JsonObject recipes = new JsonObject();
        for (File child : children) {
            // Add subdirectories files to the for-loop.
            if (child.isDirectory()) {
                File[] subChildren = child.listFiles();
                if (subChildren != null) {
                    children.addAll(Arrays.asList(subChildren));
                }
                continue;
            }
            JsonObject recipe;
            try {
                recipe = FileOutputHandler.GSON.fromJson(new JsonReader(new FileReader(child)), JsonObject.class);
            } catch (FileNotFoundException e) {
                LOGGER.error("Failed to read recipe located at '" + child + "'.", e);
                continue;
            }
            String fileName = child.getAbsolutePath().substring(recipesFolder.getAbsolutePath().length() + 1);
            // Make sure we use the correct slashes.
            fileName = fileName.replace("\\", "/");
            // Remove .json by removing last 5 chars of the name.
            String tableName = fileName.substring(0, fileName.length() - 5);
            recipes.add("minecraft:" + tableName, recipe);
        }
        return recipes;
    }
}
