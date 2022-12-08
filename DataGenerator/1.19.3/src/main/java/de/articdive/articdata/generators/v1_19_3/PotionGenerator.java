package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_19_3.common.DataGenerator_1_19_3;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
public final class PotionGenerator extends DataGenerator_1_19_3<Potion> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PotionGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : Potions.class.getDeclaredFields()) {
            if (!Potion.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                Potion p = (Potion) declaredField.get(null);
                names.put(p, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map potion naming system.", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        List<ResourceLocation> effectRLs = POTION_REGISTRY.keySet().stream().sorted(Comparator.comparingInt(value -> POTION_REGISTRY.getId(POTION_REGISTRY.get(value)))).toList();
        JsonObject potions = new JsonObject();

        for (ResourceLocation effectRL : effectRLs) {
            Potion p = POTION_REGISTRY.get(effectRL);

            JsonObject effect = new JsonObject();
            // Null safety check.
            effect.addProperty("id", POTION_REGISTRY.getId(p));
            effect.addProperty("mojangName", names.get(p));

            potions.add(effectRL.toString(), effect);
        }
        return potions;
    }
}
