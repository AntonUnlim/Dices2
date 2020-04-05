package com.example.dices2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
    private Map<Player, Cell> schoolTextViewsMap;
    private Map<Player, Cell> totalTextViewsMap;
    private Map<Player, List<Cell>> playersCellsMap;
    private Map<Player, List<Cell>> playersFullSquarePokerCellsMap;
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
    private Map<Player, Cell> placeTextViewsMap;

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
        playersFullSquarePokerCellsMap = new HashMap<>();
    }

    public void fillTable(List<Player> players, Player player) {
        this.players = players;
        mainTable.removeAllViews();
        allCells.clear();
        fillNamesRow();
        //addSeparatorRow();
        fillSchoolRows();
        fillSchoolTotalRow();
        //addSeparatorRow();
        fillFirstNonSchoolPart();
        //addSeparatorRow();
        fillSecondNonSchoolPart();
        fillChances();
        //addSeparatorRow();
        fillTotalRows();
        fillPlaceRow();
        fillSavedValues();
        if (player != null) {
            highlightPlayersMove(player);
        }
    }

    private void fillSavedValues() {
        for (Cell savedCell : savedCells) {
            for (Cell cell : allCells) {
                if (savedCell.equals(cell)) {
                    cell.setValue(savedCell.getValue());
                    mainActivity.setTextViewHighlight(cell, true);
                    enableTextView(cell, false);
                }
            }
        }
        for (Player player : players) {
            setSchoolValue(player);
            if (mainActivity.getIsCountTotalEveryMove()) {
                setTotalValue(player);
            }
            if (mainActivity.getIsCountPlaceEveryMove()) {
                setPlayerPlace(player);
            }
        }
    }

    private void fillNamesRow() {
        TableRow tableRow = createTableRow();
        tableRow.addView(createNameOfRowTextView());
        for (Player player : players) {
            TextView textView = createNameOfPlayerTextView();
            textView.setText(player.getName());
            playersNamesTextViews.put(player, textView);
            tableRow.addView(textView);
        }
        mainTable.addView(tableRow);
    }

    private void fillSchoolRows() {
        for (RowName rowName : SCHOOL_ROWS) {
            TableRow tableRow = createTableRow();
            TextView textView = createNameOfRowTextView();
            textView.setText(rowName.getName());
            tableRow.addView(textView);
            fillRowWithClickableCells(tableRow, rowName);
            mainTable.addView(tableRow);
        }
    }

    private void fillSchoolTotalRow() {
        TableRow tableRow = createTableRow();
        TextView nameOfRowTextView = createNameOfRowTextView();
        nameOfRowTextView.setText(SCHOOL.getName());
        tableRow.addView(nameOfRowTextView);
        for (Player player : players) {
            Cell cell = createNonClickableCell(player, SCHOOL);
            tableRow.addView(cell);
            schoolTextViewsMap.put(player, cell);
            allCells.add(cell);
        }
        mainTable.addView(tableRow);
    }

    private void addSeparatorRow() {
        TableRow tableRow = new TableRow(mainActivity);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.5f);
        tableRow.setLayoutParams(layoutParams);
        tableRow.addView(createNameOfRowTextView());
        for (Player player : players) {
            TextView textView = createNameOfPlayerTextView();
            tableRow.addView(textView);
        }
        mainTable.addView(tableRow);
    }

    private void fillFirstNonSchoolPart() {
        for (RowName rowName : FIRST_NON_SCHOOL_PART_ROWS) {
            TableRow tableRow = createTableRow();
            TextView textView = createNameOfRowTextView();
            textView.setText(rowName.getName());
            tableRow.addView(textView);
            fillRowWithClickableCells(tableRow, rowName);
            mainTable.addView(tableRow);
        }
    }

    private void fillSecondNonSchoolPart() {
        for (RowName rowName : FULL_SQUARE_POKER_ROWS) {
            TableRow tableRow = createTableRow();
            TextView textView = createNameOfRowTextView();
            textView.setText(rowName.getName());
            tableRow.addView(textView);
            fillRowWithNonClickableCells(tableRow, rowName);
            mainTable.addView(tableRow);
        }
    }

    private void fillChances() {
        for (RowName rowName : CHANCES_ROWS) {
            TableRow tableRow = createTableRow();
            TextView textView = createNameOfRowTextView();
            textView.setText(rowName.getName());
            tableRow.addView(textView);
            fillRowWithClickableCells(tableRow, rowName);
            mainTable.addView(tableRow);
        }
    }

    private void fillTotalRows() {
        TableRow tableRow = createTableRow();
        TextView nameOfRowTextView = createNameOfRowTextView();
        nameOfRowTextView.setText(TOTAL.getName());
        tableRow.addView(nameOfRowTextView);
        for (Player player : players) {
            Cell cell = createNonClickableCell(player, TOTAL);
            tableRow.addView(cell);
            totalTextViewsMap.put(player, cell);
            allCells.add(cell);
        }
        mainTable.addView(tableRow);
    }

    private void fillPlaceRow() {
        TableRow placeTableRow = createTableRow();
        TextView placeTextView = createNameOfRowTextView();
        placeTextView.setText(PLACE.getName());
        placeTableRow.addView(placeTextView);
        for (Player player : players) {
            Cell cell = createNonClickableCell(player, PLACE);
            placeTableRow.addView(cell);
            placeTextViewsMap.put(player, cell);
            allCells.add(cell);
        }
        mainTable.addView(placeTableRow);
    }

    private void fillRowWithClickableCells(TableRow tableRow, RowName rowName) {
        for (Player player : players) {
            Cell cell = new Cell(mainActivity, player, rowName);
            cell.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            cell.setGravity(Gravity.CENTER);
            cell.setBackground(backgroundDarkGray);
            cell.setOnClickListener(createOnClickListener());
            setMutualParams(cell);
            mainActivity.registerForContextMenu(cell);
            tableRow.addView(cell);
            fillPlayersCellsMap(player, cell);
            allCells.add(cell);
        }
    }

    private void fillRowWithNonClickableCells(TableRow tableRow, RowName rowName) {
        for (Player player : players) {
            Cell cell = new Cell(mainActivity, player, rowName);
            cell.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            cell.setGravity(Gravity.CENTER);
            cell.setBackground(backgroundDarkGray);
            setMutualParams(cell);
            mainActivity.registerForContextMenu(cell);
            tableRow.addView(cell);
            allCells.add(cell);
            if (playersFullSquarePokerCellsMap.get(player) == null) {
                List<Cell> cells = new ArrayList<>();
                cells.add(cell);
                playersFullSquarePokerCellsMap.put(player, cells);
            } else {
                playersFullSquarePokerCellsMap.get(player).add(cell);
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

    private TableRow createTableRow() {
        TableRow tableRow = new TableRow(mainActivity);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
        tableRow.setLayoutParams(layoutParams);
        tableRow.setBackground(backgroundDarkGray);
        return tableRow;
    }

    private TextView createNameOfRowTextView() {
        TextView textView = new TextView(mainActivity);
        textView.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(LEFT_RIGHT_TEXTVIEW_PADDING, 0, LEFT_RIGHT_TEXTVIEW_PADDING, 0);
        setMutualParams(textView);
        return textView;
    }

    private TextView createNameOfPlayerTextView() {
        TextView textView = new TextView(mainActivity);
        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        textView.setGravity(Gravity.CENTER);
        textView.setBackground(backgroundDarkGray);
        setMutualParams(textView);
        return textView;
    }

    private Cell createNonClickableCell(Player player, RowName rowName) {
        Cell cell = new Cell(mainActivity, player, rowName);
        cell.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        cell.setGravity(Gravity.CENTER);
        cell.setBackground(backgroundDarkGray);
        setMutualParams(cell);
        return cell;
    }

    private void setMutualParams(TextView textView) {
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(FONT_COLOR);
    }

    public void setSchoolValue(Player player) {
        Cell cell = schoolTextViewsMap.get(player);
        int schoolValue = player.getSchool();
        cell.setValue(String.valueOf(schoolValue));
        if (schoolValue < 0) {
            cell.setBackground(backgroundRed);
        } else if (player.isSchoolFinished()) {
            cell.setBackground(backgroundGreen);
        } else {
            cell.setBackground(backgroundDarkGray);
        }

    }

    public void setTotalValue(Player player) {
        totalTextViewsMap.get(player).setValue(String.valueOf(player.getTotal()));
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
        List<Cell> cells = playersFullSquarePokerCellsMap.get(player);
        playersCellsMap.get(player).addAll(cells);
        player.setSecondNonSchoolPartAdded(true);
    }

    public void disableAfterThreeClassesTextViews(Player player) {
        List<Cell> cells = playersFullSquarePokerCellsMap.get(player);
        playersCellsMap.get(player).removeAll(cells);
        player.setSecondNonSchoolPartAdded(false);
        for (Cell cell : cells) {
            cell.setOnClickListener(null);
            cell.setBackground(backgroundDarkGray);
        }
    }

    public boolean isCellInSchool(Cell cell) {
        for (RowName rowName : NAMES_OF_SCHOOL_ROWS) {
            if (cell.getRow() == rowName) {
                return true;
            }
        }
        return false;
    }

    public void setPlayerPlace(Player player) {
        placeTextViewsMap.get(player).setValue(String.valueOf(player.getCurPlace()));
    }
}
