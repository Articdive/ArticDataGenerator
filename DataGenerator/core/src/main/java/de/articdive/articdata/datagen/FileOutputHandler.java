package de.articdive.articdata.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileOutputHandler {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(FileOutputHandler.class);
    private final String versionPrefix;
    private final File outputDirectory;

    FileOutputHandler(String versionPrefix, File outputDirectory) {
        this.versionPrefix = versionPrefix;
        // Create output folder
        if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
            throw new ExceptionInInitializerError("Failed to create work folder.");
        }
        this.outputDirectory = outputDirectory;
    }

    public void outputJson(JsonElement output, String fileName) {
        if (fileName.contains("/")) {
            String[] split = fileName.split("/");
            outputJson(output, split[1], split[0]);
        } else {
            outputJson(output, fileName, outputDirectory);
        }
    }

    private void outputJson(JsonElement output, String fileName, String subFolder) {
        File outputSubDirectory = new File(this.outputDirectory, versionPrefix + subFolder);
        if (!outputSubDirectory.exists() && !outputSubDirectory.mkdirs()) {
            throw new ExceptionInInitializerError("Failed to create work sub-directory.");
        }
        outputJson(output, fileName, outputSubDirectory);
    }

    private void outputJson(JsonElement output, String fileName, File outputDirectory) {
        fileName = versionPrefix + fileName + ".json";
        try {
            Writer writer = new FileWriter(new File(outputDirectory, fileName), false);
            GSON.toJson(output, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Something went wrong while writing data to " + fileName + ".", e);
        }
    }

    public void outputDocumentation(String documentation) {
        String filename = "CONTENT_DOCUMENTATION.md";
        try {
            Writer writer = new FileWriter(new File(outputDirectory, filename), false);
            writer.write(documentation);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Something went wrong while writing the content documentation to " + filename + ".", e);
        }
    }

    public void outputTOC(String documentation) {
        String filename = "TOC.md";
        try {
            Writer writer = new FileWriter(new File(outputDirectory, filename), false);
            writer.write(documentation);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Something went wrong while writing the table of contents to " + filename + ".", e);
        }
    }
}
