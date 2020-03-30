package com.example.dices2;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static Game instance;
    private MainActivity mainActivity;
    private ListActivity listActivity;
    private List<Player> players;
    private Cell currentCell;
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
        int playersAmount = players.size();
        idCurrentPlayer = (idCurrentPlayer > playersAmount - 1) ? 0 : idCurrentPlayer;
        int idHighlightOff = (idCurrentPlayer == 0) ? playersAmount - 1 : idCurrentPlayer - 1;
        int idHighlightOn = (idCurrentPlayer > playersAmount - 1) ? 0 : idCurrentPlayer;
        mainActivity.switchOffPlayersMove(players.get(idHighlightOff));
        currentPlayer = players.get(idHighlightOn);
        mainActivity.highlightPlayersMove(players.get(idHighlightOn));
        idCurrentPlayer++;
    }

    private void makeMove() {
        totalNumberOfMoves--;
        highlightNextPlayer();
        mainActivity.setTextViewHighlight(currentCell, true);
        Log.i("MyLog", "Moves left - " + totalNumberOfMoves);
    }

    private void makeEditing(Cell cell) {
        if (currentCell.getText().equals("")) {
            mainActivity.setTextViewHighlight(currentCell, false);
        } else {
            mainActivity.setTextViewHighlight(currentCell, true);
        }
        if (cell.getOwner() == currentPlayer) {
            mainActivity.highlightPlayersMove(currentPlayer);
        }
    }

    public void keyboardActivityOkButtonClicked(String value, boolean isEdit) {
        Player player = currentCell.getOwner();
        RowName rowName = currentCell.getRow();
        player.setValue(rowName, value);
        setTextToCurrentTextView(value);
        if (isEdit) {
            makeEditing(currentCell);
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
        enableTextViewAfterThreeClasses(player);
    }

    private void enableTextViewAfterThreeClasses(Player player) {
        if (player.isTreeClassesInSchoolAreFinished()) {
            mainActivity.enableAfterThreeClassesTextViews(player);
        }
    }

    // Работа с MainActivity

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mainActivity.fillMainTable(players);
    }

    public void setCurrentCell(Cell cell) {
        this.currentCell = cell;
    }

    public void setTextToCurrentTextView(String value) {
        currentCell.setText(value);
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
        Cell cell = currentCell;
        RowName rowName = cell.getRow();
        mainActivity.startKeyboardActivityForEdit(currentCell.getText().toString(), rowName);
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
