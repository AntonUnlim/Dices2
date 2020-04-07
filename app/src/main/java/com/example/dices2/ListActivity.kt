package com.example.dices2

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dices2.databinding.ActivityListBinding
import java.util.*

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    private var game: Game? = null
    private var stringArrayAdapter: ArrayAdapter<String?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        game = Game.instance
        game!!.setListActivity(this)
    }

    fun fillListView(players: List<Player>) {
        stringArrayAdapter = ArrayAdapter(this,
                R.layout.my_simple_list_item, playersToString(players))
        binding.lvPlayers.adapter = stringArrayAdapter
        binding.lvPlayers.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val alertDialogBuilder = AlertDialog.Builder(this@ListActivity)
            alertDialogBuilder.setTitle("Удаление")
            alertDialogBuilder.setMessage("Вы уверены, что хотите удалить " + game!!.getNameOfPlayerByPosition(position) + "?")
            alertDialogBuilder.setNegativeButton("Нет", null)
            alertDialogBuilder.setPositiveButton("Да") { dialog, which -> game!!.removePlayer(position) }
            alertDialogBuilder.show()
        }
    }

    fun refreshListView(players: List<Player>) {
        stringArrayAdapter!!.clear()
        stringArrayAdapter!!.addAll(playersToString(players))
        stringArrayAdapter!!.notifyDataSetChanged()
    }

    fun addButtonClicked(view: View?) {
        val playerName = binding.etPlayerName.text.toString()
        if (game!!.isDuplicatePlayer(playerName)) {
            showToast("Имена игроков не могут повторяться!")
        } else {
            if (playerName == "") {
                showToast(getString(R.string.name_cannot_be_empty))
            } else {
                game!!.addPlayer(playerName)
                binding.etPlayerName.setText("")
            }
        }
    }

    fun clearButtonClicked(view: View?) {
        val alertDialogBuilder = AlertDialog.Builder(this@ListActivity)
        alertDialogBuilder.setTitle("Очистить список")
        alertDialogBuilder.setMessage("Вы уверены, что хотите очистить список игроков?")
        alertDialogBuilder.setNegativeButton("Нет", null)
        alertDialogBuilder.setPositiveButton("Да") { dialog, which -> game!!.clearListOfPlayers() }
        alertDialogBuilder.show()
    }

    fun startGameButtonClicked(view: View?) {
        if (binding.lvPlayers.adapter.count < 2) {
            showToast("Для начала игры надо миниум два игрока")
        } else {
            game!!.startGameButtonClicked()
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun playersToString(players: List<Player>): List<String?> {
        val result: MutableList<String?> = ArrayList()
        for (player in players) {
            result.add(player.name)
        }
        return result
    }

    private fun init() {
        // set white color to underline
        binding.etPlayerName.background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}