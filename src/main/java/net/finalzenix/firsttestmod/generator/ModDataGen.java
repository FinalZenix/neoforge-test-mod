package net.finalzenix.firsttestmod.generator;

import net.finalzenix.firsttestmod.FirstTestMod;
import net.finalzenix.firsttestmod.recipe.ModRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = FirstTestMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDataGen{

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        //Get the helpers from the event
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //Add your Recipe Provider
        generator.addProvider(
                event.includeServer(),
                new ModRecipeProvider(packOutput, lookupProvider)
        );
    }

}
