package fr.lebon.autopath;

import org.apache.logging.log4j.Level;

import fr.lebon.autopath.util.ColorProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;

public class AutoPathClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        AutoPath.log(Level.INFO, "Client initialize");
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> ColorProvider.getColor(view, pos), AutoPath.PATH_BLOCK);
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> 0x5E9D34, AutoPath.LAWN_BLOCK);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0x5E9D34, AutoPath.LAWN_ITEM);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0x5E9D34, AutoPath.PATH_BLOCK);

        BlockRenderLayerMap.INSTANCE.putBlock(AutoPath.PATH_BLOCK, RenderLayer.getCutout());//For transparancy
        BlockRenderLayerMap.INSTANCE.putBlock(AutoPath.LAWN_BLOCK, RenderLayer.getCutout());
    }
    
}
