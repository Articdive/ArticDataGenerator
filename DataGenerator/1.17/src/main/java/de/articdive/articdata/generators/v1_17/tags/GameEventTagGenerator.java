package de.articdive.articdata.generators.v1_17.tags;

import de.articdive.articdata.datagen.annotations.NoGeneratorEntries;
import de.articdive.articdata.generators.v1_16_3.tags.StandardTagGenerator;
import de.articdive.articdata.generators.v1_17.common.Initializer;
import java.io.File;

@NoGeneratorEntries
public final class GameEventTagGenerator extends StandardTagGenerator {
    public GameEventTagGenerator() {
        super(new File(new File(Initializer.DATA_FOLDER_1_17, "tags"), "game_events"));
    }
}
