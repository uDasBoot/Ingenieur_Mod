package com.mike.ingenieur.data;

import com.mike.ingenieur.Ingenieur;
import com.mike.ingenieur.block.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Ingenieur.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        axisBlock((RotatedPillarBlock) BlockRegistry.MACHINE_CORE.get(), new ResourceLocation(Ingenieur.MODID, "block/machine_core_side"),
                new ResourceLocation(Ingenieur.MODID, "block/machine_core_top"));
    }
}
