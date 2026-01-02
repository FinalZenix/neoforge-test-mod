package net.finalzenix.firsttestmod.item;

import net.finalzenix.firsttestmod.FirstTestMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FirstTestMod.MODID);
    public static final DeferredItem<Item> BISMUTH = ITEMS.register("bismuth",
            () -> new Item(new Item.Properties())
    );

    public static final DeferredItem<Item> RAW_BISMUTH = ITEMS.register("raw_bismuth",
            () -> new Item(new Item.Properties())
    );

    public static ArrayList<DeferredItem<Item>> arrayItems = new ArrayList<>(List.of(BISMUTH, RAW_BISMUTH));
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
