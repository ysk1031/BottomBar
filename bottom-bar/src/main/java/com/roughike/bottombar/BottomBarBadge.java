package com.roughike.bottombar;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by joragu on 3/28/2017.
 */

public interface BottomBarBadge {
    /**
     * Get the current count for this Badge.
     *
     * @return current count for the Badge.
     */
    int getCount();

    /**
     * Set the unread / new item / whatever count for this Badge.
     *
     * @param count the value this Badge should show.
     */
    void setCount(int count);

    /**
     * Changes the color of the badge to match the {@code backgroundColor}
     *
     * @param backgroundColor the color to use for the badge
     */
    void setBadgeColor(@ColorInt int backgroundColor);

    /**
     * This returns the view representation of this badge
     *
     * @return the view representing the badge
     */
    @NonNull
    View getView();

    /**
     * Use this method to know the current visibility of the badge
     *
     * @return true if the badge is visible, false otherwise
     */
    boolean isVisible();

    /**
     * Shows the badge with a neat little scale animation.
     */
    void show();

    /**
     * Hides the badge with a neat little scale animation.
     */
    void hide();
}
