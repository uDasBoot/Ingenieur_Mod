package com.mike.ingenieur.block.custom;

import com.mike.ingenieur.block.entity.AsteroidMinerBlockEntity;
import com.mike.ingenieur.block.entity.BlockEntityRegistry;
import com.mike.ingenieur.block.entity.PickaxeSimulatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class AsteroidMinerBlock extends IngenieurEntityBlock {

    public AsteroidMinerBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AsteroidMinerBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult result) {
        if(!level.isClientSide()){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof AsteroidMinerBlockEntity){
                NetworkHooks.openScreen(((ServerPlayer) player), (AsteroidMinerBlockEntity) blockEntity, pos);
            } else {
                throw new IllegalStateException("Container Provider missing for Pickaxe Simulator!");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityRegistry.ASTEROID_MINER.get(),
                AsteroidMinerBlockEntity::tick);
    }
}
