package com.example.dices2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Player, Integer> playersPlaces;

    private Game() {
        players = new ArrayList<>();
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
            showAllPlaces();
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

    private void showPlayerPlace(Player player, int place) {
        mainActivity.showPlayerPlace(player, place);
    }

    private void showAllPlaces() {
        calcPlaces();
        for (Player player : players) {
            showPlayerPlace(player, playersPlaces.get(player));
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

    private void calcPlaces() {
        playersPlaces = new LinkedHashMap<>();
        List<Player> playersSortedByTotalValue = new ArrayList<>(players);
        Collections.sort(playersSortedByTotalValue, Collections.<Player>reverseOrder());
        Player currentPlayer = playersSortedByTotalValue.get(0);
        int place = 1;
        int currentValue = currentPlayer.getTotal();
        playersPlaces.put(currentPlayer, place);
        for (int i = 1; i < playersSortedByTotalValue.size(); i++) {
            Player nextPlayer = playersSortedByTotalValue.get(i);
            if (currentValue == nextPlayer.getTotal()) {
                playersPlaces.put(nextPlayer, place);
            } else {
                place++;
                currentValue = nextPlayer.getTotal();
                playersPlaces.put(nextPlayer, place);
            }
        }
    }
}
