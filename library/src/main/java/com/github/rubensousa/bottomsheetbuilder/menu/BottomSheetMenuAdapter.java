package com.github.rubensousa.bottomsheetbuilder.menu;

import android.support.annotation.DrawableRes;
import android.view.MenuItem;

/**
 * Created by Baroti Csaba on 9/10/2016.
 */
public interface BottomSheetMenuAdapter {
    @DrawableRes int getDrawableRes(MenuItem subMenuItem);
}
