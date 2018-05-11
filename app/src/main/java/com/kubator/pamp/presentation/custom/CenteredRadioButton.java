package com.kubator.pamp.presentation.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewTreeObserver;

import com.kubator.pamp.R;

/**
 * Created by
 * Ferenc on 2017.11.20..
 */

public class CenteredRadioButton extends AppCompatRadioButton implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final float DEFAULT_VERTICAL_PADDING = 4F;
    private static final float DEFAULT_HORIZONTAL_PADDING = 5F;
    private static final float DEFAULT_CORNER_RADIUS = 9F;
    private static final float DEFAULT_TEXT_SIZE = 12F;

    private static final String MEASURING_SINGLE_LETTER = "A";

    private Paint mBgPaint;
    private Paint mTextPaint;
    private RectF mBgRect;

    private float mCornerRadius;
    private float mVerticalPadding;
    private float mHorizontalPadding;
    private float mTextSize;

    private int mTextHeight;
    private int mTextHalfHeight;
    private int mTextY = Integer.MIN_VALUE;
    private int mTextX = Integer.MIN_VALUE;

    private int mBgTop = Integer.MIN_VALUE;

    private int mBadgeCount = 0;
    private String mBadgeCountString = "";

    private boolean mBgShouldBeReCalculated = false;

    private Drawable mDrawable;

    public CenteredRadioButton(Context context) {
        this(context, null);
    }
    public CenteredRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context _context, @Nullable AttributeSet _attrs) {
        initDefaultValues();

        if (_attrs != null) {
            TypedArray a = _context.getTheme().obtainStyledAttributes(
                    _attrs,
                    R.styleable.CenteredRadioButton,
                    0, 0);

            try {
                mDrawable = a.getDrawable(R.styleable.CenteredRadioButton_drawable);
                mVerticalPadding = a.getDimension(R.styleable.CenteredRadioButton_badgeVerticalPadding, mVerticalPadding);
                mHorizontalPadding = a.getDimension(R.styleable.CenteredRadioButton_badgeHorizontalPadding, mHorizontalPadding);
                mCornerRadius = a.getDimension(R.styleable.CenteredRadioButton_badgeCornerRadius, mCornerRadius);
                mTextSize = a.getDimension(R.styleable.CenteredRadioButton_badgeTextSize, mTextSize);
            } finally {
                a.recycle();
            }
        }

        setButtonDrawable(android.R.color.transparent);
        initBgPaint(_context);
        initTextPaint();
        initTextHeight();

        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void initDefaultValues() {
        final float density = getResources().getDisplayMetrics().density;
        mVerticalPadding = density * DEFAULT_VERTICAL_PADDING;
        mHorizontalPadding = density * DEFAULT_HORIZONTAL_PADDING;
        mCornerRadius = density * DEFAULT_CORNER_RADIUS;
        mTextSize = density * DEFAULT_TEXT_SIZE;
    }

    private void initBgPaint(Context _context) {
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mBgPaint.setColor(ContextCompat.getColor(_context, R.color.colorRed));
        mBgPaint.setStyle(Paint.Style.FILL);
    }

    private void initTextPaint() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mTextPaint.setTextSize(getResources().getDimension(R.dimen.default_button_height));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    private void initTextHeight() {
        Rect badgeTextBounds = new Rect();
        mTextPaint.getTextBounds(MEASURING_SINGLE_LETTER, 0, 1, badgeTextBounds);
        mTextHeight = Math.abs(badgeTextBounds.top);
        mTextHalfHeight = mTextHeight >> 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDrawable != null) {
            mDrawable.setState(getDrawableState());
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int height = mDrawable.getIntrinsicHeight();

            int y = 0;

            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    y = getHeight() - height;
                    break;
                case Gravity.CENTER_VERTICAL:
                    y = (getHeight() - height) / 2;
                    break;
            }

            int buttonWidth = mDrawable.getIntrinsicWidth();
            int buttonLeft = (getWidth() - buttonWidth) / 2;
            mDrawable.setBounds(buttonLeft, y, buttonLeft+buttonWidth, y + height);
            mDrawable.draw(canvas);

            if (mBadgeCount > 0 && mBgRect != null) {
                canvas.drawRoundRect(mBgRect, mCornerRadius, mCornerRadius, mBgPaint);
                canvas.drawText(mBadgeCountString, mTextX, mTextY, mTextPaint);
            }
        }
    }

    @Override
    public void onGlobalLayout() {
        if (getHeight() != 0
                && getWidth() != 0) {
            // wait until view will have dimensions
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
            calculateTextPosition();
            if (mBgShouldBeReCalculated) {
                calculateBgRect();
                invalidate();
            }
        }
    }

    private void calculateTextPosition() {
        mTextX = (int) ((getMeasuredWidth() / 2) + (mDrawable.getIntrinsicWidth() / 2) -
                (mTextPaint.measureText(mBadgeCountString) / 2));
        mTextY = getMeasuredHeight() / 2 - mDrawable.getIntrinsicHeight() / 4 ;
    }

    private void calculateBgRect() {
        if (mTextX == Integer.MIN_VALUE && mTextY == Integer.MIN_VALUE && mBgTop == Integer.MIN_VALUE) {
            mBgShouldBeReCalculated = true;
            return;
        }
        float badgeWidth = mTextPaint.measureText(mBadgeCountString);
        mBgTop = mTextY - mTextHeight;
        mBgRect = new RectF(
                mTextX - mHorizontalPadding,
                mBgTop - mVerticalPadding,
                mTextX + badgeWidth + mHorizontalPadding,
                mTextY + mVerticalPadding
        );
    }

    public void setBadgeCount(int _badgeCount) {
        mBadgeCountString = String.valueOf(_badgeCount);
        calculateTextPosition();
        calculateBgRect();
        mBadgeCount = _badgeCount;
        invalidate();
    }

    public void resetBadgeCount() {
        mBadgeCountString = "";
        mBadgeCount = 0;
        invalidate();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.badgeCount = this.mBadgeCount;
        ss.badgeCountString = this.mBadgeCountString;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mBadgeCount = ss.badgeCount;
        this.mBadgeCountString = ss.badgeCountString;
        calculateBgRect();
        invalidate();
    }



    static class SavedState extends BaseSavedState {
        int badgeCount;
        String badgeCountString;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        private SavedState(Parcel in) {
            super(in);
            this.badgeCount = in.readInt();
            this.badgeCountString = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.badgeCount);
            out.writeString(this.badgeCountString);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}

