package com.roughike.bottombar.tab;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.roughike.bottombar.tab.TabAttribute.ACTIVE_COLOR;
import static com.roughike.bottombar.tab.TabAttribute.BADGE_BACKGROUND_COLOR;
import static com.roughike.bottombar.tab.TabAttribute.BADGE_HIDES_WHEN_ACTIVE;
import static com.roughike.bottombar.tab.TabAttribute.BAR_COLOR_WHEN_SELECTED;
import static com.roughike.bottombar.tab.TabAttribute.ICON;
import static com.roughike.bottombar.tab.TabAttribute.ID;
import static com.roughike.bottombar.tab.TabAttribute.INACTIVE_COLOR;
import static com.roughike.bottombar.tab.TabAttribute.IS_TITLELESS;
import static com.roughike.bottombar.tab.TabAttribute.TITLE;

/**
 * Created by joragu on 4/2/2017.
 */

@Retention(RetentionPolicy.SOURCE)
@StringDef({ID, ICON, TITLE, INACTIVE_COLOR, ACTIVE_COLOR, BAR_COLOR_WHEN_SELECTED, BADGE_BACKGROUND_COLOR, BADGE_HIDES_WHEN_ACTIVE, IS_TITLELESS})
public @interface TabAttribute {
    String ID = "id";
    String ICON = "icon";
    String TITLE = "title";
    String INACTIVE_COLOR = "inActiveColor";
    String ACTIVE_COLOR = "activeColor";
    String BAR_COLOR_WHEN_SELECTED = "barColorWhenSelected";
    String BADGE_BACKGROUND_COLOR = "badgeBackgroundColor";
    String BADGE_HIDES_WHEN_ACTIVE = "badgeHidesWhenActive";
    String IS_TITLELESS = "iconOnly";
}