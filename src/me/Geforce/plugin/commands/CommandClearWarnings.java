package me.Geforce.plugin.commands;

import java.io.File;

import me.Geforce.plugin.plugin_WarningSystem;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandClearWarnings implements CommandExecutor {

	private plugin_WarningSystem plugin;
	
	public CommandClearWarnings(plugin_WarningSystem plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String unknownString, String[] commandArgs) {	
		if(command.getName().equalsIgnoreCase("clearwarnings")){
			if(!sender.hasPermission("warningsystem.clearwarnings")){ sender.sendMessage(ChatColor.RED + "You do not have access to this command."); return true; }

			if(commandArgs.length == 1){
				boolean wasBanned = false;
				File file = new File(plugin.getDataFolder(), "Warnings/" + (Bukkit.getServer().getPlayer(commandArgs[0]) != null ? Bukkit.getServer().getPlayer(commandArgs[0]).getUniqueId() : Bukkit.getServer().getOfflinePlayer(commandArgs[0]).getUniqueId()) + ".yml");
				
				if(!file.exists()){
					sender.sendMessage(ChatColor.RED + "Found no warnings for player " + commandArgs[0]);
					return true;
				}
				
				file.delete();
				
				
				if(Bukkit.getServer().getBanList(Type.NAME).isBanned(commandArgs[0])){
					Bukkit.getServer().getBanList(Type.NAME).pardon(commandArgs[0]);
					wasBanned = true;
				}
				
				sender.sendMessage(ChatColor.GREEN + "Successfully " + (wasBanned ? "unbanned and " : "") + "cleared warnings for player " + commandArgs[0] + ".");
				 
				return true;
			}
		}
		
		return false;
	}

}
