package com.mike.ingenieur.data;

import com.mike.ingenieur.Ingenieur;
import com.mike.ingenieur.block.BlockRegistry;
import com.mike.ingenieur.item.ItemRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Ingenieur.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ItemRegistry.MACHINE_CONTROLLER.get());
        handheldItem(ItemRegistry.MINER_DRONE.get());
        withExistingParent(BlockRegistry.MACHINE_CORE.get().asItem().toString(), new ResourceLocation(Ingenieur.MODID, "block/machine_core"));
        withExistingParent(BlockRegistry.PICKAXE_SIMULATOR.get().asItem().toString(), new ResourceLocation(Ingenieur.MODID, "block/pickaxe_simulator"));
        withExistingParent(BlockRegistry.ASTEROID_MINER.get().asItem().toString(), new ResourceLocation(Ingenieur.MODID, "block/asteroid_miner"));
    }

    private ItemModelBuilder simpleItem(Item item){
        return withExistingParent(item.toString(), new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Ingenieur.MODID, "item/" + item.toString()));
    }

    private ItemModelBuilder handheldItem(Item item){
        return withExistingParent(item.toString(), new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Ingenieur.MODID, "item/" + item.toString()));
    }
}
