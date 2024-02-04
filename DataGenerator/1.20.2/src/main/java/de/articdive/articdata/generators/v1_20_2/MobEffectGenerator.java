package de.articdive.articdata.generators.v1_20_2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.ReflectionHelper;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_19_3.common.DataGenerator_1_19_3;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.AttributeModifierTemplate;
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

            JsonArray modifiers = new JsonArray();
            for (Map.Entry<Attribute, AttributeModifierTemplate> attributeModifierEntry : me.getAttributeModifiers().entrySet()) {
                Attribute attribute = attributeModifierEntry.getKey();
                AttributeModifierTemplate modifierTemplate = attributeModifierEntry.getValue();

                JsonObject attributeModifier = new JsonObject();
                attributeModifier.addProperty("attribute", ATTRIBUTE_REGISTRY.getKey(attribute).toString());

                // Go through every subclass
                Class<?> modifier1 = ReflectionHelper.getHiddenClass(MobEffect.class, "MobEffectAttributeModifierTemplate");


                if (modifier1.isInstance(modifierTemplate)) {
                    attributeModifier.addProperty("uuid", modifierTemplate.getAttributeModifierId().toString());
                    attributeModifier.addProperty("value", ReflectionHelper.getHiddenField(double.class, "amount", modifier1.asSubclass(Object.class), modifierTemplate));
                    attributeModifier.addProperty("operation", ReflectionHelper.getHiddenField(AttributeModifier.Operation.class, "operation", modifier1.asSubclass(Object.class), modifierTemplate).toString());
                }

                modifiers.add(attributeModifier);
            }
            effect.add("modifiers", modifiers);

            effects.add(effectRL.toString(), effect);
        }
        return effects;
    }
}
