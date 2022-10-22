package com.mike.ingenieur.block;

import com.mike.ingenieur.Ingenieur;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Ingenieur.MODID);

    public static void registerBlocks(IEventBus modBusEvent){
        BLOCKS.register(modBusEvent);
    }

}
