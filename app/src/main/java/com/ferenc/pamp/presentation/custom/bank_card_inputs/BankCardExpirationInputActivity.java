package com.ferenc.pamp.presentation.custom.bank_card_inputs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.models.ExpDate;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;

/**
 * Created by
 * Ferenc on 2017.12.25..
 */
@EActivity(R.layout.activity_bank_card_expiration_input)
public class BankCardExpirationInputActivity extends AppCompatActivity {

    @ViewById(R.id.etMonth_ABCEI)
    protected EditText etMonth;
    @ViewById(R.id.etYear_ABCII)
    protected EditText etYear;

    @Extra
    protected ExpDate mExpDate;

    private int mMonth;
    private int mYear;


    @AfterViews
    protected void initUI() {
        if (mExpDate != null) {
            etMonth.setText(String.valueOf(mExpDate.getCardExpMonth()));
            etYear.setText(String.valueOf(mExpDate.getCardExpYear()));
        }
        initEditTextListeners();
    }

    private void initEditTextListeners() {

        RxTextView.editorActionEvents(etMonth).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_NEXT) {
                etYear.setEnabled(true);
                etYear.setFocusableInTouchMode(true);
                etYear.requestFocus();
            }
        });

        RxTextView.editorActionEvents(etYear).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_DONE) {
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                mMonth = Integer.valueOf(etMonth.getText().toString().trim());
                mYear = Integer.valueOf(etYear.getText().toString().trim());
                if (0 < mMonth && mMonth < 13 && 2000 + mYear >= currentYear) {
                    setResult();
                } else {
                    ToastManager.showToast("Incorrect month or year");
                }
            }
        });

        RxTextView.textChangeEvents(etMonth).subscribe(textChangeEvents -> {
            if (!textChangeEvents.text().equals("")) {
                if (textChangeEvents.text().length() == 2) {
                    etYear.setEnabled(true);
                    etYear.setFocusableInTouchMode(true);
                    etYear.requestFocus();
                } else if (textChangeEvents.text().length() == 0) {
                    etMonth.setFocusableInTouchMode(true);
                    etMonth.requestFocus();
                }

            }
        });

        RxTextView.textChangeEvents(etYear).subscribe(textChangeEvents -> {
            if (!textChangeEvents.text().equals("")) {
                if (textChangeEvents.text().length() == 0) {
                    etMonth.setFocusableInTouchMode(true);
                    etMonth.requestFocus();
                }
            }
        });

    }

    void setResult() {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_INPUT_RESULT, new ExpDate(mMonth, mYear));
        setResult(RESULT_OK, intent);
        finish();
    }
}
