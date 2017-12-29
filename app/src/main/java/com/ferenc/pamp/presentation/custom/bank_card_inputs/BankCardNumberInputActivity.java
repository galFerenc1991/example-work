package com.ferenc.pamp.presentation.custom.bank_card_inputs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.utils.Constants;

import org.androidannotations.annotations.AfterViews;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by
 * Ferenc on 2017.12.24..
 */
@EActivity(R.layout.activity_bank_card_number_input)
public class BankCardNumberInputActivity extends AppCompatActivity {

    @ViewById(R.id.etFirst_ABCII)
    protected EditText etFirst;
    @ViewById(R.id.etSecond_ABCII)
    protected EditText etSecond;
    @ViewById(R.id.etThird_ABCII)
    protected EditText etThird;
    @ViewById(R.id.etFourth_ABCII)
    protected EditText etFourth;

    @AfterViews
    protected void initUI() {
        etFirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 4) {
                    etFirst.clearFocus();
                    etSecond.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 4) {
                    etSecond.clearFocus();
                    etThird.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etThird.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 4) {
                    etThird.clearFocus();
                    etFourth.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etFourth.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                setResult();
            }
            return false;
        });

    }

    void setResult() {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_INPUT_RESULT, etFirst.getText().toString() + etSecond.getText().toString() + etThird.getText().toString() + etFourth.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

}
