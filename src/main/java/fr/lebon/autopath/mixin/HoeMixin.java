package fr.lebon.autopath.mixin;



import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.lebon.autopath.AutoPath;
import fr.lebon.autopath.compat.VersionSupport;
import net.minecraft.block.Blocks;
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
	@Inject(method = "useOnBlock", cancellable = true, at = @At("HEAD")) //need to inject after the get call to change the prediction with our blok added
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
                if (!world.isClient()) {
                    world.setBlockState(pos, Blocks.FARMLAND.getDefaultState(), 11);
                    if (playerEntity != null) {
                        VersionSupport.damageHoe(context, playerEntity);
                    }
                }
    
                cir.setReturnValue(VersionSupport.success(world));
                cir.cancel();
            }
        }
    }
}