package com.mike.ingenieur.block.entity;

import com.mike.ingenieur.screen.PickaxeSimulatorMenu;
import com.mike.ingenieur.util.EnergyStorageIngenieur;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PickaxeSimulatorBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    private final EnergyStorageIngenieur energyStorage = new EnergyStorageIngenieur(100000, 1000) {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };

    private static final int ENERGY_REQ = 200;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    public PickaxeSimulatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.PICKAXE_SIMULATOR.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> PickaxeSimulatorBlockEntity.this.progress;
                    case 1 -> PickaxeSimulatorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0 -> PickaxeSimulatorBlockEntity.this.progress = value;
                    case 1 -> PickaxeSimulatorBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Pickaxe Simulator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        System.out.println(this.data.get(1));
        return new PickaxeSimulatorMenu(id, inv, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if(cap == ForgeCapabilities.ENERGY){
            return lazyEnergyHandler.cast();
        }

        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);
        lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.putInt("pickaxe_simulator.progress", progress);
        nbt.putInt("pickaxe_simulator.energy", energyStorage.getEnergyStored());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.progress = nbt.getInt("pickaxe_simulator.progress");
        energyStorage.setEnergy(nbt.getInt("pickaxe_simulator.energy"));
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for(int i=0; i<itemStackHandler.getSlots(); i++){
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, PickaxeSimulatorBlockEntity entity) {
        if(level.isClientSide()){
            return;
        }
            if(hasPickaxe(entity) && hasEnoughEnergy(entity)) {
                entity.progress++;
                extractEnergy(entity);
                setChanged(level, blockPos, state);
                if (entity.progress >= entity.maxProgress) {
                    BlockPos toBreak = blockPos.relative(entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING));
                    if(!level.getBlockState(toBreak).getBlock().equals(Blocks.AIR)) {
                        entity.breakBlock((ServerLevel) level, entity, toBreak);
                    } else {
                        entity.resetProgress();
                    }
                }
            }else {
                entity.resetProgress();
                entity.setChanged(level, blockPos, state);
            }
    }

    private static void extractEnergy(PickaxeSimulatorBlockEntity entity) {
        entity.energyStorage.extractEnergy(ENERGY_REQ, false);
    }

    private static boolean hasEnoughEnergy(PickaxeSimulatorBlockEntity entity) {
        return entity.energyStorage.getEnergyStored() >= ENERGY_REQ;
    }

    private static boolean hasPickaxe(PickaxeSimulatorBlockEntity entity) {
        return entity.itemStackHandler.getStackInSlot(0).getItem() instanceof PickaxeItem;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void breakBlock(ServerLevel level, PickaxeSimulatorBlockEntity entity, BlockPos toBreak) {
            List<ItemStack> drops = Block.getDrops(level.getBlockState(toBreak),
                    level,
                    toBreak,
                    null,
                    null,
                    entity.itemStackHandler.getStackInSlot(0));
            drops.forEach(itemStack -> {
                level.addFreshEntity(new ItemEntity(level, toBreak.getX(), toBreak.getY(), toBreak.getZ(), itemStack));
            });
            level.destroyBlock(toBreak, false);
            entity.itemStackHandler.getStackInSlot(0).setDamageValue(entity.itemStackHandler.getStackInSlot(0).getDamageValue() + 1);

        entity.resetProgress();
    }
}
