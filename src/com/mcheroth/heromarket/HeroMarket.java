package com.mcheroth.heromarket;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class HeroMarket extends JavaPlugin{
	Tool tool ;
	FileConfiguration cf ;
	public void onEnable(){
		getServer().getLogger().info("[HeroMarket] working");
		getConfig().options().copyDefaults();
		saveConfig();
		cf = getConfig();
		tool = new Tool(this);
		MarketListener ml = new MarketListener(this);
	}
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args){
		if(!sender.isOp())return false;
		if(cmd.getName().equalsIgnoreCase("heromarket")){
			Player player = null;
			if( args.length == 2 && args[0].equals("open")){
				player = Bukkit.getPlayer(args[1]);
				player.openInventory(tool.BuyGUI(1));
				return true;
			}
			if( args.length == 2 && args[0].equals("sell")){
				player = Bukkit.getPlayer(args[1]);
				player.openInventory(tool.sellGUI(player));
				return true;
			}
			if( args.length == 1 && args[0].equals("update")){
				reloadConfig();
				sender.sendMessage(ChatColor.GREEN+"Config was reloaded.");
				return true;
			}
		}
		return false;
	}	
}
