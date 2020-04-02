package com.example.dices2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TableLayout;

import java.util.List;

import static com.example.dices2.Consts.*;

public class MainActivity extends AppCompatActivity {
    private Game game;
    private TableLayout tableLayoutMain;
    private GameTable gameTable;
    private final int MENU_EDIT = 1;

    // TODO поиграться с цветами

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
            case R.id.new_game:
                if (game.isGameStarted()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setTitle("Прервать игру");
                    alertDialogBuilder.setMessage("Вы уверены, что хотите прервать текущую игру?");
                    alertDialogBuilder.setNegativeButton("Нет", null);
                    alertDialogBuilder.setPositiveButton("Да", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startListActivity();
                        }
                    });
                    alertDialogBuilder.show();
                } else {
                    startListActivity();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startListActivity() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        String value = data.getStringExtra(INTENT_INPUT_VALUE);
        boolean isEdit = data.getBooleanExtra(INTENT_IS_EDIT, false);
        game.okButtonOnKeyboardActivityClicked(value, isEdit);
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

    public void setTextViewHighlight(Cell currentCell, boolean isHighlight) {
        if (isHighlight) {
            currentCell.setBackground(getResources().getDrawable(R.drawable.text_view_back_light_gray));
        } else {
            currentCell.setBackground(getResources().getDrawable(R.drawable.text_view_back_dark_gray));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info) {
        menu.add(0, MENU_EDIT, 0, "Редактировать");
        game.setCurrentCell((Cell)view);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_EDIT) {
            game.onEditMenuClicked();
        }
        return super.onContextItemSelected(item);
    }

    public void startKeyboardActivityForEdit(String editedValue, RowName rowName) {
        Intent intent = new Intent(this, KeyboardActivity.class);
        intent.putExtra(INTENT_EDITED_VALUE, editedValue);
        intent.putExtra(INTENT_ROW_NAME, rowName.getName());
        intent.putExtra(INTENT_IS_EDIT, true);
        startActivityForResult(intent, 1);
    }

    public void highlightPlayersMove(Player player) {
        gameTable.highlightPlayersMove(player);
    }

    public void switchOffPlayersMove(Player player) {
        gameTable.switchOffPlayersMove(player);
    }

    public void enableFullSquarePokerTextViews(Player player) {
        gameTable.enableAfterThreeClassesTextViews(player);
    }

    public void disableFullSquarePokerTextViews(Player player) {
        gameTable.disableAfterThreeClassesTextViews(player);
    }

    public void setClickedCell(Cell cell) {
        game.setCurrentCell(cell);
    }

    public void showPlayerPlace(Player player, int place) {
        gameTable.setPlayerPlace(player, place);
    }
}
