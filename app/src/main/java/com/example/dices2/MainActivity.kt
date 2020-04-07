package com.example.dices2

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dices2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var game: Game
    private var gameTable: GameTable? = null

    lateinit var pref: SharedPreferences

    // TODO поиграться с цветами

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(Consts.APP_PREFERENCES, MODE_PRIVATE)

        game = Game.instance
        game.setMainActivity(this)
    }

    override fun onResume() {
        super.onResume()

        if (pref.contains(Consts.APP_PREFERENCES_TOTAL)) {
            game.isCountTotalEveryMove = pref.getBoolean(Consts.APP_PREFERENCES_TOTAL, false);
        }

        if (pref.contains(Consts.APP_PREFERENCES_PLACE)) {
            game.isCountPlaceEveryMove = pref.getBoolean(Consts.APP_PREFERENCES_PLACE, false);
        }

        if (pref.contains(Consts.APP_PREFERENCES_SCREEN)) {
            game.isKeelScreenOn = pref.getBoolean(Consts.APP_PREFERENCES_SCREEN, false);
            if (game.isKeelScreenOn) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
                    alertDialogBuilder.setTitle("Прервать игру")
                    alertDialogBuilder.setMessage("Вы уверены, что хотите прервать текущую игру?")
                    alertDialogBuilder.setNegativeButton("Нет", null)
                    alertDialogBuilder.setPositiveButton("Да") { dialog, which -> startListActivity() }
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
        val value = data.getStringExtra(Consts.INTENT_INPUT_VALUE)
        val isEdit = data.getBooleanExtra(Consts.INTENT_IS_EDIT, false)
        game.okButtonOnKeyboardActivityClicked(value = value, isEdit = isEdit)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
            alertDialogBuilder.setTitle("Выход")
            alertDialogBuilder.setMessage("Вы уверены, что хотите выйти?")
            alertDialogBuilder.setNegativeButton("Нет", null)
            alertDialogBuilder.setPositiveButton("Да") { dialog, which -> finish() }
            alertDialogBuilder.show()
        }
        return false
    }

    fun fillMainTable(players: List<Player>?, player: Player?) {
        gameTable = GameTable(mainActivity = this, mainTable = binding.tlMain)
        gameTable!!.fillTable(players, player)
    }

    fun showSchool(player: Player?) {
        gameTable!!.setSchoolValue(player)
    }

    fun showTotal(player: Player?) {
        gameTable!!.setTotalValue(player)
    }

    fun setTextViewHighlight(currentCell: Cell?, isHighlight: Boolean) {
        if (isHighlight) {
            currentCell!!.background = resources.getDrawable(R.drawable.text_view_back_light_gray)
        } else {
            currentCell!!.background = resources.getDrawable(R.drawable.text_view_back_dark_gray)
        }
    }

    fun startKeyboardActivityForEdit(editedValue: String?, rowName: RowName?) {
        val intent = Intent(this, KeyboardActivity::class.java)
        intent.putExtra(Consts.INTENT_EDITED_VALUE, editedValue)
        intent.putExtra(Consts.INTENT_ROW_NAME, rowName?.getName())
        intent.putExtra(Consts.INTENT_IS_EDIT, true)
        startActivityForResult(intent, 1)
    }

    fun highlightPlayersMove(player: Player?) {
        gameTable!!.highlightPlayersMove(player)
    }

    fun switchOffPlayersMove(player: Player?) {
        gameTable!!.switchOffPlayersMove(player)
    }

    fun enableFullSquarePokerTextViews(player: Player?) {
        gameTable!!.enableAfterThreeClassesTextViews(player)
    }

    fun disableFullSquarePokerTextViews(player: Player?) {
        gameTable!!.disableAfterThreeClassesTextViews(player)
    }

    fun setClickedCell(cell: Cell?) {
        game.setCurrentCell(cell)
    }

    fun showPlayerPlace(player: Player) {
        gameTable!!.setPlayerPlace(player)
    }

    val isCountTotalEveryMove: Boolean
        get() = game.getIsCountTotalEveryMove()

    val isCountPlaceEveryMove: Boolean
        get() = game.getIsCountPlaceEveryMove()

}