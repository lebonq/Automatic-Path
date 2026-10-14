package fr.lebon.autopath.mixin;

import fr.lebon.autopath.AutoPath;
import fr.lebon.autopath.blocks.PathBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EatGrassGoal.class)
public class EatGoalCanStart {
    // Extend vanilla's ground check while preserving its random delay and eating timer.
    @Redirect(method = {"canStart", "tick"}, at = @At(value = "INVOKE",
        target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private boolean isEdibleGround(BlockState state, Block block) {
        return state.isOf(block) || block == Blocks.GRASS_BLOCK && (
            state.isOf(AutoPath.LAWN_BLOCK) ||
            state.isOf(AutoPath.PATH_BLOCK) && state.get(PathBlock.STATE_RENDER) <= 3);
    }

    // Lawn feeds sheep without being consumed; vanilla still handles mobGriefing.
    @Redirect(method = "tick", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private boolean preserveLawn(World world, BlockPos pos, BlockState state, int flags) {
        return !world.getBlockState(pos).isOf(AutoPath.LAWN_BLOCK)
            && world.setBlockState(pos, state, flags);
    }
}
