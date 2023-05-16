package com.h14turkiye.bingo;
import org.bukkit.plugin.java.JavaPlugin;

import com.h14turkiye.bingo.command.GetBoard;
import com.h14turkiye.bingo.game.BingoGame;
import com.h14turkiye.bingo.scheduler.AlignedScheduler;

public final class Bingo extends JavaPlugin {
	private AlignedScheduler alignedScheduler;
	private MaterialManager resourceManager;
	private BingoGame game;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		alignedScheduler = new AlignedScheduler();
		
		resourceManager = new MaterialManager(this);
		resourceManager.extractMaterials();
		resourceManager.loadMaterials();

		game = new BingoGame(this);
		getCommand("bingo").setExecutor(new GetBoard(this));
		/*

		Bukkit.getPluginManager().registerEvents(new OnRightClick(playerManager), this);*/

	}
	
	public AlignedScheduler getAlignedScheduler() {
		return alignedScheduler;
	}

	public MaterialManager getMaterialManager() {
		return resourceManager;
	}

	public BingoGame getGame() {
		return game;
	}
	
}