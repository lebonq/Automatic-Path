package fr.lebon.autopath.blocks;

import java.util.Random;

import fr.lebon.autopath.AutoPath;
import fr.lebon.autopath.entity.PathEntity;
import fr.lebon.autopath.util.GrowRoutineGrassBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PathBlock extends BlockWithEntity implements BlockEntityProvider, Fertilizable{

    public static final IntProperty STATE_RENDER = IntProperty.of("state_render",1,5);
    public static final BooleanProperty STEPPED = BooleanProperty.of("stepped");

    /** setBlockState() flags that won't activate observers (and skips unnecessary lighting updates) */
    public static final int SKIP_ALL_NEIGHBOR_AND_LIGHTING_UPDATES = Block.NOTIFY_LISTENERS | Block.FORCE_STATE | Block.SKIP_LIGHTING_UPDATES;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(STEPPED);
        stateManager.add(STATE_RENDER);
    }

    public PathBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
            .with(STEPPED, false)
            .with(STATE_RENDER, 1));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PathEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, AutoPath.PATH_ENTITY , PathEntity::tick);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos,BlockState state, Entity entity){
        if(!(world.isClient()) && entity.isAlive()){
            if (!state.get(STEPPED)) {
                world.setBlockState(pos, state.with(STEPPED, true), SKIP_ALL_NEIGHBOR_AND_LIGHTING_UPDATES);
            }
        }
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        if(world.getBlockState(pos).get(STATE_RENDER) <= 3){ //if state ok so it can be fertilize
            return world.getBlockState(pos.up()).isAir();
        }
        return false;
    }

    @Override
    public boolean canGrow(World world, net.minecraft.util.math.random.Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, net.minecraft.util.math.random.Random random, BlockPos pos, BlockState state) {
        GrowRoutineGrassBlock.grow(world, random, pos, state, this);
    }

}