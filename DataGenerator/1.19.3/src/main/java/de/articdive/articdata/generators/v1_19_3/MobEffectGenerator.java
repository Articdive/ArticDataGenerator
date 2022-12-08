package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_19_3.common.DataGenerator_1_19_3;
import java.lang.reflect.Field;
import java.util.Comparator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Color (Decimal)", supported = true)
@GeneratorEntry(name = "Instanteneous", supported = true)
public final class MobEffectGenerator extends DataGenerator_1_19_3<MobEffect> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MobEffectGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : MobEffects.class.getDeclaredFields()) {
            if (!MobEffect.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                MobEffect me = (MobEffect) declaredField.get(null);
                names.put(me, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map effect naming system.", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        JsonObject effects = new JsonObject();

        for (ResourceLocation effectRL : MOB_EFFECT_REGISTRY.keySet().stream().sorted(Comparator.comparingInt(value -> MOB_EFFECT_REGISTRY.getId(MOB_EFFECT_REGISTRY.get(value)))).toList()) {
            MobEffect me = MOB_EFFECT_REGISTRY.get(effectRL);

            JsonObject effect = new JsonObject();
            // Null safety check.
            if (me == null) {
                continue;
            }
            effect.addProperty("id", MOB_EFFECT_REGISTRY.getId(me));
            effect.addProperty("mojangName", names.get(me));
            effect.addProperty("translationKey", me.getDescriptionId());
            effect.addProperty("color", me.getColor());
            effect.addProperty("instantaneous", me.isInstantenous());

            effects.add(effectRL.toString(), effect);
        }
        return effects;
    }
}
