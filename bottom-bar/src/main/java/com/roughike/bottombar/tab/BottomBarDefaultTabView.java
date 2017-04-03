package com.roughike.bottombar.tab;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.roughike.bottombar.R;

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

    @IdRes
    private int titleTextAppearanceResId;

    @DrawableRes
    private int iconResId;

    @NonNull
    private String title;

    private boolean iconOnly;

    private boolean badgeHidesWhenActive;

    private TextView titleView;

    private ImageView iconView;

    private TextView badgeView;

    @Nullable
    private Typeface titleTypeFace;

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

        titleView = (TextView) findViewById(R.id.bb_bottom_bar_tab_title);
        iconView = (ImageView) findViewById(R.id.bb_bottom_bar_tab_icon);
        badgeView = (TextView) findViewById(R.id.bb_bottom_bar_tab_badge);
    }

    @Override
    public void updateIcon(@DrawableRes int iconResId) {
        iconView.setImageDrawable(ContextCompat.getDrawable(getContext(), iconResId));
    }

    @Override
    public void updateTitle(@NonNull String title) {
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

    }

    @Override
    public void updateAlpha(@FloatRange(from = 0, to = 1f) float alpha) {

    }

    @Override
    public void updateInactiveColor(@ColorInt int color) {

    }

    @Override
    public void updateInactiveAlpha(@FloatRange(from = 0, to = 1f) float alpha) {

    }

    @Override
    public void updateTextAppearance(@IdRes int textAppearanceResId) {

    }

    @Override
    public void updateTypeface(@NonNull Typeface typeface) {

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