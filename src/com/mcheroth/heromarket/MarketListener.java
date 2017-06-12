package com.mcheroth.heromarket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;

public class MarketListener implements Listener{
	Tool tool ;
	Check check = new Check();
	BukkitScheduler scheduler ;
	HeroMarket hm;
	FileConfiguration cf ;
	ArrayList<Player> playerSetCredit = new ArrayList<Player>();
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
		ItemStack PlayerSkull = tool.getPlayerSkull(e.getPlayer().getName());
		cf.set("MerchantList."+player.getName()+".Skull", PlayerSkull);
		cf.set("MerchantList."+player.getName()+".ItemList", ItemStackList);
		hm.saveConfig();
	}
	@EventHandler
	public void sellInventoryClickEvent(InventoryClickEvent e){
		Player player = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		if(!inv.getName().contains("ลากไอเทมลงในช่องแล้วตั้งราคาเพื่อขาย"))return;
		if(tool.checkItemEqual(e.getCurrentItem(), tool.exiteButton())){
			e.setCancelled(true);
			tool.updatePlayerConfig(Arrays.asList(inv.getContents()),player);
			player.closeInventory();
			return;
		}
		if(tool.checkItemEqual(e.getCurrentItem(), tool.setCreditButton())){
			e.setCancelled(true);
			tool.updatePlayerConfig(Arrays.asList(inv.getContents()),player);
			player.closeInventory();
			player.openInventory(tool.setCreditGUI(player));
			return;
		}
		if(tool.checkItemEqual(e.getCurrentItem(), tool.backgoundInv())){
			e.setCancelled(true);
			return;
		}
		if(e.getRawSlot()>53)return;
		e.setCancelled(true);
		ItemStack get = e.getCurrentItem();
		e.setCurrentItem(tool.addlore(e.getCursor(), ChatColor.GREEN+"ราคา 0 credit"));
		e.setCursor(tool.removelore(get));
	}
	@EventHandler
	public void setInventoryClickEvent(InventoryClickEvent e){
		Player player = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		if(!inv.getName().contains("คลิกที่ไอเทมเพื่อตั้งราคา"))return;
		e.setCancelled(true);
		if(tool.checkItemEqual(e.getCurrentItem(), tool.exiteButton())){
			player.closeInventory();
			return;
		}
		if(tool.checkItemEqual(e.getCurrentItem(), tool.BackButton())){
			player.closeInventory();
			player.openInventory(tool.sellGUI(player));
			return;
		}
		if(e.getRawSlot()<45 && e.getRawSlot() != -999){
			if(e.getCurrentItem().getType() == Material.AIR)return;
			playerSetCredit.add(player);
			player.sendMessage(ChatColor.BLUE+"==========================");
			player.sendMessage(ChatColor.YELLOW+"         ตั้งราคา สินค้า                                   ");
			player.sendMessage(ChatColor.BLUE+"==========================");
			player.sendMessage(ChatColor.GRAY+"พิมราคาตั้งราคา");
			player.sendMessage(ChatColor.GRAY+"พิม c เมื่อต้องการยกเลิกการตั้งราคา");
			ItemStack i = e.getCurrentItem();
			i = tool.removelore(i);
			i = tool.addlore(i, "nullCredit");
			e.setCurrentItem(i);
			player.closeInventory();
		}
	}
	@EventHandler
	public void buyInventoryClickEvent(InventoryClickEvent e){
		Player player = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		if(!inv.getName().contains("ร้านขายของ    P."))return;
		e.setCancelled(true);
		String nameSkull = tool.getNameSkull(e.getCurrentItem());
		if(cf.contains("MerchantList."+nameSkull+".Skull")){
			player.closeInventory();
			player.openInventory(tool.privateBuy(nameSkull));
		}
		if(tool.checkItemEqual(e.getCurrentItem(), tool.exiteButton())){
			player.closeInventory();
		}
		if(tool.checkItemEqual(e.getCurrentItem(), tool.changPageButton(1))){
			player.closeInventory();
			player.openInventory(tool.BuyGUI(tool.getPage(inv)+1));
		}
		if(tool.checkItemEqual(e.getCurrentItem(), tool.changPageButton(2))){
			player.closeInventory();
			player.openInventory(tool.BuyGUI(tool.getPage(inv)-1));
		}
	}
	@EventHandler
	public void privatebuyInventoryClickEvent(InventoryClickEvent e){
		Player player = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		if(!inv.getName().contains("คลิกเพื่อซื้อไอเทมของ "))return;
		e.setCancelled(true);
		if(tool.checkItemEqual(e.getCurrentItem(), tool.BackButton())){
			player.closeInventory();
			player.openInventory(tool.BuyGUI(tool.getPlayerPage(inv.getName().split(" ")[1])));
			
		}
		if(tool.checkItemEqual(e.getCurrentItem(), tool.exiteButton())){
			player.closeInventory();
		}
	}
	@EventHandler
	public void sellInventoryCloseEvent(InventoryCloseEvent e){
		if(!e.getInventory().getName().contains("ลากไอเทมลงในช่องแล้วตั้งราคาเพื่อขาย"))return;
		tool.updatePlayerConfig(Arrays.asList(e.getInventory().getContents()),(Player) e.getPlayer());
	}
	@EventHandler
	public void setInventoryCloseEvent(InventoryCloseEvent e){
		if(!e.getInventory().getName().contains("คลิกที่ไอเทมเพื่อตั้งราคา"))return;
		tool.updatePlayerConfig(Arrays.asList(e.getInventory().getContents()),(Player) e.getPlayer());
	}
	@EventHandler
	public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e){
		
		Player player = e.getPlayer();
		if(!playerSetCredit.contains(player))return;
		e.setCancelled(true);
		if(e.getMessage().equals("c")){
			playerSetCredit.remove(player);
			player.sendMessage(ChatColor.GREEN+"ยกเลิกการตั้งราคา");
			tool.setCreditConfig("0", player);
			player.openInventory(tool.setCreditGUI(player));
			return;
		}
		if(check.isDouble(e.getMessage())){
			playerSetCredit.remove(player);
			player.sendMessage(ChatColor.GREEN+"คุณตั้งราคาไอเทมเป็นราคา "+e.getMessage()+" credit");
			tool.setCreditConfig(e.getMessage(), player);
			player.openInventory(tool.setCreditGUI(player));
		}else{
			player.sendMessage(ChatColor.RED+"ต้องตั้งราคาเป็นตัวเลขหรือทศนิยมเท่านั้น");
		}
	}
	@EventHandler
	public void PlayerMoveEvent(PlayerMoveEvent e){
		if(!playerSetCredit.contains(e.getPlayer()))return;
		e.setCancelled(true);
	}
}
