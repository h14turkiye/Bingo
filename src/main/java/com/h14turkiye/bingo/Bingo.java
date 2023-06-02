package com.h14turkiye.bingo;
import org.bukkit.plugin.java.JavaPlugin;

import com.h14turkiye.bingo.command.GetBoard;
import com.h14turkiye.bingo.game.BingoGame;

public final class Bingo extends JavaPlugin {
	public static final boolean FOLIA = classExist("io.papermc.paper.threadedregions.scheduler.AsyncScheduler");
	
	private MaterialManager resourceManager;
	private BingoGame game;
	
	private static boolean classExist(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		resourceManager = new MaterialManager(this);
		resourceManager.extractMaterials();
		resourceManager.loadMaterials();

		game = new BingoGame(this);
		getCommand("bingo").setExecutor(new GetBoard(this));
		/*

		Bukkit.getPluginManager().registerEvents(new OnRightClick(playerManager), this);*/

	}

	public MaterialManager getMaterialManager() {
		return resourceManager;
	}

	public BingoGame getGame() {
		return game;
	}
	
}