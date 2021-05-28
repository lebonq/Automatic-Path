package fr.lebon.autopath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.lebon.autopath.AutoPath;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.Feature;

@Mixin(Feature.class) 
public class PathBlockSapling {
	@Inject(method = "isSoil(Lnet/minecraft/block/BlockState;)Z", cancellable = true, at = @At(value = "RETURN"))
    private static void makePathSaplingable(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && (state.isOf(AutoPath.PATH_BLOCK) || state.isOf(AutoPath.LAWN_BLOCK))) cir.setReturnValue(true);
    }
}