package com.h14turkiye.bingo.game.board;

import org.bukkit.Material;

public class BingoBoard {

    private BingoItem[] bingoItems = new BingoItem[25];

    public BingoBoard(Material[] items) {
        for (int i = 0; i < items.length; i++) {
            bingoItems[i] = new BingoItem(items[i]);
        }
    }

    public BingoItem[] getItems() {
        return bingoItems;
    }

    public int getFoundItems() {
        int count = 0;
        for (BingoItem bingoItem : bingoItems) {
            if(bingoItem.isFound()) count++;
        }
        return count;
    }
    
    
}
