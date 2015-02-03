package me.Geforce.plugin.commands;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import me.Geforce.plugin.plugin_WarningSystem;
import me.Geforce.plugin.misc.HelpfulMethods;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandWarn implements CommandExecutor {
	
	private plugin_WarningSystem plugin;
	
	public CommandWarn(plugin_WarningSystem plugin){
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender senderObj, Command command, String unknownString, String[] commandArgs) {		
		if(unknownString.equalsIgnoreCase("warn")){
			if(!senderObj.hasPermission("warningsystem.warn")){ senderObj.sendMessage(ChatColor.RED + "You do not have access to this command."); return true; }

			if(commandArgs.length == 2){
				String player = commandArgs[0];
				String reason = commandArgs[1];
				Player sender = Bukkit.getServer().getPlayer(senderObj.getName());
				UUID warnedPlayerUUID = (Bukkit.getServer().getPlayer(commandArgs[0]) != null ? Bukkit.getServer().getPlayer(commandArgs[0]).getUniqueId() : Bukkit.getServer().getOfflinePlayer(commandArgs[0]).getUniqueId());
				
				if(plugin.getConfig().getConfigurationSection("WarningTypes").getConfigurationSection(reason.toLowerCase()) == null){
					sender.sendMessage(ChatColor.RED + reason + " is a invalid warning type!");
					return true;
				}
				
				File folder = new File(plugin.getDataFolder(), "Warnings");
				File file = new File(plugin.getDataFolder(), "Warnings/" + warnedPlayerUUID + ".yml");
				
				if(!folder.exists()){
					folder.mkdirs();
				}
				
				if(!file.exists()){
					try{
						file.createNewFile();
					}catch (IOException e){
						e.printStackTrace();
					}
				}
				
				FileConfiguration playerFile = YamlConfiguration.loadConfiguration(file); 
				
				if(playerFile.contains("warningPoints")){
					playerFile.set("warningPoints", (playerFile.getInt("warningPoints") + plugin.getConfig().getConfigurationSection("WarningTypes").getConfigurationSection(reason.toLowerCase()).getInt("warningPoints")));
				}else{
					playerFile.set("warningPoints", plugin.getConfig().getConfigurationSection("WarningTypes").getConfigurationSection(reason.toLowerCase()).getInt("warningPoints"));
				}
				
				if(playerFile.getConfigurationSection("Warnings") == null){
					playerFile.createSection("Warnings");
				}
				
				ConfigurationSection warningSection = playerFile.getConfigurationSection("Warnings").createSection(HelpfulMethods.getNextWarningSection(playerFile));
				warningSection.set("pointsIssued", plugin.getConfig().getConfigurationSection("WarningTypes").getConfigurationSection(reason.toLowerCase()).getInt("warningPoints"));
				warningSection.set("reason", reason);
				warningSection.set("issuer", sender.getName());
				
				try{
					playerFile.save(file);
				}catch(IOException e){
					e.printStackTrace();
				}
				
				if(playerFile.getInt("warningPoints") >= plugin.getConfig().getInt("maxWarningPoints")){					
					if(Bukkit.getPlayer(warnedPlayerUUID) != null && Bukkit.getPlayer(warnedPlayerUUID).isOnline()){
						Bukkit.getPlayer(warnedPlayerUUID).kickPlayer("You have surpassed the warning point limit, and have been automatically banned.");
					}
					
					Bukkit.getServer().getBanList(Type.NAME).addBan(player, "You have exceeded the warning point limit.", null, sender.getName());
					
					sender.sendMessage(ChatColor.GREEN + "Successfully warned and banned " + player + " for " + reason + ".");
					return true;
				}
				
				if(plugin.getConfig().getConfigurationSection("WarningTypes").getConfigurationSection(reason.toLowerCase()).getBoolean("tempbans")){
					sender.performCommand("tempban " + player + " " + plugin.getConfig().getConfigurationSection("TempbanLengths").getString(HelpfulMethods.getPointsAsString(playerFile.getInt("warningPoints"))));
				}
				
				if(Bukkit.getPlayer(warnedPlayerUUID) != null && Bukkit.getPlayer(warnedPlayerUUID).isOnline()){
					Bukkit.getPlayer(warnedPlayerUUID).sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You have received " + plugin.getConfig().getConfigurationSection("WarningTypes").getConfigurationSection(reason.toLowerCase()).getInt("warningPoints") + " warning point(s) from " + sender.getName() + " for " + reason + ". You now have " + playerFile.getInt("warningPoints") + " warning point(s).");
				}
				
				sender.sendMessage(ChatColor.GREEN + "Successfully warned " + player + " for " + reason + ".");
				return true;
			}
		}
		
		return false;
	}

}
