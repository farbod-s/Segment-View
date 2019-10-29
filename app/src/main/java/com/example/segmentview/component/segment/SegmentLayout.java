package com.example.segmentview.component.segment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.example.segmentview.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

public class SegmentLayout extends LinearLayout {

    private SegmentChangeListener mListener;
    private List<SegmentView> mTabs;

    private boolean mIsDark;
    private int mSelectedPosition;
    private int mTabsCount;
    private int mAvailableWidth;
    private int mTabsMaxWidth;
    private int margin;

    public SegmentLayout(Context context) {
        super(context);
        init();
    }

    public SegmentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SegmentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTabs = new ArrayList<>();
        margin = AndroidUtils.dpToPixel(getContext(), AndroidUtils.LARGE_PADDING_DP);
        mAvailableWidth = AndroidUtils.getScreenWidthInPx(getContext()) - (2 * margin); /* side margins */

        int childrenCount = getChildCount();
        for (int i = 0; i < childrenCount; ++i) {
            addTabView((SegmentView) getChildAt(i));
        }
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
    }

    private void updateTabsInternal() {
        mAvailableWidth = AndroidUtils.getScreenWidthInPx(getContext()) - (2 * margin); /* side margins */

        for (int i = 0; i < mTabs.size(); ++i) {
            if (i == 0) {
                mTabs.get(i).setPosition(SegmentView.TAB_LEFT);
            } else if (i == mTabs.size() - 1) {
                mTabs.get(i).setPosition(SegmentView.TAB_RIGHT);
            } else {
                mTabs.get(i).setPosition(SegmentView.TAB_MIDDLE);
            }
            mTabs.get(i).changeSelection(mIsDark, mSelectedPosition == (int) mTabs.get(i).getTag());
            mTabs.get(i).setOnClickListener(view -> mListener.onTabChanged((int) view.getTag()));
        }
        requestLayout();
    }

    public void setClickListener(SegmentChangeListener listener) {
        mListener = listener;
    }

    public void addTabView(SegmentView tab) {
        tab.setTag(mTabs.size());
        addView(tab);

        mTabs.add(tab);
        mTabsCount = mTabs.size();

        tab.measure(0, 0);
        int measuredWidth = tab.getMeasuredWidth();
        if (measuredWidth > mTabsMaxWidth) {
            mTabsMaxWidth = measuredWidth;
        }
    }

    public void changeTab(int tag) {
        mSelectedPosition = tag;
        changeTabInternal();
    }

    private void changeTabInternal() {
        for (int i = 0; i < mTabs.size(); ++i) {
            mTabs.get(i).changeSelection(mIsDark, i == mSelectedPosition);
        }
    }

    public void updateTabs(boolean isDark) {
        mIsDark = isDark;
        updateTabsInternal();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mTabsCount <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        int tabWidth;
        if (mTabsMaxWidth * mTabsCount > mAvailableWidth) {
            tabWidth = mAvailableWidth / mTabsCount;
        } else {
            tabWidth = mTabsMaxWidth;
        }

        int parentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(tabWidth, MeasureSpec.EXACTLY);
        int widthUsed = tabWidth * (mTabsCount - 1);
        for (int i = 0; i < mTabsCount; ++i) {
            measureChildWithMargins(mTabs.get(i),
                    parentWidthMeasureSpec,
                    widthUsed,
                    heightMeasureSpec,
                    0);
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int tabWidth;
        if (mTabsMaxWidth * mTabsCount > mAvailableWidth) {
            tabWidth = mAvailableWidth / mTabsCount;
        } else {
            tabWidth = mTabsMaxWidth;
        }

        int x = (mAvailableWidth / 2) - ((tabWidth * mTabsCount) / 2) + margin;
        for (int i = 0; i < mTabsCount; ++i) {
            mTabs.get(i).layout(x, t + getPaddingTop(), x + tabWidth, b - getPaddingBottom());
            x += tabWidth;
        }
    }
}
