package com.example.dices2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.dices2.Consts.*;

public class Game {
    private static Game instance;
    private MainActivity mainActivity;
    private ListActivity listActivity;
    private List<Player> players;
    private Cell currentCell;
    private boolean isCountTotalEveryMove = false;
    private boolean isCountPlaceEveryMove = false;
    private int totalNumberOfMoves;
    // TODO добавить другие виды правил игры
    private int totalNumberOfRows = 16;
    private int idCurrentPlayer = 0;
    private boolean isGameStarted;
    private Player currentPlayer;
    //private Map<Player, Integer> playersPlaces;

    private Game() {
        players = new ArrayList<>();
        savedCells = new HashSet<>();
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
        mainActivity.fillMainTable(players, currentPlayer);
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

    private void makeEdit(Cell cell) {
        if (currentCell.isEmpty()) {
            mainActivity.setTextViewHighlight(currentCell, false);
        } else {
            mainActivity.setTextViewHighlight(currentCell, true);
        }
        if (cell.getOwner() == currentPlayer) {
            mainActivity.highlightPlayersMove(currentPlayer);
        }
//        if (isEditInSchool(cell)) {
//            mainActivity.disableFullSquarePokerTextViews(currentPlayer);
//        }
    }

    public void okButtonOnKeyboardActivityClicked(String value, boolean isEdit) {
        Player player = currentCell.getOwner();
        RowName rowName = currentCell.getRow();
        player.setValue(rowName, value);
        currentCell.setValue(value);
        refreshCellsList();
        //setTextToCurrentTextView(value);
        refreshFullSquarePokerTextViews(player);
        if (isEdit) {
            makeEdit(currentCell);
        } else {
            makeMove();
        }
        showSchool(player);
        if (isCountTotalEveryMove) {
            showTotal(player);
        }
        if (isCountPlaceEveryMove) {
            showAllPlaces();
        }
        if (isGameOver()) {
            showAllTotals();
            isGameStarted = false;
            showAllPlaces();
        }
    }

    private void refreshFullSquarePokerTextViews(Player player) {
        if (player.isThreeClassesInSchoolAreFinished() && !player.isSecondNonSchoolPartAdded()) {
            mainActivity.enableFullSquarePokerTextViews(player);
        }
        if (!player.isThreeClassesInSchoolAreFinished() && player.isSecondNonSchoolPartAdded()) {
            mainActivity.disableFullSquarePokerTextViews(player);
        }
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mainActivity.fillMainTable(players, currentPlayer);
    }

    public void setCurrentCell(Cell cell) {
        this.currentCell = cell;
    }

//    public void setTextToCurrentTextView(String value) {
//        currentCell.setValue(value);
//    }

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

    private void showPlayerPlace(Player player) {
        mainActivity.showPlayerPlace(player);
    }

    private void showAllPlaces() {
        calcPlaces();
        for (Player player : players) {
            showPlayerPlace(player);
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

    public void setCountPlaceEveryMove(boolean countPlaceEveryMove) {
        isCountPlaceEveryMove = countPlaceEveryMove;
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
        //playersPlaces = new LinkedHashMap<>();
        List<Player> playersSortedByTotalValue = new ArrayList<>(players);
        Collections.sort(playersSortedByTotalValue, Collections.<Player>reverseOrder());
        Player currentPlayer = playersSortedByTotalValue.get(0);
        int place = 1;
        int currentValue = currentPlayer.getTotal();
        currentPlayer.setCurPlace(place);
        //playersPlaces.put(currentPlayer, place);
        for (int i = 1; i < playersSortedByTotalValue.size(); i++) {
            Player nextPlayer = playersSortedByTotalValue.get(i);
            if (currentValue == nextPlayer.getTotal()) {
                //playersPlaces.put(nextPlayer, place);
                nextPlayer.setCurPlace(place);
            } else {
                place++;
                currentValue = nextPlayer.getTotal();
                //playersPlaces.put(nextPlayer, place);
                nextPlayer.setCurPlace(place);
            }
        }
    }

    public boolean isCountPlaceEveryMove() {
        return isCountPlaceEveryMove;
    }

    private boolean isEditInSchool(Cell cell) {
        for (RowName rowName : NAMES_OF_SCHOOL_ROWS) {
            if (cell.getRow() == rowName) {
                return true;
            }
        }
        return false;
    }

    private void refreshCellsList() {
        savedCells.add(currentCell);
    }

    public boolean getIsCountTotalEveryMove() {
        return isCountTotalEveryMove;
    }

    public boolean getIsCountPlaceEveryMove() {
        return isCountPlaceEveryMove;
    }
}