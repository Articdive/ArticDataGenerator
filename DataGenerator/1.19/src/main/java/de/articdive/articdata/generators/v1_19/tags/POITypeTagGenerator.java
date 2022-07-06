package de.articdive.articdata.generators.v1_19.tags;

import de.articdive.articdata.datagen.annotations.NoGeneratorEntries;
import de.articdive.articdata.generators.v1_16_3.tags.StandardTagGenerator;
import de.articdive.articdata.generators.v1_19.common.Initializer;
import java.io.File;

@NoGeneratorEntries
public final class POITypeTagGenerator extends StandardTagGenerator {
    public POITypeTagGenerator() {
        super(new File(new File(Initializer.DATA_FOLDER_1_19, "tags"), "point_of_interest_type"));
    }
}
