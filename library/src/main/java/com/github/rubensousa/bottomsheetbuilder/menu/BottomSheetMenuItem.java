package com.github.rubensousa.bottomsheetbuilder.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetAdapter;
import com.github.rubensousa.bottomsheetbuilder.util.BottomSheetBuilderUtils;

public class BottomSheetMenuItem {
    protected Drawable mIcon;
    protected int mIconRes;
    protected Uri mIconPath;

    protected int mItemID;
    protected String mTitle;
    protected BottomSheetMenu mParent;
    protected boolean mIsVisible;

    private Object mBind;
    protected BottomSheetAdapter mAdapter;

    public BottomSheetMenuItem(int itemID,String title,boolean visible) {
        this(null,itemID,title,visible);
    }

    public BottomSheetMenuItem(int itemID, String title,boolean visible, @DrawableRes int iconRes) {
        this(null,itemID,title,visible,iconRes);
    }

    public BottomSheetMenuItem(int itemID,String title,boolean visible,Drawable icon) {
        this(null,itemID,title,visible,icon);
    }

    public BottomSheetMenuItem(int itemID,String title,boolean visible,Uri iconPath) {
        this(null,itemID,title,visible,iconPath);
    }

    public BottomSheetMenuItem(BottomSheetMenu parent,int itemID, String title,boolean visible) {
        mItemID=itemID;
        mTitle=title;
        mParent=parent;
        mIsVisible=visible;
    }

    public BottomSheetMenuItem(BottomSheetMenu parent,int itemID, String title,boolean visible, @DrawableRes int iconRes) {
        this(parent,itemID,title,visible);
        mIconRes=iconRes;
    }

    public BottomSheetMenuItem(BottomSheetMenu parent,int itemID,String title,boolean visible,Drawable icon) {
        this(parent,itemID,title,visible);
        mIcon=icon;
    }

    public BottomSheetMenuItem(BottomSheetMenu parent,int itemID,String title,boolean visible,Uri iconPath) {
        this(parent,itemID,title,visible);
        mIconPath=iconPath;
    }

    public void setVisible(boolean mIsVisible) {
        mIsVisible = mIsVisible;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    public void setParent(BottomSheetMenu parent) {
        mParent=parent;
    }

    public void setBind(Object bind) {
        mBind=bind;
    }

    public Drawable getIcon() {
        if (mIcon!=null)
            return mIcon;
        else if (mIconRes!=0)
            return ContextCompat.getDrawable(mParent.getContext(),mIconRes);
        else if (mIconPath!=null)
            return BottomSheetBuilderUtils.getDrawableFromUri(mParent.getContext(),mIconPath);
        else
            return null;
    }

    public int getItemId() {
        return mItemID;
    }

    public String getTitle() {
        return mTitle;
    }

    public Object getObject() {
        return mBind;
    }

    public void setAdapter(BottomSheetAdapter adapter) {
        mAdapter = adapter;
    }

    public void remove() {
        mAdapter.remove(this);
    }

    public boolean hasSubMenuBefore() {
        return mParent != null && mParent.getItemBefore(this) instanceof BottomSheetMenu;
    }

    public BottomSheetMenuItem getItemAfter() {
        return mParent==null?null:mParent.getItemAfter(this);
    }

    public BottomSheetMenuItem getItemBefore() {
        return mParent==null?null:mParent.getItemBefore(this);
    }

    public Context getContext() {
        return mAdapter.getContext();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setID(int ID) {
        mItemID = ID;
    }

    public BottomSheetMenuItem clone(IdProvider provider) {
        BottomSheetMenuItem newItem=new BottomSheetMenuItem(mParent,provider.getUniqueId(),mTitle,mIsVisible,mIcon);
        newItem.setIconRes(mIconRes);
        newItem.setIconPath(mIconPath);
        return newItem;
    }

    public void setIconPath(Uri iconPath) {
        mIconPath=iconPath;
    }

    public void setIconRes(@DrawableRes int iconRes) {
        mIconRes = iconRes;
    }

    BottomSheetMenuItem clone(IdProvider provider, BottomSheetMenu mParent) {
        BottomSheetMenuItem item=clone(provider);
        item.setParent(mParent);
        return item;
    }

    public BottomSheetMenu getParent() {
        return mParent;
    }

    public interface IdProvider {
        int getUniqueId();
    }
}
