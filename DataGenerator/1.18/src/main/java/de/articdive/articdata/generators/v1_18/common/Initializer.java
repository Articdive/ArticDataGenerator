package de.articdive.articdata.generators.v1_18.common;

import java.io.File;
import net.minecraft.SharedConstants;

public final class Initializer {
    public static File DATA_FOLDER_1_18;
    public static File REPORTS_FOLDER_1_18;

    public static void prepare() {
        SharedConstants.tryDetectVersion();
        de.articdive.articdata.generators.v1_17.common.Initializer.prepare();
        DATA_FOLDER_1_18 = de.articdive.articdata.generators.v1_17.common.Initializer.DATA_FOLDER_1_17;
        REPORTS_FOLDER_1_18 = de.articdive.articdata.generators.v1_17.common.Initializer.REPORTS_FOLDER_1_17;
    }
}
