package com.example.dices2

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import java.util.*

class GameTable(private val mainActivity: MainActivity, private val mainConstraintLayout: ConstraintLayout) {
    private val SCHOOL_ROWS = arrayOf(RowName.ONE, RowName.TWO, RowName.THREE, RowName.FOUR, RowName.FIVE, RowName.SIX)
    private val FIRST_NON_SCHOOL_PART_ROWS = arrayOf(RowName.PAIR, RowName.TWO_PLUS_TWO, RowName.TRIANGLE, RowName.SMALL, RowName.BIG)
    private val FULL_SQUARE_POKER_ROWS = arrayOf(RowName.FULL, RowName.SQUARE, RowName.POKER)
    private val CHANCES_ROWS = arrayOf(RowName.CHANCE1, RowName.CHANCE2)
    private val schoolTextViewsMap: MutableMap<Player, Cell>
    private val totalTextViewsMap: MutableMap<Player, Cell>
    private val playersCellsMap: MutableMap<Player, MutableList<Cell>>
    private val playersFullSquarePokerCellsMap: MutableMap<Player, MutableList<Cell>>
    private lateinit var players: List<Player>
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
    private lateinit var schoolPartTableLayout: TableLayout
    private lateinit var firstPartTableLayout: TableLayout
    private lateinit var secondPartTableLayout: TableLayout
    private lateinit var totalPartTableLayout: TableLayout

    init {
        // TODO getDrawable() is deprecated
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

    private fun generateTables() {
        schoolPartTableLayout = createTableLayout(numberOfRows = 8)
        firstPartTableLayout = createTableLayout(numberOfRows = 5)
        secondPartTableLayout = createTableLayout(numberOfRows = 5)
        totalPartTableLayout = createTableLayout(numberOfRows = 2)
        val separatorView1 = createSeparatorView()
        val separatorView2 = createSeparatorView()
        val separatorView3 = createSeparatorView()

        addConstraint(schoolPartTableLayout, ConstraintType.TOP, mainConstraintLayout, ConstraintType.TOP)
        addConstraint(schoolPartTableLayout, ConstraintType.BOTTOM, separatorView1, ConstraintType.TOP)
        addConstraint(separatorView1, ConstraintType.TOP, schoolPartTableLayout, ConstraintType.BOTTOM)
        addConstraint(separatorView1, ConstraintType.BOTTOM, firstPartTableLayout, ConstraintType.TOP)
        addConstraint(firstPartTableLayout, ConstraintType.TOP, separatorView1, ConstraintType.BOTTOM)
        addConstraint(firstPartTableLayout, ConstraintType.BOTTOM, separatorView2, ConstraintType.TOP)
        addConstraint(separatorView2, ConstraintType.TOP, firstPartTableLayout, ConstraintType.BOTTOM)
        addConstraint(separatorView2, ConstraintType.BOTTOM, secondPartTableLayout, ConstraintType.TOP)
        addConstraint(secondPartTableLayout, ConstraintType.TOP, separatorView2, ConstraintType.BOTTOM)
        addConstraint(secondPartTableLayout, ConstraintType.BOTTOM, separatorView3, ConstraintType.TOP)
        addConstraint(separatorView3, ConstraintType.TOP, secondPartTableLayout, ConstraintType.BOTTOM)
        addConstraint(separatorView3, ConstraintType.BOTTOM, totalPartTableLayout, ConstraintType.TOP)
        addConstraint(totalPartTableLayout, ConstraintType.TOP, separatorView3, ConstraintType.BOTTOM)
        addConstraint(totalPartTableLayout, ConstraintType.BOTTOM, mainConstraintLayout, ConstraintType.BOTTOM)
    }

    fun fillTable(players: List<Player>, player: Player?) {
        this.players = players
        generateTables()
        schoolPartTableLayout.removeAllViews()
        firstPartTableLayout.removeAllViews()
        secondPartTableLayout.removeAllViews()
        totalPartTableLayout.removeAllViews()
        Consts.allCells.clear()
        fillNamesRow()
        fillSchoolRows()
        fillSchoolTotalRow()
        fillFirstNonSchoolPart()
        fillSecondNonSchoolPart()
        fillChances()
        fillTotalRows()
        fillPlaceRow()
        fillSavedValues()
        player?.let { highlightPlayersMove(player = player) }
    }

    private fun fillSavedValues() {
        Consts.savedCells.filterNotNull().forEach { savedCell ->
            Consts.allCells.filterNotNull().forEach { cell ->
                if (savedCell == cell) {
                    cell.value = savedCell.value
                    mainActivity.setTextViewHighlight(currentCell = cell, isHighlight = true)
                    enableTextView(cell = cell, isEnabled = false)
                }
            }
        }

        players.forEach {
            setSchoolValue(player = it)
            if (mainActivity.isCountTotalEveryMove) {
                setTotalValue(player = it)
            }
            if (mainActivity.isCountPlaceEveryMove) {
                setPlayerPlace(player = it)
            }
        }
    }

    private fun fillNamesRow() {
        val tableRow = createTableRow()
        tableRow.addView(createNameOfRowTextView())
        for (player in players) {
            val textView = createNameOfPlayerTextView()
            textView.text = player.name
            playersNamesTextViews[player] = textView
            tableRow.addView(textView)
        }
        schoolPartTableLayout.addView(tableRow)
    }

    private fun fillSchoolRows() {
        for (rowName in SCHOOL_ROWS) {
            val tableRow = createTableRow()
            val textView = createNameOfRowTextView()
            textView.text = rowName.getName()
            tableRow.addView(textView)
            fillRowWithClickableCells(tableRow = tableRow, rowName = rowName)
            schoolPartTableLayout.addView(tableRow)
        }
    }

    private fun fillSchoolTotalRow() {
        val tableRow = createTableRow()
        val nameOfRowTextView = createNameOfRowTextView()
        nameOfRowTextView.text = RowName.SCHOOL.getName()
        tableRow.addView(nameOfRowTextView)
        for (player in players) {
            val cell = createNonClickableCell(player = player, rowName = RowName.SCHOOL)
            tableRow.addView(cell)
            schoolTextViewsMap[player] = cell
            Consts.allCells.add(cell)
        }
        schoolPartTableLayout.addView(tableRow)
    }

    private fun createTableRow(): TableRow {
        val tableRow = TableRow(mainActivity)
        val layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0, 200f)
        tableRow.layoutParams = layoutParams
        tableRow.background = backgroundDarkGray
        return tableRow
    }

