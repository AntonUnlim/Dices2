package com.example.dices2

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import java.util.*

class GameTable(private val mainActivity: MainActivity, private val mainTable: TableLayout?) {
    private val SCHOOL_ROWS = arrayOf(RowName.ONE, RowName.TWO, RowName.THREE, RowName.FOUR, RowName.FIVE, RowName.SIX)
    private val FIRST_NON_SCHOOL_PART_ROWS = arrayOf(RowName.PAIR, RowName.TWO_PLUS_TWO, RowName.TRIANGLE, RowName.SMALL, RowName.BIG)
    private val FULL_SQUARE_POKER_ROWS = arrayOf(RowName.FULL, RowName.SQUARE, RowName.POKER)
    private val CHANCES_ROWS = arrayOf(RowName.CHANCE1, RowName.CHANCE2)
    private val schoolTextViewsMap: MutableMap<Player, Cell>
    private val totalTextViewsMap: MutableMap<Player, Cell>
    private val playersCellsMap: MutableMap<Player, MutableList<Cell>>
    private val playersFullSquarePokerCellsMap: MutableMap<Player, MutableList<Cell>>
    private var players: List<Player>? = null
    private val FONT_COLOR = Consts.MAIN_TABLE_TEXT_COLOR
    private val backgroundDarkGray: Drawable
    private val backgroundLightGray: Drawable
    private val backgroundRed: Drawable
    private val backgroundGreen: Drawable
    private val backgroundHighlightedBorders: Drawable
    private val TEXT_SIZE = Consts.MAIN_TABLE_FONT_SIZE.toFloat()
    private val LEFT_RIGHT_TEXTVIEW_PADDING = 16
    private val playersNamesTextViews: MutableMap<Player, TextView>
    private val placeTextViewsMap: MutableMap<Player, Cell>

    init {
        backgroundDarkGray = mainActivity.resources.getDrawable(R.drawable.text_view_back_dark_gray)
        backgroundLightGray = mainActivity.resources.getDrawable(R.drawable.text_view_back_light_gray)
        backgroundRed = mainActivity.resources.getDrawable(R.drawable.text_view_back_red)
        backgroundGreen = mainActivity.resources.getDrawable(R.drawable.text_view_back_green)
        backgroundHighlightedBorders = mainActivity.resources.getDrawable(R.drawable.text_view_back_highlight)
        schoolTextViewsMap = HashMap()
        totalTextViewsMap = HashMap()
        playersCellsMap = HashMap()
        playersNamesTextViews = HashMap()
        placeTextViewsMap = HashMap()
        playersFullSquarePokerCellsMap = HashMap()
    }

    fun fillTable(players: List<Player>?, player: Player?) {
        this.players = players
        mainTable!!.removeAllViews()
        Consts.allCells.clear()
        fillNamesRow()
        //addSeparatorRow();
        fillSchoolRows()
        fillSchoolTotalRow()
        //addSeparatorRow();
        fillFirstNonSchoolPart()
        //addSeparatorRow();
        fillSecondNonSchoolPart()
        fillChances()
        //addSeparatorRow();
        fillTotalRows()
        fillPlaceRow()
        fillSavedValues()
        player?.let { highlightPlayersMove(it) }
    }

    private fun fillSavedValues() {
        Consts.savedCells.forEach { savedCell ->
            Consts.allCells.forEach { cell ->
                if (savedCell == cell) {
                    cell?.value = savedCell?.value
                    mainActivity.setTextViewHighlight(cell, true)
                    enableTextView(cell, false)
                }
            }
        }

        players!!.forEach {
            setSchoolValue(it)
            if (mainActivity.isCountTotalEveryMove) {
                setTotalValue(it)
            }
            if (mainActivity.isCountPlaceEveryMove) {
                setPlayerPlace(it)
            }
        }
    }

    private fun fillNamesRow() {
        val tableRow = createTableRow()
        tableRow.addView(createNameOfRowTextView())
        for (player in players!!) {
            val textView = createNameOfPlayerTextView()
            textView.text = player.name
            playersNamesTextViews[player] = textView
            tableRow.addView(textView)
        }
        mainTable!!.addView(tableRow)
    }

    private fun fillSchoolRows() {
        for (rowName in SCHOOL_ROWS) {
            val tableRow = createTableRow()
            val textView = createNameOfRowTextView()
            textView.text = rowName.getName()
            tableRow.addView(textView)
            fillRowWithClickableCells(tableRow, rowName)
            mainTable!!.addView(tableRow)
        }
    }

