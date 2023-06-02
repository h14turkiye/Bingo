package com.h14turkiye.bingo.game;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.h14turkiye.bingo.Bingo;
import com.h14turkiye.bingo.game.board.BingoBoard;
import com.h14turkiye.bingo.game.board.BingoItem;
import com.h14turkiye.bingo.game.board.map.BoardRenderer;
import com.h14turkiye.bingo.util.SchedulerUtil;

public class BingoGame {

	private Map<UUID, BingoPlayer> bingoPlayers = new HashMap<>();
	private Bingo plugin;
	Material[] items = new Material[25];
	private BoardRenderer renderer;

	public BingoGame(Bingo plugin) {
		this.plugin = plugin;
		createBoard();
		resetBoardTimer();

		checkCompletionTimer();
	}

	public BingoPlayer getBingoPlayer(Player player) {
		return bingoPlayers.getOrDefault(player.getUniqueId(), null);
	}

	public BingoPlayer getBingoPlayer(UUID uuid) {
		return bingoPlayers.getOrDefault(uuid, null);
	}

	public void addBingoPlayer(UUID uuid) {
		bingoPlayers.put(uuid, new BingoPlayer(uuid, new BingoBoard(items)));
	}

	public void removeBingoPlayer(UUID uuid) {
		bingoPlayers.remove(uuid);
	}

	public Collection<UUID> getUUIDS() {
		return bingoPlayers.keySet();
	}

	public Collection<BingoPlayer> getBingoPlayers() {
		return bingoPlayers.values();
	}

	public Material[] getItems() {
		return items;
	}

	public BoardRenderer getRenderer() {
		return renderer;
	}

	public void createBoard() {

		//generation of random items
		for (int i = 0; i < items.length; i++) {
			Material bingoMaterial = getRandomMaterial();
			while (Arrays.asList(items).contains(bingoMaterial)) {
				bingoMaterial = getRandomMaterial();
			}
			items[i] = bingoMaterial;
		}

		renderer = new BoardRenderer(plugin, this);
		renderer.updateImages();
	}

	public ItemStack getRenderedBingoBoardItemStack() {
		FileConfiguration config = plugin.getConfig();
		ItemStack itemStack = new ItemStack(Material.FILLED_MAP);
		MapView view = Bukkit.createMap(Bukkit.getWorlds().get(0));

		for (MapRenderer ren : view.getRenderers())
			view.removeRenderer(ren);

		view.addRenderer(renderer);
		MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
		mapMeta.setMapView(view);
		mapMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("Item.name")));
		mapMeta.setLore(com.h14turkiye.bingo.util.ChatColor.translateAlternateColorCodes('&',config.getStringList("Item.lore")));
		itemStack.setItemMeta(mapMeta);
		return itemStack;
	}

	public void checkCompletionTimer() {
		SchedulerUtil.runTaskTimer(plugin, () -> {
			for(BingoPlayer bingoPlayer : getBingoPlayers()) {
				Player player = Bukkit.getPlayer(bingoPlayer.getUuid());
				if(player == null || bingoPlayer.checkWin()) {
					continue;
				}
				for (BingoItem item : bingoPlayer.getBoard().getItems()) {
					if(item.isFound()) {
						continue;
					}
					if (player.getInventory().contains(item.getMaterial())) {
						item.setFound(true);
						player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
					}
				}
			}
		}, 20, 20);

	}


	public void resetBoardTimer() {
		String wanted = plugin.getConfig().getString("Date"); // HH:mm
		int wantedHour = Integer.parseInt(wanted.split(":")[0]);
		if (wantedHour == 24) wantedHour = 0;  
		int wantedMinute = Integer.parseInt(wanted.split(":")[1]);
		SchedulerUtil.runTaskTimer(plugin, () ->
		{
			// Resetting the board
			bingoPlayers.clear();
			createBoard();

		}, SchedulerUtil.getDelayRequired(wantedHour, wantedMinute), 1000L * 60 * 60 * 24);
	}

	public Material getRandomMaterial() {
		List<Material> materials = plugin.getMaterialManager().getMaterials();
		return (Material) materials.toArray()[new Random().nextInt(materials.size())];
	}
}
