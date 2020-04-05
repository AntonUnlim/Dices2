package com.example.dices2;

import android.content.Context;
import android.view.Gravity;

import java.util.Objects;

public class Cell extends androidx.appcompat.widget.AppCompatTextView {
    private Player owner;
    private RowName row;
    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        setText(value);
    }

    public boolean isEmpty() {
        return getText().equals("") || getText() == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return owner.equals(cell.owner) && row == cell.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, row);
    }
}
