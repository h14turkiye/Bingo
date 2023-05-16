package com.h14turkiye.bingo.listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.h14turkiye.bingo.Bingo;

public class OnRightClick implements Listener{

	private Bingo plugin;
	public OnRightClick(Bingo plugin) {
		this.plugin = plugin;
		
	}
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent event){
		Player p = event.getPlayer();
		ItemStack i = p.getInventory().getItemInMainHand();

		if( i != null && i.getType() == Material.FILLED_MAP) {
			p.getInventory().remove(i);
			p.getInventory().addItem(new ItemStack(Material.EMERALD, 16));
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0.1f);
			plugin.getGame().removeBingoPlayer(p.getUniqueId());
		}
	}
}

