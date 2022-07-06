package de.articdive.articdata.generators.v1_19.common;

import java.io.File;
import net.minecraft.SharedConstants;

public final class Initializer {
    public static File DATA_FOLDER_1_19;
    public static File REPORTS_FOLDER_1_19;

    public static void prepare() {
        SharedConstants.tryDetectVersion();
        de.articdive.articdata.generators.v1_18.common.Initializer.prepare();
        DATA_FOLDER_1_19 = de.articdive.articdata.generators.v1_18.common.Initializer.DATA_FOLDER_1_18;
        REPORTS_FOLDER_1_19 = de.articdive.articdata.generators.v1_18.common.Initializer.REPORTS_FOLDER_1_18;
    }
}
