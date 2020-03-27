package com.example.dices2;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static Game instance;
    private MainActivity mainActivity;
    private ListActivity listActivity;
    private List<Player> players;
    private TextView currentTextView;
    // TODO добавить галочку
    private boolean isCountTotalEveryMove = false;
    private int totalNumberOfMoves;
    // TODO добавить другие виды правил игры
    private int totalNumberOfRows = 16;
    private int idCurrentPlayer = 0;
    private boolean isGameStarted;
    private Player currentPlayer;

    private Game() {
        players = new ArrayList<>();
        //players.add(new Player("Test 1"));
        //playerList.add(new Player("Test 2"));
        //playerList.add(new Player("Test 3"));
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    // Ход игры

    private void startGame() {
        getTotalNumberOfMoves();
        mainActivity.fillMainTable(players);
        idCurrentPlayer = 0;
        highlightNextPlayer();
        clearPlayersValues();
        isGameStarted = true;
        currentPlayer = players.get(idCurrentPlayer);
    }

    private void clearPlayersValues() {
        for(Player player : players) {
            player.clearAllValues();
        }
    }

    private void highlightNextPlayer() {
        if (idCurrentPlayer > players.size() - 1) {
            idCurrentPlayer = 0;
            currentPlayer = players.get(idCurrentPlayer);
        }
        mainActivity.highlightPlayersMove(players.get(idCurrentPlayer++));
    }

    private void makeMove() {
        totalNumberOfMoves--;
        highlightNextPlayer();
        mainActivity.setTextViewHighlight(currentTextView, true);
    }

    private void makeEditing(Cell cell) {
        if (currentTextView.getText().equals("")) {
            mainActivity.setTextViewHighlight(currentTextView, false);
        } else {
            mainActivity.setTextViewHighlight(currentTextView, true);
        }
        if (cell.getPlayer() == currentPlayer) {
            mainActivity.highlightPlayersMove(currentPlayer);
        }
    }

    public void keyboardActivityOkButtonClicked(String value, boolean isEdit) {
        Cell cell = (Cell) currentTextView.getTag();
        Player player = cell.getPlayer();
        String rowName = cell.getRow();
        player.setValue(rowName, value);
        setTextToCurrentTextView(value);
        if (isEdit) {
            makeEditing(cell);
            //mainActivity.highlightPlayersMove(player);
        } else {
            makeMove();
        }
        showSchool(player);
        if (isCountTotalEveryMove) {
            showTotal(player);
        }
        if (isGameOver()) {
            showAllTotals();
            isGameStarted = false;
        }
    }

    // Работа с MainActivity

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mainActivity.fillMainTable(players);
    }

    public void setCurrentTextView(TextView textView) {
        this.currentTextView = textView;
    }

    public void setTextToCurrentTextView(String value) {
        currentTextView.setText(value);
    }

    private void showSchool(Player player) {
        mainActivity.showSchool(player);
    }

    private void showTotal(Player player) {
        mainActivity.showTotal(player);
    }

    private void showAllTotals() {
        for (Player player : players) {
            showTotal(player);
        }
    }

    // Работа с ListActivity

    public void setListActivity(ListActivity listActivity) {
        this.listActivity = listActivity;
        fillListView();
    }

    private void fillListView() {
        listActivity.fillListView(players);
    }

    public void addPlayer(String nameOfPlayer) {
        players.add(new Player(nameOfPlayer));
        listActivity.refreshListView(players);
    }

    public void removePlayer(int position) {
        players.remove(position);
        listActivity.refreshListView(players);
    }

    public void clearListOfPlayers() {
        players.clear();
        listActivity.refreshListView(players);
    }

    public String getNameOfPlayerByPosition(int position) {
        return players.get(position).getName();
    }

    public void startGameButtonClicked() {
        startGame();
    }

    // Прочее

    private boolean isGameOver() {
        return totalNumberOfMoves <= 0;
    }

    private int getTotalNumberOfMoves() {
        return totalNumberOfMoves = players.size() * totalNumberOfRows;
    }

    public void onEditMenuClicked() {
        Cell cell = (Cell) currentTextView.getTag();
        String rowName = cell.getRow();
        mainActivity.startKeyboardActivityForEdit(currentTextView.getText().toString(), rowName);
    }

    public void setCountTotalEveryMove(boolean countTotalEveryMove) {
        isCountTotalEveryMove = countTotalEveryMove;
    }

    public boolean isCountTotalEveryMove() {
        return isCountTotalEveryMove;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public boolean isDuplicatePlayer(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
