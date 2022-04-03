package fr.lebon.autopath.entity;

import fr.lebon.autopath.AutoPath;
import fr.lebon.autopath.config.AutoPathConfig;
import fr.lebon.autopath.blocks.PathBlock;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.Block;
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
    public boolean permanentAsDirtPath;

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
        permanentAsDirtPath = config.permanentAsDirtPath;
        //TODO Move those declaration, call at every creation of a path not efficient
    }

    public static void tick(World world, BlockPos pos, BlockState state, PathEntity blockEntity){//20 ticks 1 seconde
        if (blockEntity.permanent) return; //Si permanent on ne fait plus rien
        if (world.isClient()) return;

        // turn covered path to dirt
        BlockPos upPos = pos.up();
        if (world.getBlockState(upPos).isSolidBlock(world, upPos)) { // si le block au dessus est solide
            world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            return;
        }

        boolean stepped = state.get(PathBlock.STEPPED);
        int renderState = state.get(PathBlock.STATE_RENDER);

        if (stepped && blockEntity.nbTick >= blockEntity.upgradeTime) { // default 2400

            // upgrade render state if possible
            if (renderState + 1 <= 5) {
                state = state.with(PathBlock.STATE_RENDER, renderState + 1);
                blockEntity.nbTick = 0;
            // try turning path to permanent path
            } else if (renderState == 5) {
                if (blockEntity.permanentActivate) {
                    if (blockEntity.timesWalkOn == blockEntity.steppedBeforePermanent) {
                        if (blockEntity.permanentAsDirtPath) {
                            world.setBlockState(pos, Blocks.DIRT_PATH.getDefaultState());
                            return;
                        }
                        else{
                            blockEntity.permanent = true;
                        }
                    }
                }
                blockEntity.timesWalkOn++;
                blockEntity.nbTick = 0;
            }

        } else if (blockEntity.nbTick >= blockEntity.downgradeTime) { // default 7200

            if (renderState - 1 <= 0) {
                // downgrade back to grass
                world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState());
                return; // Le block est detruit
            }

            // downgrade render state
            state = state.with(PathBlock.STATE_RENDER, renderState - 1);
            blockEntity.timesWalkOn = 0;
            blockEntity.nbTick = 0;
        }

        world.setBlockState(pos, state.with(PathBlock.STEPPED, false), PathBlock.SKIP_ALL_NEIGHBOR_AND_LIGHTING_UPDATES); // We use our custom flag  because we dont need to notify neigbours
        blockEntity.nbTick++;
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putInt("nbTick", nbTick);
        tag.putBoolean("permanent", permanent);
        tag.putInt("timesWalkOn", timesWalkOn);

        super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        nbTick = tag.getInt("nbTick");
        permanent = tag.getBoolean("permanent");
        timesWalkOn = tag.getInt("timesWalkOn");
    }

}
