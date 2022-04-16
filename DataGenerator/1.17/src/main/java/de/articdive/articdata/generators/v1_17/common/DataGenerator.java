package de.articdive.articdata.generators.v1_17.common;

import net.minecraft.SharedConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class DataGenerator<T> extends de.articdive.articdata.datagen.DataGenerator<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);
    protected static File dataFolder;

    public static void prepare() {
        SharedConstants.tryDetectVersion();
        de.articdive.articdata.generators.v1_16_3.common.DataGenerator.prepare();
    }
}
