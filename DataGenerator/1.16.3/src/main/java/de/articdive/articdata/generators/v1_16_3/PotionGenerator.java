package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
public final class PotionGenerator extends DataGenerator<Potion> {
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
        List<ResourceLocation> effectRLs = Registry.POTION.keySet().stream().sorted(Comparator.comparingInt(value -> Registry.POTION.getId(Registry.POTION.get(value)))).toList();
        JsonObject potions = new JsonObject();

        for (ResourceLocation effectRL : effectRLs) {
            Potion p = Registry.POTION.get(effectRL);

            JsonObject effect = new JsonObject();
            // Null safety check.
            effect.addProperty("id", Registry.POTION.getId(p));
            effect.addProperty("mojangName", names.get(p));

            JsonArray potionEffects = new JsonArray();
            for (MobEffectInstance mei : p.getEffects()) {

                JsonObject potionEffect = new JsonObject();
                potionEffect.addProperty("id", Registry.MOB_EFFECT.getKey(mei.getEffect()).toString());
                potionEffect.addProperty("duration", mei.getDuration());
                potionEffect.addProperty("amplifier", mei.getAmplifier());
                potionEffect.addProperty("visible", mei.isVisible());
                potionEffect.addProperty("ambient", mei.isAmbient());
                potionEffect.addProperty("showIcon", mei.showIcon());

                potionEffects.add(potionEffect);
            }
            effect.add("potion_effects", potionEffects);

            potions.add(effectRL.toString(), effect);
        }
        return potions;
    }
}
