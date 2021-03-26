package fr.lebon.autopath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.lebon.autopath.AutoPath;
import net.minecraft.block.BlockState;
import net.minecraft.item.ShovelItem;


@Mixin(ShovelItem.class) 
/**
 * Permet de placer les cannes a sucre sur les chemins du state 3 ou plus bas
 */
public class ShovelItemMixin {
	@Inject(method = "isEffectiveOn", at = @At("TAIL"), cancellable = true)
    private void changeEffectiveOn(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity playerEntity = context.getPlayer();
         BlockState blockState2 = (BlockState)PATH_STATES.get(blockState.getBlock());
         BlockState blockState3 = null;
         if (blockState2 != null && world.getBlockState(blockPos.up()).isAir()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
            blockState3 = blockState2;
         } else if (blockState.getBlock() instanceof CampfireBlock && (Boolean)blockState.get(CampfireBlock.LIT)) {
            if (!world.isClient()) {
               world.syncWorldEvent((PlayerEntity)null, 1009, blockPos, 0);
            }

            CampfireBlock.extinguish(world, blockPos, blockState);
            blockState3 = (BlockState)blockState.with(CampfireBlock.LIT, false);
         }

         if (blockState3 != null) {
            if (!world.isClient) {
               world.setBlockState(blockPos, blockState3, 11);
               if (playerEntity != null) {
                  context.getStack().damage(1, (LivingEntity)playerEntity, (Consumer)((p) -> {
                     p.sendToolBreakStatus(context.getHand());
                  }));
               }
            }

            return ActionResult.success(world.isClient);
         } else {
            return ActionResult.PASS;
         }
    }
}