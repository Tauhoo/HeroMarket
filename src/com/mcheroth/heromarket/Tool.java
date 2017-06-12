package com.mcheroth.heromarket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
	Check check = new Check();
	HeroMarket hm;
	FileConfiguration cf ;
	public Tool(HeroMarket heromaket){
		hm = heromaket;
		cf = hm.getConfig();
	}
	public String getNameSkull(ItemStack i){
		if(!i.hasItemMeta())return "";
		ItemMeta iMeta = i.getItemMeta();
		if(!iMeta.hasDisplayName())return "";
		String s = iMeta.getDisplayName();
		return s.replace(""+ChatColor.AQUA, "");
	}
	public ItemStack addlore(ItemStack item,String text){
		if(item == null)return item;
		if(item.getType() == Material.AIR)return item;
		ItemStack i = item;
		ItemMeta iMeta = i.getItemMeta();
		List<String> lore ;
		if(!iMeta.hasLore()){
			lore = new ArrayList<String>();
		}else{
			lore = iMeta.getLore();
		}
		lore.add(text);
		iMeta.setLore(lore);
		i.setItemMeta(iMeta);
		return i;
	}
	public ItemStack removelore(ItemStack item){
		ItemStack i = item;
		if(!check.CheckHaveCredit(i))return i;
		ItemMeta iMeta = i.getItemMeta();
		List<String> lore = iMeta.getLore();
		lore.remove(lore.size()-1);
		iMeta.setLore(lore);
		i.setItemMeta(iMeta);
		return i;
	}
	public ItemStack getPlayerSkull(String name){
		ItemStack i = new ItemStack(Material.SKULL_ITEM, (short) 3);
		SkullMeta iMeta = (SkullMeta) i.getItemMeta();
		i.setAmount(1);
		i.setDurability((short) 3);
		iMeta.setOwner(name);
		iMeta.setDisplayName(ChatColor.AQUA+name);
		i.setItemMeta(iMeta);
		return i;
	}
	public void updatePlayerConfig(List<ItemStack> itemStacklist,Player player){
		Iterator<ItemStack> it = itemStacklist.iterator();
		List<ItemStack> result = new ArrayList<ItemStack>();
		while(it.hasNext()){
			ItemStack item = it.next();
			if(item!=null){
				if(checkItemEqual(item, BackButton()) || checkItemEqual(item, backgoundInv()) || checkItemEqual(item, exiteButton()) || checkItemEqual(item, setCreditButton()) || item.getType() == Material.AIR){
			
				}else{
					result.add(item);
				}
			}
		}
		cf.set("MerchantList."+player.getName()+".ItemList",result);
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
	public ItemStack BackButton(){
		ItemStack set = new ItemStack(Material.IRON_DOOR); 
		ItemMeta setMeta = set.getItemMeta();
		setMeta.setDisplayName("Back");
		setMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		setMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		set.setItemMeta(setMeta);
		return set;
	}
	public ItemStack backgoundInv(){
		ItemStack set = new ItemStack(Material.getMaterial(160)); 
		ItemMeta setMeta = set.getItemMeta();
		setMeta.setDisplayName("  ");
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
			ItemStack item = ItItemStackList.next();
			if(item!=null){
				if(checkItemEqual(item, BackButton()) || checkItemEqual(item, backgoundInv()) || checkItemEqual(item, exiteButton()) || checkItemEqual(item, setCreditButton())){
					SellGUI.addItem(new ItemStack(Material.AIR));
				}else{
					SellGUI.addItem(item);
				}
			}else{
				SellGUI.addItem(new ItemStack(Material.AIR));
			}
			
		}
		for(int i = 45; i <= 51 ; i++){
			SellGUI.setItem(i, backgoundInv());
		}
		SellGUI.setItem(52, setCreditButton());
		SellGUI.setItem(53, exiteButton());
		return SellGUI;
	}
	public Inventory setCreditGUI(Player player){
		List<ItemStack> ItemStackList = (List<ItemStack>) cf.get("MerchantList."+player.getName()+".ItemList");
		Iterator<ItemStack> ItItemStackList = ItemStackList.iterator();
		Inventory SetGUI = Bukkit.createInventory(player, 54, ChatColor.AQUA+"คลิกที่ไอเทมเพื่อตั้งราคา");
		while(ItItemStackList.hasNext()){
			ItemStack item = ItItemStackList.next();
			if(item!=null){
				if(checkItemEqual(item, BackButton()) || checkItemEqual(item, backgoundInv()) || checkItemEqual(item, exiteButton()) || checkItemEqual(item, setCreditButton())){
					SetGUI.addItem(new ItemStack(Material.AIR));
				}else{
					SetGUI.addItem(item);
				}
			}else{
				SetGUI.addItem(new ItemStack(Material.AIR));
			}
		}
		for(int i = 45; i <= 51 ; i++){
			SetGUI.setItem(i, backgoundInv());
		}
		SetGUI.setItem(52, BackButton());
		SetGUI.setItem(53, exiteButton());
		return SetGUI;
	}
	public void setCreditConfig(String credit,Player player){
		List<ItemStack> ItemStackList = (List<ItemStack>) cf.get("MerchantList."+player.getName()+".ItemList");
		List<ItemStack> ItemStackListResult =  new ArrayList<ItemStack>();
		Iterator<ItemStack> ItItemStackList = ItemStackList.iterator();
		while(ItItemStackList.hasNext()){
			ItemStack i = ItItemStackList.next();
			if(finalLore(i).equals("nullCredit")){
				removelore(i);
				addlore(i, ChatColor.GREEN+"ราคา "+credit+" credit");
			}
			ItemStackListResult.add(i);
		}
		hm.getServer().getLogger().info(ItemStackListResult.size()+"");
		updatePlayerConfig(ItemStackListResult, player);
		
	}
	public String finalLore(ItemStack i){
		if(!i.hasItemMeta())return "";
		ItemMeta iMeta = i.getItemMeta();
		if(!iMeta.hasLore())return "";
		List<String> sList = iMeta.getLore();
		return sList.get(sList.size()-1);
	}
	public Inventory BuyGUI(int page){
		Inventory buyInv = Bukkit.createInventory(null, 54, "     "+ChatColor.DARK_GREEN+"ร้านขายของ    P."+page);
		List<ItemStack> playerSkullList = getSkullList();
		int pageAmount = playerSkullList.size()/45+1;
		List<ItemStack> itemInThisPage = new ArrayList<ItemStack>();
		int amountItemInPage = page*45;
		if(page==pageAmount){
			 amountItemInPage = playerSkullList.size();
		}
		for(int i=0+45*(page-1);i<amountItemInPage;i++){
			buyInv.addItem(playerSkullList.get(i));
		}
		for(int o = 45; o < 54; o++){
			buyInv.setItem(o, backgoundInv());
		}
		if(page!=1){
			buyInv.setItem(45, changPageButton(2));
		}
		if(page!=pageAmount){
			buyInv.setItem(46, changPageButton(1));
		}
		buyInv.setItem(53, exiteButton());
		return buyInv;
	}
	public List<ItemStack> getSkullList(){
		OfflinePlayer[] opList = Bukkit.getOfflinePlayers();
		List<ItemStack> skullList = new ArrayList<ItemStack>();
		Iterator<OfflinePlayer> itopList = Arrays.asList(opList).iterator();
		while(itopList.hasNext()){
			OfflinePlayer op = itopList.next();
			if(cf.contains("MerchantList."+op.getName()+".ItemList")){
				List<ItemStack> ItemStackList = (List<ItemStack>) cf.get("MerchantList."+op.getName()+".ItemList");
				ItemStack playerSkull =   (ItemStack) cf.get("MerchantList."+op.getName()+".Skull");
				int itemAmount = ItemStackList.size();
				skullList.add(addlore(clearLore(playerSkull),ChatColor.GRAY+"มีสินค้าจำนวน "+itemAmount+" ชิ้น"));
			}
		}
		return skullList;
	}
	public ItemStack clearLore(ItemStack i){
		if(!i.hasItemMeta())return i;
		ItemStack item = i;
		ItemMeta iMeta = i.getItemMeta();
		if(!iMeta.hasLore())return i;
		iMeta.setLore(new ArrayList<String>());
		item.setItemMeta(iMeta);
		return item;
	}
	public ItemStack changPageButton(int Type){
		ItemStack button = new ItemStack(Material.DARK_OAK_DOOR_ITEM);
		ItemMeta buttonMeta = button.getItemMeta();
		buttonMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		buttonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		if(Type==1){
			buttonMeta.setDisplayName(ChatColor.LIGHT_PURPLE+"ไปหน้าต่อไป");
		}else if(Type==2){
			buttonMeta.setDisplayName(ChatColor.LIGHT_PURPLE+"กลับไปหน้าก่อน");
		}
		button.setItemMeta(buttonMeta);
		return button;
	}
	public Inventory privateBuy(String ownerName){
			List<ItemStack> ItemStackList = (List<ItemStack>) cf.get("MerchantList."+ownerName+".ItemList");
			Iterator<ItemStack> ItItemStackList = ItemStackList.iterator();
			Inventory GUI = Bukkit.createInventory(null, 54, ChatColor.GREEN+"คลิกเพื่อซื้อไอเทมของ "+ownerName);
			while(ItItemStackList.hasNext()){
				ItemStack item = ItItemStackList.next();
				if(item!=null){
					if(checkItemEqual(item, BackButton()) || checkItemEqual(item, backgoundInv()) || checkItemEqual(item, exiteButton()) || checkItemEqual(item, setCreditButton())){
						GUI.addItem(new ItemStack(Material.AIR));
					}else{
						GUI.addItem(item);
					}
				}else{
					GUI.addItem(new ItemStack(Material.AIR));
				}
				
			}
			for(int i = 45; i <= 51 ; i++){
				GUI.setItem(i, backgoundInv());
			}
			GUI.setItem(52, this.BackButton());
			GUI.setItem(53, exiteButton());
			return GUI;
		}
	public int getPage(Inventory buyInv){
		String s = buyInv.getName().replace("     "+ChatColor.DARK_GREEN+"ร้านขายของ    P.", "");
		return Integer.parseInt(s);
	}
	public int getPlayerPage(String name){
		List<OfflinePlayer> oplist = Arrays.asList(Bukkit.getOfflinePlayers());
		Iterator<OfflinePlayer> it = oplist.iterator();
		while(it.hasNext()){
			OfflinePlayer op = it.next();
			if(op.getName().equals(name)){
				return (oplist.indexOf(op))/45+1;
			}
		}
		return 0;
	}
}
