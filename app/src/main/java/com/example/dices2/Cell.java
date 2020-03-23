package com.example.dices2;

import java.io.Serializable;
import java.util.Objects;

public class Cell implements Serializable {
    private String col;
    private String row;
    public Cell(String col, String row) {
        this.col = col;
        this.row = row;
    }

    public String getCol() {
        return col;
    }

    public String getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return Objects.equals(col, cell.col) &&
                Objects.equals(row, cell.row);
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }
}
