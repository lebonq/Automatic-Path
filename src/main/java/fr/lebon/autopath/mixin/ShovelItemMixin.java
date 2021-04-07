package fr.lebon.autopath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.lebon.autopath.AutoPath;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;


@Mixin(MiningToolItem.class) 
/**
 * Permet de placer les cannes a sucre sur les chemins du state 3 ou plus bas
 */
public class ShovelItemMixin {
	@Inject(method = "getMiningSpeedMultiplier", at = @At("TAIL"), cancellable = true)
    private void changeEffectiveOn(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
      if(state.isOf(AutoPath.PATH_BLOCK)){
         cir.setReturnValue(stack.getMiningSpeedMultiplier(Blocks.GRASS_BLOCK.getDefaultState()));
         cir.cancel();
      }
   }
}