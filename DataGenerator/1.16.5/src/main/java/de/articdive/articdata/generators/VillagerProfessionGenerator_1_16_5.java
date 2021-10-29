package de.articdive.articdata.generators;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.common.DataGenerator_1_16_5;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Set;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Work Sound", supported = true)
public final class VillagerProfessionGenerator_1_16_5 extends DataGenerator_1_16_5<VillagerProfession> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VillagerProfessionGenerator_1_16_5.class);

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
        Set<ResourceLocation> villagerProfessionRLs = Registry.VILLAGER_PROFESSION.keySet();
        JsonObject villagerProfessions = new JsonObject();

        for (ResourceLocation villagerProfessionRL : villagerProfessionRLs) {
            VillagerProfession vp = Registry.VILLAGER_PROFESSION.get(villagerProfessionRL);

            JsonObject villagerProfession = new JsonObject();

            villagerProfession.addProperty("id", Registry.VILLAGER_PROFESSION.getId(vp));
            villagerProfession.addProperty("mojangName", names.get(vp));

            SoundEvent workSound = vp.getWorkSound();
            if (workSound != null) {
                ResourceLocation workSoundRL = Registry.SOUND_EVENT.getKey(workSound);
                if (workSoundRL != null) {
                    villagerProfession.addProperty("workSound", workSoundRL.toString());
                }
            }

            villagerProfessions.add(villagerProfessionRL.toString(), villagerProfession);
        }
        return villagerProfessions;
    }
}
