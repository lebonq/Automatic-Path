package fr.lebon.autopath.config;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "autopath")
public class AutoPathConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip()
    public int downgradeTime = 760;
    @ConfigEntry.Gui.Tooltip()
    public int upgradeTime = 280;
    @ConfigEntry.Gui.Tooltip()
    public boolean permanentPath = true;
    @ConfigEntry.Gui.Tooltip()
    public int steppedBeforePermanent = 12;

}