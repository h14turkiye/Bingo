package com.h14turkiye.bingo.command;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.h14turkiye.bingo.Bingo;
import com.h14turkiye.bingo.game.BingoGame;

public class GetBoard implements CommandExecutor {

	private Bingo plugin;

    public GetBoard(Bingo plugin) {
        this.plugin = plugin;
        
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		FileConfiguration config = plugin.getConfig();
		BingoGame game = plugin.getGame();
        if(!(sender instanceof Player))return false;
        Player player = (Player) sender;
        if(game.getUUIDS().contains(player.getUniqueId()) && !player.hasPermission(plugin.getConfig().getString("bypass-permission"))){
        	player.sendMessage(config.getString("Messages.1"));
        	return false;
        }
        
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK,1,1);
        ItemStack boardMap = game.getRenderedBingoBoardItemStack();
        if(!player.getInventory().addItem(boardMap).values().isEmpty()) {
        	player.sendMessage(config.getString("Messages.2"));
        	return false;
        }
        game.addBingoPlayer(player.getUniqueId());
        return true;
    }
}
