package com.kubator.pamp.presentation.custom.bank_card_inputs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;


import com.kubator.pamp.R;
import com.kubator.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by
 * Ferenc on 2017.12.25..
 */
@EActivity(R.layout.activity_bank_card_cvv_input)
public class BankCardCVVInputActivity extends AppCompatActivity {
    @ViewById(R.id.etCVV_ABCCVVI)
    protected EditText etCVV;

    @Extra
    protected String mCardCVV;

    @AfterViews
    protected void initUI() {
        if (!TextUtils.isEmpty(mCardCVV)) etCVV.setText(mCardCVV);
        initEditTextListeners();
    }

    private void initEditTextListeners() {
        RxTextView.editorActionEvents(etCVV).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_DONE)
                setResult();
        });
    }

    void setResult() {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_INPUT_RESULT, etCVV.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
