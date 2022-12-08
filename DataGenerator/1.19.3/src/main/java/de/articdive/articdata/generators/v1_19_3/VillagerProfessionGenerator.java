package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_19_3.common.DataGenerator_1_19_3;
import java.lang.reflect.Field;
import java.util.Comparator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Work Sound", supported = true)
public final class VillagerProfessionGenerator extends DataGenerator_1_19_3<VillagerProfession> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VillagerProfessionGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : VillagerProfession.class.getDeclaredFields()) {
            if (!VillagerProfession.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                VillagerProfession vp = (VillagerProfession) declaredField.get(null);
                names.put(vp, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map villager profession naming system", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        JsonObject villagerProfessions = new JsonObject();

        for (ResourceLocation villagerProfessionRL : VILLAGER_PROFESSION_REGISTRY.keySet().stream().sorted(Comparator.comparingInt(value -> VILLAGER_PROFESSION_REGISTRY.getId(VILLAGER_PROFESSION_REGISTRY.get(value)))).toList()) {
            VillagerProfession vp = VILLAGER_PROFESSION_REGISTRY.get(villagerProfessionRL);

            JsonObject villagerProfession = new JsonObject();

            villagerProfession.addProperty("id", VILLAGER_PROFESSION_REGISTRY.getId(vp));
            villagerProfession.addProperty("mojangName", names.get(vp));

            SoundEvent workSound = vp.workSound();
            if (workSound != null) {
                ResourceLocation workSoundRL = SOUND_EVENT_REGISTRY.getKey(workSound);
                if (workSoundRL != null) {
                    villagerProfession.addProperty("workSound", workSoundRL.toString());
                }
            }

            villagerProfessions.add(villagerProfessionRL.toString(), villagerProfession);
        }
        return villagerProfessions;
    }
}
