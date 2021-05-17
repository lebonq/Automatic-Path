package fr.lebon.autopath;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lebon.autopath.blocks.LawnBlock;
import fr.lebon.autopath.blocks.PathBlock;
import fr.lebon.autopath.config.AutoPathConfig;
import fr.lebon.autopath.entity.PathEntity;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AutoPath implements ModInitializer{

        public static Logger LOGGER = LogManager.getLogger();

        public static final String MOD_ID = "autopath";
        public static final String MOD_NAME = "Automatic Path";

        public static final Block PATH_BLOCK = new PathBlock(FabricBlockSettings.of(Material.SOIL).hardness(0.5f).sounds(BlockSoundGroup.GRASS).breakByHand(true));
        public static final Block LAWN_BLOCK = new LawnBlock(FabricBlockSettings.of(Material.SOIL).hardness(0.5f).sounds(BlockSoundGroup.GRASS).breakByHand(true));

        public static final BlockItem LAWN_ITEM = new BlockItem(LAWN_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
        public static final BlockItem PATH_ITEM = new BlockItem(PATH_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
        public static BlockEntityType<PathEntity> PATH_ENTITY;

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");

        log(Level.INFO, "Register Blocks");

        Registry.register(Registry.BLOCK, new Identifier("autopath", "path"), PATH_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("autopath", "path"), PATH_ITEM);

        Registry.register(Registry.BLOCK, new Identifier("autopath", "lawn"), LAWN_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("autopath", "lawn"), LAWN_ITEM);
        
        PATH_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "autopath:path", BlockEntityType.Builder.create(PathEntity::new, PATH_BLOCK).build(null));

        log(Level.INFO, "Initializing config");
        AutoConfig.register(AutoPathConfig.class, GsonConfigSerializer::new);
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}