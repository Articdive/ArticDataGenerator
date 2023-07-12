package de.articdive.articdata.generators.v1_17;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Color (Decimal)", supported = true)
@GeneratorEntry(name = "Instanteneous", supported = true)
@GeneratorEntry(name = "Attribute Modifiers", supported = true)
public final class MobEffectGenerator extends DataGenerator<MobEffect> {
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
        List<ResourceLocation> effectRLs = Registry.MOB_EFFECT.keySet().stream().sorted(Comparator.comparingInt(value -> Registry.MOB_EFFECT.getId(Registry.MOB_EFFECT.get(value)))).toList();
        JsonObject effects = new JsonObject();

        for (ResourceLocation effectRL : effectRLs) {
            MobEffect me = Registry.MOB_EFFECT.get(effectRL);

            JsonObject effect = new JsonObject();
            // Null safety check.
            if (me == null) {
                continue;
            }
            effect.addProperty("id", Registry.MOB_EFFECT.getId(me));
            effect.addProperty("mojangName", names.get(me));
            effect.addProperty("translationKey", me.getDescriptionId());
            effect.addProperty("color", me.getColor());
            effect.addProperty("instantaneous", me.isInstantenous());

            JsonArray modifiers = new JsonArray();
            for (Map.Entry<Attribute, AttributeModifier> attributeModifierEntry : me.getAttributeModifiers().entrySet()) {
                Attribute attribute = attributeModifierEntry.getKey();
                AttributeModifier modifier = attributeModifierEntry.getValue();

                JsonObject attributeModifier = new JsonObject();
                attributeModifier.addProperty("attribute", attribute.getDescriptionId());
                attributeModifier.addProperty("uuid", modifier.getId().toString());
                attributeModifier.addProperty("value", modifier.getAmount());
                attributeModifier.addProperty("operation", modifier.getOperation().toString());

                modifiers.add(attributeModifier);
            }
            effect.add("modifiers", modifiers);

            effects.add(effectRL.toString(), effect);
        }
        return effects;
    }
}