    private fun fillFirstNonSchoolPart() {
        for (rowName in FIRST_NON_SCHOOL_PART_ROWS) {
            val tableRow = createTableRow()
            val textView = createNameOfRowTextView()
            textView.text = rowName.getName()
            tableRow.addView(textView)
            fillRowWithClickableCells(tableRow = tableRow, rowName = rowName)
            firstPartTableLayout.addView(tableRow)
        }
    }

    private fun fillSecondNonSchoolPart() {
        for (rowName in FULL_SQUARE_POKER_ROWS) {
            val tableRow = createTableRow()
            val textView = createNameOfRowTextView()
            textView.text = rowName.getName()
            tableRow.addView(textView)
            fillRowWithNonClickableCells(tableRow = tableRow, rowName = rowName)
            secondPartTableLayout.addView(tableRow)
        }
    }

    private fun fillChances() {
        for (rowName in CHANCES_ROWS) {
            val tableRow = createTableRow()
            val textView = createNameOfRowTextView()
            textView.text = rowName.getName()
            tableRow.addView(textView)
            fillRowWithClickableCells(tableRow = tableRow, rowName = rowName)
            secondPartTableLayout.addView(tableRow)
        }
    }

    private fun fillTotalRows() {
        val tableRow = createTableRow()
        val nameOfRowTextView = createNameOfRowTextView()
        nameOfRowTextView.text = RowName.TOTAL.getName()
        tableRow.addView(nameOfRowTextView)
        for (player in players) {
            val cell = createNonClickableCell(player = player, rowName = RowName.TOTAL)
            tableRow.addView(cell)
            totalTextViewsMap[player] = cell
            Consts.allCells.add(cell)
        }
        totalPartTableLayout.addView(tableRow)
    }

