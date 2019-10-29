package com.example.segmentview.component.segment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.segmentview.R;
import com.example.segmentview.AndroidUtils;

public class SegmentView extends TextView {
    public static final int TAB_LEFT = -1;
    public static final int TAB_MIDDLE = 0;
    public static final int TAB_RIGHT = 1;

    private float mTextSize;
    private float mMaxTextSize = 14;
    private float mMinTextSize = 8;
    private float mSpacingMulti = 1.0f;
    private float mSpacingAdd = 0.0f;
    private boolean mNeedsResize = false;

    private Rect mBoundRect;
    private int mPosition = 0;
    private boolean mSelected = false;

    public SegmentView(Context context) {
        super(context);
        init();
    }

    public SegmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SegmentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mBoundRect = new Rect();
        mMinTextSize = getResources().getDimension(R.dimen.text_size_micro);
        mMaxTextSize = getResources().getDimension(R.dimen.text_size_small);

        setClickable(true);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_small));
        setLineSpacing(mSpacingAdd, mSpacingMulti);
        setPadding((int) getResources().getDimension(R.dimen.micro_padding),
                0,
                (int) getResources().getDimension(R.dimen.micro_padding),
                0);
    }

    private void syncColors(boolean isDark, boolean selected) {
        switch (mPosition) {
            case TAB_LEFT:
                if (isDark) {
                    if (selected) {
                        setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        AndroidUtils.setBackgroundDrawable(this, R.drawable.design_tab_left_select_dark);
                    } else {
                        setTextColor(ContextCompat.getColor(getContext(), R.color.text_light_secondary));
                        AndroidUtils.setBackgroundDrawable(this, R.drawable.design_tab_left_unselect_dark);
                    }
                } else {
                    if (selected) {
                        setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        AndroidUtils.setBackgroundDrawable(this, R.drawable.design_tab_left_select_light);
                    } else {
                        setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        AndroidUtils.setBackgroundDrawable(this, R.drawable.design_tab_left_unselect_light);
                    }
                }
                break;

            case TAB_MIDDLE:
                if (isDark) {
                    if (selected) {
                        setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        AndroidUtils.setBackgroundDrawable(this, R.drawable.design_tab_middle_select_dark);
                    } else {
                        setTextColor(ContextCompat.getColor(getContext(), R.color.text_light_secondary));
                        AndroidUtils.setBackgroundDrawable(this, R.drawable.design_tab_middle_unselect_dark);
                    }
                } else {
                    if (selected) {
                        setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        AndroidUtils.setBackgroundDrawable(this, R.drawable.design_tab_middle_select_light);
                    } else {
                        setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        AndroidUtils.setBackgroundDrawable(this, R.drawable.design_tab_middle_unselect_light);
                    }
                }
                break;

            case TAB_RIGHT:
                if (isDark) {
                    if (selected) {
                        setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        AndroidUtils.setBackgroundDrawable(this, R.drawable.design_tab_right_select_dark);
                    } else {
                        setTextColor(ContextCompat.getColor(getContext(), R.color.text_light_secondary));
                        AndroidUtils.setBackgroundDrawable(this, R.drawable.design_tab_right_unselect_dark);
                    }
                } else {
                    if (selected) {
                        setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        AndroidUtils.setBackgroundDrawable(this, R.drawable.design_tab_right_select_light);
                    } else {
                        setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        AndroidUtils.setBackgroundDrawable(this, R.drawable.design_tab_right_unselect_light);
                    }
                }
                break;
        }
    }

    public void changeSelection(boolean isDark, boolean selected) {
        mSelected = selected;
        syncColors(isDark, mSelected);
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        mNeedsResize = true;
        resetTextSize();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            mNeedsResize = true;
        }
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        mTextSize = getTextSize();
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        mTextSize = getTextSize();
    }

    @Override
    public void setLineSpacing(float add, float multi) {
        super.setLineSpacing(add, multi);
        mSpacingMulti = multi;
        mSpacingAdd = add;
    }

    public void setMaxTextSize(float maxTextSize) {
        mMaxTextSize = maxTextSize;
        requestLayout();
        invalidate();
    }

    public void setMinTextSize(float minTextSize) {
        mMinTextSize = minTextSize;
        requestLayout();
        invalidate();
    }

    public void resetTextSize() {
        if (mTextSize > 0) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mMaxTextSize = mTextSize;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed || mNeedsResize) {
            int widthLimit = (right - left) - getCompoundPaddingLeft() - getCompoundPaddingRight();
            int heightLimit = (bottom - top) - getCompoundPaddingBottom() - getCompoundPaddingTop();
            resizeText(widthLimit, heightLimit);
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.getClipBounds(mBoundRect);
        int canvasWidth = mBoundRect.width();
        int canvasHeight = mBoundRect.height();

        canvas.save();
        canvas.translate(mBoundRect.left, mBoundRect.top);

        getPaint().setColor(getTextColors().getDefaultColor());
        getPaint().setTextAlign(Paint.Align.CENTER);
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), mBoundRect);
        float x = (canvasWidth / 2f);
        float y = (canvasHeight / 2f) + (mBoundRect.height() / 2f) - mBoundRect.bottom;
        canvas.drawText(getText().toString(), x, y, getPaint());

        canvas.restore();
    }

    private void resizeText(int width, int height) {
        CharSequence text = getText();
        // Do not resize if the view does not have dimensions or there is no text
        if (text == null || text.length() == 0 || height <= 0 || width <= 0 || mTextSize == 0) {
            return;
        }

        if (getTransformationMethod() != null) {
            text = getTransformationMethod().getTransformation(text, this);
        }

        // Get the text view's paint object
        TextPaint textPaint = getPaint();

        // If there is a max text size set, use the lesser of that and the default text size
        float targetTextSize = mMaxTextSize > 0 ? Math.min(mTextSize, mMaxTextSize) : mTextSize;

        // Get the required text height
        int textHeight = getTextHeight(text, textPaint, width, targetTextSize);

        // Until we either fit within our text view or we had reached our min text size, incrementally try smaller sizes
        while (textHeight > height && targetTextSize > mMinTextSize) {
            targetTextSize = Math.max(targetTextSize - 2, mMinTextSize);
            textHeight = getTextHeight(text, textPaint, width, targetTextSize);
        }

        // Some devices try to auto adjust line spacing, so force default line spacing
        // and invalidate the layout as a side effect
        setTextSize(TypedValue.COMPLEX_UNIT_PX, targetTextSize);
        setLineSpacing(mSpacingAdd, mSpacingMulti);

        // Reset force resize flag
        mNeedsResize = false;
    }

    // Set the text size of the text paint object and use a static layout to render text off screen before measuring
    private int getTextHeight(CharSequence source, TextPaint paint, int width, float textSize) {
        // modified: make a copy of the original TextPaint object for measuring
        // (apparently the object gets modified while measuring, see also the
        // docs for TextView.getPaint() (which states to access it read-only)
        TextPaint paintCopy = new TextPaint(paint);
        // Update the text paint object
        paintCopy.setTextSize(textSize);
        // Measure using a static layout
        StaticLayout layout = new StaticLayout(source,
                paintCopy,
                width,
                Layout.Alignment.ALIGN_CENTER,
                mSpacingMulti,
                mSpacingAdd,
                true);
        return layout.getHeight();
    }
}
