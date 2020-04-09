package com.example.dices2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dices2.databinding.ActivityKeyboardBinding

class KeyboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKeyboardBinding

    private var result = ""
    private var isEdit = false
    private var isInSchool = false
    private var enabledTextColor = 0
    private var disabledTextColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeyboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        val intent = intent
        val rowName = intent.getStringExtra(Consts.INTENT_ROW_NAME)
        binding.tvKeyboardTitle.text = rowName
        isEdit = intent.getBooleanExtra(Consts.INTENT_IS_EDIT, false)
        isInSchool = intent.getBooleanExtra(Consts.INTENT_IS_IN_SCHOOL, false)
        if (isEdit) {
            getIntent().getStringExtra(Consts.INTENT_EDITED_VALUE)?.let { result = it }
            binding.tvKeyboardResult.text = result
        }
        enableButton(textView = binding.bSave, isEnabled = isEdit)
        enableButton(textView = binding.bPlus, isEnabled = isInSchool)
        if (isInSchool) {
            enableDigitButtons(isEnabled = false)
        } else {
            enableDigitButtons(isEnabled = true)
        }
    }

    private fun init() {
        // TODO getColor deprecated
        enabledTextColor = resources.getColor(R.color.white)
        disabledTextColor = resources.getColor(R.color.colorBackgroundLight)
    }

    fun onKeyboardButtonClicked(view: View) {
        val symbol = (view as TextView).text.toString()
        val isCountKeyboardInputSymbolsLessThanThree = result.length < 3
        val isResultEmpty = result == ""
        val isFirstResultSymbolPlusOrMinus = result == "+" || result == "-"
        val isSymbolPlusOrMinus = symbol == "+" || symbol == "-"
        val isSymbolZero = symbol == "0"
        // TODO disable to input school without first '+' or '-'
        val isFirstSymbolInSchoolPlusOrMinus = (isInSchool && isResultEmpty && isSymbolPlusOrMinus) || isFirstResultSymbolPlusOrMinus
        if ((isResultEmpty && !isSymbolZero ||
                        isFirstResultSymbolPlusOrMinus && !isSymbolPlusOrMinus && !isSymbolZero ||
                        !isResultEmpty && !isFirstResultSymbolPlusOrMinus && !isSymbolPlusOrMinus)
                && isCountKeyboardInputSymbolsLessThanThree) {
            result += symbol
            binding.tvKeyboardResult.text = result
            enableButton(textView = binding.bSave, isEnabled = true)
            enableDigitButtons(isEnabled = true)
        }
    }

    fun onBackspaceButtonClicked(view: View?) {
        if (result != "") {
            result = result.substring(0, result.length - 1)
            binding.tvKeyboardResult.text = result
        }
        if (result == "" && !isEdit) {
            enableButton(textView = binding.bSave, isEnabled = false)
            if (isInSchool) {
                enableDigitButtons(isEnabled = false)
            }
        }
    }

    fun onCancelButtonClicked(view: View?) {
        finish()
    }

    fun onSaveButtonClicked(view: View?) {
        val intent = Intent()
        intent.putExtra(Consts.INTENT_INPUT_VALUE, result)
        intent.putExtra(Consts.INTENT_IS_EDIT, isEdit)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun enableButton(textView: TextView, isEnabled: Boolean) {
        textView.setTextColor(if (isEnabled) enabledTextColor else disabledTextColor)
        textView.isEnabled = isEnabled
    }

    private fun enableDigitButtons(isEnabled: Boolean) {
        binding.b0.setTextColor(if (isEnabled) enabledTextColor else disabledTextColor)
        binding.b1.setTextColor(if (isEnabled) enabledTextColor else disabledTextColor)
        binding.b2.setTextColor(if (isEnabled) enabledTextColor else disabledTextColor)
        binding.b3.setTextColor(if (isEnabled) enabledTextColor else disabledTextColor)
        binding.b4.setTextColor(if (isEnabled) enabledTextColor else disabledTextColor)
        binding.b5.setTextColor(if (isEnabled) enabledTextColor else disabledTextColor)
        binding.b6.setTextColor(if (isEnabled) enabledTextColor else disabledTextColor)
        binding.b7.setTextColor(if (isEnabled) enabledTextColor else disabledTextColor)
        binding.b8.setTextColor(if (isEnabled) enabledTextColor else disabledTextColor)
        binding.b9.setTextColor(if (isEnabled) enabledTextColor else disabledTextColor)
        binding.b0.isEnabled = isEnabled
        binding.b1.isEnabled = isEnabled
        binding.b2.isEnabled = isEnabled
        binding.b3.isEnabled = isEnabled
        binding.b4.isEnabled = isEnabled
        binding.b5.isEnabled = isEnabled
        binding.b6.isEnabled = isEnabled
        binding.b7.isEnabled = isEnabled
        binding.b8.isEnabled = isEnabled
        binding.b9.isEnabled = isEnabled
    }
}