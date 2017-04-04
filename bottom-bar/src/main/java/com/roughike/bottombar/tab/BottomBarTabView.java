package com.roughike.bottombar.tab;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.roughike.bottombar.bar.BottomBarBehavior;
import com.roughike.bottombar.bar.BottomBarConfig;

/**
 * Created by joragu on 4/2/2017.
 */

public abstract class BottomBarTabView extends FrameLayout {
    @BottomBarBehavior
    private int behaviors;

    @ColorInt
    private int barColorWhenSelected;

    @IntRange(from = 0)
    private int tabIndex;

    public BottomBarTabView(@NonNull Context context) {
        this(context, null);
    }

    public BottomBarTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarTabView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomBarTabView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {

    }

    public void updateConfig(@NonNull BottomBarConfig config) {
        updateInactiveAlpha(config.inactiveTabAlpha);
        updateAlpha(config.activeTabAlpha);
        updateInactiveColor(config.inactiveTabColor);
        updateColor(config.activeTabColor);
        setBarColorWhenSelected(config.barColorWhenSelected);
        updateBadgeBackgroundColor(config.badgeBackgroundColor);

        if (config.badgeHidesWhenSelected) {
            hideBadgeWhenActive();
        } else {
            showBadgeWhenActive();
        }

        updateTextAppearance(config.titleTextAppearance);

        updateBehavior(config.behavior);

        if (config.titleTypeFace != null) {
            updateTypeface(config.titleTypeFace);
        }
    }

    private void updateBehavior(@BottomBarBehavior int behavior) {
        this.behaviors = behavior;
        if (isBehaviorActive(BottomBarBehavior.ICONS_ONLY) || isBehaviorActive(BottomBarBehavior.TABLET_MODE)) {
            displayIconOnly();
        } else {
            displayAllContent();
        }
    }

    public int getBarColorWhenSelected() {
        return barColorWhenSelected;
    }

    public void setBarColorWhenSelected(int barColorWhenSelected) {
        this.barColorWhenSelected = barColorWhenSelected;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.tabIndex = this.tabIndex;
        savedState.barColorWhenSelected = barColorWhenSelected;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.tabIndex = savedState.tabIndex;
        this.barColorWhenSelected = savedState.barColorWhenSelected;
    }

    protected boolean isBehaviorActive(@BottomBarBehavior int behavior) {
        return (behaviors & behavior) == behavior;
    }

    public abstract void onTabSelected(@AnimationType int animationType);

    public abstract void onTabDeselected(@AnimationType int animationType);

    public abstract void updateIcon(@DrawableRes int iconResId);

    public abstract void updateTitle(@NonNull String title);

    public abstract void displayAllContent();

    public abstract void displayIconOnly();

    public abstract void updateColor(@ColorInt int color);

    public abstract void updateAlpha(@FloatRange(from = 0, to = 1f) float alpha);

    public abstract void updateInactiveColor(@ColorInt int color);

    public abstract void updateInactiveAlpha(@FloatRange(from = 0, to = 1f) float alpha);

    public abstract void updateTextAppearance(@StyleRes int textAppearanceResId);

    public abstract void updateTypeface(@NonNull Typeface typeface);

    public abstract void updateBadgeCount(int badgeCount);

    public abstract void showBadgeWhenActive();

    public abstract void hideBadgeWhenActive();

    public abstract void updateBadgeBackgroundColor(@ColorInt int badgeBackgroundColor);

    private static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        int barColorWhenSelected;
        int tabIndex;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.tabIndex = in.readInt();
            this.barColorWhenSelected = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.tabIndex);
            out.writeInt(this.barColorWhenSelected);
        }
    }
}
