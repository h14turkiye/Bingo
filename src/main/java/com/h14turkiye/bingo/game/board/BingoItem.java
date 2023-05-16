package com.h14turkiye.bingo.game.board;

import org.bukkit.Material;

public class BingoItem {

    private final Material item;
    private boolean found = false;

    public BingoItem(Material item) {
        this.item = item;
    }

    public Material getMaterial() {
        return item;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }
}
