package com.troblecodings.tcredstone.item;

import java.util.function.BiPredicate;

import com.troblecodings.linkableapi.Linkingtool;
import com.troblecodings.tcredstone.tile.TileRedstoneEmitter;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RemoteActivator extends Linkingtool {

    public RemoteActivator(final CreativeModeTab tab,
            final BiPredicate<Level, BlockPos> predicate) {
        super(tab, predicate, _u -> false);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player,
            final InteractionHand hand) {
        final ItemStack itemstack = player.getItemInHand(hand);
        final CompoundTag tag = itemstack.getOrCreateTag();
        if (tag.contains(LINKINGTOOL_TAG)) {
            if (!hand.equals(InteractionHand.MAIN_HAND) || level.isClientSide)
                return InteractionResultHolder.pass(itemstack);
            final CompoundTag comp = tag.getCompound(LINKINGTOOL_TAG);
            final boolean containsPos =
                    comp.contains("X") && comp.contains("Y") && comp.contains("Z");
            if (containsPos) {
                final BlockPos linkpos = NbtUtils.readBlockPos(comp);
                final boolean state = TileRedstoneEmitter.redstoneUpdate(linkpos, level);
                message(player, "ra.state", String.valueOf(state));
                return InteractionResultHolder.success(itemstack);
            }
        }
        return InteractionResultHolder.success(itemstack);
    }

}
