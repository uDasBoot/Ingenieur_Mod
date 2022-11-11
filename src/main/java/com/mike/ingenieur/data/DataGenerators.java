package com.mike.ingenieur.data;

import com.mike.ingenieur.Ingenieur;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ingenieur.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent dataEvent){
        DataGenerator generator = dataEvent.getGenerator();
        ExistingFileHelper efh = dataEvent.getExistingFileHelper();

        generator.addProvider(true, new ModBlockStateProvider(generator, efh));
        generator.addProvider(true, new ModItemModelProvider(generator, efh));
        generator.addProvider(true, new ModLootTableProvider(generator));
        generator.addProvider(true, new ModRecipeProvider(generator));
    }

}
