package com.troblecodings.tcredstone.item;

import java.util.function.BiPredicate;

import com.troblecodings.linkableapi.Linkingtool;
import com.troblecodings.tcredstone.tile.TileRedstoneEmitter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RemoteActivator extends Linkingtool {

    public RemoteActivator(final ItemGroup tab, final BiPredicate<World, BlockPos> predicate) {
        super(tab, predicate, _u1 -> false);
    }

    @Override
    public TypedActionResult<ItemStack> use(final World level, final PlayerEntity player,
            final Hand hand) {
        final ItemStack itemstack = player.getStackInHand(hand);
        final NbtCompound tag = itemstack.getOrCreateNbt();
        if (tag.contains(LINKINGTOOL_TAG)) {
            if (!hand.equals(Hand.MAIN_HAND) || level.isClient())
                return TypedActionResult.pass(itemstack);
            final NbtCompound comp = tag.getCompound(LINKINGTOOL_TAG);
            final boolean containsPos =
                    comp.contains("X") && comp.contains("Y") && comp.contains("Z");
            if (containsPos) {
                final BlockPos linkpos = NbtHelper.toBlockPos(comp);
                final boolean state = TileRedstoneEmitter.redstoneUpdate(linkpos, level);
                message(player, "ra.state", String.valueOf(state));
                return TypedActionResult.success(itemstack);
            }
        }
        return TypedActionResult.success(itemstack);
    }
}
