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

package com.github.rubensousa.bottomsheetbuilder;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.MenuRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetAdapter;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetColors;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetView;
import com.github.rubensousa.bottomsheetbuilder.menu.BottomSheetMenu;
import com.github.rubensousa.bottomsheetbuilder.menu.BottomSheetMenuAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public final class BottomSheetBuilder {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_LIST,MODE_GRID})
    public @interface BottomSheetMode {}
    public static final int MODE_LIST = 0;
    public static final int MODE_GRID = 1;

    @StyleRes
    private int mTheme;

    private BottomSheetColors mColors;

    private boolean mDelayedDismiss = true;
    private boolean mExpandOnStart = false;
    private BottomSheetAdapter mAdapterBuilder;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Context mContext;
    private BottomSheetItemClickListener mItemClickListener;
    private BottomSheetMenu mMenu;
    private Menu mAndroidMenu;
    private boolean mEditingEnabled;
    private BottomSheetMenuAdapter mMenuAdapter;

    public BottomSheetBuilder(Context context, CoordinatorLayout coordinatorLayout) {
        mContext = context;
        mCoordinatorLayout = coordinatorLayout;
        mColors=new BottomSheetColors();
        mAdapterBuilder = new BottomSheetAdapter(mContext,mColors);
    }

    public BottomSheetBuilder(Context context) {
        this(context, 0);
    }

    public BottomSheetBuilder(Context context, @StyleRes int theme) {
        mContext = context;
        mTheme = theme;
        mColors=new BottomSheetColors();
        mAdapterBuilder = new BottomSheetAdapter(mContext, mColors);
    }

    public BottomSheetBuilder setMode(@BottomSheetMode int mode) {
        mAdapterBuilder.setMode(mode);
        return this;
    }

    public BottomSheetBuilder setItemClickListener(BottomSheetItemClickListener listener) {
        mAdapterBuilder.setItemClickListener(listener);
        return this;
    }

    public BottomSheetBuilder setMenu(@MenuRes int menu) {
        Menu newMenu = new MenuBuilder(mContext);
        new SupportMenuInflater(mContext).inflate(menu, newMenu);
        return setMenu(newMenu);
    }

    public BottomSheetBuilder setMenu(Menu menu) {
        mAndroidMenu=menu;
        return this;
    }

    public BottomSheetBuilder setMenu(BottomSheetMenu menu) {
        mMenu=menu;
        return this;
    }

    public BottomSheetBuilder setItemTextColor(@ColorRes int color) {
        mColors.setItemTextColor(ContextCompat.getColor(mContext,color));
        return this;
    }

    public BottomSheetBuilder setTitleTextColor(@ColorRes int color) {
        mColors.setTitleTextColor(ContextCompat.getColor(mContext,color));
        return this;
    }

    public BottomSheetBuilder setBackground(@DrawableRes int background) {
        mColors.setBackground(background);
        return this;
    }

    public BottomSheetBuilder setBackgroundColor(@ColorRes int background) {
        mColors.setBackgroundColor(ContextCompat.getColor(mContext,background));
        return this;
    }

    public BottomSheetBuilder setDividerBackground(@DrawableRes int background) {
        mColors.setDividerBackground(background);
        return this;
    }

    public BottomSheetBuilder setItemBackground(@DrawableRes int background) {
        mColors.setItemBackground(background);
        return this;
    }

    public BottomSheetBuilder expandOnStart(boolean expand) {
        mExpandOnStart = expand;
        return this;
    }

    public BottomSheetBuilder delayDismissOnItemClick(boolean dismiss) {
        mDelayedDismiss = dismiss;
        return this;
    }

    public BottomSheetBuilder setAppBarLayout(AppBarLayout appbar) {
        mAppBarLayout = appbar;
        return this;
    }

    public BottomSheetBuilder setIconTintColorResource(@ColorRes int color) {
        mColors.setIconTintColor(ContextCompat.getColor(mContext,color));
        return this;
    }

    public BottomSheetBuilder setIconTintColor(@ColorInt int color) {
        mColors.setIconTintColor(color);
        return this;
    }

    public BottomSheetBuilder setEditorEnabled(boolean editorEnabled) {
        mEditingEnabled=editorEnabled;
        return this;
    }

    public BottomSheetBuilder setEditorEnabled(BottomSheetMenuAdapter adapter) {
        mMenuAdapter=adapter;
        mEditingEnabled=true;
        return this;
    }


    private BottomSheetView getView() {
        if (mEditingEnabled && mMenuAdapter==null && mAndroidMenu!=null && mMenu==null)
            throw new IllegalStateException("You need to provide a menu adapter if you want" +
                    " to edit runtime.");

        if (mMenu==null && mAndroidMenu!=null)
            mMenu=BottomSheetMenu.from(mAndroidMenu,mMenuAdapter);
        else if (mMenu == null) {
            throw new IllegalStateException("You need to provide at least one Menu" +
                    "or a Menu resource id");
        }

        return mAdapterBuilder.createView(mMenu,mEditingEnabled);
    }

    public BottomSheetView createView() {
        if (mCoordinatorLayout == null) {
            throw new IllegalStateException("You need to provide a coordinatorLayout" +
                    "so the view can be placed on it");
        }

        BottomSheetView sheet=getView();

        ViewCompat.setElevation(sheet, mContext.getResources()
                .getDimensionPixelSize(R.dimen.bottomsheet_elevation));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sheet.findViewById(R.id.fakeShadow).setVisibility(View.GONE);
        }

        CoordinatorLayout.LayoutParams layoutParams
                = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.setBehavior(new BottomSheetBehavior());

        if (mContext.getResources().getBoolean(R.bool.tablet_landscape)) {
            layoutParams.width = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.bottomsheet_width);
        }

        mCoordinatorLayout.addView(sheet, layoutParams);
        mCoordinatorLayout.postInvalidate();
        return sheet;
    }

    public BottomSheetMenuDialog createDialog() {
        BottomSheetMenuDialog dialog = mTheme == 0
                ? new BottomSheetMenuDialog(mContext, R.style.BottomSheetBuilder_DialogStyle)
                : new BottomSheetMenuDialog(mContext, mTheme);

        setItemClickListener(dialog);

        View sheet = getView();

        sheet.findViewById(R.id.fakeShadow).setVisibility(View.GONE);
        dialog.setAppBar(mAppBarLayout);
        dialog.expandOnStart(mExpandOnStart);
        dialog.delayDismiss(mDelayedDismiss);
        dialog.setBottomSheetItemClickListener(mItemClickListener);

        if (mContext.getResources().getBoolean(R.bool.tablet_landscape)) {
            FrameLayout.LayoutParams layoutParams
                    = new FrameLayout.LayoutParams(mContext.getResources()
                    .getDimensionPixelSize(R.dimen.bottomsheet_width),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setContentView(sheet, layoutParams);
        } else {
            dialog.setContentView(sheet);
        }

        return dialog;
    }

}
