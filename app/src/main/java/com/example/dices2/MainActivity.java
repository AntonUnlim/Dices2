package com.example.dices2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import java.util.List;

import static com.example.dices2.Consts.*;

public class MainActivity extends AppCompatActivity {
    private Game game;
    private TableLayout tableLayoutMain;
    private GameTable gameTable;

    // TODO поиграться с цветами

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        game = Game.getInstance();
        game.setMainActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.players_list:
                Intent intent = new Intent(this, ListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        String value = data.getStringExtra(INTENT_INPUT_VALUE);
        game.setTextToCurrentTextView(value);
        game.makeMove(value);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Выход");
            alertDialogBuilder.setMessage("Вы уверены, что хотите выйти?");
            alertDialogBuilder.setNegativeButton("Нет", null);
            alertDialogBuilder.setPositiveButton("Да", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }});
            alertDialogBuilder.show();
        }
        return false;
    }

    public void fillMainTable(List<Player> players) {
        gameTable = new GameTable(this, tableLayoutMain);
        gameTable.fillTable(players);
    }

    public void showSchool(Player player) {
        gameTable.setSchoolValue(player);
    }

    public void showTotal(Player player) {
        gameTable.setTotalValue(player);
    }

    private void init() {
        tableLayoutMain = findViewById(R.id.tl_main);
    }
}
