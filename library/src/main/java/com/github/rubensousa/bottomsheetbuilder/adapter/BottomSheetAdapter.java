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


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.R;
import com.github.rubensousa.bottomsheetbuilder.menu.BottomSheetMenu;
import com.github.rubensousa.bottomsheetbuilder.menu.BottomSheetMenuItem;

import java.util.ArrayList;
import java.util.List;

public final class BottomSheetAdapter {

    @BottomSheetBuilder.BottomSheetMode
    private int mMode;

    private Context mContext;

    private BottomSheetItemAdapter mAdapter;
    private BottomSheetColors mColors;

    private BottomSheetItemClickListener mItemClickListener;
    private RecyclerView mRecyclerView;

    private List<BottomSheetItem> mItems;
    private BottomSheetView mSheet;

    public BottomSheetAdapter(Context context, BottomSheetColors colors) {
        mContext = context;
        mColors=colors;
    }

    public void setMode(@BottomSheetBuilder.BottomSheetMode int mode) {
        mMode = mode;
    }

    @SuppressLint("InflateParams")
    public BottomSheetView createView(@NonNull BottomSheetMenu menu,boolean isEditingEnabled) {

        mItems=new ArrayList<>();

        mSheet = mMode == BottomSheetBuilder.MODE_GRID ?
                BottomSheetView.from(mContext, R.layout.bottomsheetbuilder_sheet_grid)
                : BottomSheetView.from(mContext, R.layout.bottomsheetbuilder_sheet_list);

        mRecyclerView = (RecyclerView) mSheet.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (mColors.hasBackgroundRes())
            mRecyclerView.setBackgroundResource(mColors.getBackgroundRes());
        else {
            Drawable background = mColors.getBackground(mContext);
            if (background != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mRecyclerView.setBackground(background);
                } else
                    //noinspection deprecation
                    mRecyclerView.setBackgroundDrawable(background);
            } else
                mRecyclerView.setBackgroundColor(mColors.getBackgroundColor());
        }

        mItems=new ArrayList<>();

        mAdapter = new BottomSheetItemAdapter(mItems, mMode,
                mItemClickListener);

        menu.inflateAdapter(this);

        if (isEditingEnabled)
            mSheet.setEditableClasses(menu,mRecyclerView);

