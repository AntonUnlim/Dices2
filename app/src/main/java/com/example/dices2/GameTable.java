package com.example.dices2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.dices2.Consts.*;
import static com.example.dices2.RowName.*;


public class GameTable {
    private MainActivity mainActivity;
    private TableLayout mainTable;
    private final RowName[] SCHOOL_ROWS = {ONE, TWO, THREE, FOUR, FIVE, SIX};
    private final RowName[] FIRST_NON_SCHOOL_PART_ROWS = {PAIR, TWO_PLUS_TWO, TRIANGLE, SMALL, BIG};
    private final RowName[] FULL_SQUARE_POKER_ROWS = {FULL, SQUARE, POKER};
    private final RowName[] CHANCES_ROWS = {CHANCE1, CHANCE2};
    private Map<Player, TextView> schoolTextViewsMap;
    private Map<Player, TextView> totalTextViewsMap;
    private Map<Player, List<Cell>> playersCellsMap;
    private Map<Player, List<Cell>> playersCellsAfterThreeClassesMap;
    private List<Player> players;
    private final int FONT_COLOR = MAIN_TABLE_TEXT_COLOR;
    private Drawable backgroundDarkGray;
    private Drawable backgroundLightGray;
    private Drawable backgroundRed;
    private Drawable backgroundGreen;
    private Drawable backgroundHighlightedBorders;
    private final float TEXT_SIZE = MAIN_TABLE_FONT_SIZE;
    private final int LEFT_RIGHT_TEXTVIEW_PADDING = 16;
    private Map<Player, TextView> playersNamesTextViews;
    private Map<Player, TextView> placeTextViewsMap;

    public GameTable(MainActivity mainActivity, TableLayout mainTable) {
        this.mainActivity = mainActivity;
        this.mainTable = mainTable;
        backgroundDarkGray = mainActivity.getResources().getDrawable(R.drawable.text_view_back_dark_gray);
        backgroundLightGray = mainActivity.getResources().getDrawable(R.drawable.text_view_back_light_gray);
        backgroundRed = mainActivity.getResources().getDrawable(R.drawable.text_view_back_red);
        backgroundGreen = mainActivity.getResources().getDrawable(R.drawable.text_view_back_green);
        backgroundHighlightedBorders = mainActivity.getResources().getDrawable(R.drawable.text_view_back_highlight);
        schoolTextViewsMap = new HashMap<>();
        totalTextViewsMap = new HashMap<>();
        playersCellsMap = new HashMap<>();
        playersNamesTextViews = new HashMap<>();
        placeTextViewsMap = new HashMap<>();
        playersCellsAfterThreeClassesMap = new HashMap<>();
    }

    public void fillTable(List<Player> players) {
        this.players = players;
        mainTable.removeAllViews();
        fillNamesRow();
        fillSchoolRows();
        fillSchoolTotalRow();
        addSeparatorRow();
        fillFirstNonSchoolPart();
        addSeparatorRow();
        fillSecondNonSchoolPart();
        fillChances();
        addSeparatorRow();
        fillTotalRows();
        fillPlaceRow();
    }

    private void fillNamesRow() {
        TableRow tableRow = createTableRow();
        TextView emptyTextView = new TextView(mainActivity);
        tableRow.addView(emptyTextView);
        for (Player player : players) {
            TextView nameTextView = createNameTextView();
            nameTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            nameTextView.setText(player.getName());
            playersNamesTextViews.put(player, nameTextView);
            tableRow.addView(nameTextView);
        }
        mainTable.addView(tableRow);
    }

    private void fillSchoolRows() {
        for (RowName rowName : SCHOOL_ROWS) {
            TableRow tableRow = createTableRow();
            TextView nameRowTextView = createNameTextView();
            nameRowTextView.setText(rowName.getName());
            tableRow.addView(nameRowTextView);
            fillRowWithClickableCells(tableRow, rowName);
            mainTable.addView(tableRow);
        }
    }

