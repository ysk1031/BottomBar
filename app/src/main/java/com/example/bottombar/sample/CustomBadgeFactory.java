package com.example.bottombar.sample;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.roughike.bottombar.BadgeFactory;
import com.roughike.bottombar.BottomBarBadge;

/**
 * Created by joragu on 3/28/2017.
 */

public class CustomBadgeFactory implements BadgeFactory {
    @Override
    public BottomBarBadge createBadge(@NonNull Context context, @ColorInt int badgeColor, @IdRes int tabId) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(28, 28);

        CustomBadgeView badge = new CustomBadgeView(context);
        badge.setBadgeColor(badgeColor);
        badge.setLayoutParams(params);
        badge.setBackgroundResource(R.drawable.dot);

        return badge;
    }
}
