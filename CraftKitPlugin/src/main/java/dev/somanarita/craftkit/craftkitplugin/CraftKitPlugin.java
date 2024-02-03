package dev.somanarita.craftkit.craftkitplugin;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftKitPlugin extends JavaPlugin{
	@Override
	public void onEnable() {
		getServer().setDefaultGameMode(GameMode.CREATIVE);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}
}
