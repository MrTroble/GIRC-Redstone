package com.troblecodings.tcredstone.tile;

import java.util.ArrayList;
import java.util.List;

import com.troblecodings.linkableapi.ILinkableTile;
import com.troblecodings.tcredstone.block.BlockRedstoneAcceptor;
import com.troblecodings.tcredstone.init.GIRCInit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileRedstoneMultiEmitter extends BlockEntity implements ILinkableTile {

    private List<BlockPos> listOfPositions = new ArrayList<>();

    private static final String LINKED_POS_LIST = "linkedPos";

    public TileRedstoneMultiEmitter(final BlockPos pos, final BlockState state) {
        super(GIRCInit.MULTI_EMITER_TILE.get(), pos, state);
    }

    public CompoundTag writeBlockPosToNBT(final List<BlockPos> pos, final CompoundTag compound) {
        if (pos != null && compound != null) {

            final ListTag list = new ListTag();
            listOfPositions.forEach(blockpos -> {
                final CompoundTag item = NbtUtils.writeBlockPos(blockpos);
                list.add(item);
            });
            compound.put(LINKED_POS_LIST, list);
        }
        return compound;
    }

    public List<BlockPos> readBlockPosFromNBT(final CompoundTag compound) {
        final ListTag list = (ListTag) compound.get(LINKED_POS_LIST);
        if (list != null) {
            listOfPositions.clear();
            list.forEach(pos -> {
                final CompoundTag item = (CompoundTag) pos;
                listOfPositions.add(NbtUtils.readBlockPos(item));
            });
            return listOfPositions;
        }
        return null;
    }

    @Override
    public boolean hasLink() {
        return !listOfPositions.isEmpty();
    }

    @Override
    public boolean link(final BlockPos pos) {
        if (pos != null && !listOfPositions.contains(pos)) {
            listOfPositions.add(pos);
            return true;
        }
        return false;
    }

    @Override
    public boolean unlink() {
        if (listOfPositions.isEmpty())
            return false;
        listOfPositions.clear();
        return true;
    }

    public List<BlockPos> getLinkedPos() {
        return this.listOfPositions;
    }

    @Override
    public void load(final CompoundTag compound) {
        super.load(compound);
        this.listOfPositions = readBlockPosFromNBT(compound);
    }

    @Override
    protected void saveAdditional(final CompoundTag compound) {
        super.saveAdditional(compound);
        writeBlockPosToNBT(listOfPositions, compound);
    }

    public void redstoneUpdate(final boolean enabled) {
        listOfPositions.forEach(blockpos -> redstoneUpdate(enabled, blockpos, level));
    }

    public static boolean redstoneUpdate(final boolean enabled, final BlockPos linkedpos,
            final Level level) {
        if (linkedpos != null) {
            final BlockState state = level.getBlockState(linkedpos);
            if (state.getBlock() instanceof BlockRedstoneAcceptor) {
                level.setBlock(linkedpos, state.setValue(BlockRedstoneAcceptor.POWER, enabled), 3);
            }
        }
        return enabled;
    }

    public static boolean redstoneUpdate(final BlockPos linkedpos, final Level level) {
        if (linkedpos != null) {
            final BlockState state = level.getBlockState(linkedpos);
            if (state.getBlock() instanceof BlockRedstoneAcceptor) {
                final boolean newState = !state.getValue(BlockRedstoneAcceptor.POWER);
                level.setBlock(linkedpos, state.setValue(BlockRedstoneAcceptor.POWER, newState), 3);
                return newState;
            }
        }
        return false;
    }
}
