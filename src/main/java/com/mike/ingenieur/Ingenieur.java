package com.mike.ingenieur;

import com.mike.ingenieur.block.BlockRegistry;
import com.mike.ingenieur.block.entity.BlockEntityRegistry;
import com.mike.ingenieur.item.ItemRegistry;
import com.mike.ingenieur.networking.IngenieurMessages;
import com.mike.ingenieur.screen.AsteroidMinerScreen;
import com.mike.ingenieur.screen.MenuTypesRegistry;
import com.mike.ingenieur.screen.PickaxeSimulatorScreen;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Ingenieur.MODID)
public class Ingenieur
{
    public static final String MODID = "ingenieur";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CreativeModeTab INGENIEUR_TAB = new CreativeModeTab("Ingenieur") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.MACHINE_CONTROLLER.get());
        }
    };

    public Ingenieur()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BlockRegistry.registerBlocks(modEventBus);

        ItemRegistry.registerItems(modEventBus);

        BlockEntityRegistry.registerBlockEntities(modEventBus);

        MenuTypesRegistry.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            IngenieurMessages.register();
        });
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientStartup(FMLClientSetupEvent event){
            MenuScreens.register(MenuTypesRegistry.PICKAXE_SIMULATOR_MENU.get(), PickaxeSimulatorScreen::new);
            MenuScreens.register(MenuTypesRegistry.ASTEROID_MINER_MENU.get(), AsteroidMinerScreen::new);
        }
    }
}
