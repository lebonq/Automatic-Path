package fr.lebon.autopath.compat;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NbtCompound;

/** Minecraft's persistence API changes; path progression is shared in PathEntity. */
public abstract class PathEntityData extends BlockEntity {
    public int nbTick;
    public int timesWalkOn;
    public boolean permanent;

    protected PathEntityData(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putInt("nbTick", nbTick);
        tag.putBoolean("permanent", permanent);
        tag.putInt("timesWalkOn", timesWalkOn);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        nbTick = tag.getInt("nbTick");
        permanent = tag.getBoolean("permanent");
        timesWalkOn = tag.getInt("timesWalkOn");
    }
}
