package com.troblecodings.tcredstone.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.troblecodings.linkableapi.Linkingtool;
import com.troblecodings.tcredstone.TCRedstoneMain;
import com.troblecodings.tcredstone.block.BlockRedstoneAcceptor;
import com.troblecodings.tcredstone.item.RemoteActivator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class TCRedstoneItems {
    
    public static final Item LINKER = new Linkingtool(CreativeTabs.REDSTONE,
            TCRedstoneItems::acceptAcceptor);
    public static final Item ACTIVATOR = new RemoteActivator(CreativeTabs.REDSTONE,
            TCRedstoneItems::acceptAcceptor);
    
    public static ArrayList<Item> itemsToRegister = new ArrayList<>();
    
    public static void init() {
        final Field[] fields = TCRedstoneItems.class.getFields();
        for (final Field field : fields) {
            final int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)
                    && Modifier.isPublic(modifiers)) {
                final String name = field.getName().toLowerCase();
                try {
                    final Item item = (Item) field.get(null);
                    item.setRegistryName(new ResourceLocation(TCRedstoneMain.MODID, name));
                    item.setUnlocalizedName(name);
                    itemsToRegister.add(item);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerItem(final RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();
        itemsToRegister.forEach(registry::register);
    }

    public static void setName(final Item item, final String name) {
        item.setRegistryName(new ResourceLocation(TCRedstoneMain.MODID, name));
        item.setUnlocalizedName(name);
        itemsToRegister.add(item);
    }

    public static boolean acceptAcceptor(final World world, final BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof BlockRedstoneAcceptor;
    }

}
