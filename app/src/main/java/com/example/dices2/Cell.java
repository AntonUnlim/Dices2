package com.example.dices2;

import java.io.Serializable;

public class Cell implements Serializable {
    private Player player;
    private String row;

    public Cell(Player player, String row) {
        this.player = player;
        this.row = row;
    }

    public Player getPlayer() {
        return player;
    }

    public String getRow() {
        return row;
    }
}
