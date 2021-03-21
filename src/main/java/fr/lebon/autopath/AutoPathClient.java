package fr.lebon.autopath;

import org.apache.logging.log4j.Level;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;

public class AutoPathClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        AutoPath.log(Level.INFO, "Client initialize");
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> BiomeColors.getGrassColor(view, pos), AutoPath.PATH);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0x507C44, AutoPath.PATH_ITEM);
        BlockRenderLayerMap.INSTANCE.putBlock(AutoPath.PATH, RenderLayer.getCutout());//For transparancy
    }
    
}
