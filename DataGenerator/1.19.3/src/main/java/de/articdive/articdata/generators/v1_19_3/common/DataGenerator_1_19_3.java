package de.articdive.articdata.generators.v1_19_3.common;

import de.articdive.articdata.datagen.DataGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

public abstract class DataGenerator_1_19_3<T> extends DataGenerator<T> {
    protected static Registry<Attribute> ATTRIBUTE_REGISTRY = getRegistry(Registries.ATTRIBUTE);
    protected static Registry<Block> BLOCK_REGISTRY = getRegistry(Registries.BLOCK);
    protected static Registry<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY = getRegistry(Registries.BLOCK_ENTITY_TYPE);
    protected static Registry<ResourceLocation> CUSTOM_STAT_REGISTRY = getRegistry(Registries.CUSTOM_STAT);
    protected static Registry<Enchantment> ENCHANTMENT_REGISTRY = getRegistry(Registries.ENCHANTMENT);
    protected static Registry<EntityType<?>> ENTITY_TYPE_REGISTRY = getRegistry(Registries.ENTITY_TYPE);
    protected static Registry<Fluid> FLUID_REGISTRY = getRegistry(Registries.FLUID);
    protected static Registry<GameEvent> GAME_EVENT_REGISTRY = getRegistry(Registries.GAME_EVENT);
    protected static Registry<Item> ITEM_REGISTRY = getRegistry(Registries.ITEM);
    protected static Registry<MobEffect> MOB_EFFECT_REGISTRY = getRegistry(Registries.MOB_EFFECT);
    protected static Registry<ParticleType<?>> PARTICLE_TYPE_REGISTRY = getRegistry(Registries.PARTICLE_TYPE);
    protected static Registry<Potion> POTION_REGISTRY = getRegistry(Registries.POTION);
    protected static Registry<SoundEvent> SOUND_EVENT_REGISTRY = getRegistry(Registries.SOUND_EVENT);
    protected static Registry<VillagerProfession> VILLAGER_PROFESSION_REGISTRY = getRegistry(Registries.VILLAGER_PROFESSION);
    protected static Registry<VillagerType> VILLAGER_TYPE_REGISTRY = getRegistry(Registries.VILLAGER_TYPE);


    @SuppressWarnings("unchecked")
    private static <T> Registry<T> getRegistry(ResourceKey<Registry<T>> rl) {
        return (Registry<T>) BuiltInRegistries.REGISTRY.get(rl.location());
    }
}
