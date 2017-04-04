package com.roughike.bottombar.bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.annotation.XmlRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.R;
import com.roughike.bottombar.TabSelectionInterceptor;
import com.roughike.bottombar.tab.AnimationType;
import com.roughike.bottombar.tab.BottomBarTabView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joragu on 4/2/2017.
 */

public class CustomBottomBar extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {
    private static final float DEFAULT_INACTIVE_SHIFTING_TAB_ALPHA = 0.6f;

    private final List<BottomBarTabView> currentTabs = new ArrayList<>();

    private int currentTabIndex = 0;

    @BottomBarBehavior
    private int behaviors;
    private float inActiveTabAlpha;
    private float activeTabAlpha;
    private int inActiveTabColor;
    private int activeTabColor;
    private int badgeBackgroundColor;
    private boolean hideBadgeWhenActive;
    private boolean longPressHintsEnabled;
    private int titleTextAppearance;
    private Typeface titleTypeFace;
    private boolean showShadow;

    @Nullable
    private TabSelectionInterceptor tabSelectionInterceptor;

    @ColorInt
    private int defaultBackgroundColor = Color.WHITE;

    @ColorInt
    private int primaryColor;

    public CustomBottomBar(@NonNull Context context) {
        this(context, null);
    }

    public CustomBottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        primaryColor = MiscUtils.getColor(getContext(), R.attr.colorPrimary);
        int defaultInActiveColor = isShiftingMode() ? Color.WHITE : ContextCompat.getColor(context, R.color.bb_inActiveBottomBarItemColor);
        int defaultActiveColor = isShiftingMode() ? Color.WHITE : primaryColor;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBar, defStyleAttr, defStyleRes);
        int tabXmlResource;
        try {
            //noinspection WrongConstant
            behaviors = ta.getInteger(R.styleable.BottomBar_bb_behavior, BottomBarBehavior.NONE);
            tabXmlResource = ta.getResourceId(R.styleable.BottomBar_bb_tabXmlResource, 0);
            inActiveTabAlpha = ta.getFloat(R.styleable.BottomBar_bb_inActiveTabAlpha, isShiftingMode() ? DEFAULT_INACTIVE_SHIFTING_TAB_ALPHA : 1);
            activeTabAlpha = ta.getFloat(R.styleable.BottomBar_bb_activeTabAlpha, 1);
            longPressHintsEnabled = ta.getBoolean(R.styleable.BottomBar_bb_longPressHintsEnabled, true);
            inActiveTabColor = ta.getColor(R.styleable.BottomBar_bb_inActiveTabColor, defaultInActiveColor);
            activeTabColor = ta.getColor(R.styleable.BottomBar_bb_activeTabColor, defaultActiveColor);
            badgeBackgroundColor = ta.getColor(R.styleable.BottomBar_bb_badgeBackgroundColor, Color.RED);
            hideBadgeWhenActive = ta.getBoolean(R.styleable.BottomBar_bb_badgesHideWhenActive, true);
            titleTextAppearance = ta.getResourceId(R.styleable.BottomBar_bb_titleTextAppearance, 0);
            titleTypeFace = MiscUtils.getTypeFaceFromAsset(context, ta.getString(R.styleable.BottomBar_bb_titleTypeFace));
            showShadow = ta.getBoolean(R.styleable.BottomBar_bb_showShadow, true);
        } finally {
            ta.recycle();
        }

        determineInitialBackgroundColor();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            init21();
        }

        if (tabXmlResource != 0) {
            setTabs(tabXmlResource);
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void init21() {
        if (!showShadow) {
            setOutlineProvider(null);
        }

        float shadowElevation = getElevation();
        shadowElevation = shadowElevation > 0
                ? shadowElevation
                : getResources().getDimensionPixelSize(R.dimen.bb_default_elevation);
        setElevation(shadowElevation);
        setOutlineProvider(ViewOutlineProvider.BOUNDS);
    }

    private void determineInitialBackgroundColor() {
        if (isShiftingMode()) {
            defaultBackgroundColor = primaryColor;
        }

        Drawable userDefinedBackground = getBackground();

        boolean userHasDefinedBackgroundColor = userDefinedBackground != null
                && userDefinedBackground instanceof ColorDrawable;

        if (userHasDefinedBackgroundColor) {
            defaultBackgroundColor = ((ColorDrawable) userDefinedBackground).getColor();
            setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        if (child instanceof BottomBarTabView) {
            BottomBarTabView tab = (BottomBarTabView) child;
            tab.setOnClickListener(this);
            tab.setOnLongClickListener(this);
            currentTabs.add(tab);
        } else {
            throw new IllegalArgumentException("You can only add views that extend from BottomBarTabView objects to the BottomBar");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        final int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        final int numberOfTabs = currentTabs.isEmpty() ? 1 : currentTabs.size();
        final int tabWidth = width / numberOfTabs;
        final int tabWidthSpec = MeasureSpec.makeMeasureSpec(tabWidth, MeasureSpec.EXACTLY);
        for (BottomBarTabView tab : currentTabs) {
            if (tab.getVisibility() == GONE) {
                continue;
            }

            tab.measure(tabWidthSpec, heightMeasureSpec);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!changed) {
            return;
        }

        final int height = bottom - top;
        final int numberOfTabs = currentTabs.isEmpty() ? 1 : currentTabs.size();
        final int tabWidth = (right - left) / numberOfTabs;
        int tabStart = -tabWidth;
        for (BottomBarTabView tab : currentTabs) {
            tabStart += tabWidth;
            int tabEnd = tabStart + tabWidth;
            if (tab.getVisibility() == GONE) {
                continue;
            }

            tab.layout(tabStart, 0, tabEnd, height);
        }
    }

    /**
     * Set a listener that gets fired when the selected {@link BottomBarTab} is about to change.
     *
     * @param interceptor a listener for potentially interrupting changes in tab selection.
     */
    public void setTabSelectionInterceptor(@NonNull TabSelectionInterceptor interceptor) {
        tabSelectionInterceptor = interceptor;
    }

    /**
     * Removes the current {@link TabSelectionInterceptor} listener
     */
    public void removeOverrideTabSelectionListener() {
        tabSelectionInterceptor = null;
    }

    public void removeTabs() {
        for (BottomBarTabView tab : currentTabs) {
            removeView(tab);
        }
        currentTabs.clear();
    }

    /**
     * Set the items for the BottomBar from XML Resource.
     */
    public void setTabs(@XmlRes int xmlRes) {
        setTabs(xmlRes, null);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(defaultBackgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        super.dispatchDraw(canvas);
    }

    /**
     * Set the item for the BottomBar from XML Resource with a default configuration
     * for each tab.
     */
    public void setTabs(@XmlRes int xmlRes, @Nullable BottomBarConfig defaultTabConfig) {
        if (xmlRes == 0) {
            throw new RuntimeException("No items specified for the BottomBar!");
        }

        if (defaultTabConfig == null) {
            defaultTabConfig = getTabConfig();
        }

        removeTabs();
        TabParser parser = new TabParser(getContext(), defaultTabConfig);
        List<BottomBarTabView> tabs = parser.parseTabs(xmlRes);
        addTabs(tabs);
    }

    /**
     * Adds the {@code tabs} to the BottomBar
     */
    public void addTabs(@NonNull List<BottomBarTabView> tabs) {
        for (BottomBarTabView tab : tabs) {
            addTab(tab);
        }
    }

    /**
     * Adds the {@code tab} to the BottomBar
     */
    public void addTab(@NonNull BottomBarTabView tab) {
        addView(tab);
    }


    private BottomBarConfig getTabConfig() {
        return new BottomBarConfig.Builder().inActiveTabAlpha(inActiveTabAlpha)
                                            .activeTabAlpha(activeTabAlpha)
                                            .inActiveTabColor(inActiveTabColor)
                                            .activeTabColor(activeTabColor)
                                            .barColorWhenSelected(defaultBackgroundColor)
                                            .badgeBackgroundColor(badgeBackgroundColor)
                                            .hideBadgeWhenSelected(hideBadgeWhenActive)
                                            .titleTextAppearance(titleTextAppearance)
                                            .behavior(behaviors)
                                            .titleTypeFace(titleTypeFace)
                                            .build();
    }

    private boolean isShiftingMode() {
        return !isBehaviorActive(BottomBarBehavior.TABLET_MODE) && isBehaviorActive(BottomBarBehavior.SHIFTING);
    }

    private boolean isBehaviorActive(@BottomBarBehavior int behavior) {
        return (behaviors & behavior) == behavior;
    }

    @Override
    public void onClick(View v) {
        BottomBarTabView nextTab = (BottomBarTabView) v;
        BottomBarTabView currentTab = currentTabs.get(currentTabIndex);

        if (tabSelectionInterceptor != null && tabSelectionInterceptor.shouldInterceptTabSelection(currentTab.getId(), nextTab.getId())) {
            return;
        }

        currentTab.onTabDeselected(AnimationType.WITH_ANIMATION);
        nextTab.onTabSelected(AnimationType.WITH_ANIMATION);

//        updateSelectedTab(position);
//        shiftingMagic(oldTab, newTab, animate);
//        handleBackgroundColorChange(newTab, animate);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}

