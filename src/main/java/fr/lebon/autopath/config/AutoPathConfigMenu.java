package fr.lebon.autopath.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class AutoPathConfigMenu implements ModMenuApi{

    @Override
    @Environment(EnvType.CLIENT)
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(AutoPathConfig.class, parent).get();
    }
}
