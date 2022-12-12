package com.mike.ingenieur.screen;

import com.mike.ingenieur.Ingenieur;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypesRegistry {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Ingenieur.MODID);

    public static final RegistryObject<MenuType<PickaxeSimulatorMenu>> PICKAXE_SIMULATOR_MENU =
            registerMenuType(PickaxeSimulatorMenu::new, "pickaxe_simulator_menu");

    public static final RegistryObject<MenuType<AsteroidMinerMenu>> ASTEROID_MINER_MENU =
            registerMenuType(AsteroidMinerMenu::new, "asteroid_miner_menu");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }

}
