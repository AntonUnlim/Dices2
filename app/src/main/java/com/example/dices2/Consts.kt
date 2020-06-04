package com.example.dices2

import android.graphics.Color
import java.util.*

object Consts {
    val namesOfRows = arrayOf(RowName.NAMES, RowName.ONE, RowName.TWO, RowName.THREE, RowName.FOUR, RowName.FIVE, RowName.SIX, RowName.SCHOOL, RowName.SEPARATOR, RowName.PAIR, RowName.TWO_PLUS_TWO, RowName.TRIANGLE, RowName.SMALL, RowName.BIG, RowName.SEPARATOR, RowName.FULL, RowName.SQUARE, RowName.POKER, RowName.CHANCE1, RowName.CHANCE2, RowName.SEPARATOR, RowName.TOTAL, RowName.PLACE)
    val namesOfValuableRows = arrayOf(RowName.ONE, RowName.TWO, RowName.THREE, RowName.FOUR, RowName.FIVE, RowName.SIX, RowName.PAIR, RowName.TWO_PLUS_TWO, RowName.TRIANGLE, RowName.SMALL, RowName.BIG, RowName.FULL, RowName.SQUARE, RowName.POKER, RowName.CHANCE1, RowName.CHANCE2)
    val NAMES_OF_ROWS_WITHOUT_SCHOOL = arrayOf(RowName.PAIR, RowName.TWO_PLUS_TWO, RowName.TRIANGLE, RowName.SMALL, RowName.BIG, RowName.FULL, RowName.SQUARE, RowName.POKER, RowName.CHANCE1, RowName.CHANCE2)
    val NAMES_OF_SCHOOL_ROWS = arrayOf(RowName.ONE, RowName.TWO, RowName.THREE, RowName.FOUR, RowName.FIVE, RowName.SIX)
    const val INTENT_INPUT_VALUE = "input_value"
    const val INTENT_ROW_NAME = "row_name"
    const val INTENT_IS_EDIT = "is_edit"
    const val INTENT_EDITED_VALUE = "edited_value"
    const val INTENT_IS_IN_SCHOOL = "is_in_school"
    const val MAIN_TABLE_FONT_SIZE = 24
    const val MAIN_TABLE_TEXT_COLOR = Color.WHITE
    const val SEPARATOR_HEIGHT = 10
    var allCells: MutableList<Cell?> = ArrayList()
    var savedCells: MutableSet<Cell?> = HashSet()

    // save settings
    const val APP_PREFERENCES = "diceSettings"
    const val APP_PREFERENCES_TOTAL = "total"
    const val APP_PREFERENCES_PLACE = "place"
    const val APP_PREFERENCES_SCREEN = "screen"
    const val APP_PREFERENCES_PLAYERS = "players"
    const val APP_PREFERENCES_IS_SECOND_NON_SCHOOL_PART_ADDED = "isSecondNonSchoolPartAdded"
}