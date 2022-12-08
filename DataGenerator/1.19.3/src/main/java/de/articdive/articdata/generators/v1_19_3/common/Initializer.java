package de.articdive.articdata.generators.v1_19_3.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import net.minecraft.SharedConstants;
import net.minecraft.data.Main;
import net.minecraft.server.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Initializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Initializer.class);
    public static File DATA_FOLDER_1_19_3;
    public static File REPORTS_FOLDER_1_19_3;

    public static void prepare() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();

        // Create a temp file, run Mojang's data generator and "recompile" that data.
        File tempDirFile;
        try {
            tempDirFile = Files.createTempDirectory("mojang_gen_data").toFile();
            Main.main(new String[]{
                    "--all",
                    "--output=" + tempDirFile
            });
            tempDirFile.deleteOnExit();
        } catch (IOException e) {
            LOGGER.error("Something went wrong while running Mojang's data generator.", e);
            return;
        }
        // Points to data/minecraft
        DATA_FOLDER_1_19_3 = new File(tempDirFile, "data" + File.separator + "minecraft");
        REPORTS_FOLDER_1_19_3 = new File(tempDirFile, "reports");
    }
}
