package com.mike.ingenieur.item;

import com.mike.ingenieur.Ingenieur;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Ingenieur.MODID);

    public static void registerItems(IEventBus modBusEvent){
        ITEMS.register(modBusEvent);
    }

}
