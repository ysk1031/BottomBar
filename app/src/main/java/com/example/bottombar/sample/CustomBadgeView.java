package com.example.bottombar.sample;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.roughike.bottombar.BottomBarBadge;

/**
 * Created by joragu on 3/28/2017.
 */

public class CustomBadgeView extends View implements BottomBarBadge {
    private int count;
    private boolean isVisible = false;

    CustomBadgeView(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;

        if (count <= 0) {
            hide();
        } else {
            if (!isVisible()) {
                show();
            }
        }
    }

    @Override
    public void show() {
        isVisible = true;
        ViewCompat.animate(this)
                .setDuration(150)
                .alpha(1)
                .scaleX(1)
                .scaleY(1)
                .start();
    }

    @Override
    public void hide() {
        isVisible = false;
        ViewCompat.animate(this)
                .setDuration(150)
                .alpha(0)
                .scaleX(0)
                .scaleY(0)
                .start();
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setBadgeColor(@ColorInt int backgroundColor) {
    }
}