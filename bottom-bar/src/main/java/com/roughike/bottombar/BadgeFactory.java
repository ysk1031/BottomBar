package com.roughike.bottombar;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

/**
 * Created by joragu on 3/28/2017.
 */

public interface BadgeFactory {

    BottomBarBadge createBadge(@NonNull Context context, @ColorInt int badgeColor, @IdRes int tabId);

}