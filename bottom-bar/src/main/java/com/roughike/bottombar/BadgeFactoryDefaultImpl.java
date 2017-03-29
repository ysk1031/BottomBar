package com.roughike.bottombar;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;

/**
 * Created by joragu on 3/28/2017.
 */

class BadgeFactoryDefaultImpl implements BadgeFactory {
    @Override
    public BottomBarBadge createBadge(@NonNull Context context, @ColorInt int badgeColor, @IdRes int tabId) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        BottomBarBadgeView badge = new BottomBarBadgeView(context);
        badge.setBadgeColor(badgeColor);
        badge.setLayoutParams(params);
        badge.setGravity(Gravity.CENTER);

        MiscUtils.setTextAppearance(badge, R.style.BB_BottomBarBadge_Text);
        return badge;
    }
}
