package fr.lebon.autopath;

import org.apache.logging.log4j.Level;

import fr.lebon.autopath.util.ColorProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.BlockRenderLayer;

public class AutoPathClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        AutoPath.log(Level.INFO, "Client initialize");
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> ColorProvider.getColor(view, pos), AutoPath.PATH_BLOCK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> 0x5E9D34, AutoPath.LAWN_BLOCK);


        BlockRenderLayerMap.putBlock(AutoPath.PATH_BLOCK, BlockRenderLayer.CUTOUT);//For transparancy
        BlockRenderLayerMap.putBlock(AutoPath.LAWN_BLOCK, BlockRenderLayer.CUTOUT);
    }
    
}
