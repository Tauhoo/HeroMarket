package com.mcheroth.heromarket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;

public class Tool {
	HeroMarket hm;
	FileConfiguration cf ;
	public Tool(HeroMarket heromaket){
		hm = heromaket;
		cf = hm.getConfig();
	}
	public ItemStack addlore(ItemStack item,String text){
		ItemStack i = item;
		ItemMeta iMeta = i.getItemMeta();
		List<String> lore = iMeta.getLore();
		lore.add(text);
		iMeta.setLore(lore);
		i.setItemMeta(iMeta);
		return i;
	}
	public ItemStack getPlayerSkull(Player player){
		ItemStack i = new ItemStack(Material.SKULL_ITEM, (short) 3);
		SkullMeta iMeta = (SkullMeta) i.getItemMeta();
		i.setAmount(0);
		i.setDurability((short) 3);
		iMeta.setOwner(player.getName());
		i.setItemMeta(iMeta);
		return i;
	}
	public void updatePlayerConfig(List<ItemStack> itemStacklist,List<Double> creditlist,Player player){
		cf.set("MerchantList."+player.getName()+".ItemList",itemStacklist);
		cf.set("MerchantList."+player.getName()+".CreditList",creditlist);
		hm.saveConfig();
	}
	public ItemStack exiteButton(){
		ItemStack exite = new ItemStack(Material.REDSTONE_BLOCK); 
		ItemMeta exiteMeta = exite.getItemMeta();
		exiteMeta.setDisplayName("Close");
		exiteMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		exiteMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		exite.setItemMeta(exiteMeta);
		return exite;
	}
	public ItemStack setCreditButton(){
		ItemStack set = new ItemStack(Material.JUKEBOX); 
		ItemMeta setMeta = set.getItemMeta();
		setMeta.setDisplayName("ตั้งราคาไอเทม");
		setMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		setMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		set.setItemMeta(setMeta);
		return set;
	}
	public ItemStack backgoundInv(){
		ItemStack set = new ItemStack(Material.getMaterial(160)); 
		ItemMeta setMeta = set.getItemMeta();
		setMeta.setDisplayName("");
		set.setItemMeta(setMeta);
		return set;
	}
	public boolean checkItemEqual(ItemStack first,ItemStack second){
		if(first==null||second==null)return false;
		if(first.getType()==Material.AIR || second.getType()==Material.AIR)return false;
		ItemStack i1 = first;
		ItemStack i2 = second;
		i1.setAmount(1);
		i2.setAmount(1);
		if(i1.equals(i2)){
			return true;
		}
		return false;
	}
	public Inventory sellGUI(Player player){
		List<ItemStack> ItemStackList = (List<ItemStack>) cf.get("MerchantList."+player.getName()+".ItemList");
		Iterator<ItemStack> ItItemStackList = ItemStackList.iterator();
		Inventory SellGUI = Bukkit.createInventory(player, 54, ChatColor.GREEN+"ลากไอเทมลงในช่องแล้วตั้งราคาเพื่อขาย");
		while(ItItemStackList.hasNext()){
			SellGUI.addItem(ItItemStackList.next());
		}
		for(int i = 45; i <= 51 ; i++){
			SellGUI.setItem(i, backgoundInv());
		}
		SellGUI.setItem(52, setCreditButton());
		SellGUI.setItem(53, exiteButton());
		return SellGUI;
	}
}
