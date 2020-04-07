package com.example.dices2

import android.content.Context
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import java.util.*

class Cell(context: Context?, val owner: Player, val row: RowName) : AppCompatTextView(context) {
    var value: String? = null
        set(value) {
            field = value
            text = value
        }

    val isEmpty: Boolean
        get() = text == "" || text == null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val cell = other as Cell
        return owner == cell.owner && row == cell.row
    }

    override fun hashCode(): Int {
        return Objects.hash(owner, row)
    }

    init {
        setTextColor(Consts.MAIN_TABLE_TEXT_COLOR)
        gravity = Gravity.CENTER_HORIZONTAL
        textSize = Consts.MAIN_TABLE_FONT_SIZE.toFloat()
    }
}