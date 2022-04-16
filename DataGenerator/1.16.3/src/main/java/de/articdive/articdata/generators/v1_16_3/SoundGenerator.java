package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_16_3.common.DataGenerator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Set;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Translation Key", supported = true)
public final class SoundGenerator extends DataGenerator<SoundEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoundGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : SoundEvents.class.getDeclaredFields()) {
            if (!SoundEvent.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                SoundEvent se = (SoundEvent) declaredField.get(null);
                names.put(se, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map sound naming system", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        Set<ResourceLocation> soundRLs = Registry.SOUND_EVENT.keySet();
        JsonObject sounds = new JsonObject();

        for (ResourceLocation soundRL : soundRLs) {
            SoundEvent se = Registry.SOUND_EVENT.get(soundRL);
            if (se == null) {
                continue;
            }
            JsonObject sound = new JsonObject();

            sound.addProperty("id", Registry.SOUND_EVENT.getId(se));
            sound.addProperty("mojangName", names.get(se));

            sounds.add(soundRL.toString(), sound);
        }
        return sounds;
    }
}
