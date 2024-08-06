package com.troblecodings.tcredstone.init;

import com.troblecodings.linkableapi.Linkingtool;
import com.troblecodings.linkableapi.MultiLinkingTool;
import com.troblecodings.tcredstone.TCRedstoneMain;
import com.troblecodings.tcredstone.block.BlockRedstoneAcceptor;
import com.troblecodings.tcredstone.block.BlockRedstoneEmitter;
import com.troblecodings.tcredstone.block.BlockRedstoneMultiEmitter;
import com.troblecodings.tcredstone.item.RemoteActivator;
import com.troblecodings.tcredstone.tile.TileRedstoneEmitter;
import com.troblecodings.tcredstone.tile.TileRedstoneMultiEmitter;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class TCInit {

    public static final Item RS_LINKER =
            registerItem("linker", new Linkingtool(ItemGroup.REDSTONE, TCInit::acceptAcceptor));
    public static final Item RS_MULTILINKER = registerItem("multilinker",
            new MultiLinkingTool(ItemGroup.REDSTONE, TCInit::acceptAcceptor));
    public static final Item REMOTE_ACTIVATOR = registerItem("activator",
            new RemoteActivator(ItemGroup.REDSTONE, TCInit::acceptAcceptor));

    public static final Block RS_ACCEPTOR = registerBlock("acceptor",
            new BlockRedstoneAcceptor(FabricBlockSettings.of(Material.STONE).strength(1.5f, 6.0f)),
            ItemGroup.REDSTONE);
    public static final Block RS_EMITTER = registerBlock("emitter",
            new BlockRedstoneEmitter(FabricBlockSettings.of(Material.STONE).strength(1.5f, 6.0f)),
            ItemGroup.REDSTONE);
    public static final Block RS_MULTI_EMITTER = registerBlock("multiemitter",
            new BlockRedstoneMultiEmitter(
                    FabricBlockSettings.of(Material.STONE).strength(1.5f, 6.0f)),
            ItemGroup.REDSTONE);

    public static final BlockEntityType<TileRedstoneEmitter> EMITER_TILE = Registry.register(
            Registry.BLOCK_ENTITY_TYPE, new Identifier(TCRedstoneMain.MODID, "emitter"),
            BlockEntityType.Builder.create(TileRedstoneEmitter::new, RS_EMITTER).build(null));
    public static final BlockEntityType<TileRedstoneMultiEmitter> MULTI_EMITER_TILE =
            Registry.register(Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(TCRedstoneMain.MODID, "multiemitter"), BlockEntityType.Builder
                            .create(TileRedstoneMultiEmitter::new, RS_MULTI_EMITTER).build(null));

    public static boolean acceptAcceptor(final World level, final BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof BlockRedstoneAcceptor;
    }

    private static Block registerBlock(final String name, final Block block,
            final ItemGroup redstone) {
        registerBlockItem(name, block, redstone);
        return Registry.register(Registry.BLOCK, new Identifier(TCRedstoneMain.MODID, name), block);
    }

    private static Item registerBlockItem(final String name, final Block block,
            final ItemGroup redstone) {
        return Registry.register(Registry.ITEM, new Identifier(TCRedstoneMain.MODID, name),
                new BlockItem(block, new FabricItemSettings().group(redstone)));
    }

    private static Item registerItem(final String name, final Item item) {
        return Registry.register(Registry.ITEM, new Identifier(TCRedstoneMain.MODID, name), item);
    }

    public static void init() {
    }

}
