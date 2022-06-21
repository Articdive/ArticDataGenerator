package de.articdive.articdata.generators.v1_19;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_18.common.DataGenerator;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true, description = "Goat Horn Sound Variants are not named by Mojang.")
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
        // Add pesky Goat Sounds
        for (int i = 0; i < SoundEvents.GOAT_HORN_SOUND_VARIANTS.size(); i++) {
            SoundEvent se = SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(i);
            names.put(se, "GOAT_HORN_" + i);
        }
    }

    @Override
    public JsonObject generate() {
        List<ResourceLocation> soundRLs = Registry.SOUND_EVENT.keySet().stream().sorted(Comparator.comparingInt(value -> Registry.SOUND_EVENT.getId(Registry.SOUND_EVENT.get(value)))).toList();;
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
