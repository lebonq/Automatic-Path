package fr.lebon.autopath.mixin;

import fr.lebon.autopath.blocks.PathBlock;
import fr.lebon.autopath.config.AutoPathConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.SheepEntity;
import org.apache.logging.log4j.Level;
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
        AutoPathConfig config = AutoConfig.getConfigHolder(AutoPathConfig.class).getConfig();
        boolean mob = config.enableMobPathCreation;
        //TODO Move those declaration, call at every creation of a path not efficient
        AutoPath.log(Level.DEBUG,"" + mob);
        if(!mob && (entity instanceof MobEntity)) cir.cancel();
        else if(state.isOf(Blocks.GRASS_BLOCK) && (entity instanceof LivingEntity)){ //select grass and if entity is a mob or player
            world.setBlockState(pos,AutoPath.PATH_BLOCK.getDefaultState()); // We replace by a block path and notify other
            cir.cancel();
        }
    }
}