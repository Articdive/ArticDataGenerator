package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_16_3.common.DataGenerator;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
public final class ParticleGenerator extends DataGenerator<ParticleType<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParticleGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : ParticleTypes.class.getDeclaredFields()) {
            if (!ParticleType.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                ParticleType<?> pt = (ParticleType<?>) declaredField.get(null);
                names.put(pt, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map particle naming system", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        List<ResourceLocation> particleRLs = Registry.PARTICLE_TYPE.keySet().stream().sorted(Comparator.comparingInt(value -> Registry.PARTICLE_TYPE.getId(Registry.PARTICLE_TYPE.get(value)))).toList();;
        JsonObject particles = new JsonObject();

        for (ResourceLocation particleRL : particleRLs) {
            ParticleType<?> pt = Registry.PARTICLE_TYPE.get(particleRL);
            if (pt == null) {
                continue;
            }
            JsonObject particle = new JsonObject();

            particle.addProperty("id", Registry.PARTICLE_TYPE.getId(pt));
            particle.addProperty("mojangName", names.get(pt));
            particles.add(particleRL.toString(), particle);
        }
        return particles;
    }
}
