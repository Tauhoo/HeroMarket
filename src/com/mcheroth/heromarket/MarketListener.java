package com.mcheroth.heromarket;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

public class MarketListener implements Listener{
	Tool tool ;
	BukkitScheduler scheduler ;
	HeroMarket hm;
	FileConfiguration cf ;
	public MarketListener(HeroMarket heromarket){
		Bukkit.getPluginManager().registerEvents(this, heromarket);
		hm = heromarket;
		cf = hm.getConfig();
		scheduler = hm.getServer().getScheduler();
		tool = new Tool(hm); 
	}
	@EventHandler
	public void PlayerJoinEvent(PlayerJoinEvent e){
		Player player = e.getPlayer();
		if(cf.contains("MerchantList."+player.getName()))return;
		List<ItemStack> ItemStackList = new ArrayList<ItemStack>();
		List<Double> CreditList = new ArrayList<Double>();
		ItemStack PlayerSkull = tool.getPlayerSkull(e.getPlayer());
		cf.set("MerchantList."+player.getName()+".Skull", PlayerSkull);
		cf.set("MerchantList."+player.getName()+".ItemList", ItemStackList);
		cf.set("MerchantList."+player.getName()+".CreditList", CreditList);
		hm.saveConfig();
	}
	@EventHandler
	public void InventoryClickEvent(InventoryClickEvent e){
		Player player = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		if(!inv.getName().contains("ลากไอเทมลงในช่องแล้วตั้งราคาเพื่อขาย"))return;
		if(tool.checkItemEqual(e.getCurrentItem(), tool.exiteButton())){
			e.setCancelled(true);
			player.closeInventory();
		}
		if(tool.checkItemEqual(e.getCurrentItem(), tool.setCreditButton())){
			e.setCancelled(true);
		}
		if(tool.checkItemEqual(e.getCurrentItem(), tool.backgoundInv())){
			e.setCancelled(true);
		}
	}
	
	
}
