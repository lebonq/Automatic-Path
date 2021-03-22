package fr.lebon.autopath;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lebon.autopath.blocks.PathBlock;
import fr.lebon.autopath.entity.PathEntity;

public class AutoPath implements ModInitializer{

        public static Logger LOGGER = LogManager.getLogger();

        public static final String MOD_ID = "autopath";
        public static final String MOD_NAME = "Automatic Path";

        public static final Block PATH = new PathBlock(FabricBlockSettings.of(Material.SOIL).hardness(0.5f).nonOpaque().sounds(BlockSoundGroup.GRASS).breakByHand(true));
        public static final BlockItem PATH_ITEM = new BlockItem(PATH, new Item.Settings().group(ItemGroup.MISC));
        public static BlockEntityType<PathEntity> PATH_ENTITY;

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");

        log(Level.INFO, "Register Blocks");

        Registry.register(Registry.BLOCK, new Identifier("autopath", "path"), PATH);
        Registry.register(Registry.ITEM, new Identifier("autopath", "path"), PATH_ITEM);
        
        PATH_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "autopath:path", BlockEntityType.Builder.create(PathEntity::new, PATH).build(null));
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}