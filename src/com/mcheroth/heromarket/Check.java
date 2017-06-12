package com.mcheroth.heromarket;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Check {
	public boolean CheckPlaceItemInInventory(InventoryClickEvent e){
		if(e.getRawSlot() == -999)return false;
		if(e.getCursor()==null)return false;
		if(e.getCursor().getType() == Material.AIR)return false;
		return true;
	}
	public boolean CheckGetItemInInventory(InventoryClickEvent e){
		if(e.getRawSlot() == -999)return false;
		if(e.getCurrentItem()==null)return false;
		if(e.getCurrentItem().getType() == Material.AIR)return false;
		return true;
	}
	public boolean CheckHaveCredit(ItemStack i){
		if(!i.hasItemMeta())return false;
		ItemMeta iMeta = i.getItemMeta();
		if(!iMeta.hasLore())return false;
		Iterator<String> it = iMeta.getLore().iterator();
		while(it.hasNext()){
			String s = it.next();
			if(s.contains("ราคา ")&&s.contains(" credit")){
				return true;
			}
			if(s.contains("nullCredit")){
				return true;
			}
		}
		return false;
	}
	public boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
