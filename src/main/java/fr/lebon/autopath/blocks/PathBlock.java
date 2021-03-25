package fr.lebon.autopath.blocks;

import fr.lebon.autopath.entity.PathEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PathBlock extends Block implements BlockEntityProvider{

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

}