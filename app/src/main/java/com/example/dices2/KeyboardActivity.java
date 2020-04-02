package com.example.dices2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.dices2.Consts.*;

public class KeyboardActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewResult;
    private TextView buttonSave;
    private TextView buttonPlus;
    private String result = "";
    private boolean isEdit;
    private boolean isInSchool;
    private int enabledTextColor;
    private int disabledTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        init();
        Intent intent = getIntent();
        String rowName = intent.getStringExtra(INTENT_ROW_NAME);
        textViewTitle.setText(rowName);
        isEdit = intent.getBooleanExtra(INTENT_IS_EDIT, false);
        isInSchool = intent.getBooleanExtra(INTENT_IS_IN_SCHOOL, false);
        if (isEdit) {
            result = getIntent().getStringExtra(INTENT_EDITED_VALUE);
            textViewResult.setText(result);
        }
        enableButton(buttonSave, isEdit);
        enableButton(buttonPlus, isInSchool);
    }

    private void init() {
        textViewTitle = findViewById(R.id.tv_keyboard_title);
        textViewResult = findViewById(R.id.tv_keyboard_result);
        buttonSave = findViewById(R.id.b_save);
        buttonPlus = findViewById(R.id.b_plus);
        enabledTextColor = getResources().getColor(R.color.white);
        disabledTextColor = getResources().getColor(R.color.colorBackgroundLight);
    }

    public void onKeyboardButtonClicked(View view) {
        String symbol = ((TextView)view).getText().toString();
        boolean isResultEmpty = result.equals("");
        boolean isFirstResultSymbolPlusOrMinus = result.equals("+") || result.equals("-");
        boolean isSymbolPlusOrMinus = symbol.equals("+") || symbol.equals("-");
        boolean isSymbolZero = symbol.equals("0");
        if ((isResultEmpty && !isSymbolZero) ||
                (isFirstResultSymbolPlusOrMinus && !isSymbolPlusOrMinus && !isSymbolZero) ||
                (!isResultEmpty && !isFirstResultSymbolPlusOrMinus && !isSymbolPlusOrMinus)) {
            result += symbol;
            textViewResult.setText(result);
            enableButton(buttonSave, true);
        }
    }

    public void onBackspaceButtonClicked(View view) {
        if (!result.equals("")) {
            result = result.substring(0, result.length() - 1);
            textViewResult.setText(result);
        }
        if (result.equals("") && !isEdit) {
            enableButton(buttonSave, false);
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

    private void enableButton(TextView textView, boolean isEnabled) {
        textView.setTextColor(isEnabled ? enabledTextColor : disabledTextColor);
        textView.setEnabled(isEnabled);
    }
}
