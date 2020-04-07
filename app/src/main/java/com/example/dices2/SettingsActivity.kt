package com.example.dices2

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.dices2.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var pref: SharedPreferences
    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(Consts.APP_PREFERENCES, MODE_PRIVATE)

        game = Game.instance
        binding.isCountTotalEveryMove.isChecked = game.isCountTotalEveryMove
        binding.isCountPlaceEveryMove.isChecked = game.isCountPlaceEveryMove
        binding.isKeepScreenOn.isChecked = game.isKeelScreenOn
    }

    fun onSettingsCancelButtonClicked(view: View?) {
        finish()
    }

    fun onSettingsSaveButtonClicked(view: View?) {
        game.isCountTotalEveryMove = binding.isCountTotalEveryMove.isChecked
        game.isCountPlaceEveryMove = binding.isCountPlaceEveryMove.isChecked
        game.isKeelScreenOn = binding.isKeepScreenOn.isChecked
        finish()
    }

    override fun onStop() {
        super.onStop()
        val editor = pref.edit()
        editor.putBoolean(Consts.APP_PREFERENCES_TOTAL, game.isCountTotalEveryMove)
        editor.putBoolean(Consts.APP_PREFERENCES_PLACE, game.isCountPlaceEveryMove)
        editor.putBoolean(Consts.APP_PREFERENCES_SCREEN, game.isKeelScreenOn)
        editor.apply()
    }
}