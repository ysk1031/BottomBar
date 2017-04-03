package com.roughike.bottombar.bar;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.roughike.bottombar.bar.BottomBarBehavior.DRAW_UNDER_NAV;
import static com.roughike.bottombar.bar.BottomBarBehavior.ICONS_ONLY;
import static com.roughike.bottombar.bar.BottomBarBehavior.NONE;
import static com.roughike.bottombar.bar.BottomBarBehavior.SHIFTING;
import static com.roughike.bottombar.bar.BottomBarBehavior.SHY;
import static com.roughike.bottombar.bar.BottomBarBehavior.TABLET_MODE;

/**
 * Created by joragu on 4/2/2017.
 */

@SuppressWarnings("PointlessBitwiseExpression")
@IntDef(value = {NONE, SHIFTING, SHY, DRAW_UNDER_NAV, ICONS_ONLY, TABLET_MODE}, flag = true)
@Retention(RetentionPolicy.SOURCE)
public @interface BottomBarBehavior {
    int NONE = 0;
    int SHIFTING = 1 << 0;
    int SHY = 1 << 1;
    int DRAW_UNDER_NAV = 1 << 2;
    int ICONS_ONLY = 1 << 3;
    int TABLET_MODE = 1 << 4;
}
