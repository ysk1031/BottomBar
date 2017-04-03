package com.roughike.bottombar.tab;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.roughike.bottombar.R;
import com.roughike.bottombar.bar.MiscUtils;

/**
 * Created by joragu on 4/2/2017.
 */

public class BottomBarDefaultTabView extends BottomBarTabView {
    private static final long ANIMATION_DURATION = 150;
    private static final float ACTIVE_TITLE_SCALE = 1;
    private static final float INACTIVE_FIXED_TITLE_SCALE = 0.86f;
    private static final float ACTIVE_SHIFTING_TITLELESS_ICON_SCALE = 1.24f;
    private static final float INACTIVE_SHIFTING_TITLELESS_ICON_SCALE = 1f;

    @FloatRange(from = 0, to = 1f)
    private float inActiveAlpha;

    @FloatRange(from = 0, to = 1f)
    private float activeAlpha;

    @ColorInt
    private int inactiveColor;

    @ColorInt
    private int activeColor;

    @ColorInt
    private int barColorWhenSelected;

    @ColorInt
    private int badgeBackgroundColor;

    @StyleRes
    private int textAppearanceResId;

    @DrawableRes
    private int iconResId;

    @NonNull
    private String title;

    private boolean iconOnly;

    private boolean badgeHidesWhenActive;

    @Nullable
    private Typeface titleTypeFace;

    private boolean selectedTab;

    private AppCompatTextView titleView;

    private AppCompatImageView iconView;

    private AppCompatTextView badgeView;

    public BottomBarDefaultTabView(@NonNull Context context) {
        this(context, null);
    }

    public BottomBarDefaultTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarDefaultTabView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomBarDefaultTabView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.bb_bottom_bar_tab, this, true);

        titleView = (AppCompatTextView) findViewById(R.id.bb_bottom_bar_tab_title);
        iconView = (AppCompatImageView) findViewById(R.id.bb_bottom_bar_tab_icon);
        badgeView = (AppCompatTextView) findViewById(R.id.bb_bottom_bar_tab_badge);
    }

    @Override
    public void updateIcon(@DrawableRes int iconResId) {
        this.iconResId = iconResId;
        iconView.setImageDrawable(ContextCompat.getDrawable(getContext(), iconResId));
    }

    @Override
    public void updateTitle(@NonNull String title) {
        this.title = title;
        titleView.setText(title);
    }

    @Override
    public void displayAllContent() {
        titleView.setVisibility(VISIBLE);
    }

    @Override
    public void displayIconOnly() {
        titleView.setVisibility(GONE);
    }

    @Override
    public void updateColor(@ColorInt int color) {
        activeColor = color;
        updateColors();
    }

    @Override
    public void updateInactiveColor(@ColorInt int color) {
        inactiveColor = color;
        updateColors();
    }

    @Override
    public void updateAlpha(@FloatRange(from = 0, to = 1f) float alpha) {
        activeAlpha = alpha;
        updateColors();
    }

    @Override
    public void updateInactiveAlpha(@FloatRange(from = 0, to = 1f) float alpha) {
        inActiveAlpha = alpha;
        updateColors();
    }

    private void updateColors() {
        if (selectedTab) {
            final int colorActiveWithAlpha = MiscUtils.applyAlpahToColor(activeColor, activeAlpha);
            iconView.setColorFilter(colorActiveWithAlpha);
            titleView.setTextColor(colorActiveWithAlpha);
        } else {
            final int colorInactiveWithAlpha = MiscUtils.applyAlpahToColor(inactiveColor, activeAlpha);
            iconView.setColorFilter(colorInactiveWithAlpha);
            titleView.setTextColor(colorInactiveWithAlpha);
        }
    }

    @Override
    public void updateTextAppearance(@StyleRes int textAppearanceResId) {
        if (titleView == null || textAppearanceResId == 0 || textAppearanceResId == this.textAppearanceResId) {
            return;
        }

        this.textAppearanceResId = textAppearanceResId;
        titleView.setTextAppearance(getContext(), textAppearanceResId);
        titleView.setTag(R.id.bb_bottom_bar_appearance_id, textAppearanceResId);
    }

    @Override
    public void updateTypeface(@NonNull Typeface typeface) {
        titleView.setTypeface(titleTypeFace);
    }

    @Override
    public void showBadgeWhenActive() {

    }

    @Override
    public void hideBadgeWhenActive() {

    }

    @Override
    public void updateBadgeBackgroundColor(@ColorInt int badgeBackgroundColor) {
    }
}