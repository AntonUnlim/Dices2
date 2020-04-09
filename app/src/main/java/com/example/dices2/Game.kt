package com.example.dices2

import android.content.SharedPreferences
import java.util.*

class Game private constructor() {
    private lateinit var mainActivity: MainActivity
    private lateinit var listActivity: ListActivity
    private var players: MutableList<Player> = ArrayList()
    private lateinit var currentCell: Cell
    var isCountTotalEveryMove = false
    var isCountPlaceEveryMove = false
    var isKeelScreenOn = true
    private var totalNumberOfMoves = 0
    // TODO добавить другие виды правил игры
    private val totalNumberOfRows = 16
    private var idCurrentPlayer = 0
    var isGameStarted = false
    private var currentPlayer: Player? = null

    init {
        Consts.savedCells = HashSet()
    }

    private val isGameOver: Boolean
        get() = totalNumberOfMoves <= 0

    // Ход игры
    private fun startGame() {
        isGameStarted = true
        getTotalNumberOfMoves()
        Consts.savedCells.clear()
        clearPlayersValues()
        idCurrentPlayer = 0
        currentPlayer = players[idCurrentPlayer]
        mainActivity.fillMainTable(players = players, player = currentPlayer)
        highlightNextPlayer()
    }

    private fun clearPlayersValues() {
        players.forEach {
            it.clearAllValues()
            it.clearPlace()
        }
    }

    private fun highlightNextPlayer() {
        val playersAmount = players.size
        idCurrentPlayer = if (idCurrentPlayer > playersAmount - 1) 0 else idCurrentPlayer
        val idHighlightOff = if (idCurrentPlayer == 0) playersAmount - 1 else idCurrentPlayer - 1
        val idHighlightOn = if (idCurrentPlayer > playersAmount - 1) 0 else idCurrentPlayer
        mainActivity.switchOffPlayersMove(player = players[idHighlightOff])
        currentPlayer = players[idHighlightOn]
        mainActivity.highlightPlayersMove(player = players[idHighlightOn])
        idCurrentPlayer++
    }

    private fun makeMove() {
        totalNumberOfMoves--
        highlightNextPlayer()
        mainActivity.setTextViewHighlight(currentCell = currentCell, isHighlight = true)
    }

    private fun makeEdit(cell: Cell) {
        if (currentCell.isEmpty) {
            mainActivity.setTextViewHighlight(currentCell = currentCell, isHighlight = false)
        } else {
            mainActivity.setTextViewHighlight(currentCell = currentCell, isHighlight = true)
        }
        if (cell.owner == currentPlayer) {
            mainActivity.highlightPlayersMove(player = currentPlayer)
        }
    }

    fun okButtonOnKeyboardActivityClicked(value: String, isEdit: Boolean) {
        val player = currentCell.owner
        val rowName = currentCell.row
        player.setValue(rowName = rowName, value = value)
        currentCell.value = value
        refreshCellsList()
        refreshFullSquarePokerTextViews(player = player)
        if (isEdit) {
            makeEdit(cell = currentCell)
        } else {
            makeMove()
        }
        showSchool(player = player)
        if (isCountTotalEveryMove) {
            showTotal(player = player)
        }
        if (isCountPlaceEveryMove) {
            showAllPlaces()
        }
        if (isGameOver) {
            showAllTotals()
            //isGameStarted = false
            showAllPlaces()
        }
    }

    private fun refreshFullSquarePokerTextViews(player: Player) {
        if (player.isThreeClassesInSchoolAreFinished && !player.isSecondNonSchoolPartAdded) {
            mainActivity.enableFullSquarePokerTextViews(player = player)
        }
        if (!player.isThreeClassesInSchoolAreFinished && player.isSecondNonSchoolPartAdded) {
            mainActivity.disableFullSquarePokerTextViews(player = player)
        }
    }

