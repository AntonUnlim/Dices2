package com.example.dices2;

public class GameValue {
    private String value;

    public GameValue(String value) {
        this.value = value;
    }

    public int getValue() {
        if (value.equals("-") || value.equals("+")) {
            return 0;
        } else {
            return Integer.parseInt(value);
        }
    }

    public void setValue(String value) {
        this.value = value;
    }
}
