package com.troblecodings.tcredstone.block;

import java.util.List;

import com.troblecodings.linkableapi.Message;
import com.troblecodings.tcredstone.init.GIRCInit;
import com.troblecodings.tcredstone.tile.TileRedstoneMultiEmitter;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockRedstoneMultiEmitter extends BlockRedstoneEmitter implements Message {

    public BlockRedstoneMultiEmitter(final Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new TileRedstoneMultiEmitter(pos, state);
    }

    @Override
    public InteractionResult use(final BlockState state, final Level world, final BlockPos pos,
            final Player player, final InteractionHand hand, final BlockHitResult hit) {
        if (world.isClientSide)
            return InteractionResult.PASS;
        if (player.getItemInHand(hand).getItem().equals(GIRCInit.RS_LINKER.get()))
            return InteractionResult.PASS;
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof TileRedstoneMultiEmitter) {
            final TileRedstoneMultiEmitter emitter = (TileRedstoneMultiEmitter) entity;
            final List<BlockPos> listOfPositions = emitter.getLinkedPos();
            if (listOfPositions == null) {
                message(player, "em.notlinked");
            } else {
                if (player.isCrouching()) {
                    emitter.unlink();
                    listOfPositions.forEach(blockpos -> message(player, "em.unlink",
                            blockpos.getX(), blockpos.getY(), blockpos.getZ()));
                } else {
                    listOfPositions.forEach(blockpos -> message(player, "lt.linkedpos",
                            blockpos.getX(), blockpos.getY(), blockpos.getZ()));
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void neighborChanged(final BlockState state, final Level world, final BlockPos pos,
            final Block blockIn, final BlockPos fromPos, final boolean isMoving) {
        if (world.isClientSide)
            return;
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof TileRedstoneMultiEmitter) {
            final TileRedstoneMultiEmitter emitter = (TileRedstoneMultiEmitter) entity;
            emitter.redstoneUpdate(world.hasNeighborSignal(pos));
        }
    }

}