    private fun fillSchoolTotalRow() {
        val tableRow = createTableRow()
        val nameOfRowTextView = createNameOfRowTextView()
        nameOfRowTextView.text = RowName.SCHOOL.getName()
        tableRow.addView(nameOfRowTextView)
        for (player in players!!) {
            val cell = createNonClickableCell(player, RowName.SCHOOL)
            tableRow.addView(cell)
            schoolTextViewsMap[player] = cell
            Consts.allCells.add(cell)
        }
        mainTable!!.addView(tableRow)
    }

    private fun addSeparatorRow() {
        val tableRow = TableRow(mainActivity)
        val layoutParams = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.5f)
        tableRow.layoutParams = layoutParams
        tableRow.addView(createNameOfRowTextView())
        for (player in players!!) {
            val textView = createNameOfPlayerTextView()
            tableRow.addView(textView)
        }
        mainTable!!.addView(tableRow)
    }

    private fun fillFirstNonSchoolPart() {
        for (rowName in FIRST_NON_SCHOOL_PART_ROWS) {
            val tableRow = createTableRow()
            val textView = createNameOfRowTextView()
            textView.text = rowName.getName()
            tableRow.addView(textView)
            fillRowWithClickableCells(tableRow, rowName)
            mainTable!!.addView(tableRow)
        }
    }

    private fun fillSecondNonSchoolPart() {
        for (rowName in FULL_SQUARE_POKER_ROWS) {
            val tableRow = createTableRow()
            val textView = createNameOfRowTextView()
            textView.text = rowName.getName()
            tableRow.addView(textView)
            fillRowWithNonClickableCells(tableRow, rowName)
            mainTable!!.addView(tableRow)
        }
    }

    private fun fillChances() {
        for (rowName in CHANCES_ROWS) {
            val tableRow = createTableRow()
            val textView = createNameOfRowTextView()
            textView.text = rowName.getName()
            tableRow.addView(textView)
            fillRowWithClickableCells(tableRow, rowName)
            mainTable!!.addView(tableRow)
        }
    }

    private fun fillTotalRows() {
        val tableRow = createTableRow()
        val nameOfRowTextView = createNameOfRowTextView()
        nameOfRowTextView.text = RowName.TOTAL.getName()
        tableRow.addView(nameOfRowTextView)
        for (player in players!!) {
            val cell = createNonClickableCell(player, RowName.TOTAL)
            tableRow.addView(cell)
            totalTextViewsMap[player] = cell
            Consts.allCells.add(cell)
        }
        mainTable!!.addView(tableRow)
    }

    private fun fillPlaceRow() {
        val placeTableRow = createTableRow()
        val placeTextView = createNameOfRowTextView()
        placeTextView.text = RowName.PLACE.getName()
        placeTableRow.addView(placeTextView)
        for (player in players!!) {
            val cell = createNonClickableCell(player, RowName.PLACE)
            placeTableRow.addView(cell)
            placeTextViewsMap[player] = cell
            Consts.allCells.add(cell)
        }
        mainTable!!.addView(placeTableRow)
    }

    private fun fillRowWithClickableCells(tableRow: TableRow, rowName: RowName) {
        for (player in players!!) {
            val cell = Cell(mainActivity, player, rowName)
            cell.layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
            cell.gravity = Gravity.CENTER
            cell.background = backgroundDarkGray
            cell.setOnClickListener(createOnClickListener())
            cell.setOnLongClickListener(createOnLongClickListener())
            setMutualParams(cell)
            mainActivity.registerForContextMenu(cell)
            tableRow.addView(cell)
            fillPlayersCellsMap(player, cell)
            Consts.allCells.add(cell)
        }
    }

    private fun fillRowWithNonClickableCells(tableRow: TableRow, rowName: RowName) {
        for (player in players!!) {
            val cell = Cell(mainActivity, player, rowName)
            cell.layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
            cell.gravity = Gravity.CENTER
            cell.background = backgroundDarkGray
            setMutualParams(cell)
            mainActivity.registerForContextMenu(cell)
            tableRow.addView(cell)
            Consts.allCells.add(cell)
            if (playersFullSquarePokerCellsMap[player] == null) {
                val cells: MutableList<Cell> = ArrayList()
                cells.add(cell)
                playersFullSquarePokerCellsMap[player] = cells
            } else {
                playersFullSquarePokerCellsMap[player]!!.add(cell)
            }
        }
    }

    private fun fillPlayersCellsMap(player: Player, cell: Cell) {
        if (playersCellsMap[player] == null) {
            val cells: MutableList<Cell> = ArrayList()
            cells.add(cell)
            playersCellsMap[player] = cells
        } else {
            playersCellsMap[player]!!.add(cell)
        }
    }

    private fun createTableRow(): TableRow {
        val tableRow = TableRow(mainActivity)
        val layoutParams = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        tableRow.layoutParams = layoutParams
        tableRow.background = backgroundDarkGray
        return tableRow
    }

    private fun createNameOfRowTextView(): TextView {
        val textView = TextView(mainActivity)
        textView.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setPadding(LEFT_RIGHT_TEXTVIEW_PADDING, 0, LEFT_RIGHT_TEXTVIEW_PADDING, 0)
        setMutualParams(textView)
        return textView
    }

    private fun createNameOfPlayerTextView(): TextView {
        val textView = TextView(mainActivity)
        textView.layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        textView.gravity = Gravity.CENTER
        textView.background = backgroundDarkGray
        setMutualParams(textView)
        return textView
    }

    private fun createNonClickableCell(player: Player, rowName: RowName): Cell {
        val cell = Cell(mainActivity, player, rowName)
        cell.layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        cell.gravity = Gravity.CENTER
        cell.background = backgroundDarkGray
        setMutualParams(cell)
        return cell
    }

    private fun setMutualParams(textView: TextView) {
        textView.textSize = TEXT_SIZE
        textView.setTextColor(FONT_COLOR)
    }

    fun setSchoolValue(player: Player?) {
        val cell = schoolTextViewsMap[player]
        val schoolValue = player?.school
        cell?.value = schoolValue.toString()
        schoolValue?.let {
            if (schoolValue < 0) {
                cell!!.background = backgroundRed
            } else if (player!!.isSchoolFinished) {
                cell!!.background = backgroundGreen
            } else {
                cell!!.background = backgroundDarkGray
            }
        }
    }

    fun setTotalValue(player: Player?) {
        totalTextViewsMap[player]?.value = player?.total.toString()
    }

    fun highlightPlayersMove(player: Player?) {
        val cellToHighlight: List<Cell>? = playersCellsMap[player]
        for (cell in cellToHighlight!!) {
            if (cell.isEmpty) {
                enableTextView(cell, true)
            } else {
                enableTextView(cell, false)
            }
        }
    }

    fun switchOffPlayersMove(player: Player?) {
        val cellToSwitchOff: List<Cell>? = playersCellsMap[player]
        for (cell in cellToSwitchOff!!) {
            if (cell.isEmpty) {
                enableTextView(cell, false)
            }
        }
    }

    private fun createOnClickListener(): View.OnClickListener {
        return View.OnClickListener { v ->
            val cell = v as Cell
            if (cell.isEmpty) {
                mainActivity.setClickedCell(cell)
                val intent = Intent(mainActivity, KeyboardActivity::class.java)
                intent.putExtra(Consts.INTENT_ROW_NAME, cell.row.getName())
                intent.putExtra(Consts.INTENT_IS_EDIT, false)
                intent.putExtra(Consts.INTENT_IS_IN_SCHOOL, isCellInSchool(cell))
                mainActivity.startActivityForResult(intent, 1)
            }
        }
    }

    private fun createOnLongClickListener(): View.OnLongClickListener {
        return View.OnLongClickListener { v ->
            val cell = v as Cell
            mainActivity.setClickedCell(cell)
            mainActivity!!.startKeyboardActivityForEdit(v.text.toString(), cell.row)
            true
        }
    }

    fun enableTextView(cell: Cell?, isEnabled: Boolean) {
        if (isEnabled) {
            cell!!.background = backgroundHighlightedBorders
            cell.setOnClickListener(createOnClickListener())
        } else {
            cell?.let {
                if (cell.isEmpty) {
                    cell.background = backgroundDarkGray
                } else {
                    cell.background = backgroundLightGray
                }
                cell.setOnClickListener(null)
            }
        }
    }

    fun enableAfterThreeClassesTextViews(player: Player?) {
        val cells: List<Cell>? = playersFullSquarePokerCellsMap[player]
        playersCellsMap[player]!!.addAll(cells!!)
        player?.isSecondNonSchoolPartAdded = true
    }

    fun disableAfterThreeClassesTextViews(player: Player?) {
        val cells: List<Cell>? = playersFullSquarePokerCellsMap[player]
        playersCellsMap[player]?.removeAll { it in cells!! }
        player?.isSecondNonSchoolPartAdded = false
        for (cell in cells!!) {
            cell.setOnClickListener(null)
            cell.background = backgroundDarkGray
        }
    }

    fun isCellInSchool(cell: Cell): Boolean {
        for (rowName in Consts.NAMES_OF_SCHOOL_ROWS) {
            if (cell.row == rowName) {
                return true
            }
        }
        return false
    }

    fun setPlayerPlace(player: Player) {
        placeTextViewsMap[player]?.value = player.curPlace.toString()
    }
}