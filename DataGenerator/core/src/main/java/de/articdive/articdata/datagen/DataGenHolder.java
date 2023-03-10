package de.articdive.articdata.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.datagen.annotations.NoGeneratorEntries;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DataGenHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenHolder.class);
    private static final Map<DataGenType, DataGenerator<?>> generators = new LinkedHashMap<>();

    private DataGenHolder() {

    }

    public static void addGenerator(DataGenType type, MinecraftVersion[] lowerVersions) {
        for (int i = lowerVersions.length - 1; i >= 0; i--) {
            MinecraftVersion version = lowerVersions[i];
            // e.g. de.articdive.articdata.generators.1_16_5.AttributeGenerator
            String dataGeneratorReference = "de.articdive.articdata.generators." + version.getPackageName() + "." + type.getClassName();
            Class<?> dataGeneratorClazz;
            try {
                dataGeneratorClazz = Class.forName(dataGeneratorReference);
            } catch (ClassNotFoundException e) {
                // Expected behaviour
                continue;
            }
            try {
                DataGenerator<?> dg = (DataGenerator<?>) dataGeneratorClazz.getConstructor().newInstance();
                generators.put(type, dg);
                return;
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalStateException("Could not find generator for type: " + type.name());
    }

    public static void runGenerators(FileOutputHandler fileOutputHandler) {
        // Extract all names we will need
        for (DataGenerator<?> generator : generators.values()) {
            generator.generateNames();
        }
        // Run actual generators
        for (Map.Entry<DataGenType, DataGenerator<?>> entry : generators.entrySet()) {
            DataGenType type = entry.getKey();
            DataGenerator<?> generator = entry.getValue();

            LOGGER.info("Generating {}.", type.name());
            long currentTime = System.currentTimeMillis();
            JsonElement data = generator.generate();
            if (data instanceof JsonObject jsonObject) {
                data = JsonUtil.sortAlphabetically(jsonObject);
            }
            long finishedTime = System.currentTimeMillis();
            LOGGER.info("Finished generating {}. Took {}ms", type.name(), finishedTime - currentTime);

            fileOutputHandler.outputJson(data, type.getFileName());
        }
        StringBuilder TOC = new StringBuilder();
        StringBuilder fullDocumentationSection = new StringBuilder();
        // Run documentation generators (in alphabetical order)
        for (Map.Entry<DataGenType, DataGenerator<?>> entry :
                generators.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().name())).toList()
        ) {
            DataGenType type = entry.getKey();
            DataGenerator<?> generator = entry.getValue();

            if (generator.getClass().isAnnotationPresent(NoGeneratorEntries.class)) {
                continue;
            }

            String header = type.getHeader();
            // We also need to append a "hotlink" to the section we are just about to generate.
            // Should convert "Attributes" to "    - [Attributes](#attributes)"
            TOC.append("    - [").append(header).append("](").append("#").append(header.toLowerCase(Locale.ROOT).replaceAll(" ", "-")).append(")").append("\n");

            GeneratorEntry[] entries = generator.getClass().getDeclaredAnnotationsByType(GeneratorEntry.class);
            // Output the documentation
            StringBuilder typeDocumentationSegment = new StringBuilder();
            typeDocumentationSegment.append(new Heading(header, 3)).append("\n\n");

            boolean usesDescription = Arrays.stream(entries).anyMatch(generatorEntry -> !generatorEntry.description().isEmpty());

            Table.Builder tableBuilder = new Table.Builder().withAlignments(Table.ALIGN_CENTER, Table.ALIGN_CENTER, Table.ALIGN_CENTER);
            if (usesDescription) {
                tableBuilder.addRow("Data Type", "Supported?", "Description");
            } else {
                tableBuilder.addRow("Data Type", "Supported?");
            }
            // GeneratorEntries inserted here
            for (GeneratorEntry generatorEntry : entries) {
                String name = generatorEntry.name();
                boolean supported = generatorEntry.supported();
                String desc = generatorEntry.description();
                if (usesDescription) {
                    tableBuilder.addRow(name, supported ? ":heavy_check_mark: " : ":x:", desc);
                } else {
                    tableBuilder.addRow(name, supported ? ":heavy_check_mark: " : ":x:");
                }
            }
            typeDocumentationSegment.append(tableBuilder.build()).append("\n\n");
            fullDocumentationSection.append(typeDocumentationSegment);

        }
        fileOutputHandler.outputTOC(TOC.deleteCharAt(TOC.lastIndexOf("\n")).toString());
        fileOutputHandler.outputDocumentation(fullDocumentationSection.toString());
    }

    public static Map<?, String> getNameMap(DataGenType type) {
        return generators.get(type).names;
    }
}
