package com.mike.ingenieur.item;

import com.mike.ingenieur.Ingenieur;
import com.mike.ingenieur.item.custom.MinerDrone;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Ingenieur.MODID);

    public static final RegistryObject<Item> MACHINE_CONTROLLER = ITEMS.register("machine_controller",
            () -> new Item(new Item.Properties().tab(Ingenieur.INGENIEUR_TAB)));

    public static final RegistryObject<Item> MINER_DRONE = ITEMS.register("miner_drone",
            () -> new MinerDrone(new Item.Properties().tab(Ingenieur.INGENIEUR_TAB).stacksTo(1)));

    public static void registerItems(IEventBus modBusEvent){
        ITEMS.register(modBusEvent);
    }

}
