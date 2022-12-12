package com.mike.ingenieur.block.entity;

import com.mike.ingenieur.Ingenieur;
import com.mike.ingenieur.block.BlockRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Ingenieur.MODID);

    public static final RegistryObject<BlockEntityType<PickaxeSimulatorBlockEntity>> PICKAXE_SIMULATOR =
            BLOCK_ENTITIES.register("pickaxe_simulator", () ->
                    BlockEntityType.Builder.of(PickaxeSimulatorBlockEntity::new,
                            BlockRegistry.PICKAXE_SIMULATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<AsteroidMinerBlockEntity>> ASTEROID_MINER =
            BLOCK_ENTITIES.register("asteroid_miner", () ->
                    BlockEntityType.Builder.of(AsteroidMinerBlockEntity::new,
                            BlockRegistry.ASTEROID_MINER.get()).build(null));


    public static void registerBlockEntities(IEventBus modEventBus){
        BLOCK_ENTITIES.register(modEventBus);
    }

}
