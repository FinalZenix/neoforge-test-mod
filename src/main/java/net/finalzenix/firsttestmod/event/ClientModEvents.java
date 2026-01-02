package net.finalzenix.firsttestmod.event;

import net.finalzenix.firsttestmod.FirstTestMod;
import net.finalzenix.firsttestmod.entity.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.finalzenix.firsttestmod.entity.client.BrokenScriptRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;

@EventBusSubscriber(modid = FirstTestMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.FIRST_ENTITY.get(), BrokenScriptRenderer::new);
    }
}
