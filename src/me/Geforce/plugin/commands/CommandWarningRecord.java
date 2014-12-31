package me.Geforce.plugin.commands;

import java.io.File;

import me.Geforce.plugin.plugin_WarningSystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.BanList.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CommandWarningRecord implements CommandExecutor {

	private plugin_WarningSystem plugin;
	
	public CommandWarningRecord(plugin_WarningSystem plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String unknownString, String[] commandArgs) {	
		if(command.getName().equalsIgnoreCase("warningrecord")){

			if(commandArgs.length == 0){
				if(!sender.hasPermission("warningsystem.warningrecord")){ sender.sendMessage(ChatColor.RED + "You do not have access to this command."); return true; }
				File file = new File(plugin.getDataFolder(), "Warnings/" + sender.getName() + ".yml");
				
				if(!file.exists()){
					sender.sendMessage(ChatColor.RED + "You have not been issued any warnings.");
					return true;
				}
				
				FileConfiguration playerFile = YamlConfiguration.loadConfiguration(file); 
				
				if(playerFile.getConfigurationSection("Warnings") != null){
					
					sender.sendMessage(ChatColor.GOLD + "Warnings issued to " + sender.getName() + (Bukkit.getServer().getBanList(Type.NAME).isBanned(sender.getName()) ? " (" + ChatColor.DARK_RED + "banned" + ChatColor.GOLD + ")" : "") + ":");
					
					for(int i = 1; i <= 100; i++){
						ConfigurationSection warning = playerFile.getConfigurationSection("Warnings").getConfigurationSection("Warning" + i);
						
						sender.sendMessage("Warning #" + i + ":");
						sender.sendMessage("- Reason: " + warning.getString("reason"));
						sender.sendMessage("- Points issued: " + warning.getInt("pointsIssued"));
						sender.sendMessage("- Issuer: " + warning.getString("issuer"));
						
						if(playerFile.getConfigurationSection("Warnings").getConfigurationSection("Warning" + (i + 1)) != null){
							sender.sendMessage("------------");
						}else{
							break;
						}
					}
				}
				
				return true;
			}else if(commandArgs.length == 1){
				if(!sender.hasPermission("warningsystem.warningrecord.others")){ sender.sendMessage(ChatColor.RED + "You do not have access to this command."); return true; }
				File file = null;
				
				File[] files = new File(plugin.getDataFolder(), "Warnings/").listFiles();
				
				for(File tempFile : files){
					if(tempFile.getName().equalsIgnoreCase(commandArgs[0] + ".yml")){
						file = tempFile;
					}
				}
				
				if(file == null){
					sender.sendMessage(ChatColor.RED + commandArgs[0] + " has not been issued any warnings.");
					return true;
				}
				
				FileConfiguration playerFile = YamlConfiguration.loadConfiguration(file); 
				
				if(playerFile.getConfigurationSection("Warnings") != null){
					
					sender.sendMessage(ChatColor.GOLD + "Warnings issued to " + file.getName().replace(".yml", "") + (Bukkit.getServer().getBanList(Type.NAME).isBanned(commandArgs[0]) ? " (" + ChatColor.DARK_RED + "banned" + ChatColor.GOLD + ")" : "") + ":");
					
					for(int i = 1; i <= 100; i++){
						ConfigurationSection warning = playerFile.getConfigurationSection("Warnings").getConfigurationSection("Warning" + i);
						
						sender.sendMessage("Warning #" + i + ":");
						sender.sendMessage("- Reason: " + warning.getString("reason"));
						sender.sendMessage("- Points issued: " + warning.getInt("pointsIssued"));
						sender.sendMessage("- Issuer: " + warning.getString("issuer"));
						
						if(playerFile.getConfigurationSection("Warnings").getConfigurationSection("Warning" + (i + 1)) != null){
							sender.sendMessage("------------");
						}else{
							break;
						}
					}
				}
			}
			
			return true;
		}
		
		return false;
	}

}
