package fr.lebon.autopath.entity;

import fr.lebon.autopath.AutoPath;
import fr.lebon.autopath.config.AutoPathConfig;
import fr.lebon.autopath.blocks.PathBlock;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class PathEntity extends BlockEntity implements Tickable{
    
    private int downgradeTime;
    private int upgradeTime;
    private boolean permanentActivate;
    private int steppedBeforePermanent;

    private boolean permanent;
    private int timesWalkOn;
    private int nbTick; //Game will crash after 3years non stop of existing for the block 

    public PathEntity() {
        super(AutoPath.PATH_ENTITY);
        
        AutoPathConfig config = AutoConfig.getConfigHolder(AutoPathConfig.class).getConfig();
        downgradeTime = config.downgradeTime*20;//GEt time and converte from second to int
        upgradeTime = config.upgradeTime*20;
        permanentActivate = config.permanentPath;
        steppedBeforePermanent = config.steppedBeforePermanent;
    }

    @Override
    public void tick() {//20 ticks 1 seconde
        if(permanent){
            return; //Si permanent on ne fait plus rien
        } 

        if(world.isClient()) return;

        if(world.getBlockState(pos.up()).isSolidBlock(world, pos.up())){//si le block au dessus est solide
            world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            return;
        }

        int currentRenderState = world.getBlockState(pos).get(PathBlock.STATE_RENDER);

        if(nbTick >= downgradeTime){//7200
            timesWalkOn = 0;
            if(currentRenderState - 1 <= 0){
                world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState());//On place un block de grass
                return; //Le block est detruit
            }
            else{
                world.setBlockState(pos, world.getBlockState(pos).with(PathBlock.STATE_RENDER, currentRenderState - 1));
                nbTick = 0;
           }

        }

        if(world.getBlockState(pos).get(PathBlock.STEPPED) && nbTick >= upgradeTime && currentRenderState + 1 <= 5){//2400
            world.setBlockState(pos, world.getBlockState(pos).with(PathBlock.STATE_RENDER, currentRenderState + 1));
            nbTick = 0;
        }

        if(world.getBlockState(pos).get(PathBlock.STEPPED) && currentRenderState == 5){
            if(timesWalkOn == steppedBeforePermanent){
                permanent = true;
            }        

            if(permanentActivate){
                timesWalkOn++;
            }
            nbTick = 0;
        }
        
        world.setBlockState(pos, world.getBlockState(pos).with(PathBlock.STEPPED, false));
        nbTick++;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
      super.toTag(tag);
      tag.putInt("nbTick", nbTick);
      tag.putBoolean("permanent", permanent);
      tag.putInt("timesWalkOn", timesWalkOn);
 
      return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        nbTick = tag.getInt("nbTick");
        permanent = tag.getBoolean("permanent");
        timesWalkOn = tag.getInt("timesWalkOn");
    }
}