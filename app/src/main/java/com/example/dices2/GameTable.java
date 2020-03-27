package com.example.dices2;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
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
    private final int TEXT_HEIGHT = 48;
    private final int SEPARATOR_HEIGHT = 10;
    private Map<String, TextView> schoolTextViewsMap;
    private Map<String, TextView> totalTextViewsMap;
    private Map<String, TextView> namesTextViewMap;
    private Map<String, List<TextView>> playersTextViewsMap;
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
        namesTextViewMap = new HashMap<>();
        playersTextViewsMap = new HashMap<>();
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
                    namesTextViewMap.put(player.getName(), nameTextView);
                }
            } else if (rowName.equals(SEPARATOR)) {
                textView.setHeight(SEPARATOR_HEIGHT);
                tableRow.addView(textView);
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
            result.setOnClickListener(createOnClickListener());
        }
        result.setHeight(TEXT_HEIGHT);
        result.setWidth(100);
        result.setTextSize(mainActivity.getResources().getDimension(R.dimen.main_table_font_size));
        result.setTextColor(fontColor);
        result.setGravity(Gravity.CENTER_HORIZONTAL);
        mainActivity.registerForContextMenu(result);
        return result;
    }

    private void fillRowWithTextViews(TableRow tableRow, String rowName) {
        for(int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            TextView emptyTextView = createTextView(rowName);
            emptyTextView.setTag(new Cell(player, rowName));
            tableRow.addView(emptyTextView);
            List<TextView> listOfTextViews = playersTextViewsMap.get(player.getName());
            if (listOfTextViews == null) {
                listOfTextViews = new ArrayList<>();
            }
            listOfTextViews.add(emptyTextView);
            playersTextViewsMap.put(player.getName(), listOfTextViews);
            if (rowName.equals(SCHOOL)) {
                schoolTextViewsMap.put(player.getName(), emptyTextView);
            }
            if (rowName.equals(TOTAL)) {
                totalTextViewsMap.put(player.getName(), emptyTextView);
            }
        }
    }

    public void setSchoolValue(Player player) {
        TextView currentTextView = schoolTextViewsMap.get(player.getName());
        int schoolValue = player.getSchool();
        currentTextView.setText(String.valueOf(schoolValue));
        if (schoolValue < 0) {
            currentTextView.setBackground(mainActivity.getResources().getDrawable(R.drawable.text_view_back_red));
        } else if (player.isSchoolFinished()) {
            currentTextView.setBackground(mainActivity.getResources().getDrawable(R.drawable.text_view_back_green));
        } else {
            currentTextView.setBackground(mainActivity.getResources().getDrawable(R.drawable.text_view_back_dark_gray));
        }

    }

    public void setTotalValue(Player player) {
        totalTextViewsMap.get(player.getName()).setText(String.valueOf(player.getTotal()));
    }

    public void highlightPlayersMove(Player player) {
        List<TextView> listOfTextViews;
        for (Map.Entry<String, List<TextView>> entry : playersTextViewsMap.entrySet()) {
            if (entry.getKey().equals(player.getName())) {
                listOfTextViews = entry.getValue();
                for (TextView textView : listOfTextViews) {
                    if (textView.getText().equals("")) {
                        Cell cell = (Cell)textView.getTag();
                        if (!Arrays.asList(namesOfUnclickableRows).contains(cell.getRow())) {
                            textView.setBackground(mainActivity.getResources().getDrawable(R.drawable.text_view_back_highlight));
                            textView.setOnClickListener(createOnClickListener());
                        }
                    }
                }
            } else {
                listOfTextViews = entry.getValue();
                for (TextView textView : listOfTextViews) {
                    if (textView.getText().equals("")) {
                        textView.setBackground(mainActivity.getResources().getDrawable(R.drawable.text_view_back_dark_gray));
                        textView.setOnClickListener(null);
                    }
                }
            }
        }
    }

    private View.OnClickListener createOnClickListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((TextView)v).getText().equals("")) {
                    game.setCurrentTextView((TextView) v);
                    Intent intent = new Intent(mainActivity, KeyboardActivity.class);
                    intent.putExtra(Consts.INTENT_ROW_NAME, ((Cell) v.getTag()).getRow());
                    intent.putExtra(INTENT_IS_EDIT, false);
                    mainActivity.startActivityForResult(intent, 1);
                }
            }
        };
        return onClickListener;
    }
}
