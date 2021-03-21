package fr.lebon.autopath.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.lebon.autopath.AutoPath;
import fr.lebon.autopath.blocks.PathBlock;

//Thanks to @SpaceWalker for the help on this !
@Mixin(PlantBlock.class)
public class PathBlockPlant {
	@Inject(method = "canPlantOnTop", cancellable = true, at = @At(value = "HEAD"))
    private void makePathPlantable(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (floor.isOf(AutoPath.PATH)) {
            if(floor.get(PathBlock.STATE_RENDER) <=3){
                cir.setReturnValue(true);
                cir.cancel();
            }
            else{
                cir.setReturnValue(false);
                cir.cancel();
            }
            
        }
    }
}