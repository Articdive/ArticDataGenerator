package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenerator;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FluidPropertyGenerator extends DataGenerator<Property<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FluidPropertyGenerator.class);
    private static final ScanResult REFLECTIONS = new ClassGraph().acceptPackages("net.minecraft.world.level.material").scan();

    @Override
    public void generateNames() {
        for (final Class<? extends Fluid> fluid : REFLECTIONS.getSubclasses(Fluid.class).loadClasses(Fluid.class)) {
            for (final Field declaredField : fluid.getDeclaredFields()) {
                if (!Modifier.isStatic(declaredField.getModifiers())) continue;
                if (!Property.class.isAssignableFrom(declaredField.getType())) continue;
                try {
                    final Property<?> property = (Property<?>) declaredField.get(null);
                    names.put(property, declaredField.getName());
                } catch (final IllegalAccessException exception) {
                    LOGGER.error("Failed to map fluid state property naming system", exception);
                    return;
                }
            }
        }
    }

    @Override
    public JsonElement generate() {
        final JsonObject fluidProperties = new JsonObject();
        for (final Class<? extends Fluid> fluid : REFLECTIONS.getSubclasses(Fluid.class).loadClasses(Fluid.class)) {
            for (final Field declaredField : fluid.getDeclaredFields()) {
                if (!Modifier.isStatic(declaredField.getModifiers())) continue;
                if (!Property.class.isAssignableFrom(declaredField.getType())) continue;
                final JsonObject propertyData = new JsonObject();
                try {
                    final Property<?> property = (Property<?>) declaredField.get(null);
                    final String fieldName = declaredField.getName();
                    final String propertyKey = property.getName();
                    propertyData.addProperty("key", propertyKey);
                    // Properties
                    final JsonArray values = new JsonArray();
                    if (property instanceof BooleanProperty) {
                        for (final boolean value : ((BooleanProperty) property).getPossibleValues()) {
                            values.add(value);
                        }
                    } else if (property instanceof IntegerProperty) {
                        for (final int value : ((IntegerProperty) property).getPossibleValues()) {
                            values.add(value);
                        }
                    } else if (property instanceof EnumProperty<?>) {
                        propertyData.addProperty("enumMojangName", property.getValueClass().getSimpleName());
                        for (final Enum<? extends Enum<?>> value : ((EnumProperty<? extends Enum<?>>) property).getPossibleValues()) {
                            values.add(value.name());
                        }
                    }
                    propertyData.add("values", values);

                    fluidProperties.add(fieldName, propertyData);
                } catch (final IllegalAccessException exception) {
                    LOGGER.error("Failed to get property from the property mapping system", exception);
                }
            }
        }
        return fluidProperties;
    }
}
