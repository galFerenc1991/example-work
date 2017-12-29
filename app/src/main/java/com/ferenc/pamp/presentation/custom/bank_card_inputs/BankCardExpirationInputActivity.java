package com.ferenc.pamp.presentation.custom.bank_card_inputs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.models.ExpDate;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ToastManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
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

    private int mMonth;
    private int mYear;


    @AfterViews
    protected void initUI() {
        etYear.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
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
            return false;
        });

    }

    void setResult() {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_INPUT_RESULT, new ExpDate(mMonth, mYear));
        setResult(RESULT_OK, intent);
        finish();
    }
}
