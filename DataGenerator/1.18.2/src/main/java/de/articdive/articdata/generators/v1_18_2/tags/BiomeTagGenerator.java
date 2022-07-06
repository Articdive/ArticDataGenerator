package de.articdive.articdata.generators.v1_18_2.tags;

import de.articdive.articdata.datagen.annotations.NoGeneratorEntries;
import de.articdive.articdata.generators.v1_16_3.tags.StandardTagGenerator;
import de.articdive.articdata.generators.v1_18.common.Initializer;
import java.io.File;

@NoGeneratorEntries
public final class BiomeTagGenerator extends StandardTagGenerator {
    public BiomeTagGenerator() {
        super(new File(new File(Initializer.DATA_FOLDER_1_18, "tags"), "worldgen/biome"));
    }
}
