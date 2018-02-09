package com.ferenc.pamp.presentation.custom.bank_card_inputs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.models.BankCardNumber;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.androidannotations.annotations.AfterViews;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
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

    @Extra
    protected BankCardNumber mBankCardNumber;

//    private static String first4;
//    private static String second4;
//    private static String third4;
//    private static String fourth4;

    @AfterViews
    protected void initUI() {
        initEditTextListeners();
//        if (!TextUtils.isEmpty(first4)) {
//            etFirst.setText(first4);
//            etSecond.setText(second4);
//            etThird.setText(third4);
//            etFourth.setText(fourth4);
//        }

        if (mBankCardNumber != null) {
            etFirst.setText(mBankCardNumber.getFirst4());
            etSecond.setText(mBankCardNumber.getSecond4());
            etThird.setText(mBankCardNumber.getThird4());
            etFourth.setText(mBankCardNumber.getFourth4());
        }
    }

    private void initEditTextListeners() {

//        etFirst.setFocusableInTouchMode(true);
//        etFirst.requestFocus();

        RxTextView.editorActionEvents(etFirst).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_NEXT) {
                etSecond.setEnabled(true);
                etSecond.setFocusableInTouchMode(true);
                etSecond.requestFocus();
            }
        });

        RxTextView.editorActionEvents(etSecond).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_NEXT) {
                etThird.setEnabled(true);
                etThird.setFocusableInTouchMode(true);
                etThird.requestFocus();
            }
        });

        RxTextView.editorActionEvents(etThird).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_NEXT) {
                etFourth.setEnabled(true);
                etFourth.setFocusableInTouchMode(true);
                etFourth.requestFocus();
            }
        });

        RxTextView.editorActionEvents(etFourth).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_DONE)
                setResult();
        });

        RxTextView.textChangeEvents(etFirst).subscribe(textChangeEvents -> {
            if (!textChangeEvents.text().equals("")) {
                if (textChangeEvents.text().length() == 4) {
                    etSecond.setEnabled(true);
                    etSecond.setFocusableInTouchMode(true);
                    etSecond.requestFocus();
                } else {
                    etSecond.setEnabled(false);
                }
            }
        });

        RxTextView.textChangeEvents(etSecond).subscribe(textChangeEvents -> {
            if (!textChangeEvents.text().equals("")) {
                if (textChangeEvents.text().length() == 4) {
                    etThird.setEnabled(true);
                    etThird.setFocusableInTouchMode(true);
                    etThird.requestFocus();
                } else if (textChangeEvents.text().length() == 0) {
                    etFirst.setFocusableInTouchMode(true);
                    etFirst.requestFocus();
                } else {
                    etThird.setEnabled(false);
                }
            }
        });

        RxTextView.textChangeEvents(etThird).subscribe(textChangeEvents -> {
            if (!textChangeEvents.text().equals("")) {
                if (textChangeEvents.text().length() == 4) {
                    etFourth.setEnabled(true);
                    etFourth.setFocusableInTouchMode(true);
                    etFourth.requestFocus();
                } else if (textChangeEvents.text().length() == 0) {
                    etSecond.setFocusableInTouchMode(true);
                    etSecond.requestFocus();
                } else {
                    etFourth.setEnabled(false);
                }
            }
        });

        RxTextView.textChangeEvents(etFourth).subscribe(textChangeEvents -> {
            if (!textChangeEvents.text().equals("")) {
                if (textChangeEvents.text().length() == 0) {
                    etThird.setFocusableInTouchMode(true);
                    etThird.requestFocus();
                }
            }
        });

    }

    void setResult() {
        Intent intent = new Intent();
//        first4 =;
//        second4 = etSecond.getText().toString();
//        third4 =;
//        fourth4 =;

        mBankCardNumber = new BankCardNumber.Builder()
                .setFirst4(etFirst.getText().toString())
                .setSecond4(etSecond.getText().toString())
                .setThird4(etThird.getText().toString())
                .setFourth4(etFourth.getText().toString())
                .build();

        intent.putExtra(Constants.KEY_INPUT_RESULT, mBankCardNumber);
        setResult(RESULT_OK, intent);
        finish();
    }

}



