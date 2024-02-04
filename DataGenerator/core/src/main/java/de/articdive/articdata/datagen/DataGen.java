package de.articdive.articdata.datagen;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataGen {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenHolder.class);

    private DataGen() {

    }

    public static void main(String[] args) {
        if (args.length == 0) {
            LOGGER.info("You must specify a version to generate data for.");
            return;
        }
        String version = args[0].toLowerCase(Locale.ROOT).replace('.', '_');
        if (!version.matches("\\d+_\\d+") && !version.matches("\\d+_\\d+_\\d+")) {
            LOGGER.error("The version specified is not explicitly defined.");
            LOGGER.error("The generator will fallback to 1.16.5 and attempt to use its generators.");
            version = "1_16_3";
        }
        MinecraftVersion minecraftVersion = MinecraftVersion.valueOf("V" + version);
        switch (minecraftVersion) {
            case V1_20_4, V1_20_3, V1_20_2, V1_20_1, V1_20, V1_19_4, V1_19_3 -> {
                // Run 1.19.3
                try {
                    Class<?> dgCommon1_19_3 = Class.forName("de.articdive.articdata.generators.v1_19_3.common.Initializer");
                    Method initMethod1_19_3 = dgCommon1_19_3.getDeclaredMethod("prepare");
                    initMethod1_19_3.invoke(null);


                    for (DataGenType supportedDataGenerator : minecraftVersion.getSupportedDataGenerators()) {
                        DataGenHolder.addGenerator(supportedDataGenerator, minecraftVersion.lessAndEqualVersions());
                    }
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                         IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            case V1_19_2, V1_19_1, V1_19 -> {
                // Run 1.19
                try {
                    Class<?> dgCommon1_19 = Class.forName("de.articdive.articdata.generators.v1_19.common.Initializer");
                    Method initMethod1_19 = dgCommon1_19.getDeclaredMethod("prepare");
                    initMethod1_19.invoke(null);


                    for (DataGenType supportedDataGenerator : minecraftVersion.getSupportedDataGenerators()) {
                        DataGenHolder.addGenerator(supportedDataGenerator, minecraftVersion.lessAndEqualVersions());
                    }
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                         IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            case V1_18_2, V1_18_1, V1_18 -> {
                // Run 1.18
                try {
                    Class<?> dgCommon1_18 = Class.forName("de.articdive.articdata.generators.v1_18.common.Initializer");
                    Method initMethod1_18 = dgCommon1_18.getDeclaredMethod("prepare");
                    initMethod1_18.invoke(null);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                         IllegalAccessException e) {
                    e.printStackTrace();
                }
                for (DataGenType supportedDataGenerator : minecraftVersion.getSupportedDataGenerators()) {
                    DataGenHolder.addGenerator(supportedDataGenerator, minecraftVersion.lessAndEqualVersions());
                }
            }
            case V1_17_1, V1_17 -> {
                // Run 1.17
                try {
                    Class<?> dgCommon1_17 = Class.forName("de.articdive.articdata.generators.v1_17.common.Initializer");
                    Method initMethod1_17 = dgCommon1_17.getDeclaredMethod("prepare");
                    initMethod1_17.invoke(null);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                         IllegalAccessException e) {
                    e.printStackTrace();
                }
                for (DataGenType supportedDataGenerator : minecraftVersion.getSupportedDataGenerators()) {
                    DataGenHolder.addGenerator(supportedDataGenerator, minecraftVersion.lessAndEqualVersions());
                }
            }
            case V1_16_5, V1_16_4, V1_16_3 -> {
                // Run 1.16_3
                try {
                    Class<?> dgCommon1_16_3 = Class.forName("de.articdive.articdata.generators.v1_16_3.common.Initializer");
                    Method initMethod1_16_3 = dgCommon1_16_3.getDeclaredMethod("prepare");
                    initMethod1_16_3.invoke(null);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                         IllegalAccessException e) {
                    e.printStackTrace();
                }
                for (DataGenType supportedDataGenerator : minecraftVersion.getSupportedDataGenerators()) {
                    DataGenHolder.addGenerator(supportedDataGenerator, minecraftVersion.lessAndEqualVersions());
                }
            }
            default -> {
                throw new IllegalStateException("Version was not added into the DataGen correctly");
            }
        }

        // Folder for the output.
        // Remove a character at the end since the prefix includes an _ at the end
        File outputFolder = new File("../ArticData/" + version + "/");
        if (args.length >= 2) {
            outputFolder = new File(args[1]);
        }
        DataGenHolder.runGenerators(new FileOutputHandler(version + "_", outputFolder));

        LOGGER.info("Output data in: " + outputFolder.getAbsolutePath());
        System.exit(0);
    }
}
