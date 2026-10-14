package fr.lebon.autopath.util;

import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

public class ColorProvider {
    public static int getColor(BlockRenderView view, BlockPos pos) {
        return view == null || pos == null ? 0x5E9D34 : BiomeColors.getGrassColor(view, pos);
    }
}
