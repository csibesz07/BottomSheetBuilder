package com.github.rubensousa.bottomsheetbuilder.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.github.rubensousa.bottomsheetbuilder.util.BottomSheetBuilderUtils;

public class BottomSheetColors {
    private Drawable mBackgroundDrawable;

    private Uri mBackgroundUri;

    @DrawableRes
    private int mBackgroundRes;

    private Drawable mDividerBackgroundDrawable;

    private Drawable mItemBackgroundDrawable;

    private Uri mDividerBackgroundUri;

    private Uri mItemBackgroundUri;

    @DrawableRes
    private int mDividerBackgroundRes;

    @DrawableRes
    private int mItemBackgroundRes;

    @ColorInt
    private int mBackgroundColor;

    @ColorInt
    private int mItemTextColor;

    @ColorInt
    private int mTitleTextColor;

    @ColorInt
    private int mIconTintColor=-1;


    @ColorInt
    private int mHeaderTextColor;

    public void setItemTextColor(@ColorInt int itemTextColor) {
        mItemTextColor = itemTextColor;
    }

    public void setTitleTextColor(@ColorInt int titleTextColor) {
        mTitleTextColor = titleTextColor;
    }

    // TODO: 9/10/2016 Make header color public
    void setHeaderText(@ColorInt int color) {
        mHeaderTextColor = color;
    }

    public void setBackground(@DrawableRes int background) {
        mBackgroundRes=background;
    }

    public void setBackground(Uri background) {
        mBackgroundUri =background;
    }

    public void setBackground(Drawable background) {
        mBackgroundDrawable = background;
    }

    public void setBackgroundColor(@ColorInt int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public void setDividerBackground(@DrawableRes int dividerBackground) {
        mDividerBackgroundRes = dividerBackground;
    }

    public void setDividerBackground(Uri dividerBackground) {
        mDividerBackgroundUri = dividerBackground;
    }

    public void setDividerBackground(Drawable dividerBackground) {
        mDividerBackgroundDrawable = dividerBackground;
    }

    public void setItemBackground(@DrawableRes int itemBackground) {
        mItemBackgroundRes = itemBackground;
    }

    public void setItemBackground(Uri itemBackground) {
        mItemBackgroundUri = itemBackground;
    }

    public void setItemBackground(Drawable itemBackground) {
        mItemBackgroundDrawable = itemBackground;
    }

    public void setIconTintColor(@ColorInt int iconTintColor) {
        mIconTintColor = iconTintColor;
    }

    public @ColorInt int getItemTextColor() {
        return mItemTextColor;
    }

    @ColorInt
    public int getHeaderTextColor() {
        return mHeaderTextColor;
    }

    @ColorInt int getTitleTextColor() {
        return mTitleTextColor;
    }

    @ColorInt int getIconTintColor() {
        return mIconTintColor;
    }

    public @ColorInt int getBackgroundColor() {
        return mBackgroundColor;
    }

    public Drawable getDividerBackground(Context context) {
        if (mDividerBackgroundUri!=null)
            return BottomSheetBuilderUtils.getDrawableFromUri(context,mDividerBackgroundUri);
        else if (mDividerBackgroundRes!=0)
            return ContextCompat.getDrawable(context,mDividerBackgroundRes);
        else if (mDividerBackgroundDrawable!=null)
            return mDividerBackgroundDrawable;
        else
            return null;
    }

    public Drawable getItemBackground(Context context) {
        if (mItemBackgroundUri!=null)
            return BottomSheetBuilderUtils.getDrawableFromUri(context,mItemBackgroundUri);
        else if (mItemBackgroundRes!=0)
            return ContextCompat.getDrawable(context,mItemBackgroundRes);
        if (mItemBackgroundDrawable!=null)
            return mItemBackgroundDrawable;
        else
            return null;
    }

    public Drawable getBackground(Context context) {
        if (mBackgroundUri !=null)
            return BottomSheetBuilderUtils.getDrawableFromUri(context,mBackgroundUri);
        else if (mBackgroundRes!=0)
            return ContextCompat.getDrawable(context,mBackgroundRes);
        if (mBackgroundDrawable!=null)
            return mBackgroundDrawable;
        else
            return null;
    }

    @DrawableRes
    public int getBackgroundRes() {
        return mBackgroundRes;
    }

    @DrawableRes
    public int getItemBackgroundRes() {
        return mItemBackgroundRes;
    }

    @DrawableRes
    public int getDividerBackgroundRes() {
        return mDividerBackgroundRes;
    }

    public boolean hasBackgroundRes() {
        return mBackgroundRes!=0;
    }

    public boolean hasItemBackgroundRes() {
        return mItemBackgroundRes!=0;
    }

    public boolean hasDividerBackgroundRes() {
        return mDividerBackgroundRes!=0;
    }

    public Uri getBackgroundUri() {
        return mBackgroundUri;
    }

    public Uri getItemBackgroundUri() {
        return mItemBackgroundUri;
    }

    public Uri getDividerBackgroundUri() {
        return mDividerBackgroundUri;
    }

    public boolean hasBackgroundUri() {
        return mBackgroundUri!=null;
    }

    public boolean hasItemBackgroundUri() {
        return mItemBackgroundUri!=null;
    }

    public boolean hasDividerBackgroundUri() {
        return mDividerBackgroundUri!=null;
    }
}
