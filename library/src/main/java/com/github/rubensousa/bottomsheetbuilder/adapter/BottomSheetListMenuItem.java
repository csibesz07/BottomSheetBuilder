/*
 * Copyright 2016 Rúben Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.rubensousa.bottomsheetbuilder.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem;

import com.github.rubensousa.bottomsheetbuilder.menu.BottomSheetMenuItem;


class BottomSheetListMenuItem implements BottomSheetItem {

    private BottomSheetColors mColors;
    private Drawable mIcon;
    private int mItemID;
    private String mTitle;

    //WILL NOT HOLD REFERENCE TO BottomSheetMenuItem

    public BottomSheetListMenuItem(BottomSheetMenuItem item, BottomSheetColors colors) {
        mColors=colors;
        mIcon=item.getIcon();
        mItemID=item.getItemId();
        mTitle=item.getTitle();
    }

    public Drawable getIcon() {
        if (mColors.getIconTintColor()!=-1) {
            mIcon = DrawableCompat.wrap(mIcon);
            DrawableCompat.setTint(mIcon, mColors.getIconTintColor());
        }
        return mIcon;
    }

    Drawable getBackground(Context context) {
        return mColors.getBackground(context);
    }

    public boolean hasBackgroundRes() {
        return mColors.hasItemBackgroundRes();
    }

    @DrawableRes
    public int getBackgroundRes() {
        return mColors.getItemBackgroundRes();
    }

    public int getId() {
        return mItemID;
    }

    @ColorInt
    public int getTextColor() {
        return mColors.getTitleTextColor();
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}
