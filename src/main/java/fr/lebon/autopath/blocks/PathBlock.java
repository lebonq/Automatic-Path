package fr.lebon.autopath.blocks;

import java.util.Random;

import fr.lebon.autopath.entity.PathEntity;
import fr.lebon.autopath.util.GrowRoutineGrassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
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
       GrowRoutineGrassBlock.grow(world, random, pos, state, this);
    }
}