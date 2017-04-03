package com.roughike.bottombar.bar;

import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

/**
 * Created by joragu on 4/2/2017.
 */

@SuppressWarnings("WeakerAccess")
public class BottomBarConfig {
    @FloatRange(from = 0, to = 1)
    public final float inactiveTabAlpha;

    @FloatRange(from = 0, to = 1)
    public final float activeTabAlpha;

    @ColorInt
    public final int inactiveTabColor;

    @ColorInt
    public final int activeTabColor;

    @ColorInt
    public final int barColorWhenSelected;

    @ColorInt
    public final int badgeBackgroundColor;

    @StyleRes
    public final int titleTextAppearance;

    @BottomBarBehavior
    public final int behavior;

    @Nullable
    public final Typeface titleTypeFace;

    public final boolean badgeHidesWhenSelected;

    public BottomBarConfig(Builder builder) {
        this.inactiveTabAlpha = builder.inActiveTabAlpha;
        this.activeTabAlpha = builder.activeTabAlpha;
        this.inactiveTabColor = builder.inActiveTabColor;
        this.activeTabColor = builder.activeTabColor;
        this.barColorWhenSelected = builder.barColorWhenSelected;
        this.badgeBackgroundColor = builder.badgeBackgroundColor;
        this.badgeHidesWhenSelected = builder.hidesBadgeWhenSelected;
        this.titleTextAppearance = builder.titleTextAppearance;
        this.behavior = builder.behavior;
        this.titleTypeFace = builder.titleTypeFace;
    }

    @SuppressWarnings("unused")
    public static class Builder {
        private float inActiveTabAlpha;
        private float activeTabAlpha;
        private int inActiveTabColor;
        private int activeTabColor;
        private int barColorWhenSelected;
        private int badgeBackgroundColor;
        private boolean hidesBadgeWhenSelected = true;
        private int titleTextAppearance;
        private int behavior;
        private Typeface titleTypeFace;

        public Builder inActiveTabAlpha(float alpha) {
            this.inActiveTabAlpha = alpha;
            return this;
        }

        public Builder activeTabAlpha(float alpha) {
            this.activeTabAlpha = alpha;
            return this;
        }

        public Builder inActiveTabColor(@ColorInt int color) {
            this.inActiveTabColor = color;
            return this;
        }

        public Builder activeTabColor(@ColorInt int color) {
            this.activeTabColor = color;
            return this;
        }

        public Builder barColorWhenSelected(@ColorInt int color) {
            this.barColorWhenSelected = color;
            return this;
        }

        public Builder badgeBackgroundColor(@ColorInt int color) {
            this.badgeBackgroundColor = color;
            return this;
        }

        public Builder hideBadgeWhenSelected(boolean hide) {
            this.hidesBadgeWhenSelected = hide;
            return this;
        }

        public Builder titleTextAppearance(@StyleRes int titleTextAppearance) {
            this.titleTextAppearance = titleTextAppearance;
            return this;
        }

        public Builder behavior(@BottomBarBehavior int behavior) {
            this.behavior = behavior;
            return this;
        }

        public Builder titleTypeFace(Typeface titleTypeFace) {
            this.titleTypeFace = titleTypeFace;
            return this;
        }

        public BottomBarConfig build() {
            return new BottomBarConfig(this);
        }
    }
}
