package fr.lebon.autopath.config;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "autopath")
public class AutoPathConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip()
    public boolean enableMobPathCreation = false; //Mob aren't enable by default for performance issue
    @ConfigEntry.Gui.Tooltip()
    public int downgradeTime = 760;
    @ConfigEntry.Gui.Tooltip()
    public int upgradeTime = 280;
    @ConfigEntry.Gui.Tooltip()
    public boolean permanentPath = false;
    @ConfigEntry.Gui.Tooltip()
    public int steppedBeforePermanent = 12;
    @ConfigEntry.Gui.Tooltip()
    public boolean permanentAsDirtPath = false;
}
