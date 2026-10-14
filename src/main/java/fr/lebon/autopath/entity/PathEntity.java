package fr.lebon.autopath.entity;

import fr.lebon.autopath.AutoPath;
import fr.lebon.autopath.config.AutoPathConfig;
import fr.lebon.autopath.blocks.PathBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import fr.lebon.autopath.compat.PathEntityData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PathEntity extends PathEntityData {
    public static final int MAINTENANCE_INTERVAL_TICKS = 40;

    public PathEntity(BlockPos pos, BlockState state) {
        super(AutoPath.PATH_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, PathEntity blockEntity){//20 ticks 1 seconde
        tickWithInterval(world, pos, state, blockEntity, MAINTENANCE_INTERVAL_TICKS);
    }

    // Shared by production and the opt-in benchmark so both measure the same maintenance code.
    static void tickWithInterval(World world, BlockPos pos, BlockState state, PathEntity blockEntity, int interval) {
        blockEntity.nbTick++;
        if (blockEntity.nbTick % interval != 0) return;
        if (blockEntity.permanent) return; //Si permanent on ne fait plus rien
        if (world.isClient()) return;

        // turn covered path to dirt
        BlockPos upPos = pos.up();
        if (world.getBlockState(upPos).isSolidBlock(world, upPos)) { // si le block au dessus est solide
            world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            return;
        }

        AutoPathConfig config = AutoPath.getConfig();
        boolean stepped = state.get(PathBlock.STEPPED);
        int renderState = state.get(PathBlock.STATE_RENDER);

        if (stepped && blockEntity.nbTick >= config.upgradeTime * 20L) {

            // upgrade render state if possible
            if (renderState + 1 <= 5) {
                state = state.with(PathBlock.STATE_RENDER, renderState + 1);
                blockEntity.nbTick = 0;
            // try turning path to permanent path
            } else if (renderState == 5) {
                if (config.permanentPath) {
                    if (blockEntity.timesWalkOn >= config.steppedBeforePermanent) {
                        if (config.permanentAsDirtPath) {
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

        } else if (blockEntity.nbTick >= config.downgradeTime * 20L) {

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

        world.setBlockState(pos, state.with(PathBlock.STEPPED, false), PathBlock.QUIET_STATE_UPDATE_FLAGS); // We use our custom flag  because we dont need to notify neigbours
    }

}
