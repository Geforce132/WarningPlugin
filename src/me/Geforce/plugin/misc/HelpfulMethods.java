package me.Geforce.plugin.misc;

import java.util.ArrayList;
import java.util.Scanner;

import org.bukkit.configuration.file.FileConfiguration;

public class HelpfulMethods {
	
	public static ArrayList<String> getCommandArgs(String command){
		ArrayList<String> list = new ArrayList<String>();
		Scanner scanner = new Scanner(command);
		scanner.useDelimiter(" ");
		
		while(scanner.hasNext()){
			list.add(scanner.next());
		}
		
		return list;		
	}
	
	public static String getNextWarningSection(FileConfiguration yamlFile){
		for(int i = 1; i <= 100; i++){
			if(yamlFile.getConfigurationSection("Warnings").getConfigurationSection("Warning" + i) == null){
				return ("Warning" + i);
			}else{
				continue;
			}
		}
		
		return null;
	}

}
