package fr.lebon.autopath.compat;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

/** Minecraft's persistence API changes; path progression is shared in PathEntity. */
public abstract class PathEntityData extends BlockEntity {
    public int nbTick;
    public int timesWalkOn;
    public boolean permanent;

    protected PathEntityData(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putInt("nbTick", nbTick);
        view.putBoolean("permanent", permanent);
        view.putInt("timesWalkOn", timesWalkOn);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        nbTick = view.getInt("nbTick", 0);
        permanent = view.getBoolean("permanent", false);
        timesWalkOn = view.getInt("timesWalkOn", 0);
    }
}
