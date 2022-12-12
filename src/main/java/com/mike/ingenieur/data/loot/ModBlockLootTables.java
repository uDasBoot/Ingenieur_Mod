package com.mike.ingenieur.data.loot;

import com.mike.ingenieur.block.BlockRegistry;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTables extends BlockLoot {

    @Override
    protected void addTables() {
        this.dropSelf(BlockRegistry.MACHINE_CORE.get());
        this.dropSelf(BlockRegistry.PICKAXE_SIMULATOR.get());
        this.dropSelf(BlockRegistry.ASTEROID_MINER.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