    private void fillSchoolTotalRow() {
        TableRow tableRow = createTableRow();
        TextView nameRowTextView = createNameTextView();
        nameRowTextView.setText(SCHOOL.getName());
        tableRow.addView(nameRowTextView);
        for (Player player : players) {
            TextView textView = createNameTextView();
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(textView);
            schoolTextViewsMap.put(player, textView);
        }
        mainTable.addView(tableRow);
    }

    private void addSeparatorRow() {
        TableRow tableRow = createTableRow();
        TextView textView = new TextView(mainActivity);
        textView.setHeight(SEPARATOR_HEIGHT);
        for(Player player : players) {
            TextView tv = new TextView(mainActivity);
            tv.setHeight(SEPARATOR_HEIGHT);
            tableRow.addView(tv);
        }
        tableRow.addView(textView);
        mainTable.addView(tableRow);
    }

    private void fillFirstNonSchoolPart() {
        for (RowName rowName : FIRST_NON_SCHOOL_PART_ROWS) {
            TableRow tableRow = createTableRow();
            TextView nameTextView = createNameTextView();
            nameTextView.setText(rowName.getName());
            tableRow.addView(nameTextView);
            fillRowWithClickableCells(tableRow, rowName);
            mainTable.addView(tableRow);
        }
    }

    private void fillSecondNonSchoolPart() {
        for (RowName rowName : FULL_SQUARE_POKER_ROWS) {
            TableRow tableRow = createTableRow();
            TextView nameTextView = createNameTextView();
            nameTextView.setText(rowName.getName());
            tableRow.addView(nameTextView);
            fillRowWithNonClickableCells(tableRow, rowName);
            mainTable.addView(tableRow);
        }
    }

    private void fillChances() {
        for (RowName rowName : CHANCES_ROWS) {
            TableRow tableRow = createTableRow();
            TextView nameTextView = createNameTextView();
            nameTextView.setText(rowName.getName());
            tableRow.addView(nameTextView);
            fillRowWithClickableCells(tableRow, rowName);
            mainTable.addView(tableRow);
        }
    }

    private void fillTotalRows() {
        TableRow totalTableRow = createTableRow();
        TextView totalTextView = createNameTextView();
        totalTextView.setText(TOTAL.getName());
        totalTableRow.addView(totalTextView);
        for (Player player : players) {
            TextView textView = createTotalPlaceTextView();
            totalTableRow.addView(textView);
            totalTextViewsMap.put(player, textView);
        }
        mainTable.addView(totalTableRow);
    }

    private void fillPlaceRow() {
        TableRow placeTableRow = createTableRow();
        TextView placeTextView = createNameTextView();
        placeTextView.setText(PLACE.getName());
        placeTableRow.addView(placeTextView);
        for (Player player : players) {
            TextView textView = createTotalPlaceTextView();
            placeTableRow.addView(textView);
            placeTextViewsMap.put(player, textView);
        }
        mainTable.addView(placeTableRow);
    }

    private TableRow createTableRow() {
        return new TableRow(mainActivity);
    }

    private void fillRowWithClickableCells(TableRow tableRow, RowName rowName) {
        for (Player player : players) {
            Cell newCell = new Cell(mainActivity, player, rowName);
            mainActivity.registerForContextMenu(newCell);
            newCell.setOnClickListener(createOnClickListener());
            newCell.setBackground(backgroundDarkGray);
            tableRow.addView(newCell);
            fillPlayersCellsMap(player, newCell);
        }
    }

    private void fillRowWithNonClickableCells(TableRow tableRow, RowName rowName) {
        for (Player player : players) {
            Cell newCell = new Cell(mainActivity, player, rowName);
            newCell.setBackground(backgroundDarkGray);
            mainActivity.registerForContextMenu(newCell);
            tableRow.addView(newCell);
            if (playersCellsAfterThreeClassesMap.get(player) == null) {
                List<Cell> cells = new ArrayList<>();
                cells.add(newCell);
                playersCellsAfterThreeClassesMap.put(player, cells);
            } else {
                playersCellsAfterThreeClassesMap.get(player).add(newCell);
            }
        }
    }

