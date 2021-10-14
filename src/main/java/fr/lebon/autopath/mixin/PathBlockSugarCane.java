package fr.lebon.autopath.mixin;

import java.util.Iterator;

import net.minecraft.block.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.lebon.autopath.AutoPath;
import fr.lebon.autopath.blocks.PathBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;


@Mixin(SugarCaneBlock.class) 
/**
 * Permet de placer les cannes a sucre sur les chemins du state 3 ou plus bas
 */
public class PathBlockSugarCane {
	@Inject(method = "canPlaceAt", at = @At("TAIL"), cancellable = true)
    private void test(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = world.getBlockState(pos.down());
        boolean trigger = false;

        if (blockState.isOf(AutoPath.PATH_BLOCK)) {
            if(blockState.get(PathBlock.STATE_RENDER) <=3){//can't merge those 2 statement because we must be sure that the block is a Path block
                trigger = true;
            }
        }

        if(blockState.isOf(AutoPath.LAWN_BLOCK)){
            trigger = true;
        }

        if(trigger){
            BlockPos blockPos = pos.down();
            Iterator var6 = Direction.Type.HORIZONTAL.iterator();

            while (var6.hasNext()) {
                Direction direction = (Direction) var6.next();
                BlockState blockState2 = world.getBlockState(blockPos.offset(direction));
                FluidState fluidState = world.getFluidState(blockPos.offset(direction));
                if (fluidState.isIn(FluidTags.WATER) || blockState2.isOf(Blocks.FROSTED_ICE)) {
                   cir.setReturnValue(true);
               }
            }
        }
    }
}