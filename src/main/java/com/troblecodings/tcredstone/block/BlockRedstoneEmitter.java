package com.troblecodings.tcredstone.block;

import com.troblecodings.linkableapi.Message;
import com.troblecodings.tcredstone.init.GIRCInit;
import com.troblecodings.tcredstone.tile.TileRedstoneEmitter;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockRedstoneEmitter extends Block implements EntityBlock, Message {

    public BlockRedstoneEmitter(final Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(final BlockState state, final Level world, final BlockPos pos,
            final Player player, final InteractionHand hand, final BlockHitResult hit) {
        if (world.isClientSide)
            return InteractionResult.PASS;
        if (player.getItemInHand(hand).getItem().equals(GIRCInit.RS_LINKER.get()))
            return InteractionResult.PASS;
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof TileRedstoneEmitter) {
            final TileRedstoneEmitter emitter = (TileRedstoneEmitter) entity;
            final BlockPos linkedpos = emitter.getLinkedPos();
            if (linkedpos == null) {
                message(player, "em.notlinked");
            } else {
                if (player.isCrouching()) {
                    emitter.unlink();
                    message(player, "em.unlink", linkedpos.getX(), linkedpos.getY(),
                            linkedpos.getZ());
                } else {
                    message(player, "lt.linkedpos", linkedpos.getX(), linkedpos.getY(),
                            linkedpos.getZ());
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
        if (entity instanceof TileRedstoneEmitter) {
            final TileRedstoneEmitter emitter = (TileRedstoneEmitter) entity;
            emitter.redstoneUpdate(world.hasNeighborSignal(pos));
        }
    }

    @Override
    public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new TileRedstoneEmitter(pos, state);
    }

    @Override
    public void message(final Player player, final String text, final Object... obj) {
        player.sendSystemMessage(getComponent(text, obj));
    }

    @Override
    public MutableComponent getComponent(final String text, final Object... obj) {
        return MutableComponent.create(new TranslatableContents(text, text, obj));
    }

}
