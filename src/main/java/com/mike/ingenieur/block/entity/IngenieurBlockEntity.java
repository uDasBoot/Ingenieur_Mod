package com.mike.ingenieur.block.entity;

import com.mike.ingenieur.networking.IngenieurMessages;
import com.mike.ingenieur.networking.packet.EnergySyncS2CPacket;
import com.mike.ingenieur.util.EnergyStorageIngenieur;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class IngenieurBlockEntity extends BlockEntity implements MenuProvider {

    protected final ContainerData data;
    public int progress = 0;
    public int maxProgress = 78;

    private final EnergyStorageIngenieur energyStorage = new EnergyStorageIngenieur(100000, 1000) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            IngenieurMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };

    private static final int ENERGY_REQ = 200;

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    public IngenieurBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> IngenieurBlockEntity.this.progress;
                    case 1 -> IngenieurBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0 -> IngenieurBlockEntity.this.progress = value;
                    case 1 -> IngenieurBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public void setEnergyLevel(int energy) {
        this.energyStorage.setEnergy(energy);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY){
            return lazyEnergyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("pickaxe_simulator.progress", progress);
        nbt.putInt("pickaxe_simulator.energy", energyStorage.getEnergyStored());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.progress = nbt.getInt("pickaxe_simulator.progress");
        energyStorage.setEnergy(nbt.getInt("pickaxe_simulator.energy"));
    }

    public abstract void drops();

    public void extractEnergy() {
        this.energyStorage.extractEnergy(ENERGY_REQ, false);
    }

    public boolean hasEnoughEnergy() {
        return this.energyStorage.getEnergyStored() >= ENERGY_REQ;
    }

    public abstract void doWork(Level level, BlockPos blockPos, BlockState state, IngenieurBlockEntity entity);

    public abstract boolean canWork(Level level, BlockPos blockPos, BlockState state, IngenieurBlockEntity entity);

    public void resetProgress() {
        this.progress = 0;
    }
}
