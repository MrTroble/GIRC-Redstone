package com.troblecodings.tcredstone.item;

import java.util.function.BiPredicate;

import com.troblecodings.linkableapi.Linkingtool;
import com.troblecodings.tcredstone.tile.TileRedstoneEmitter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RemoteActivator extends Linkingtool {

    public RemoteActivator(final ItemGroup tab, final BiPredicate<World, BlockPos> predicate) {
        super(tab, predicate, _u -> false);
    }

    @Override
    public ActionResult<ItemStack> use(final World level, final PlayerEntity player,
            final Hand hand) {
        final ItemStack itemstack = player.getItemInHand(hand);
        final CompoundNBT tag = getOrCreateForStack(itemstack);
        if (tag.contains(LINKINGTOOL_TAG)) {
            if (!hand.equals(Hand.MAIN_HAND) || level.isClientSide)
                return ActionResult.newResult(ActionResultType.PASS, itemstack);
            final CompoundNBT comp = tag.getCompound(LINKINGTOOL_TAG);
            final boolean containsPos =
                    comp.contains("X") && comp.contains("Y") && comp.contains("Z");
            if (containsPos) {
                final BlockPos linkpos = NBTUtil.readBlockPos(comp);
                final boolean state = TileRedstoneEmitter.redstoneUpdate(linkpos, level);
                message(player, "ra.state", String.valueOf(state));
                return ActionResult.newResult(ActionResultType.SUCCESS, itemstack);
            }
        }
        return ActionResult.newResult(ActionResultType.PASS, itemstack);
    }

}
