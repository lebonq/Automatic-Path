package fr.lebon.autopath.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.lebon.autopath.AutoPath;
import fr.lebon.autopath.blocks.PathBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;


@Mixin(EatGrassGoal.class)
public class EatGoalCanStart {
    @Final
	@Mutable
	@Shadow
	private MobEntity mob;
    @Final
	@Mutable
	@Shadow
	private World world;
    @Final
	@Mutable
	@Shadow
	private int timer;

	@Inject(method = "canStart", cancellable = true, at = @At(value = "TAIL"))
    private void canStart(CallbackInfoReturnable<Boolean> cir) {

        BlockPos blockPosAutoPath = this.mob.getBlockPos();
        if(this.world.getBlockState(blockPosAutoPath.down()).isOf(AutoPath.LAWN_BLOCK)){
            cir.setReturnValue(true);
            cir.cancel();
        }
        if (this.world.getBlockState(blockPosAutoPath.down()).isOf(AutoPath.PATH_BLOCK)) {
            if(this.world.getBlockState(blockPosAutoPath.down()).get(PathBlock.STATE_RENDER) > 3){
                cir.setReturnValue(false);
                cir.cancel();
            }
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "tick", cancellable = true, at = @At(value = "HEAD"))
    private void tick(CallbackInfo cir) {
        BlockPos blockPos = this.mob.getBlockPos();
        BlockPos blockPos2 = blockPos.down();
        if (this.world.getBlockState(blockPos2).isOf(AutoPath.LAWN_BLOCK)) {
            this.mob.onEatingGrass();
            cir.cancel();
        }

        if (this.world.getBlockState(blockPos2).isOf(AutoPath.PATH_BLOCK)) {
            if(this.world.getBlockState(blockPos2).get(PathBlock.STATE_RENDER) > 3){cir.cancel();}
            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                this.world.syncWorldEvent(2001, blockPos2, Block.getRawIdFromState(Blocks.GRASS_BLOCK.getDefaultState()));
                this.world.setBlockState(blockPos2, Blocks.DIRT.getDefaultState(), 2);
            }
            this.mob.onEatingGrass();
            cir.cancel();
        }
    }
}