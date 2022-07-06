package de.articdive.articdata.generators.v1_16_3.tags;

import de.articdive.articdata.datagen.annotations.NoGeneratorEntries;
import de.articdive.articdata.generators.v1_16_3.common.Initializer;
import java.io.File;

@NoGeneratorEntries
public final class FluidTagGenerator extends StandardTagGenerator {
    public FluidTagGenerator() {
        super(new File(new File(Initializer.DATA_FOLDER_1_16_3, "tags"), "fluids"));
    }
}
