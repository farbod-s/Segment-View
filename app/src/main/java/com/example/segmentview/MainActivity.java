package com.example.segmentview;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.segmentview.component.adapter.SwipeListAdapter;
import com.example.segmentview.component.fragment.BaseFragment;
import com.example.segmentview.component.segment.SegmentLayout;
import com.example.segmentview.component.segment.SegmentView;
import com.example.segmentview.component.viewpager.LockableViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAB_ONE = "Tab One";
    private static final String TAB_TWO = "Tab Two";
    private static final String TAB_THREE = "Tab Three";

    private int defaultTab = 0;
    private boolean tabChangeRequested = false;

    private LockableViewPager viewPager;
    private SegmentLayout tabsView;
    private Animation fadeInAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        BaseFragment fragmentOne = new BaseFragment();
        fragmentOne.setArguments(BaseFragment.serializeBundle(TAB_ONE));
        fragments.add(fragmentOne);
        titles.add(TAB_ONE);

        BaseFragment fragmentTwo = new BaseFragment();
        fragmentTwo.setArguments(BaseFragment.serializeBundle(TAB_TWO));
        fragments.add(fragmentTwo);
        titles.add(TAB_TWO);

        BaseFragment fragmentThree = new BaseFragment();
        fragmentThree.setArguments(BaseFragment.serializeBundle(TAB_THREE));
        fragments.add(fragmentThree);
        titles.add(TAB_THREE);

        // adapter
        SwipeListAdapter adapter = new SwipeListAdapter(getSupportFragmentManager(), fragments, titles);

        // transformer
        FadePageTransformer pageTransformer = new FadePageTransformer();

        // animations
        fadeInAnimation = new AlphaAnimation(0, 1);
        fadeInAnimation.setInterpolator(new DecelerateInterpolator());
        fadeInAnimation.setDuration(100);
        fadeInAnimation.setStartOffset(100);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(defaultTab);
        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(false, pageTransformer);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // nothing!
            }

            @Override
            public void onPageSelected(int position) {
                defaultTab = position;
                tabsView.changeTab(defaultTab);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // nothing!
            }
        });

        View tabs = getTabsView();
        LinearLayout layout = findViewById(R.id.linearLayout);
        layout.addView(tabs);

        tabsView.changeTab(defaultTab);
        tabsView.updateTabs(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        tabChangeRequested = false;
    }

    private View getTabsView() {
        SegmentView mLeftTab = new SegmentView(this);
        mLeftTab.setText(TAB_ONE);

        SegmentView mMiddleTab = new SegmentView(this);
        mMiddleTab.setText(TAB_TWO);

        SegmentView mRightTab = new SegmentView(this);
        mRightTab.setText(TAB_THREE);

        int smallDp = AndroidUtils.dpToPixel(this, AndroidUtils.SMALL_PADDING_DP);
        tabsView = new SegmentLayout(this);
        tabsView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tabsView.setPadding(0, smallDp, 0, smallDp);
        tabsView.addTabView(mLeftTab);
        tabsView.addTabView(mMiddleTab);
        tabsView.addTabView(mRightTab);
        tabsView.setClickListener(position -> {
            if (defaultTab == position) {
                // do nothing!
            } else {
                defaultTab = position;
                changeTab(defaultTab);
            }
        });

        return tabsView;
    }

    private void changeTab(int selectedTab) {
        tabChangeRequested = true;
        viewPager.setCurrentItem(selectedTab, false);
    }

    private class FadePageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position <= -1 || position >= 1) {
                // page is not an immediate sibling, just make transparent
                page.setAlpha(0);
            } else if (position == 0) {
                // page is active, make fully visible
                page.setAlpha(1);
                if (tabChangeRequested) {
                    page.startAnimation(fadeInAnimation);
                    tabChangeRequested = false;
                }
            }
        }
    }
}
