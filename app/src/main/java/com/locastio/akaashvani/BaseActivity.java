package com.locastio.akaashvani;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * @param isShow
     */
    public void isShowActionBarHomeButton(boolean isShow) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(isShow);
        actionBar.setDisplayHomeAsUpEnabled(isShow);
    }

    /**
     * set action bar bg as a transparent color..
     */
    public void setActionTransparentColor() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    protected void setActionBarIcon(int iconRes) {
        if (toolbar != null) {
            toolbar.setNavigationIcon(iconRes);
        }
    }


    /**
     * set custom action bar color
     *
     * @param colorCode
     */
    public void setCustomActionBarColor(String colorCode) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(colorCode)));
    }

    /**
     * set action bar title
     */
    public void setActionBarTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar == null) {
                return;
            }
            actionBar.setTitle(title);
        } else {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar == null) {
                return;
            }
            actionBar.setTitle("");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
