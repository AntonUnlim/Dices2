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

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Game game;
    private TableLayout tlMain;
    private GameTable gameTable;

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

    private void init() {
        tlMain = findViewById(R.id.tl_main);
    }

    public void fillMainTable(List<String> players) {
        gameTable = new GameTable(this, tlMain);
        gameTable.fillTable(players);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        String value = data.getStringExtra("result_value");
        //Cell cell = (Cell)data.getSerializableExtra("result_cell");
        game.setCurButtonText(value);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
            adb.setTitle("Выход");
            adb.setMessage("Вы уверены, что хотите выйти?");
            adb.setNegativeButton("Нет", null);
            adb.setPositiveButton("Да", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }});
            adb.show();
        }
        return false;
    }
}
