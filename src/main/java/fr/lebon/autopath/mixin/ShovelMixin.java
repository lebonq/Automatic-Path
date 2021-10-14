package fr.lebon.autopath.mixin;


import java.util.function.Consumer;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.item.ShovelItem;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.lebon.autopath.AutoPath;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


@Mixin(ShovelItem.class)

public class ShovelMixin {
    @Inject(method = "useOnBlock", cancellable = true, at = @At("HEAD"))
    private void ShovelModification(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        if (context.getSide() == Direction.DOWN) {
            cir.setReturnValue(ActionResult.PASS);
        } else {
            PlayerEntity playerEntity = context.getPlayer();
            BlockState blockState2 = null;

            if (world.getBlockState(blockPos).isOf(AutoPath.PATH_BLOCK) ||
                    world.getBlockState(blockPos).isOf(AutoPath.LAWN_BLOCK)){
                blockState2 = Blocks.DIRT_PATH.getDefaultState();
            }

            BlockState blockState3 = null;
            if (blockState2 != null && world.getBlockState(blockPos.up()).isAir()) {
                world.playSound(playerEntity, blockPos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                blockState3 = blockState2;
                AutoPath.log(Level.INFO, "Shovel work");
            } else if (blockState.getBlock() instanceof CampfireBlock && (Boolean)blockState.get(CampfireBlock.LIT)) {
                if (!world.isClient()) {
                    world.syncWorldEvent((PlayerEntity)null, 1009, blockPos, 0);
                }

                CampfireBlock.extinguish(context.getPlayer(), world, blockPos, blockState);
                blockState3 = (BlockState)blockState.with(CampfireBlock.LIT, false);
            }

            if (blockState3 != null) {
                if (!world.isClient) {
                    world.setBlockState(blockPos, blockState3, 11);
                    if (playerEntity != null) {
                        context.getStack().damage(1, playerEntity, (p) -> {
                            p.sendToolBreakStatus(context.getHand());
                        });
                    }
                }

                cir.setReturnValue(ActionResult.success(world.isClient));
                cir.cancel();
            }
            //Continue to check for other game or vanilla
        }
    }
}