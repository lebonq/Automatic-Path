package fr.lebon.autopath.entity;

import fr.lebon.autopath.AutoPath;
import fr.lebon.autopath.config.AutoPathConfig;
import fr.lebon.autopath.blocks.PathBlock;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PathEntity extends BlockEntity {
    
    public int downgradeTime;
    public int upgradeTime;
    public boolean permanentActivate;
    public int steppedBeforePermanent;

    public boolean permanent;
    public int timesWalkOn;
    public int nbTick; //Game will crash after 3years non stop of existing for the block 

    public PathEntity(BlockPos pos,BlockState state) {
        super(AutoPath.PATH_ENTITY, pos, state);
        
        AutoPathConfig config = AutoConfig.getConfigHolder(AutoPathConfig.class).getConfig();
        downgradeTime = config.downgradeTime*20;//Get time and convert from second to int
        upgradeTime = config.upgradeTime*20;
        permanentActivate = config.permanentPath;
        steppedBeforePermanent = config.steppedBeforePermanent;
    }

    public static void tick(World world, BlockPos pos,BlockState state, PathEntity blockEntity){//20 ticks 1 seconde
        if(blockEntity.permanent){
            return; //Si permanent on ne fait plus rien
        } 

        if(world.isClient()) return;

        if(world.getBlockState(pos.up()).isSolidBlock(world, pos.up())){//si le block au dessus est solide
            world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            return;
        }

        int currentRenderState = world.getBlockState(pos).get(PathBlock.STATE_RENDER);

        if(blockEntity.nbTick >= blockEntity.downgradeTime){//7200
            blockEntity.timesWalkOn = 0;
            if(currentRenderState - 1 <= 0){
                world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState());//On place un block de grass
                return; //Le block est detruit
            }
            else{
                world.setBlockState(pos, world.getBlockState(pos).with(PathBlock.STATE_RENDER, currentRenderState - 1));
                blockEntity.nbTick = 0;
           }

        }

        if(world.getBlockState(pos).get(PathBlock.STEPPED) && blockEntity.nbTick >= blockEntity.upgradeTime && currentRenderState + 1 <= 5){//2400
            world.setBlockState(pos, world.getBlockState(pos).with(PathBlock.STATE_RENDER, currentRenderState + 1));
            blockEntity.nbTick = 0;
        }

        if(world.getBlockState(pos).get(PathBlock.STEPPED) && blockEntity.nbTick >= blockEntity.upgradeTime && currentRenderState == 5){
            if(blockEntity.timesWalkOn == blockEntity.steppedBeforePermanent){
                blockEntity.permanent = true;
            }        

            if(blockEntity.permanentActivate){
                blockEntity.timesWalkOn++;
            }
            blockEntity.nbTick = 0;
        }
        
        world.setBlockState(pos, world.getBlockState(pos).with(PathBlock.STEPPED, false));
        blockEntity.nbTick++;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
      super.writeNbt(tag);
      tag.putInt("nbTick", nbTick);
      tag.putBoolean("permanent", permanent);
      tag.putInt("timesWalkOn", timesWalkOn);
 
      return tag;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        nbTick = tag.getInt("nbTick");
        permanent = tag.getBoolean("permanent");
        timesWalkOn = tag.getInt("timesWalkOn");
    }

}