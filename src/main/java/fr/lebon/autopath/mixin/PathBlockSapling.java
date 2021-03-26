package fr.lebon.autopath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.lebon.autopath.AutoPath;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.Feature;

@Mixin(Feature.class) 
public class PathBlockSapling {
	@Inject(method = "isSoil(Lnet/minecraft/block/Block;)Z", cancellable = true, at = @At(value = "RETURN"))
    private static void makePathSaplingable(Block block, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && block.is(AutoPath.PATH_BLOCK) || block.is(AutoPath.LAWN_BLOCK)) cir.setReturnValue(true);
    }
}