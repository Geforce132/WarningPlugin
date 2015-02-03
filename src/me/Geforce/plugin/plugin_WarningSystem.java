package me.Geforce.plugin;

import java.util.logging.Logger;

import me.Geforce.plugin.commands.CommandClearWarnings;
import me.Geforce.plugin.commands.CommandWarn;
import me.Geforce.plugin.commands.CommandWarningRecord;
import me.Geforce.plugin.handlers.WSEventHandler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class plugin_WarningSystem extends JavaPlugin{
	
	private PluginDescriptionFile pdf = this.getDescription();
	
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public void onEnable(){
		logger.info(pdf.getName() + " v" + pdf.getVersion() + " has been enabled!");
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		Bukkit.getServer().getPluginManager().registerEvents(new WSEventHandler(this), this);		
		
		this.getCommand("warn").setExecutor(new CommandWarn(this));
		this.getCommand("clearwarnings").setExecutor(new CommandClearWarnings(this));
		this.getCommand("warningrecord").setExecutor(new CommandWarningRecord(this));
	}
	
	public void onDisable(){
		logger.info(pdf.getName() + " v" + pdf.getVersion() + " has been disabled!");
	}

}
