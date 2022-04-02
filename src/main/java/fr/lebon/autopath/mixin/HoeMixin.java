package fr.lebon.autopath.mixin;


import java.util.function.Consumer;

import net.minecraft.item.ShovelItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.lebon.autopath.AutoPath;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


@Mixin(HoeItem.class) 

public class HoeMixin {
	@Inject(method = "useOnBlock", cancellable = true, at = @At("HEAD"))
    private void HoeModification(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        if (context.getSide() != Direction.DOWN && world.getBlockState(pos.up()).isAir()) {
            boolean trigger = false;

            if (world.getBlockState(pos).isOf(AutoPath.PATH_BLOCK) || world.getBlockState(pos).isOf(AutoPath.LAWN_BLOCK)){
                trigger = true;
            }

            if(trigger){
                PlayerEntity playerEntity = context.getPlayer();
                world.playSound(playerEntity, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClient) {
                    world.setBlockState(pos, Blocks.FARMLAND.getDefaultState(), 11);
                    if (playerEntity != null) {
                        context.getStack().damage(1, (LivingEntity)playerEntity, (Consumer)((p) -> {
                            ((LivingEntity) p).sendToolBreakStatus(context.getHand());
                        }));
                    }
                }
    
                cir.setReturnValue(ActionResult.success(world.isClient));
                cir.cancel();
            }
        }
    }
}