package de.articdive.articdata.datagen;

import com.google.gson.JsonElement;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.datagen.annotations.NoGeneratorEntries;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public final class DataGenHolder {
    private static final Map<DataGenType, DataGenerator<?>> generators = new HashMap<>();

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

            JsonElement data = generator.generate();
            fileOutputHandler.outputJson(data, type.getFileName());
        }
        StringBuilder TOC = new StringBuilder();
        StringBuilder fullDoc = new StringBuilder();
        // Run documentation generators (in alphabetical order)
        for (Map.Entry<DataGenType, DataGenerator<?>> entry :
                generators.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().name())).collect(Collectors.toList())
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
            StringBuilder genDoc = new StringBuilder();
            genDoc.append("### ").append(header).append("\n");
            genDoc.append("\n");
            // Within one line
            genDoc.append("| Data Type                               | Supported?         |\n");
            genDoc.append("| :-------------------------------------: | :----------------: |\n");
            // GeneratorEntries inserted here
            for (GeneratorEntry generatorEntry : entries) {
                String name = generatorEntry.name();
                boolean supported = generatorEntry.supported();
                genDoc.append("| ").append(name).append(" ".repeat(40 - name.length())).append("| ")
                        .append(supported ? ":heavy_check_mark: " : ":x:                ").append("|\n");
            }
            genDoc.append("\n");
            fullDoc.append(genDoc);

        }
        fileOutputHandler.outputTOC(TOC.deleteCharAt(TOC.lastIndexOf("\n")).toString());
        fileOutputHandler.outputDocumentation(fullDoc.toString());
    }

    public static Map<?, String> getNameMap(DataGenType type) {
        return generators.get(type).names;
    }


}
