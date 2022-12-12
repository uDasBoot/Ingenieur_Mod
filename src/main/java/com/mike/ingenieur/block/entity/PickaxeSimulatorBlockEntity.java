package com.mike.ingenieur.block.entity;

import com.mike.ingenieur.screen.AsteroidMinerMenu;
import com.mike.ingenieur.screen.PickaxeSimulatorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PickaxeSimulatorBlockEntity extends IngenieurBlockEntity {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public PickaxeSimulatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.PICKAXE_SIMULATOR.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Pickaxe Simulator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new PickaxeSimulatorMenu(id, inv, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for(int i=0; i<itemStackHandler.getSlots(); i++){
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


    public static void tick(Level level, BlockPos blockPos, BlockState state, IngenieurBlockEntity entity) {
        if(level.isClientSide()){
            return;
        }
        if(entity.canWork(level, blockPos, state, entity)) {
            entity.progress++;
            entity.extractEnergy();
            setChanged(level, blockPos, state);
            if (entity.progress >= entity.maxProgress) {
                entity.doWork(level, blockPos, state, entity);
            }
        }else {
            entity.resetProgress();
            entity.setChanged(level, blockPos, state);
        }
    }

    @Override
    public boolean canWork(Level level, BlockPos blockPos, BlockState state, IngenieurBlockEntity entity) {
        BlockPos toBreak = blockPos.relative(entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING));
        if(hasPickaxe((PickaxeSimulatorBlockEntity) entity) && entity.hasEnoughEnergy()){
            if(!level.getBlockState(toBreak).getBlock().equals(Blocks.AIR)){
                return true;
            }
        }
        return false;
    }

    private static boolean hasPickaxe(PickaxeSimulatorBlockEntity entity) {
        return entity.itemStackHandler.getStackInSlot(0).getItem() instanceof PickaxeItem;
    }

    @Override
    public void doWork(Level level, BlockPos blockPos, BlockState state, IngenieurBlockEntity entity) {
        PickaxeSimulatorBlockEntity blockEntity = (PickaxeSimulatorBlockEntity) entity;
        BlockPos toBreak = blockPos.relative(entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING));
            List<ItemStack> drops = Block.getDrops(level.getBlockState(toBreak),
                    (ServerLevel) level,
                    toBreak,
                    null,
                    null,
                    blockEntity.itemStackHandler.getStackInSlot(0));
            drops.forEach(itemStack -> {
                level.addFreshEntity(new ItemEntity(level, toBreak.getX(), toBreak.getY(), toBreak.getZ(), itemStack));
            });
            level.destroyBlock(toBreak, false);
        blockEntity.itemStackHandler.getStackInSlot(0).setDamageValue(blockEntity.itemStackHandler.getStackInSlot(0).getDamageValue() + 1);

        entity.resetProgress();
    }

}
