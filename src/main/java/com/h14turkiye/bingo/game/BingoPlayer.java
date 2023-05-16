package com.h14turkiye.bingo.game;

import java.util.UUID;

import com.h14turkiye.bingo.game.board.BingoBoard;

public class BingoPlayer {
	private BingoBoard board;
	private UUID uuid;

	public BingoPlayer(UUID uuid, BingoBoard board) {
		this.uuid = uuid;
		this.board = board;
	}
	

	public UUID getUuid() {
		return uuid;
	}
	
	public BingoBoard getBoard() {
		return board;
	}
	
	public void setBoard(BingoBoard board) {
		this.board = board;
	}
	
	public boolean checkWin() {
        int[][] winSituations = new int[][]{
                //horizonzal
                {0, 1, 2, 3, 4},
                {5, 6, 7, 8, 9},
                {10, 11, 12, 13, 14},
                {15, 16, 17, 18, 19},
                {20, 21, 22, 23, 24},
                //vertical
                {0, 5, 10, 15, 20},
                {1, 6, 11, 16, 21},
                {2, 7, 12, 17, 22},
                {3, 8, 13, 18, 23},
                {4, 9, 14, 19, 24},
                //diagonal
                {0, 6, 12, 18, 24},
                {4, 8, 12, 16, 20}
        };
        for (int[] winSituation : winSituations) {
            boolean win = true;
            for (int i : winSituation) {
                if (!board.getItems()[i].isFound()) {
                    win = false;
                }
            }
            if (win) {
                return true;
            }
        }
        return false;
    }

}
