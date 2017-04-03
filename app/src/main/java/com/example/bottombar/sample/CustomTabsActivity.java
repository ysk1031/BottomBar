package com.example.bottombar.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.roughike.bottombar.bar.CustomBottomBar;

/**
 * Created by iiro on 7.6.2016.
 */
public class CustomTabsActivity extends Activity {
    private TextView messageView;
    private CustomBottomBar bottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_tabs);

        messageView = (TextView) findViewById(R.id.messageView);

        bottomBar = (CustomBottomBar) findViewById(R.id.bottomBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        BottomBarTabView tab = new BottomBarDefaultTabView(this);
//        tab.updateIcon(R.drawable.ic_favorites);
//        tab.updateTitle("Something");
        bottomBar.setTabs(R.xml.bottombar_tabs_three);
    }
}