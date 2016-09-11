package com.github.rubensousa.bottomsheetbuilder.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.github.rubensousa.bottomsheetbuilder.menu.BottomSheetMenu;

public final class BottomSheetView extends LinearLayout {
    private BottomSheetMenu mMenu;
    private RecyclerView mRecyclerView;

    public BottomSheetView(Context context) {
        super(context);
    }

    public static BottomSheetView from(Context context, @LayoutRes int layout) {
        BottomSheetView view=new BottomSheetView(context);
        LayoutInflater.from(context).inflate(layout,view);
        return view;
    }

    void setEditableMenu(BottomSheetMenu editableMenu) {
        mMenu=editableMenu;
    }

    public void setRecyclerHasFixedSize(boolean fixedSize) {
        mRecyclerView.setHasFixedSize(fixedSize);
    }

    public BottomSheetBehavior getBehavior() {
        CoordinatorLayout.LayoutParams params= (CoordinatorLayout.LayoutParams) getLayoutParams();
        return params==null?null:((BottomSheetBehavior) params.getBehavior());
    }

    public BottomSheetMenu getEditableMenu() {
        return mMenu;
    }

    public void setEditableClasses(BottomSheetMenu menu, RecyclerView recyclerView) {
        mMenu=menu;
        mRecyclerView=recyclerView;
    }
}
