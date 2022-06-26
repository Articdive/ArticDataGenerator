package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.ReflectionHelper;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_16_3.common.DataGenerator;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Translation Key", supported = true)
@GeneratorEntry(name = "Default Value", supported = true)
@GeneratorEntry(name = "Client Synchronization", supported = true)
@GeneratorEntry(name = "Attribute Range", supported = true)
public final class AttributeGenerator extends DataGenerator<Attribute> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : Attributes.class.getDeclaredFields()) {
            if (!Attribute.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                Attribute a = (Attribute) declaredField.get(null);
                names.put(a, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map attribute naming system.", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        List<ResourceLocation> attributeRLs = Registry.ATTRIBUTE.keySet().stream().sorted().toList();
        JsonObject attributes = new JsonObject();

        for (ResourceLocation attributeRL : attributeRLs) {
            Attribute a = Registry.ATTRIBUTE.get(attributeRL);

            JsonObject attribute = new JsonObject();
            if (a == null) {
                continue;
            }
            attribute.addProperty("id", Registry.ATTRIBUTE.getId(a));
            attribute.addProperty("mojangName", names.get(a));
            attribute.addProperty("translationKey", a.getDescriptionId());
            attribute.addProperty("defaultValue", a.getDefaultValue());
            attribute.addProperty("clientSync", a.isClientSyncable());
            if (a instanceof RangedAttribute ra) {
                // Unfortuantely get via reflection
                JsonObject range = new JsonObject();
                range.addProperty("maxValue", ReflectionHelper.getHiddenField(double.class, "maxValue", RangedAttribute.class, ra));
                range.addProperty("minValue", ReflectionHelper.getHiddenField(double.class, "minValue", RangedAttribute.class, ra));
                attribute.add("range", range);
            }

            attributes.add(attributeRL.toString(), attribute);
        }
        return attributes;
    }
}
