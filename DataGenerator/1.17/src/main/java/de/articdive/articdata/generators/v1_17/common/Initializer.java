package de.articdive.articdata.generators.v1_17.common;

import java.io.File;
import net.minecraft.SharedConstants;

public final class Initializer {
    public static File DATA_FOLDER_1_17;
    public static File REPORTS_FOLDER_1_17;

    public static void prepare() {
        SharedConstants.tryDetectVersion();
        de.articdive.articdata.generators.v1_16_3.common.Initializer.prepare();
        DATA_FOLDER_1_17 = de.articdive.articdata.generators.v1_16_3.common.Initializer.DATA_FOLDER_1_16_3;
        REPORTS_FOLDER_1_17 = de.articdive.articdata.generators.v1_16_3.common.Initializer.REPORTS_FOLDER_1_16_3;
    }
}
