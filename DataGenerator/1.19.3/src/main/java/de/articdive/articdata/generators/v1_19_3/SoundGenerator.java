package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.ReflectionHelper;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_19_3.common.DataGenerator_1_19_3;
import java.lang.reflect.Field;
import java.util.Comparator;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true, description = "Goat Horn Sound Variants are not named by Mojang.")
@GeneratorEntry(name = "Translation Key", supported = true)
public final class SoundGenerator extends DataGenerator_1_19_3<SoundEvent> {
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
        // Add pesky Goat Sounds
        for (int i = 0; i < SoundEvents.GOAT_HORN_SOUND_VARIANTS.size(); i++) {
            Holder.Reference<SoundEvent> se = SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(i);
            names.put(se.value(), "GOAT_HORN_" + i);
        }
    }

    @Override
    public JsonObject generate() {
        JsonObject sounds = new JsonObject();

        for (ResourceLocation soundRL : SOUND_EVENT_REGISTRY.keySet().stream().sorted(Comparator.comparingInt(value -> SOUND_EVENT_REGISTRY.getId(SOUND_EVENT_REGISTRY.get(value)))).toList()) {
            SoundEvent se = SOUND_EVENT_REGISTRY.get(soundRL);
            if (se == null) {
                continue;
            }
            JsonObject sound = new JsonObject();

            sound.addProperty("id", SOUND_EVENT_REGISTRY.getId(se));
            sound.addProperty("mojangName", names.get(se));
            sound.addProperty("range", ReflectionHelper.getHiddenField(float.class,"range", SoundEvent.class, se));

            sounds.add(soundRL.toString(), sound);
        }
        return sounds;
    }
}
