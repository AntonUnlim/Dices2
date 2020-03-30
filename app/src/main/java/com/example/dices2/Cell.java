package com.example.dices2;

import android.content.Context;
import android.view.Gravity;

import java.io.Serializable;

public class Cell extends androidx.appcompat.widget.AppCompatTextView implements Serializable {
    private Player owner;
    private RowName row;

    public Cell(Context context, Player owner, RowName row) {
        super(context);
        this.owner = owner;
        this.row = row;
        setTextColor(Consts.MAIN_TABLE_TEXT_COLOR);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setTextSize(Consts.MAIN_TABLE_FONT_SIZE);
    }

    public Player getOwner() {
        return owner;
    }

    public RowName getRow() {
        return row;
    }

    public boolean isEmpty() {
        return getText().equals("") || getText() == null;
    }
}
