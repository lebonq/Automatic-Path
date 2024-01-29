package fr.lebon.autopath.util;

import java.util.List;
import java.util.Optional;

import net.minecraft.block.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.util.math.random.Random;


public class GrowRoutineGrassBlock {

    private GrowRoutineGrassBlock(){//Util class
}

    public static void grow(ServerWorld world, Random random, BlockPos pos, BlockState state,Block callerBlock) {
        BlockPos blockPos = pos.up();
        BlockState blockState = Blocks.GRASS_BLOCK.getDefaultState();
        Optional<RegistryEntry.Reference<PlacedFeature>> optional = world.getRegistryManager().get(RegistryKeys.PLACED_FEATURE).getEntry(VegetationPlacedFeatures.GRASS_BONEMEAL);

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
				RegistryEntry registryEntry;
				if (random.nextInt(8) == 0) {
					List<ConfiguredFeature<?, ?>> list = ((Biome)world.getBiome(blockPos2).value()).getGenerationSettings().getFlowerFeatures();
					if (list.isEmpty()) {
						continue;
					}

					registryEntry = ((RandomPatchFeatureConfig)((ConfiguredFeature)list.get(0)).config()).feature();
				} else {
					if (!optional.isPresent()) {
						continue;
					}

					registryEntry = (RegistryEntry)optional.get();
				}

				((PlacedFeature)registryEntry.value()).generateUnregistered(world, world.getChunkManager().getChunkGenerator(), random, blockPos2);
			}
        }
    }
}
