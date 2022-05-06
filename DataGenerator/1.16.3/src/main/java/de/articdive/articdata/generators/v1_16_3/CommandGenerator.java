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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Entire Command Tree", supported = true, description = "See https://wiki.vg/Command_Data")
public final class CommandGenerator extends DataGenerator<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeGenerator.class);

    @Override
    public void generateNames() {
    }

    @Override
    public JsonElement generate() {
        File commandsFile = new File(reportsFolder, "commands.json");
        JsonObject commands = new JsonObject();
        try {
            commands = FileOutputHandler.GSON.fromJson(new JsonReader(new FileReader(commandsFile)), JsonObject.class);
        } catch (FileNotFoundException e) {
            LOGGER.error("Failed to read commands located at '" + commandsFile + "'.", e);
        }
        return commands;
    }
}
