package com.example.dices2;

import android.graphics.Color;

import static com.example.dices2.RowName.*;

public class Consts {
    public static final RowName[] namesOfRows = {NAMES, ONE, TWO, THREE, FOUR, FIVE, SIX, SCHOOL, SEPARATOR,
                                                PAIR, TWO_PLUS_TWO, TRIANGLE, SMALL, BIG, SEPARATOR,
                                                FULL, SQUARE, POKER, CHANCE1, CHANCE2, SEPARATOR, TOTAL, PLACE};
    public static final RowName[] namesOfValuableRows = {
            ONE, TWO, THREE, FOUR, FIVE, SIX,
            PAIR, TWO_PLUS_TWO, TRIANGLE, SMALL, BIG,
            FULL, SQUARE, POKER, CHANCE1, CHANCE2};
    public static final RowName[] namesOfRowsWithoutSchool = {
            PAIR, TWO_PLUS_TWO, TRIANGLE, SMALL, BIG,
            FULL, SQUARE, POKER, CHANCE1, CHANCE2};
    public static final RowName[] NAMES_OF_SCHOOL_ROWS = {ONE, TWO, THREE, FOUR, FIVE, SIX};

    public static final String INTENT_INPUT_VALUE = "input_value";
    public static final String INTENT_ROW_NAME = "row_name";
    public static final String INTENT_IS_EDIT = "is_edit";
    public static final String INTENT_EDITED_VALUE = "edited_value";
    public static final String INTENT_IS_IN_SCHOOL = "is_in_school";

    public static final int MAIN_TABLE_FONT_SIZE = 32;
    public static final int MAIN_TABLE_TEXT_COLOR = Color.WHITE;
    public static final int SEPARATOR_HEIGHT = 10;
}
