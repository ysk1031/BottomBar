package com.roughike.bottombar.tab;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.roughike.bottombar.tab.AnimationType.NO_ANIMATION;
import static com.roughike.bottombar.tab.AnimationType.WITH_ANIMATION;

/**
 * Created by joragu on 4/3/2017.
 */

@IntDef(value = {NO_ANIMATION, WITH_ANIMATION})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimationType {
    int NO_ANIMATION = 0;
    int WITH_ANIMATION = 1;
}