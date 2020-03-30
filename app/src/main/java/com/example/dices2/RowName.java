package com.example.dices2;

public enum RowName {
    NAMES(null),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SCHOOL("Школа"),
    SEPARATOR(null),
    PAIR("Два"),
    TWO_PLUS_TWO("2+2"),
    TRIANGLE("Три"),
    SMALL("Малый"),
    BIG("Большой"),
    FULL("Фула"),
    SQUARE("Каре"),
    POKER("Покер"),
    CHANCE1("Шанс 1"),
    CHANCE2("Шанс 2"),
    TOTAL("Итого"),
    PLACE("Место");

    private String name;

    RowName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
