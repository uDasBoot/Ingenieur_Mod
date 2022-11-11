package com.mike.ingenieur.item;

import com.mike.ingenieur.Ingenieur;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Ingenieur.MODID);

    public static final RegistryObject<Item> machine_controller = ITEMS.register("machine_controller",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));

    public static void registerItems(IEventBus modBusEvent){
        ITEMS.register(modBusEvent);
    }

}
