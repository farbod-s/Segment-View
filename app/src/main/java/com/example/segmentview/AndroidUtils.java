package com.example.segmentview;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.core.content.ContextCompat;

public class AndroidUtils {
    // Dimension
    public final static int SMALL_PADDING_DP = 8;
    public final static int LARGE_PADDING_DP = 16;

    private static DisplayMetrics DISPLAY_METRIC;

    private AndroidUtils() {
        // no implementation
    }

    public static void setBackgroundDrawable(View view, int drawableResId) {
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                view.setBackground(ContextCompat.getDrawable(view.getContext(), drawableResId));
            } else {
                view.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), drawableResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getScreenWidthInPx(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int dpToPixel(Context context, float dp) {
        float px = dp * (getDisplayMetrics(context).densityDpi / 160f);
        return Math.round(px);
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return context.getResources().getDisplayMetrics();
        } else if (DISPLAY_METRIC == null) {
            DISPLAY_METRIC = context.getResources().getDisplayMetrics();
        }
        return DISPLAY_METRIC;
    }
}
