package com.example.dices2;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.dices2.Consts.*;

public class GameTable {
    private MainActivity mainActivity;
    private TableLayout mainTable;
    private String[] namesOfUnclickableRows = {SCHOOL, TOTAL, PLACE};
    private Map<String, Integer> rowsNamesMap;
    private Map<String, Integer> playersNamesMap;
    private Game game;
    private final int TEXT_HEIGHT = 64;
    private final int SEPARATOR_HEIGHT = 10;
    private Map<String, TextView> schoolTextViewsMap;
    private Map<String, TextView> totalTextViewsMap;
    private List<Player> players;
    private int fontColor = Color.WHITE;

    public GameTable(MainActivity mainActivity, TableLayout mainTable) {
        this.mainActivity = mainActivity;
        this.mainTable = mainTable;
        rowsNamesMap = new HashMap<>();
        fillRowNamesMap();
        playersNamesMap = new HashMap<>();
        game = Game.getInstance();
        schoolTextViewsMap = new HashMap<>();
        totalTextViewsMap = new HashMap<>();
    }

    public void fillTable(List<Player> players) {
        this.players = players;
        //fillPlayersNameMap(players);
        mainTable.removeAllViews();
        for (String rowName : namesOfRows) {
            TableRow tableRow = new TableRow(mainActivity);
            tableRow.setBackground(mainActivity.getResources().getDrawable(R.drawable.text_view_back_dark_gray));
            TextView textView = new TextView(mainActivity);
            textView.setPadding(16, 0, 16, 0);
            if (rowName.equals(NAMES)) {
                tableRow.addView(textView);
                for (Player player : players) {
                    TextView nameTextView = new TextView(mainActivity);
                    nameTextView.setTextSize(mainActivity.getResources().getDimension(R.dimen.main_table_font_size));
                    nameTextView.setTextColor(fontColor);
                    nameTextView.setText(player.getName());
                    nameTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                    tableRow.addView(nameTextView);
                }
            } else if (rowName.equals(SEPARATOR)) {
                textView.setHeight(SEPARATOR_HEIGHT);
                tableRow.addView(textView);
            } else if (rowName.equals(TRIANGLE)) {
                textView.setCompoundDrawablesWithIntrinsicBounds(mainActivity.getResources().getDrawable(R.drawable.triangle_24dp), null, null, null);
                textView.setHeight(TEXT_HEIGHT);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                tableRow.addView(textView);
                fillRowWithTextViews(tableRow, rowName);
            } else {
                textView.setTextSize(mainActivity.getResources().getDimension(R.dimen.main_table_font_size));
                textView.setTextColor(fontColor);
                textView.setText(rowName);
                tableRow.addView(textView);
                fillRowWithTextViews(tableRow, rowName);
            }
            mainTable.addView(tableRow);
        }
    }

    private void fillRowNamesMap() {
        int index = 0;
        for (String name : namesOfRows) {
            if(!name.equals(NAMES)) {
                rowsNamesMap.put(name, index++);
            }
        }
    }

    private void fillPlayersNameMap(List<String> players) {
        int index = 0;
        for (String name : players) {
            playersNamesMap.put(name, index++);
        }
    }

    private TextView createTextView(String rowName) {
        TextView result = new TextView(mainActivity);
        result.setBackground(mainActivity.getResources().getDrawable(R.drawable.text_view_back_dark_gray));
        if (!Arrays.asList(namesOfUnclickableRows).contains(rowName)) {
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    game.setCurrentTextView((TextView) v);
                    Intent intent = new Intent(mainActivity, KeyboardActivity.class);
                    intent.putExtra(Consts.INTENT_ROW_NAME, ((Cell)v.getTag()).getRow());
                    mainActivity.startActivityForResult(intent, 1);
                }
            };
            result.setOnClickListener(onClickListener);
        }
        result.setLayoutParams(new ConstraintLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//        result.setHeight(TEXT_HEIGHT);
//        result.setWidth(100);
        result.setTextSize(mainActivity.getResources().getDimension(R.dimen.main_table_font_size));
        result.setTextColor(fontColor);
        result.setGravity(Gravity.CENTER_HORIZONTAL);
        return result;
    }

    private void fillRowWithTextViews(TableRow tableRow, String rowName) {
        for(int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            TextView emptyTextView = createTextView(rowName);
            emptyTextView.setTag(new Cell(player, rowName));
            tableRow.addView(emptyTextView);
            if (rowName.equals(SCHOOL)) {
                schoolTextViewsMap.put(player.getName(), emptyTextView);
            }
            if (rowName.equals(TOTAL)) {
                totalTextViewsMap.put(player.getName(), emptyTextView);
            }
        }
    }

    public void setSchoolValue(Player player) {
        schoolTextViewsMap.get(player.getName()).setText(String.valueOf(player.getSchool()));
    }

    public void setTotalValue(Player player) {
        totalTextViewsMap.get(player.getName()).setText(String.valueOf(player.getTotal()));
    }
}
