package fr.lebon.autopath.util;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FlowerFeature;

public class GrowRoutineGrassBlock {
    
    private GrowRoutineGrassBlock(){//Util class
}

    public static void grow(ServerWorld world, Random random, BlockPos pos, BlockState state,Block callerBlock) {
        BlockPos blockPos = pos.up();
        BlockState blockState = Blocks.GRASS.getDefaultState();

        label48:
        for(int i = 0; i < 128; ++i) {
            BlockPos blockPos2 = blockPos;

            for(int j = 0; j < i / 16; ++j) {
                blockPos2 = blockPos2.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!world.getBlockState(blockPos2.down()).isOf(callerBlock) || world.getBlockState(blockPos2).isFullCube(world, blockPos2)) {
                continue label48;
                }
            }

            BlockState blockState2 = world.getBlockState(blockPos2);
            if (blockState2.isOf(blockState.getBlock()) && random.nextInt(10) == 0) {
                ((Fertilizable)blockState.getBlock()).grow(world, random, blockPos2, blockState2);
            }

            if (blockState2.isAir()) {
                BlockState blockState4;
                if (random.nextInt(8) == 0) {
                List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos2).getGenerationSettings().getFlowerFeatures();
                if (list.isEmpty()) {
                    continue;
                }

                ConfiguredFeature<?, ?> configuredFeature = (ConfiguredFeature)list.get(0);
                FlowerFeature flowerFeature = (FlowerFeature)configuredFeature.feature;
                blockState4 = flowerFeature.getFlowerState(random, blockPos2, configuredFeature.getConfig());
                } else {
                blockState4 = blockState;
                }

                if (blockState4.canPlaceAt(world, blockPos2)) {
                world.setBlockState(blockPos2, blockState4, 3);
                }
            }
        }

    }
}
