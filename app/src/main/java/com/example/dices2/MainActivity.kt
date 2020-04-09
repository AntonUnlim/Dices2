package com.example.dices2

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dices2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var game: Game
    private lateinit var gameTable: GameTable
    private lateinit var pref: SharedPreferences

    // TODO поиграться с цветами

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(Consts.APP_PREFERENCES, MODE_PRIVATE)

        game = Game.instance
        game.setMainActivity(mainActivity = this)
    }

    override fun onResume() {
        super.onResume()

        if (!game.isGameStarted) {
            if (pref.contains(Consts.APP_PREFERENCES_TOTAL)) {
                game.isCountTotalEveryMove = pref.getBoolean(Consts.APP_PREFERENCES_TOTAL, false)
            }

            if (pref.contains(Consts.APP_PREFERENCES_PLACE)) {
                game.isCountPlaceEveryMove = pref.getBoolean(Consts.APP_PREFERENCES_PLACE, false)
            }

            if (pref.contains(Consts.APP_PREFERENCES_SCREEN)) {
                game.isKeelScreenOn = pref.getBoolean(Consts.APP_PREFERENCES_SCREEN, false)
                if (game.isKeelScreenOn) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }

            if (pref.contains(Consts.APP_PREFERENCES_PLAYERS)) {
                game.fillStartPlayers(playersSet = pref.getStringSet(Consts.APP_PREFERENCES_PLAYERS, HashSet<String>()))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_game -> {
                if (game.isGameStarted) {
                    val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                    alertDialogBuilder.setTitle(getString(R.string.break_game))
                    alertDialogBuilder.setMessage(getString(R.string.are_you_sure_to_break_game))
                    alertDialogBuilder.setNegativeButton(getString(R.string.no), null)
                    alertDialogBuilder.setPositiveButton(getString(R.string.yes)) { _, _ -> startListActivity() }
                    alertDialogBuilder.show()
                } else {
                    startListActivity()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startListActivity() {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        var value = ""
        data.getStringExtra(Consts.INTENT_INPUT_VALUE)?.let { value = it }
        val isEdit = data.getBooleanExtra(Consts.INTENT_IS_EDIT, false)
        game.okButtonOnKeyboardActivityClicked(value = value, isEdit = isEdit)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (game.isGameStarted) {

                val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                alertDialogBuilder.setTitle(getString(R.string.exit))
                alertDialogBuilder.setMessage(getString(R.string.are_you_sure_to_exit))
                alertDialogBuilder.setNegativeButton(getString(R.string.no), null)
                alertDialogBuilder.setPositiveButton(getString(R.string.yes)) { _, _ -> exit() }
                //alertDialogBuilder.create()
                val dialog = alertDialogBuilder.create()
                dialog.show()
                dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.no, resources.newTheme()))
                dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.yes, resources.newTheme()))
                dialog.getButton(Dialog.BUTTON_POSITIVE).textSize = 24f
            } else {
                exit()
            }
        }
        return false
    }

    private fun exit() {
        game.isGameStarted = false
        game.savePlayers(pref = pref)
        finish()
    }

    fun fillMainTable(players: List<Player>, player: Player?) {
        gameTable = GameTable(mainActivity = this, mainTable = binding.tlMain)
        gameTable.fillTable(players = players, player = player)
    }

    fun showSchool(player: Player) {
        gameTable.setSchoolValue(player = player)
    }

    fun showTotal(player: Player) {
        gameTable.setTotalValue(player = player)
    }

    fun setTextViewHighlight(currentCell: Cell, isHighlight: Boolean) {
        if (isHighlight) {
            // TODO getDrawable deprecated
            currentCell.background = resources.getDrawable(R.drawable.text_view_back_light_gray)
        } else {
            currentCell.background = resources.getDrawable(R.drawable.text_view_back_dark_gray)
        }
    }

    fun startKeyboardActivityForEdit(cell: Cell) {
        val intent = Intent(this, KeyboardActivity::class.java)
        intent.putExtra(Consts.INTENT_EDITED_VALUE, cell.value)
        intent.putExtra(Consts.INTENT_ROW_NAME, cell.row.getName())
        intent.putExtra(Consts.INTENT_IS_EDIT, true)
        intent.putExtra(Consts.INTENT_IS_IN_SCHOOL, gameTable.isCellInSchool(cell = cell))
        startActivityForResult(intent, 1)
    }

    fun highlightPlayersMove(player: Player?) {
        gameTable.highlightPlayersMove(player = player)
    }

    fun switchOffPlayersMove(player: Player) {
        gameTable.switchOffPlayersMove(player = player)
    }

    fun enableFullSquarePokerTextViews(player: Player) {
        gameTable.enableAfterThreeClassesTextViews(player = player)
    }

    fun disableFullSquarePokerTextViews(player: Player) {
        gameTable.disableAfterThreeClassesTextViews(player = player)
    }

    fun setClickedCell(cell: Cell) {
        game.setCurrentCell(cell = cell)
    }

    fun showPlayerPlace(player: Player) {
        gameTable.setPlayerPlace(player = player)
    }

    val isCountTotalEveryMove: Boolean
        get() = game.getIsCountTotalEveryMove()

    val isCountPlaceEveryMove: Boolean
        get() = game.getIsCountPlaceEveryMove()

}