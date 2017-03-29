package com.roughike.bottombar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

/*
 * BottomBar library for Android
 * Copyright (c) 2016 Iiro Krankka (http://github.com/roughike).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class BottomBarBadgeView extends AppCompatTextView implements BottomBarBadge {
    private int count;
    private boolean isVisible = false;

    BottomBarBadgeView(Context context) {
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
            setText(String.valueOf(count));

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
        int innerPadding = MiscUtils.dpToPixel(getContext(), 1);
        ShapeDrawable backgroundCircle = BadgeCircle.make(innerPadding * 3, backgroundColor);
        setPadding(innerPadding, innerPadding, innerPadding, innerPadding);
        setBackgroundCompat(backgroundCircle);
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(background);
        } else {
            setBackgroundDrawable(background);
        }
    }
}