        if (mMode == BottomSheetBuilder.MODE_LIST) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            final int columns = mContext.getResources().getInteger(R.integer.bottomsheet_grid_columns);
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, columns);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {

                    float margin = mContext.getResources()
                            .getDimensionPixelSize(R.dimen.bottomsheet_grid_horizontal_margin);
                    mAdapter.setItemWidth((int) ((mRecyclerView.getWidth() - 2 * margin) / columns));
                    mRecyclerView.setAdapter(mAdapter);
                }
            });
        }

        return mSheet;

    }

    BottomSheetItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setItemClickListener(BottomSheetItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void addItem(BottomSheetMenuItem item) {
        addItem(item,true);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private void addItem(BottomSheetMenuItem item, boolean notify) {
        //if (mSheet.getBehavior()!=null) mSheet.getBehavior().setState(BottomSheetBehavior.STATE_COLLAPSED);

        BottomSheetItem starter = null;
        BottomSheetItem toAdd;
        int newPosition=item.getItemAfter()==null?mItems.size():mItems.indexOf(item.getItemAfter().getObject());
        int position=newPosition;

        if (item.isVisible())
            //is submenu or not
            if (item instanceof BottomSheetMenu) {
                BottomSheetMenu menu= (BottomSheetMenu) item;

                if (mMode == BottomSheetBuilder.MODE_GRID) {
                    throw new IllegalArgumentException("MODE_GRID can't have submenus." +
                            " Use MODE_LIST instead");
                }

                if (menu.hasSubMenuBefore()) {
                    starter=new BottomSheetDivider(mColors);
                    mItems.add(newPosition++,starter);
                }

                CharSequence title = item.getTitle();
                if (title != null && !title.equals("")) {
                    toAdd= new BottomSheetHeader(title.toString(), mColors);
                    mItems.add(newPosition++,toAdd);
                    if (starter==null) starter=toAdd;
                }

                for (BottomSheetMenuItem i: menu.getMenuItems()) {
                    if (i instanceof BottomSheetMenu)
                        throw new IllegalStateException("Submenu cannot have another submenu.");
                    toAdd=new BottomSheetListMenuItem(i,mColors);
                    mItems.add(newPosition++,toAdd);
                    i.setBind(toAdd);
                    if (starter==null) starter=toAdd;
                }
            } else {
                if (item.hasSubMenuBefore()) {
                    starter=new BottomSheetDivider(mColors);
                    mItems.add(newPosition++,starter);
                }
                toAdd=new BottomSheetListMenuItem(item,mColors);
                if (starter==null) starter=toAdd;
                mItems.add(newPosition++,toAdd);
            }

        //next item is submenu
        if (item.getItemAfter() instanceof BottomSheetMenu && !item.hasSubMenuBefore() && item.getParent()==null) {
            toAdd=new BottomSheetDivider(mColors);
            mItems.add(newPosition++, toAdd);
            item.getItemAfter().setBind(toAdd);
        }
        if (notify) mAdapter.notifyItemRangeInserted(position,newPosition-position);
        item.setBind(starter);
    }


    ///////////////////////////ADDITIONAL METHODS FOR RUNTIME MODIFICATIONS////////////////////////

    public void remove(BottomSheetMenuItem item) {
        remove(item,true);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private void remove(BottomSheetMenuItem item, boolean notify) {
        //if (mSheet.getBehavior()!=null) mSheet.getBehavior().setState(BottomSheetBehavior.STATE_COLLAPSED);

        int firstPos=mItems.indexOf(item.getObject());
        int itemsCount=0;

        int upperLimit=-1;

        if (item instanceof BottomSheetMenu) {
            List<BottomSheetMenuItem> itemsToBeRemoved=((BottomSheetMenu)item).getMenuItems();

            upperLimit=mItems.indexOf(itemsToBeRemoved.get(itemsToBeRemoved.size()-1).getObject())+1;
        }
        else {
            BottomSheetMenuItem itemAfter=item.getItemAfter();

            if (itemAfter==null) {
                mItems.remove(item.getObject());
                itemsCount++;
            }
            else
                upperLimit=mItems.indexOf(itemAfter.getObject());
        }

        if (upperLimit!=-1) {
            itemsCount=upperLimit-firstPos;
            for (int i=0;i<itemsCount;++i)
                mItems.remove(firstPos);
        }


        //no submenu before, remove divider on next item
        if (!(item.getItemBefore() instanceof BottomSheetMenu) && item instanceof BottomSheetMenu) {
            int toRemove=mItems.indexOf(item.getItemAfter().getObject());
            item.getItemAfter().setBind(mItems.get(toRemove+1));
            mItems.remove(toRemove);
            itemsCount++;
        }

        if (notify) mAdapter.notifyItemRangeRemoved(firstPos,itemsCount);
    }


    public void change(BottomSheetMenuItem i, BottomSheetMenuItem newItem) {
        if (i instanceof BottomSheetMenu) {
            if (newItem instanceof BottomSheetMenu)
                changeMenu2ItemOrMenu((BottomSheetMenu)i,newItem);
        }
        else {
            if (newItem instanceof BottomSheetMenu)
                changeItem2Menu(i,(BottomSheetMenu)newItem);
            else
                changeItem2Item(i,newItem,true);
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private void changeItem2Item(BottomSheetMenuItem i, BottomSheetMenuItem newItem,boolean notify) {
        BottomSheetItem toAdd=new BottomSheetListMenuItem(newItem,mColors);
        mItems.add(mItems.indexOf(i.getObject()),toAdd);
        remove(i, false);
        newItem.setBind(toAdd);
        if (notify) mAdapter.notifyItemChanged(mItems.indexOf(toAdd));
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private void changeItem2Menu(BottomSheetMenuItem i, BottomSheetMenu newItem) {
        int indexOfOldItem=mItems.indexOf(i.getObject());
        mItems.remove(i);
        mAdapter.notifyItemRemoved(indexOfOldItem);
        addItem(newItem);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private void changeMenu2ItemOrMenu(BottomSheetMenu i, BottomSheetMenuItem newItem) {
        addItem(newItem);
        remove(i, true);
    }

    public Context getContext() {
        return mContext;
    }
}
