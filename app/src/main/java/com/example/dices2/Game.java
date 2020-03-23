package com.example.dices2;

import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static Game instance;
    private MainActivity mainActivity;
    private ListActivity listActivity;
    private List<String> listOfPlayers  = new ArrayList<>();
    private Button curButton;


    private Game() {}

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mainActivity.fillMainTable(listOfPlayers);
    }

    public void setListActivity(ListActivity listActivity) {
        this.listActivity = listActivity;
        fillListView();
    }

    private void fillListView() {
        listActivity.fillListView(listOfPlayers);
    }

    public void onAddButtonClicked(String playerName) {
        listOfPlayers.add(playerName);
        listActivity.refreshListView();
    }

    public String getPlayerNameByPosition(int position) {
        return listOfPlayers.get(position);
    }

    public void deletePlayerFromList(int position) {
        listOfPlayers.remove(position);
    }

    public void clearListOfPlayers() {
        listOfPlayers.clear();
    }

    public void okButtonClicked() {
        mainActivity.fillMainTable(listOfPlayers);
    }

    public void setCurButton(Button button) {
        this.curButton = button;
    }

    public void setCurButtonText(String value) {
        curButton.setText(value);
    }
}
