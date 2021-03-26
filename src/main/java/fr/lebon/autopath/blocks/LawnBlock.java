package fr.lebon.autopath.blocks;

import java.util.Random;

import fr.lebon.autopath.util.GrowRoutineGrassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LawnBlock extends Block implements Fertilizable{

    public LawnBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
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
