package net.finalzenix.firsttestmod.entity;

import net.finalzenix.firsttestmod.FirstTestMod;
import net.finalzenix.firsttestmod.entity.custom.FirstEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, FirstTestMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<FirstEntity>> FIRST_ENTITY =
            ENTITY_TYPES.register("first_entity", () -> EntityType.Builder.of(FirstEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f) // Hitbox size (Width, Height) - similar to a Zombie
                    .build("first_entity"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
