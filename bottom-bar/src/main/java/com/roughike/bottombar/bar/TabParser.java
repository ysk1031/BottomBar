package com.roughike.bottombar.bar;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.XmlRes;
import android.support.v4.content.ContextCompat;

import com.roughike.bottombar.tab.BottomBarDefaultTabView;
import com.roughike.bottombar.tab.BottomBarTabView;
import com.roughike.bottombar.tab.TabAttribute;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by iiro on 21.7.2016.
 * <p>
 * BottomBar library for Android
 * Copyright (c) 2016 Iiro Krankka (http://github.com/roughike).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class TabParser {
    private static final String TAB_TAG = "tab";
    private static final int AVG_NUMBER_OF_TABS = 5;
    private static final int COLOR_NOT_SET = -1;
    private static final int RESOURCE_NOT_FOUND = 0;

    @NonNull
    private final Context context;

    @NonNull
    private final BottomBarConfig defaultTabConfig;

    TabParser(@NonNull Context context, @NonNull BottomBarConfig defaultTabConfig) {
        this.context = context;
        this.defaultTabConfig = defaultTabConfig;
    }

    @CheckResult
    @NonNull
    public List<BottomBarTabView> parseTabs(@XmlRes int tabsXmlResId) {
        final XmlResourceParser parser = context.getResources()
                                                .getXml(tabsXmlResId);

        List<BottomBarTabView> tabs = new ArrayList<>(AVG_NUMBER_OF_TABS);
        try {
            int eventType;
            do {
                eventType = parser.next();
                if (eventType == XmlResourceParser.START_TAG && TAB_TAG.equals(parser.getName())) {
                    BottomBarTabView bottomBarTab = parseNewTab(parser, tabs.size());
                    tabs.add(bottomBarTab);
                }
            } while (eventType != XmlResourceParser.END_DOCUMENT);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            throw new TabParserException();
        }

        return tabs;
    }

    @NonNull
    private BottomBarTabView parseNewTab(@NonNull XmlResourceParser parser, @IntRange(from = 0) int containerPosition) {
        BottomBarTabView workingTab = tabWithDefaults();
        workingTab.setTabIndex(containerPosition);

        final int numberOfAttributes = parser.getAttributeCount();
        for (int i = 0; i < numberOfAttributes; i++) {
            @TabAttribute
            String attrName = parser.getAttributeName(i);
            switch (attrName) {
                case TabAttribute.ID:
                    workingTab.setId(parser.getIdAttributeResourceValue(i));
                    break;
                case TabAttribute.ICON:
                    workingTab.updateIcon(parser.getAttributeResourceValue(i, RESOURCE_NOT_FOUND));
                    break;
                case TabAttribute.TITLE:
                    workingTab.updateTitle(getTitleValue(parser, i));
                    break;
                case TabAttribute.INACTIVE_COLOR:
                    int inactiveColor = getColorValue(parser, i);
                    if (inactiveColor == COLOR_NOT_SET) continue;
                    workingTab.updateInactiveColor(inactiveColor);
                    break;
                case TabAttribute.ACTIVE_COLOR:
                    int activeColor = getColorValue(parser, i);
                    if (activeColor == COLOR_NOT_SET) continue;
                    workingTab.updateColor(activeColor);
                    break;
                case TabAttribute.BAR_COLOR_WHEN_SELECTED:
                    int barColorWhenSelected = getColorValue(parser, i);
                    if (barColorWhenSelected == COLOR_NOT_SET) continue;
                    workingTab.setBarColorWhenSelected(barColorWhenSelected);
                    break;
                case TabAttribute.BADGE_BACKGROUND_COLOR:
                    int badgeBackgroundColor = getColorValue(parser, i);
                    if (badgeBackgroundColor == COLOR_NOT_SET) continue;
                    workingTab.updateBadgeBackgroundColor(badgeBackgroundColor);
                    break;
                case TabAttribute.BADGE_HIDES_WHEN_ACTIVE:
                    boolean badgeHidesWhenActive = parser.getAttributeBooleanValue(i, true);
                    if (badgeHidesWhenActive) {
                        workingTab.hideBadgeWhenActive();
                    } else {
                        workingTab.showBadgeWhenActive();
                    }
                    break;
                case TabAttribute.IS_TITLELESS:
                    boolean iconOnly = parser.getAttributeBooleanValue(i, false);
                    if (iconOnly) {
                        workingTab.displayIconOnly();
                    } else {
                        workingTab.displayAllContent();
                    }
                    break;
            }
        }

        return workingTab;
    }

    @NonNull
    private BottomBarTabView tabWithDefaults() {
        BottomBarTabView tab = new BottomBarDefaultTabView(context);
        tab.updateConfig(defaultTabConfig);
        return tab;
    }

    @NonNull
    private String getTitleValue(@NonNull XmlResourceParser parser, @IntRange(from = 0) int attrIndex) {
        int titleResource = parser.getAttributeResourceValue(attrIndex, 0);
        return titleResource == RESOURCE_NOT_FOUND
                ? parser.getAttributeValue(attrIndex) : context.getString(titleResource);
    }

    @ColorInt
    private int getColorValue(@NonNull XmlResourceParser parser, @IntRange(from = 0) int attrIndex) {
        int colorResource = parser.getAttributeResourceValue(attrIndex, 0);

        if (colorResource == RESOURCE_NOT_FOUND) {
            try {
                String colorValue = parser.getAttributeValue(attrIndex);
                return Color.parseColor(colorValue);
            } catch (Exception ignored) {
                return COLOR_NOT_SET;
            }
        }

        return ContextCompat.getColor(context, colorResource);
    }

    @SuppressWarnings("WeakerAccess")
    public static class TabParserException extends RuntimeException {
        // This class is just to be able to have a type of Runtime Exception that will make it clear where the error originated.
    }
}
