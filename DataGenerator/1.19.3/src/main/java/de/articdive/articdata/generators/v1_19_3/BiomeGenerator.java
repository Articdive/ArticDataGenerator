package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import de.articdive.articdata.datagen.FileOutputHandler;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_19_3.common.DataGenerator_1_19_3;
import de.articdive.articdata.generators.v1_19_3.common.Initializer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Carvers, Features & Creatures", supported = true)
@GeneratorEntry(name = "Mob Spawning", supported = true)
@GeneratorEntry(name = "Temperature", supported = true)
@GeneratorEntry(name = "Percipitation", supported = true)
public final class BiomeGenerator extends DataGenerator_1_19_3<Biome> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiomeGenerator.class);

    @Override
    public void generateNames() {
    }

    @Override
    public JsonObject generate() {
        File biomesFolder = new File(new File(Initializer.DATA_FOLDER_1_19_3, "worldgen"), "biome");

        List<File> children = new ArrayList<>(Arrays.stream(Objects.requireNonNull(biomesFolder.listFiles())).sorted(Comparator.comparing(File::getName)).toList());
        JsonObject biomes = new JsonObject();
        for (File child : children) {
            // Add subdirectories files to the for-loop.
            if (child.isDirectory()) {
                File[] subChildren = child.listFiles();
                if (subChildren != null) {
                    children.addAll(Arrays.asList(subChildren));
                }
                continue;
            }
            JsonObject biome;
            try {
                biome = FileOutputHandler.GSON.fromJson(new JsonReader(new FileReader(child)), JsonObject.class);
            } catch (FileNotFoundException e) {
                LOGGER.error("Failed to read biome located at '" + child + "'.", e);
                continue;
            }
            String fileName = child.getAbsolutePath().substring(biomesFolder.getAbsolutePath().length() + 1);
            // Make sure we use the correct slashes.
            fileName = fileName.replace("\\", "/");
            // Remove .json by removing last 5 chars of the name.
            String tableName = fileName.substring(0, fileName.length() - 5);
            biomes.add("minecraft:" + tableName, biome);
        }
        return biomes;
    }
}
