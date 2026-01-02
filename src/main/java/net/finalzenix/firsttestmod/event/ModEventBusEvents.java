package net.finalzenix.firsttestmod.event;

import net.finalzenix.firsttestmod.FirstTestMod;
import net.finalzenix.firsttestmod.entity.ModEntities;
import net.finalzenix.firsttestmod.entity.custom.FirstEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = FirstTestMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.FIRST_ENTITY.get(), FirstEntity.createAttributes().build());
    }
}
