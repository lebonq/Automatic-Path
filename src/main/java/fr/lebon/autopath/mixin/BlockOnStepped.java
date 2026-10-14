package fr.lebon.autopath.mixin;

import fr.lebon.autopath.AutoPath;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockOnStepped {
    @Inject(method = "onSteppedOn", cancellable = true, at = @At("HEAD"))
    private void transformGrassToPathWhenSteppedOn(World world, BlockPos pos, BlockState state,
                                                  Entity entity, CallbackInfo ci) {
        if (world.isClient() || !state.isOf(Blocks.GRASS_BLOCK) || !(entity instanceof LivingEntity)) {
            return;
        }
        if (entity instanceof MobEntity && !AutoPath.getConfig().enableMobPathCreation) {
            return;
        }
        world.setBlockState(pos, AutoPath.PATH_BLOCK.getDefaultState());
        ci.cancel();
    }
}
