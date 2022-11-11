package com.mike.ingenieur.block;

import com.mike.ingenieur.Ingenieur;
import com.mike.ingenieur.block.custom.PickaxeSimulatorBlock;
import com.mike.ingenieur.item.ItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Ingenieur.MODID);

    public static final RegistryObject<Block> MACHINE_CORE = registerSimpleBlock("machine_core",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)), CreativeModeTab.TAB_MISC);

    public static final RegistryObject<Block> PICKAXE_SIMULATOR = registerSimpleBlock("pickaxe_simulator",
        () -> new PickaxeSimulatorBlock(BlockBehaviour.Properties.of(Material.AMETHYST)), CreativeModeTab.TAB_MISC);

    private static <T extends Block> RegistryObject<T> registerSimpleBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return  toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){
        return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void registerBlocks(IEventBus modBusEvent){
        BLOCKS.register(modBusEvent);
    }

}
