package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_16_3.common.DataGenerator;
import net.minecraft.sounds.SoundSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Type", supported = true)
public final class SoundSourceGenerator extends DataGenerator<SoundSource> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoundSourceGenerator.class);

    @Override
    public void generateNames() {
        // Might be possible to remove this.
        // It may appear one day that people need the SoundSource name.
        for (SoundSource soundSource : SoundSource.values()) {
            names.put(soundSource, soundSource.name());
        }
    }

    @Override
    public JsonArray generate() {
        JsonArray sounds = new JsonArray();

        for (SoundSource ss : SoundSource.values()) {
            JsonObject soundSource = new JsonObject();
            soundSource.addProperty("id", ss.ordinal());
            soundSource.addProperty("mojangName", ss.name());
            soundSource.addProperty("type", ss.getName());

            sounds.add(soundSource);
        }

        return sounds;
    }
}
