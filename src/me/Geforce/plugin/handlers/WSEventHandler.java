package me.Geforce.plugin.handlers;

import java.io.File;

import me.Geforce.plugin.plugin_WarningSystem;
import me.Geforce.plugin.misc.HelpfulMethods;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class WSEventHandler implements Listener {
	
	private plugin_WarningSystem plugin;

	public WSEventHandler(plugin_WarningSystem plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onCommandFired(PlayerCommandPreprocessEvent event){
		if(event.getMessage().replaceFirst("/", "").startsWith("unban")){
			File file = new File(plugin.getDataFolder(), "Warnings/" + HelpfulMethods.getCommandArgs(event.getMessage().replaceFirst("/", "")).get(1) + ".yml");
			
			if(file.exists()){
				FileConfiguration playerFile = YamlConfiguration.loadConfiguration(file); 
				
				if(playerFile.getInt("warningPoints") >= plugin.getConfig().getInt("maxWarningPoints")){
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.RED + "This player has exceeded the maximum number of warning points allowed, and was banned automatically. Please use /clearwarnings <player> to unban them.");
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerJoined(AsyncPlayerPreLoginEvent event){
		File file = new File(plugin.getDataFolder(), "Warnings/" + event.getName() + ".yml");
		
		if(file != null){
			FileConfiguration playerFile = YamlConfiguration.loadConfiguration(file); 
			
			if(playerFile.getInt("warningPoints") >= plugin.getConfig().getInt("maxWarningPoints")){
				event.setKickMessage("You have exceeded the warning point limit.");
				event.setLoginResult(Result.KICK_BANNED);
			}
		}

		
	}

}