    private fun fillPlaceRow() {
        val placeTableRow = createTableRow()
        val placeTextView = createNameOfRowTextView()
        placeTextView.text = RowName.PLACE.getName()
        placeTableRow.addView(placeTextView)
        for (player in players) {
            val cell = createNonClickableCell(player = player, rowName = RowName.PLACE)
            placeTableRow.addView(cell)
            placeTextViewsMap[player] = cell
            Consts.allCells.add(cell)
        }
        totalPartTableLayout.addView(placeTableRow)
    }

    private fun fillRowWithClickableCells(tableRow: TableRow, rowName: RowName) {
        for (player in players) {
            val cell = Cell(mainActivity, player, rowName)
            cell.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f)
            cell.gravity = Gravity.CENTER
            cell.background = backgroundDarkGray
            cell.setOnClickListener(createOnClickListener())
            cell.setOnLongClickListener(createOnLongClickListener())
            setMutualParams(textView = cell)
            //mainActivity.registerForContextMenu(cell)
            tableRow.addView(cell)
            fillPlayersCellsMap(player = player, cell = cell)
            Consts.allCells.add(cell)
        }
    }

    private fun fillRowWithNonClickableCells(tableRow: TableRow, rowName: RowName) {
        for (player in players) {
            val cell = Cell(context = mainActivity, owner = player, row = rowName)
            cell.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f)
            cell.gravity = Gravity.CENTER
            cell.background = backgroundDarkGray
            setMutualParams(textView = cell)
            //mainActivity.registerForContextMenu(cell)
            tableRow.addView(cell)
            Consts.allCells.add(cell)
            if (playersFullSquarePokerCellsMap[player] == null) {
                val cells: MutableList<Cell> = ArrayList()
                cells.add(cell)
                playersFullSquarePokerCellsMap[player] = cells
            } else {
                playersFullSquarePokerCellsMap[player]?.add(cell)
            }
        }
    }

    private fun fillPlayersCellsMap(player: Player, cell: Cell) {
        if (playersCellsMap[player] == null) {
            val cells: MutableList<Cell> = ArrayList()
            cells.add(cell)
            playersCellsMap[player] = cells
        } else {
            playersCellsMap[player]?.add(cell)
        }
    }

    private fun createNameOfRowTextView(): TextView {
        val textView = TextView(mainActivity)
        textView.layoutParams = TableRow.LayoutParams(170, TableRow.LayoutParams.MATCH_PARENT)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setPadding(LEFT_RIGHT_TEXTVIEW_PADDING, 0, LEFT_RIGHT_TEXTVIEW_PADDING, 0)
        setMutualParams(textView = textView)
        return textView
    }

    private fun createNameOfPlayerTextView(): TextView {
        val textView = TextView(mainActivity)
        textView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f)
        textView.gravity = Gravity.CENTER
        textView.background = backgroundDarkGray
        setMutualParams(textView = textView)
        return textView
    }

    private fun createNonClickableCell(player: Player, rowName: RowName): Cell {
        val cell = Cell(mainActivity, player, rowName)
        cell.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f)
        cell.gravity = Gravity.CENTER
        cell.background = backgroundDarkGray
        setMutualParams(textView = cell)
        return cell
    }

    private fun setMutualParams(textView: TextView) {
        textView.textSize = TEXT_SIZE
        textView.setTextColor(FONT_COLOR)
    }

    fun setSchoolValue(player: Player) {
        val cell = schoolTextViewsMap[player]
        val schoolValue = player.school
        cell?.value = schoolValue.toString()
        when {
            schoolValue < 0 -> {
                cell?.background = backgroundRed
            }
            player.isSchoolFinished -> {
                cell?.background = backgroundGreen
            }
            else -> {
                cell?.background = backgroundDarkGray
            }
        }
    }

    fun setTotalValue(player: Player) {
        totalTextViewsMap[player]?.value = player.total.toString()
    }

    fun highlightPlayersMove(player: Player?) {
        val cellToHighlight: List<Cell>? = playersCellsMap[player]
        cellToHighlight?.let {
            for (cell in cellToHighlight) {
                if (cell.isEmpty) {
                    enableTextView(cell = cell, isEnabled = true)
                } else {
                    enableTextView(cell = cell, isEnabled = false)
                }
            }
        }
    }

    fun switchOffPlayersMove(player: Player) {
        val cellToSwitchOff: List<Cell>? = playersCellsMap[player]
        for (cell in cellToSwitchOff!!) {
            if (cell.isEmpty) {
                enableTextView(cell = cell, isEnabled = false)
            }
        }
    }

    private fun createOnClickListener(): View.OnClickListener {
        return View.OnClickListener { v ->
            val cell = v as Cell
            if (cell.isEmpty) {
                mainActivity.setClickedCell(cell = cell)
                val intent = Intent(mainActivity, KeyboardActivity::class.java)
                intent.putExtra(Consts.INTENT_ROW_NAME, cell.row.getName())
                intent.putExtra(Consts.INTENT_IS_EDIT, false)
                intent.putExtra(Consts.INTENT_IS_IN_SCHOOL, isCellInSchool(cell = cell))
                mainActivity.startActivityForResult(intent, 1)
            }
        }
    }

    private fun createOnLongClickListener(): View.OnLongClickListener {
        return View.OnLongClickListener { v ->
            val cell = v as Cell
            mainActivity.setClickedCell(cell = cell)
            mainActivity.startKeyboardActivityForEdit(cell = cell)
            true
        }
    }

    private fun enableTextView(cell: Cell, isEnabled: Boolean) {
        if (isEnabled) {
            cell.background = backgroundHighlightedBorders
            cell.setOnClickListener(createOnClickListener())
            cell.setOnLongClickListener(createOnLongClickListener())
        } else {
            if (cell.isEmpty) {
                cell.background = backgroundDarkGray
            } else {
                cell.background = backgroundLightGray
            }
            cell.setOnClickListener(null)
        }
    }

    fun enableAfterThreeClassesTextViews(player: Player) {
        val cells: List<Cell>? = playersFullSquarePokerCellsMap[player]
        cells?.let { playersCellsMap[player]?.addAll(cells) }
        player.isSecondNonSchoolPartAdded = true
    }

    fun disableAfterThreeClassesTextViews(player: Player) {
        val cells: List<Cell>? = playersFullSquarePokerCellsMap[player]
        cells?.let {
            playersCellsMap[player]?.removeAll { it in cells }
            for (cell in cells) {
                cell.setOnClickListener(null)
                cell.background = backgroundDarkGray
            }
        }
        player.isSecondNonSchoolPartAdded = false
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

    private fun createSeparatorView(): View {
        val view = View(mainActivity)
        view.id = View.generateViewId()
        view.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 15)
        mainConstraintLayout.addView(view)
        val constraintSet = ConstraintSet()
        constraintSet.clone(mainConstraintLayout)
        constraintSet.connect(view.id, ConstraintSet.START, R.id.mainConstraintLayout, ConstraintSet.START)
        constraintSet.connect(view.id, ConstraintSet.END, R.id.mainConstraintLayout, ConstraintSet.END)
        constraintSet.applyTo(mainConstraintLayout)
        return view
    }

    private fun addConstraint(view: View, type: ConstraintType, constrainToView: View, constrainToType: ConstraintType) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(mainConstraintLayout)
        constraintSet.connect(
                view.id,
                when(type) {
                    ConstraintType.TOP -> ConstraintSet.TOP
                    ConstraintType.BOTTOM -> ConstraintSet.BOTTOM
                },
                constrainToView.id,
                when(constrainToType) {
                    ConstraintType.TOP -> ConstraintSet.TOP
                    ConstraintType.BOTTOM -> ConstraintSet.BOTTOM
                })
        constraintSet.applyTo(mainConstraintLayout)
    }

    private fun createTableLayout(numberOfRows: Int): TableLayout {
        val parentId = R.id.mainConstraintLayout
        val table = TableLayout(mainActivity)
        table.id = View.generateViewId()
        table.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0)
        mainConstraintLayout.addView(table)
        val constraintSet = ConstraintSet()
        constraintSet.clone(mainConstraintLayout)
        constraintSet.setVerticalWeight(table.id, numberOfRows.toFloat())
        constraintSet.connect(table.id, ConstraintSet.START, parentId, ConstraintSet.START)
        constraintSet.connect(table.id, ConstraintSet.END, parentId, ConstraintSet.END)
        constraintSet.applyTo(mainConstraintLayout)
        return table
    }
}