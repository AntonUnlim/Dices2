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

import java.util.List;

public class ListActivity extends AppCompatActivity {
    private Game game;
    private EditText etPlayerName;
    private ListView lvPlayers;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        game = Game.getInstance();
        game.setListActivity(this);
    }

    public void fillListView(List<String> players) {
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, players);
        lvPlayers.setAdapter(adapter);
        lvPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AlertDialog.Builder adb=new AlertDialog.Builder(ListActivity.this);
                adb.setTitle("Удаление");
                adb.setMessage("Вы уверены, что хотите удалить " + game.getPlayerNameByPosition(position) + "?");
                final int positionToRemove = position;
                adb.setNegativeButton("Нет", null);
                adb.setPositiveButton("Да", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        game.deletePlayerFromList(positionToRemove);
                        refreshListView();
                    }});
                adb.show();
            }
        });
    }

    public void refreshListView() {
        adapter.notifyDataSetChanged();
    }

    public void addButtonClicked(View view) {
        String playerName = etPlayerName.getText().toString();
        if (playerName.equals("")) {
            showToast(getString(R.string.name_cannot_be_empty));
        } else {
            game.onAddButtonClicked(playerName);
            etPlayerName.setText("");
        }
    }

    public void clearButtonClicked(View view) {
        AlertDialog.Builder adb=new AlertDialog.Builder(ListActivity.this);
        adb.setTitle("Очистить список");
        adb.setMessage("Вы уверены, что хотите очистить список игроков?");
        adb.setNegativeButton("Нет", null);
        adb.setPositiveButton("Да", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                game.clearListOfPlayers();
                refreshListView();
            }});
        adb.show();
    }

    public void okButtonClicked(View view) {
        game.okButtonClicked();
        finish();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void init() {
        etPlayerName = findViewById(R.id.et_player_name);
        lvPlayers = findViewById(R.id.lv_players);
    }
}
