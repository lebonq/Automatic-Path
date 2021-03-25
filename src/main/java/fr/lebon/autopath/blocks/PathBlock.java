package fr.lebon.autopath.blocks;

import java.util.List;
import java.util.Random;

import fr.lebon.autopath.entity.PathEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FlowerFeature;

public class PathBlock extends Block implements BlockEntityProvider, Fertilizable{ 

    public static final IntProperty STATE_RENDER = IntProperty.of("state_render",1,5);
    public static final BooleanProperty  STEPPED = BooleanProperty .of("stepped");

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(STEPPED);
        stateManager.add(STATE_RENDER);
    }

    public PathBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(STEPPED, false));
        setDefaultState(getStateManager().getDefaultState().with(STATE_RENDER, 1));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
      return new PathEntity();
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, Entity entity){

        if(!(world.isClient())){
            world.setBlockState(pos, world.getBlockState(pos).with(STEPPED, true));
        }
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        if(world.getBlockState(pos).get(STATE_RENDER) <= 3){ //if state ok so it can be fertilizable
            return world.getBlockState(pos.up()).isAir();
        }
        return false;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    /**
     * We want the same behavior as GrassBlock so I copy paste mojang code for grassblock
     */
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.up();
        BlockState blockState = Blocks.GRASS.getDefaultState();

        label48:
        for(int i = 0; i < 128; ++i) {
            BlockPos blockPos2 = blockPos;

            for(int j = 0; j < i / 16; ++j) {
                blockPos2 = blockPos2.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!world.getBlockState(blockPos2.down()).isOf(this) || world.getBlockState(blockPos2).isFullCube(world, blockPos2)) {
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