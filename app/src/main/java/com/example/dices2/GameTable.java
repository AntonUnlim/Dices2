package com.example.dices2;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameTable {
    private MainActivity mainActivity;
    private TableLayout mainTable;
    private final String NAMES = "Names";
    private String[] namesOfRows = {NAMES,"1","2","3","4","5","6","School","Separator","Two","2+2","Three","Small","Big","Separator","Full","Square","Poker","Chance 1","Chance 2","Separator","Total","Place"};
    private Map<String, Integer> rowsNamesMap;
    private Map<String, Integer> playersNamesMap;
    private GameValue[][] gameValues;
    private Game game;

    public GameTable(MainActivity mainActivity, TableLayout mainTable) {
        this.mainActivity = mainActivity;
        this.mainTable = mainTable;
        rowsNamesMap = new HashMap<>();
        fillRowNamesMap();
        playersNamesMap = new HashMap<>();
        game = Game.getInstance();
    }

    public void fillTable(List<String> players) {
        fillPlayersNameMap(players);
        createGameValuesArray();
        mainTable.removeAllViews();
        for (String rowName : namesOfRows) {
            TableRow tableRow = new TableRow(mainActivity);
            TextView textView = new TextView(mainActivity);
            if (rowName.equals("Names")) {
                tableRow.addView(textView);
                for (String name : players) {
                    TextView nameTextView = new TextView(mainActivity);
                    nameTextView.setText(name);
                    tableRow.addView(nameTextView);
                }
            } else {
                textView.setText(rowName);
                tableRow.addView(textView);
                for(int i = 0; i < players.size(); i++) {
                    Button emptyTextView = new Button(mainActivity);
                    emptyTextView.setTag(new Cell(players.get(i), rowName));
                    View.OnClickListener onClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            game.setCurButton((Button) v);
                            Intent intent = new Intent(mainActivity, KeyboardActivity.class);
                            intent.putExtra("cell", (Cell)v.getTag());
                            mainActivity.startActivityForResult(intent, 1);
                        }
                    };
                    emptyTextView.setOnClickListener(onClickListener);
                    tableRow.addView(emptyTextView);
                }
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

    private void createGameValuesArray() {
        gameValues = new GameValue[playersNamesMap.size()][rowsNamesMap.size()];
    }

    public void setNewValue(String value) {
        //gameValues[playersNamesMap.get(cell.getCol())][rowsNamesMap.get(cell.getRow())] = new GameValue(value);
    }


}
