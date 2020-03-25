package com.example.dices2;

public class Consts {
    public static final String NAMES = "Names";
    public static final String SEPARATOR = "Separator";
    public static final String TRIANGLE = "Three";
    public static final String SCHOOL = "Школа";
    public static final String TOTAL = "Итого";
    public static final String PLACE = "Место";
    public static final String[] namesOfRows = {
            NAMES, "1", "2", "3", "4", "5", "6", SCHOOL, SEPARATOR,
            "Два", "2+2", TRIANGLE, "Малый", "Большой", SEPARATOR,
            "Фула", "Каре", "Покер", "Шанс 1", "Шанс 2", SEPARATOR, TOTAL, PLACE};
    public static final String[] namesOfValuableRows = {
            "1", "2", "3", "4", "5", "6",
            "Два", "2+2", TRIANGLE, "Малый", "Большой",
            "Фула", "Каре", "Покер", "Шанс 1", "Шанс 2"};
    public static final String[] namesOfRowsWithoutSchool = {
            "Два", "2+2", TRIANGLE, "Малый", "Большой",
            "Фула", "Каре", "Покер", "Шанс 1", "Шанс 2"};
    public static final String[] namesOfSchoolRows = {"1", "2", "3", "4", "5", "6"};
    public static final String INTENT_INPUT_VALUE = "input_value";
    public static final String INTENT_ROW_NAME = "row_name";
}
