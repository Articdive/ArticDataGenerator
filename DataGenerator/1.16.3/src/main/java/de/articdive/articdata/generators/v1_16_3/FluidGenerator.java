package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenHolder;
import de.articdive.articdata.datagen.DataGenType;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_16_3.common.DataGenerator;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Corresponding Bucket Item", supported = true)
public final class FluidGenerator extends DataGenerator<Fluid> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FluidGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : Fluids.class.getDeclaredFields()) {
            if (!Fluid.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                Fluid f = (Fluid) declaredField.get(null);
                names.put(f, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map fluid naming system.", e);
                return;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonObject generate() {
        final Map<Property<?>, String> fsPropertyNames = (Map<Property<?>, String>) DataGenHolder.getNameMap(DataGenType.FLUID_PROPERTIES);

        List<ResourceLocation> fluidRLs = Registry.FLUID.keySet().stream().sorted(Comparator.comparingInt(value -> Registry.FLUID.getId(Registry.FLUID.get(value)))).toList();
        JsonObject fluids = new JsonObject();

        for (ResourceLocation fluidRL : fluidRLs) {
            Fluid f = Registry.FLUID.get(fluidRL);

            JsonObject fluid = new JsonObject();
            fluid.addProperty("id", Registry.FLUID.getId(f));
            fluid.addProperty("mojangName", names.get(f));
            fluid.addProperty("bucketId", Registry.ITEM.getKey(f.getBucket()).toString());
            fluid.addProperty("empty", f.defaultFluidState().isEmpty());
            fluid.addProperty("randomlyTicking", f.defaultFluidState().isRandomlyTicking());
            fluid.addProperty("explosionResistance", f.defaultFluidState().getExplosionResistance());
            fluid.addProperty("defaultStateId", Fluid.FLUID_STATE_REGISTRY.getId(f.defaultFluidState()));

            {
                final JsonArray properties = new JsonArray();
                for (final Property<?> p : f.getStateDefinition().getProperties()) {
                    properties.add(fsPropertyNames.get(p));
                }
                fluid.add("properties", properties);
            }
            {
                // Fluid states
                final JsonArray fluidStates = new JsonArray();
                for (final FluidState fs : f.getStateDefinition().getPossibleStates()) {
                    final JsonObject state = new JsonObject();

                    {
                        final JsonObject properties = new JsonObject();
                        for (final Map.Entry<Property<?>, Comparable<?>> entry : fs.getValues().entrySet()) {
                            final Class<?> valClass = entry.getKey().getValueClass();
                            if (valClass.equals(Integer.class)) {
                                properties.addProperty(entry.getKey().getName(), (Integer) entry.getValue());
                            } else if (valClass.equals(Boolean.class)) {
                                properties.addProperty(entry.getKey().getName(), (Boolean) entry.getValue());
                            } else {
                                properties.addProperty(entry.getKey().getName(), String.valueOf(entry.getValue()));
                            }
                        }
                        state.add("properties", properties);
                    }

                    state.addProperty("stateId", Fluid.FLUID_STATE_REGISTRY.getId(fs));

                    // Default values
                    state.addProperty("source", fs.isSource());
                    state.addProperty("height", fs.getHeight(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
                    state.addProperty("ownHeight", fs.getOwnHeight());
                    state.addProperty("amount", fs.getAmount());
                    state.addProperty("blockState", Registry.BLOCK.getKey(fs.createLegacyBlock().getBlock()).toString());

                    fluidStates.add(state);
                }
                fluid.add("states", fluidStates);
            }

            fluids.add(fluidRL.toString(), fluid);
        }
        return fluids;
    }
}
