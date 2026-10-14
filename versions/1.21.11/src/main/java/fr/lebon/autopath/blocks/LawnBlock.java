package fr.lebon.autopath.blocks;

import com.mojang.serialization.MapCodec;

import fr.lebon.autopath.util.GrowRoutineGrassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class LawnBlock extends Block implements Fertilizable {
    public static final MapCodec<LawnBlock> CODEC = createCodec(LawnBlock::new);

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }


    public LawnBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return true;
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
