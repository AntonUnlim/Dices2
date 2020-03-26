package com.example.dices2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.dices2.Consts.*;

public class KeyboardActivity extends AppCompatActivity {
    private TextView textViewTitle;
    private TextView textViewResult;
    private Button buttonSave;
    private String result = "";
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        init();
        String rowName = getIntent().getStringExtra(INTENT_ROW_NAME);
        isEdit = getIntent().getBooleanExtra(INTENT_IS_EDIT, false);
        textViewTitle.setText(rowName);
        if (isEdit) {
            result = getIntent().getStringExtra(INTENT_EDITED_VALUE);
            textViewResult.setText(result);
        }
    }

    public void onKeyboardButtonClicked(View v) {
        String symbol = ((TextView)v).getText().toString();
        boolean isResultEmpty = result.equals("");
        boolean isFirstResultSymbolPlusOrMinus = result.equals("+") || result.equals("-");
        boolean isSymbolPlusOrMinus = symbol.equals("+") || symbol.equals("-");
        boolean isSymbolZero = symbol.equals("0");
        if ((isResultEmpty && !isSymbolZero) ||
                (isFirstResultSymbolPlusOrMinus && !isSymbolPlusOrMinus && !isSymbolZero) ||
                (!isResultEmpty && !isFirstResultSymbolPlusOrMinus && !isSymbolPlusOrMinus)) {
            result += symbol;
            textViewResult.setText(result);
            buttonSave.setEnabled(true);
        }
    }

    public void onBackspaceButtonClicked(View view) {
        if (!result.equals("")) {
            result = result.substring(0, result.length() - 1);
            textViewResult.setText(result);
        }
        if (result.equals("")) {
            buttonSave.setEnabled(false);
        }
    }

    public void onCancelButtonClicked(View view) {
        finish();
    }

    public void onSaveButtonClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra(Consts.INTENT_INPUT_VALUE, result);
        intent.putExtra(INTENT_IS_EDIT, isEdit);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void init() {
        textViewTitle = findViewById(R.id.tv_keyboard_title);
        textViewResult = findViewById(R.id.tv_keyboard_result);
        buttonSave = findViewById(R.id.b_save);
    }
}
