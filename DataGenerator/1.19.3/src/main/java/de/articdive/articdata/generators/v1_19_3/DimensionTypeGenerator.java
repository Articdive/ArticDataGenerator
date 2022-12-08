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
import net.minecraft.world.level.dimension.DimensionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Bed Works", supported = true)
@GeneratorEntry(name = "Coordinate Scale", supported = true)
@GeneratorEntry(name = "Ceiling Height", supported = true)
@GeneratorEntry(name = "Fixed Time", supported = true)
@GeneratorEntry(name = "Raids", supported = true)
@GeneratorEntry(name = "Sky Light", supported = true)
@GeneratorEntry(name = "Piglin Safe", supported = true)
@GeneratorEntry(name = "Logical Height", supported = true)
@GeneratorEntry(name = "Natural", supported = true)
@GeneratorEntry(name = "Ultra Warm", supported = true)
@GeneratorEntry(name = "Respawn Anchor Works", supported = true)
@GeneratorEntry(name = "MinY", supported = true)
@GeneratorEntry(name = "Height (maxY)", supported = true)
public final class DimensionTypeGenerator extends DataGenerator_1_19_3<DimensionType> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DimensionTypeGenerator.class);

    @Override
    public void generateNames() {
        // Not required for dimension types.
    }

    @Override
    public JsonObject generate() {
        File biomesFolder = new File(Initializer.DATA_FOLDER_1_19_3, "dimension_type");

        List<File> children = new ArrayList<>(Arrays.stream(Objects.requireNonNull(biomesFolder.listFiles())).sorted(Comparator.comparing(File::getName)).toList());
        JsonObject dimensionTypes = new JsonObject();
        for (File child : children) {
            // Add subdirectories files to the for-loop.
            if (child.isDirectory()) {
                File[] subChildren = child.listFiles();
                if (subChildren != null) {
                    children.addAll(Arrays.asList(subChildren));
                }
                continue;
            }
            JsonObject dimensionType;
            try {
                dimensionType = FileOutputHandler.GSON.fromJson(new JsonReader(new FileReader(child)), JsonObject.class);
            } catch (FileNotFoundException e) {
                LOGGER.error("Failed to read dimensionType located at '" + child + "'.", e);
                continue;
            }
            String fileName = child.getAbsolutePath().substring(biomesFolder.getAbsolutePath().length() + 1);
            // Make sure we use the correct slashes.
            fileName = fileName.replace("\\", "/");
            // Remove .json by removing last 5 chars of the name.
            String tableName = fileName.substring(0, fileName.length() - 5);
            dimensionTypes.add("minecraft:" + tableName, dimensionType);
        }
        return dimensionTypes;
    }
}
