package fr.lebon.autopath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.lebon.autopath.AutoPath;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(Block.class)
public class BlockOnStepped {
	@Inject(method = "onSteppedOn", cancellable = true, at = @At(value = "HEAD"))
    private void transformGrassToPathWhenSteppedOn(World world, BlockPos pos, Entity entity,CallbackInfo cir) {
        if(world.getBlockState(pos).isOf(Blocks.GRASS_BLOCK)){ //select grass
            world.setBlockState(pos,AutoPath.PATH.getDefaultState()); //On remplace par un block path
            cir.cancel();
        }
    }
}