    fun setMainActivity(mainActivity: MainActivity) {
        this.mainActivity = mainActivity
        mainActivity.fillMainTable(players = players, player = currentPlayer)
    }

    fun setCurrentCell(cell: Cell) {
        currentCell = cell
    }

    private fun showSchool(player: Player) {
        mainActivity.showSchool(player = player)
    }

    private fun showTotal(player: Player) {
        mainActivity.showTotal(player = player)
    }

    private fun showAllTotals() {
        for (player in players) {
            showTotal(player = player)
        }
    }

    private fun showPlayerPlace(player: Player) {
        mainActivity.showPlayerPlace(player = player)
    }

    private fun showAllPlaces() {
        calcPlaces()
        for (player in players) {
            showPlayerPlace(player = player)
        }
    }

    // Работа с ListActivity
    fun setListActivity(listActivity: ListActivity) {
        this.listActivity = listActivity
        fillListView()
    }

    private fun fillListView() {
        listActivity.fillListView(players = players)
    }

    fun addPlayer(nameOfPlayer: String) {
        players.add(Player(name = nameOfPlayer))
        listActivity.refreshListView(players = players)
    }

    fun removePlayer(position: Int) {
        players.removeAt(position)
        listActivity.refreshListView(players = players)
    }

    fun clearListOfPlayersAndRefreshListView() {
        clearListOfPlayers()
        listActivity.refreshListView(players = players)
    }

    private fun clearListOfPlayers() {
        players.clear()
    }

    fun getNameOfPlayerByPosition(position: Int): String {
        return players[position].name
    }

    fun startGameButtonClicked() {
        startGame()
    }

    // Прочее

    private fun getTotalNumberOfMoves() {
        totalNumberOfMoves = players.size * totalNumberOfRows
    }

    fun isDuplicatePlayer(name: String): Boolean {
        for (player in players) {
            if (player.name == name) {
                return true
            }
        }
        return false
    }

    private fun calcPlaces() {
        val playersSortedByTotalValue: List<Player> = ArrayList(players)
        Collections.sort(playersSortedByTotalValue, Collections.reverseOrder())
        val currentPlayer = playersSortedByTotalValue[0]
        var place = 1
        var currentValue = currentPlayer.total
        currentPlayer.curPlace = place

        for (i in 1 until playersSortedByTotalValue.size) {
            val nextPlayer = playersSortedByTotalValue[i]
            if (currentValue == nextPlayer.total) {
                nextPlayer.curPlace = place
            } else {
                place++
                currentValue = nextPlayer.total
                nextPlayer.curPlace = place
            }
        }
    }

    private fun isEditInSchool(cell: Cell): Boolean {
        for (rowName in Consts.NAMES_OF_SCHOOL_ROWS) {
            if (cell.row == rowName) {
                return true
            }
        }
        return false
    }

    private fun refreshCellsList() {
        Consts.savedCells.add(currentCell)
    }

    fun getIsCountTotalEveryMove() = isCountTotalEveryMove

    fun getIsCountPlaceEveryMove() = isCountPlaceEveryMove

    fun getIsKeepScreenOn() = isKeelScreenOn

    fun savePlayers(pref: SharedPreferences) {
        val editor = pref.edit()
        val playersSet = getPlayersSet()
        editor.putStringSet(Consts.APP_PREFERENCES_PLAYERS, playersSet)
        editor.apply()
    }

    private fun getPlayersSet(): MutableSet<String> {
        val set = HashSet<String>()
        players.forEach { set.add(it.name) }
        return set
    }

    fun fillStartPlayers(playersSet: Set<String>?) {
        playersSet?.let {
            players.clear()
            playersSet.forEach {
                players.add(Player(it))
            }
        }
        if (players.size > 0) {
            startGame()
        }
    }

    private object HOLDER {
        val INSTANCE = Game()
    }

    companion object {
        val instance: Game by lazy { HOLDER.INSTANCE }
    }
}