    private void fillPlayersCellsMap(Player player, Cell cell) {
        if (playersCellsMap.get(player) == null) {
            List<Cell> cells = new ArrayList<>();
            cells.add(cell);
            playersCellsMap.put(player, cells);
        } else {
            playersCellsMap.get(player).add(cell);
        }
    }

    private TextView createNameTextView() {
        TextView textView = new TextView(mainActivity);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(FONT_COLOR);
        textView.setPadding(LEFT_RIGHT_TEXTVIEW_PADDING, 0, LEFT_RIGHT_TEXTVIEW_PADDING, 0);
        textView.setBackground(backgroundDarkGray);
        textView.setWidth(170);
        return textView;
    }

    private TextView createTotalPlaceTextView() {
        TextView textView = new TextView(mainActivity);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(FONT_COLOR);
        textView.setPadding(LEFT_RIGHT_TEXTVIEW_PADDING, 0, LEFT_RIGHT_TEXTVIEW_PADDING, 0);
        textView.setBackground(backgroundDarkGray);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        return textView;
    }

    public void setSchoolValue(Player player) {
        TextView currentTextView = schoolTextViewsMap.get(player);
        int schoolValue = player.getSchool();
        currentTextView.setText(String.valueOf(schoolValue));
        if (schoolValue < 0) {
            currentTextView.setBackground(backgroundRed);
        } else if (player.isSchoolFinished()) {
            currentTextView.setBackground(backgroundGreen);
        } else {
            currentTextView.setBackground(backgroundDarkGray);
        }

    }

    public void setTotalValue(Player player) {
        totalTextViewsMap.get(player).setText(String.valueOf(player.getTotal()));
    }

    public void highlightPlayersMove(Player player) {
        List<Cell> cellToHighlight = playersCellsMap.get(player);
        for (Cell cell : cellToHighlight) {
            if (cell.isEmpty()) {
                enableTextView(cell, true);
            } else {
                enableTextView(cell, false);
            }
        }
    }

    public void switchOffPlayersMove(Player player) {
        List<Cell> cellToSwitchOff = playersCellsMap.get(player);
        for (Cell cell : cellToSwitchOff) {
            if (cell.isEmpty()) {
                enableTextView(cell, false);
            }
        }
    }

    private View.OnClickListener createOnClickListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cell cell = (Cell)v;
                if (cell.isEmpty()) {
                    mainActivity.setClickedCell(cell);
                    Intent intent = new Intent(mainActivity, KeyboardActivity.class);
                    intent.putExtra(INTENT_ROW_NAME, cell.getRow().getName());
                    intent.putExtra(INTENT_IS_EDIT, false);
                    intent.putExtra(INTENT_IS_IN_SCHOOL, isCellInSchool(cell));
                    mainActivity.startActivityForResult(intent, 1);
                }
            }
        };
        return onClickListener;
    }

    public void enableTextView(Cell cell, boolean isEnabled) {
        if (isEnabled) {
            cell.setBackground(backgroundHighlightedBorders);
            cell.setOnClickListener(createOnClickListener());
        } else {
            if (cell.isEmpty()) {
                cell.setBackground(backgroundDarkGray);
            } else {
                cell.setBackground(backgroundLightGray);
            }
            cell.setOnClickListener(null);
        }
    }

    public void enableAfterThreeClassesTextViews(Player player) {
        List<Cell> cells = playersCellsAfterThreeClassesMap.get(player);
        playersCellsMap.get(player).addAll(cells);
    }

    public boolean isCellInSchool(Cell cell) {
        for (RowName rowName : NAMES_OF_SCHOOL_ROWS) {
            if (cell.getRow() == rowName) {
                return true;
            }
        }
        return false;
    }

    public void setPlayerPlace(Player player, int place) {
        placeTextViewsMap.get(player).setText(String.valueOf(place));
    }
}
