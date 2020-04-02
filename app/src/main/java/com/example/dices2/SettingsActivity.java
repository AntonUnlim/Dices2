package com.example.dices2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Game game;
    private Switch isCountTotalEveryMoveSwitch;
    private Switch isCountPlaceEveryMoveSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        game = Game.getInstance();
        isCountTotalEveryMoveSwitch = findViewById(R.id.is_count_total_every_move);
        isCountTotalEveryMoveSwitch.setChecked(game.isCountTotalEveryMove());
        isCountPlaceEveryMoveSwitch = findViewById(R.id.is_count_place_every_move);
        isCountPlaceEveryMoveSwitch.setChecked(game.isCountPlaceEveryMove());
    }

    public void onSettingsCancelButtonClicked(View view) {
        finish();
    }

    public void onSettingsSaveButtonClicked(View view) {
        game.setCountTotalEveryMove(isCountTotalEveryMoveSwitch.isChecked());
        game.setCountPlaceEveryMove(isCountPlaceEveryMoveSwitch.isChecked());
        finish();
    }
}
