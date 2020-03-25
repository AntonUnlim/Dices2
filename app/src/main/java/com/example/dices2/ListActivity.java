package com.example.dices2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
                android.R.layout.simple_list_item_1, playersToString(players));
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
        if (playerName.equals("")) {
            showToast(getString(R.string.name_cannot_be_empty));
        } else {
            game.addPlayer(playerName);
            editTextPlayerName.setText("");
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
        game.startGameButtonClicked();
        finish();
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
    }
}
