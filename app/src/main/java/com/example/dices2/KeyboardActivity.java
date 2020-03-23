package com.example.dices2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class KeyboardActivity extends AppCompatActivity {

    private Cell cell;
    private TextView tvTitle;
    private String result = "";
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        init();
        cell = (Cell)getIntent().getSerializableExtra("cell");
        tvTitle.setText(cell.getRow());
    }

    public void onButtonClicked(View v) {
        result += ((TextView)v).getText().toString();
        tvResult.setText(result);
    }

    private void init() {
        tvTitle = findViewById(R.id.tv_keyboard_title);
        tvResult = findViewById(R.id.tv_keyboard_result);
    }

    public void onCancelClicked(View view) {
        finish();
    }

    public void onOkClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra("result_value", result);
        //intent.putExtra("result_cell", cell);
        setResult(RESULT_OK, intent);
        finish();
    }
}
