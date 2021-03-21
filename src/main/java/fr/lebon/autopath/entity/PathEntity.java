package fr.lebon.autopath.entity;

import fr.lebon.autopath.AutoPath;
import fr.lebon.autopath.blocks.PathBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;

public class PathEntity extends BlockEntity implements Tickable{
    
    private int nbTick; //Game will crash after 3years non stop of existing for the block 

    public PathEntity() {
        super(AutoPath.PATH_ENTITY);
    }

    @Override
    public void tick() {//20 ticks 1 seconde
        if(world.isClient()) return;

        int currentRenderState = world.getBlockState(pos).get(PathBlock.STATE_RENDER);

        if(nbTick >= 7200){//7200
            if(currentRenderState - 1 <= 0){
                world.setBlockState(pos, Block.postProcessState(Block.getStateFromRawId(8), world, pos));//On place un block de grass avec la couleur du biome
                return; //Le block est detruit
            }
            else{
                world.setBlockState(pos, world.getBlockState(pos).with(PathBlock.STATE_RENDER, currentRenderState - 1));
                nbTick = 0;
           }

        }

        if(world.getBlockState(pos).get(PathBlock.STEPPED)){
            if(nbTick >= 2400){ //2400
               if(currentRenderState + 1 <= 5){
                    world.setBlockState(pos, world.getBlockState(pos).with(PathBlock.STATE_RENDER, currentRenderState + 1));
                    nbTick = 0;
                }
            }
        }
        
        world.setBlockState(pos, world.getBlockState(pos).with(PathBlock.STEPPED, false));
        nbTick++;
    }


}
