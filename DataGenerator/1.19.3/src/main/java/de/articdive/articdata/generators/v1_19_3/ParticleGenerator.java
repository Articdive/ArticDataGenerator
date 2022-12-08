package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_19_3.common.DataGenerator_1_19_3;
import java.lang.reflect.Field;
import java.util.Comparator;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
public final class ParticleGenerator extends DataGenerator_1_19_3<ParticleType<?>> {
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
        JsonObject particles = new JsonObject();

        for (ResourceLocation particleRL : PARTICLE_TYPE_REGISTRY.keySet().stream().sorted(Comparator.comparingInt(value -> PARTICLE_TYPE_REGISTRY.getId(PARTICLE_TYPE_REGISTRY.get(value)))).toList()) {
            ParticleType<?> pt = PARTICLE_TYPE_REGISTRY.get(particleRL);
            if (pt == null) {
                continue;
            }
            JsonObject particle = new JsonObject();

            particle.addProperty("id", PARTICLE_TYPE_REGISTRY.getId(pt));
            particle.addProperty("mojangName", names.get(pt));
            particles.add(particleRL.toString(), particle);
        }
        return particles;
    }
}
