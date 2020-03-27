package com.example.dices2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private Game game;
    private EditText editTextPlayerName;
    private ListView listViewPlayers;
    ArrayAdapter<String> stringArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        game = Game.getInstance();
        game.setListActivity(this);
    }

    public void fillListView(final List<Player> players) {
        stringArrayAdapter = new ArrayAdapter<>(this,
                R.layout.my_simple_list_item, playersToString(players));
        listViewPlayers.setAdapter(stringArrayAdapter);
        listViewPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListActivity.this);
                alertDialogBuilder.setTitle("Удаление");
                alertDialogBuilder.setMessage("Вы уверены, что хотите удалить " + game.getNameOfPlayerByPosition(position) + "?");
                final int positionToRemove = position;
                alertDialogBuilder.setNegativeButton("Нет", null);
                alertDialogBuilder.setPositiveButton("Да", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        game.removePlayer(positionToRemove);
                    }});
                alertDialogBuilder.show();
            }
        });
    }

    public void refreshListView(List<Player> players) {
        stringArrayAdapter.clear();
        stringArrayAdapter.addAll(playersToString(players));
        stringArrayAdapter.notifyDataSetChanged();
    }

    public void addButtonClicked(View view) {
        String playerName = editTextPlayerName.getText().toString();
        if (game.isDuplicatePlayer(playerName)) {
            showToast("Имена игроков не могут повторяться!");
        } else {
            if (playerName.equals("")) {
                showToast(getString(R.string.name_cannot_be_empty));
            } else {
                game.addPlayer(playerName);
                editTextPlayerName.setText("");
            }
        }
    }

    public void clearButtonClicked(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListActivity.this);
        alertDialogBuilder.setTitle("Очистить список");
        alertDialogBuilder.setMessage("Вы уверены, что хотите очистить список игроков?");
        alertDialogBuilder.setNegativeButton("Нет", null);
        alertDialogBuilder.setPositiveButton("Да", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                game.clearListOfPlayers();
            }});
        alertDialogBuilder.show();
    }

    public void startGameButtonClicked(View view) {
        if (listViewPlayers.getAdapter().getCount() < 2) {
            showToast("Для начала игры надо миниум два игрока");
        } else {
            game.startGameButtonClicked();
            finish();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private List<String> playersToString(List<Player> players) {
        List<String> result = new ArrayList<>();
        for (Player player : players) {
            result.add(player.getName());
        }
        return result;
    }

    private void init() {
        editTextPlayerName = findViewById(R.id.et_player_name);
        listViewPlayers = findViewById(R.id.lv_players);
        // set white color to underline
        editTextPlayerName.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
