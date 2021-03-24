package fr.lebon.autopath.config;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "autopath")
public class AutoPathConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip()
    public int downgradeTime = 360;
    @ConfigEntry.Gui.Tooltip()
    public int upgradeTime = 120;

}