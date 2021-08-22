package fr.lebon.autopath.mixin;

import fr.lebon.autopath.blocks.PathBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fr.lebon.autopath.AutoPath;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(Block.class)
public class BlockOnStepped {
	@Inject(method = "onSteppedOn", cancellable = true, at = @At(value = "HEAD"))
    private void transformGrassToPathWhenSteppedOn(World world, BlockPos pos, BlockState state, Entity entity,CallbackInfo cir) {
        if(state.isOf(Blocks.GRASS_BLOCK) && (entity instanceof LivingEntity)){ //select grass and if entity is a mob or player
            world.setBlockState(pos,AutoPath.PATH_BLOCK.getDefaultState(), PathBlock.SKIP_ALL_NEIGHBOR_AND_LIGHTING_UPDATES); //On remplace par un block path
            cir.cancel();
        }
    }
}