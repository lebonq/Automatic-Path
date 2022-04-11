package fr.lebon.autopath.util;

import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

public class ColorProvider {
    static public int getColor(BlockRenderView view, BlockPos pos){
        if(view == null || pos == null){
            return GrassColors.getColor(0.5, 1.0);
        }
        return BiomeColors.getGrassColor(view, pos);
    }
}
