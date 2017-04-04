package com.roughike.bottombar.tab;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.roughike.bottombar.R;
import com.roughike.bottombar.bar.BottomBarBehavior;
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

    private int badgeCount;

    private boolean selectedTab;

    private AppCompatTextView titleView;

    private AppCompatImageView iconView;

    private AppCompatTextView badgeView;

    @Px
    private int sixDps;

    @Px
    private int eightDps;

    @Px
    private int sixteenDps;

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

        sixDps = MiscUtils.dpToPixel(context, 6);
        eightDps = MiscUtils.dpToPixel(context, 8);
        sixteenDps = MiscUtils.dpToPixel(context, 16);
    }

    @Override
    public void onTabSelected(@AnimationType int animationType) {
        selectedTab = true;
        setSelected(true);

        switch (animationType) {
            case AnimationType.NO_ANIMATION:
                setTitleScale(ACTIVE_TITLE_SCALE);
                setTopPadding(sixDps);
                setIconScale(ACTIVE_SHIFTING_TITLELESS_ICON_SCALE);
                updateColors();
                updateBadge();
                break;
            case AnimationType.WITH_ANIMATION:
                animateIcon(activeAlpha, ACTIVE_SHIFTING_TITLELESS_ICON_SCALE);
                animateTitle(sixDps, ACTIVE_TITLE_SCALE, activeAlpha);
                animateColors(inactiveColor, activeColor);
                if (badgeView.getVisibility() != GONE && badgeHidesWhenActive) {
                    animateBadgeHide();
                }
                break;
        }
    }

    @Override
    public void onTabDeselected(@AnimationType int animationType) {
        selectedTab = false;
        setSelected(false);

        final boolean isShifting = isBehaviorActive(BottomBarBehavior.SHIFTING);
        float titleScale = isShifting ? 0 : INACTIVE_FIXED_TITLE_SCALE;
        int iconPaddingTop = isShifting ? sixteenDps : eightDps;

        switch (animationType) {
            case AnimationType.NO_ANIMATION:
                setTitleScale(titleScale);
                setTopPadding(iconPaddingTop);
                setIconScale(INACTIVE_SHIFTING_TITLELESS_ICON_SCALE);
                updateColors();
                updateBadge();
                break;
            case AnimationType.WITH_ANIMATION:
                animateTitle(iconPaddingTop, titleScale, inActiveAlpha);
                animateIcon(inActiveAlpha, INACTIVE_SHIFTING_TITLELESS_ICON_SCALE);
                animateColors(activeColor, inactiveColor);
                if (badgeCount > 0 && badgeView.getVisibility() != VISIBLE) {
                    animateBadgeShow();
                }
                break;
        }
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
    public void updateBadgeCount(int badgeCount) {
        this.badgeCount = badgeCount;
        badgeView.setText(String.valueOf(badgeCount));
        badgeView.setVisibility(badgeHidesWhenActive && selectedTab ? GONE : VISIBLE);
    }

    @Override
    public void showBadgeWhenActive() {
        this.badgeHidesWhenActive = false;
        updateBadge();
    }

    @Override
    public void hideBadgeWhenActive() {
        this.badgeHidesWhenActive = true;
        updateBadge();
    }

    @Override
    public void updateBadgeBackgroundColor(@ColorInt int badgeBackgroundColor) {
        this.badgeBackgroundColor = badgeBackgroundColor;
        Drawable badgeDrawable = badgeView.getBackground();
        badgeDrawable.setColorFilter(badgeBackgroundColor, PorterDuff.Mode.DST_IN);
    }

    private void updateBadge() {
        updateBadgeCount(badgeCount);
    }

    private void animateBadgeShow() {
        ViewCompat.animate(badgeView)
                  .setDuration(ANIMATION_DURATION)
                  .alpha(1)
                  .scaleX(1)
                  .scaleY(1)
                  .start();
    }

    private void animateBadgeHide() {
        ViewCompat.animate(badgeView)
                  .setDuration(ANIMATION_DURATION)
                  .alpha(0)
                  .scaleX(0)
                  .scaleY(0)
                  .start();
    }

    private void animateTitle(int padding, float scale, float alpha) {
        if (isBehaviorActive(BottomBarBehavior.TABLET_MODE) || isBehaviorActive(BottomBarBehavior.ICONS_ONLY)) {
            return;
        }

        setTopPaddingAnimated(iconView.getPaddingTop(), padding);

        ViewPropertyAnimatorCompat titleAnimator = ViewCompat.animate(titleView)
                                                             .setDuration(ANIMATION_DURATION)
                                                             .scaleX(scale)
                                                             .scaleY(scale);
        titleAnimator.alpha(alpha);
        titleAnimator.start();
    }

    private void setTopPaddingAnimated(int start, int end) {
        if (isBehaviorActive(BottomBarBehavior.TABLET_MODE) || isBehaviorActive(BottomBarBehavior.ICONS_ONLY)) {
            return;
        }

        ValueAnimator paddingAnimator = ValueAnimator.ofInt(start, end);
        paddingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iconView.getLayoutParams();
                params.setMargins(params.leftMargin, (Integer) animation.getAnimatedValue(), params.rightMargin, params.bottomMargin);
                iconView.setLayoutParams(params);
            }
        });

        paddingAnimator.setDuration(ANIMATION_DURATION);
        paddingAnimator.start();
    }

    private void animateIconScale(float scale) {
        ViewCompat.animate(iconView)
                  .setDuration(ANIMATION_DURATION)
                  .scaleX(scale)
                  .scaleY(scale)
                  .start();
    }

    private void animateIcon(float alpha, float scale) {
        ViewCompat.animate(iconView)
                  .setDuration(ANIMATION_DURATION)
                  .alpha(alpha)
                  .start();

        if (isBehaviorActive(BottomBarBehavior.SHIFTING) || isBehaviorActive(BottomBarBehavior.ICONS_ONLY)) {
            animateIconScale(scale);
        }
    }

    private void animateColors(int previousColor, int color) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(previousColor, color);
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                updateColor((Integer) valueAnimator.getAnimatedValue());
            }
        });

        anim.setDuration(150);
        anim.start();
    }

    private void setTopPadding(int topPadding) {
        if (isBehaviorActive(BottomBarBehavior.TABLET_MODE) || isBehaviorActive(BottomBarBehavior.ICONS_ONLY)) {
            return;
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iconView.getLayoutParams();
        params.setMargins(params.leftMargin, topPadding, params.rightMargin, params.bottomMargin);
        iconView.setLayoutParams(params);
    }

    private void setTitleScale(float scale) {
        if (isBehaviorActive(BottomBarBehavior.TABLET_MODE) || isBehaviorActive(BottomBarBehavior.ICONS_ONLY)) {
            return;
        }

        ViewCompat.setScaleX(titleView, scale);
        ViewCompat.setScaleY(titleView, scale);
    }

    private void setIconScale(float scale) {
        if (!isBehaviorActive(BottomBarBehavior.SHIFTING) || !isBehaviorActive(BottomBarBehavior.ICONS_ONLY)) {
            return;
        }

        ViewCompat.setScaleX(iconView, scale);
        ViewCompat.setScaleY(iconView, scale);
    }
}