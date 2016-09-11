package com.github.rubensousa.bottomsheetbuilder.menu;

import android.content.Context;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetAdapter;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetMenu extends BottomSheetMenuItem{
    private List<BottomSheetMenuItem> mItems;

    public BottomSheetMenu(int itemID,String title) {
        this(null,itemID,title,true);
    }

    public BottomSheetMenu(int itemID,String title,boolean visible) {
        this(null,itemID,title,visible);
    }

    public BottomSheetMenu(BottomSheetMenu parent,int itemID, String title,boolean visible) {
        super(parent,itemID,title,visible);
        mItems=new ArrayList<>();
    }

    @Override
    public void setAdapter(BottomSheetAdapter adapter) {
        mAdapter=adapter;
        for (BottomSheetMenuItem i:mItems)
            i.setAdapter(adapter);
    }

    public void insertMenuItem(int position,BottomSheetMenuItem item) {
        insertMenuItem(position,item,true);
    }

    private void insertMenuItem(int position,BottomSheetMenuItem item,boolean addToAdapter) {
        int size=mItems.size();

        if (position<0 || position>size)
            throw new IndexOutOfBoundsException();

        if (position==mItems.size())
            addMenuItem(item);

        item.setParent(this);
        item.setAdapter(mAdapter);
        mItems.add(position,item);

        if (addToAdapter) mAdapter.addItem(item);
    }

    public void addMenuItem(BottomSheetMenuItem item) {
        item.setParent(this);
        item.setAdapter(mAdapter);
        mItems.add(item);
        if (mAdapter!=null) mAdapter.addItem(item);
    }

    public void addMenuItemToSubmenu(int position,BottomSheetMenuItem item) {
        if (item instanceof BottomSheetMenu)
            throw new IllegalArgumentException("Submenu cannot have another submenu.");

        BottomSheetMenuItem menuItem=mItems.get(position);

        if (menuItem==null)
            throw new ArrayIndexOutOfBoundsException();

        if (!(menuItem instanceof BottomSheetMenu))
            throw new IllegalArgumentException("Item at position "+position+" is not a submenu.");

        ((BottomSheetMenu) menuItem).addMenuItem(item);
    }

    public BottomSheetMenuItem removeMenuItem(int itemId) {
        for (BottomSheetMenuItem i:mItems)
            if (i.getItemId()==itemId) {
                i.remove();
                return mItems.remove(mItems.indexOf(i));
            }
            else if (i instanceof BottomSheetMenu) {
                BottomSheetMenuItem item=((BottomSheetMenu) i).removeMenuItem(itemId);
                if (item!=null) return item;
            }
        return null;
    }

    public BottomSheetMenuItem changeMenuItem(int itemId,BottomSheetMenuItem newItem) {
        for (BottomSheetMenuItem i:mItems)
            if (i.getItemId()==itemId) {
                newItem.setAdapter(mAdapter);
                newItem.setParent(this);
                mItems.add(mItems.indexOf(i),newItem);
                mAdapter.change(i,newItem);
                return mItems.remove(mItems.indexOf(i));
            }
            else if (i instanceof BottomSheetMenu) {
                BottomSheetMenuItem item=((BottomSheetMenu) i).changeMenuItem(itemId,newItem);
                if (item!=null) return item;
            }
        return null;
    }

    public static BottomSheetMenu from(Menu mAndroidMenu, BottomSheetMenuAdapter menuAdapter) {
        BottomSheetMenu menu=new BottomSheetMenu(0,null);

        for (int i=0;i<mAndroidMenu.size();++i) {
            MenuItem item=mAndroidMenu.getItem(i);

            if (item.hasSubMenu()) {
                BottomSheetMenu newSubmenu=new BottomSheetMenu(menu,item.getItemId(),item.getTitle().toString(),item.isVisible());

                SubMenu subMenu=item.getSubMenu();
                for (int j=0;j<subMenu.size();++j) {
                    MenuItem subMenuItem=subMenu.getItem(j);

                    if (menuAdapter==null)
                        newSubmenu.addMenuItem(new BottomSheetMenuItem(newSubmenu,
                                subMenuItem.getItemId(),subMenuItem.getTitle().toString(),item.isVisible(),subMenuItem.getIcon()));
                    else
                        newSubmenu.addMenuItem(new BottomSheetMenuItem(newSubmenu,
                                subMenuItem.getItemId(),subMenuItem.getTitle().toString(),item.isVisible(),menuAdapter.getDrawableRes(subMenuItem)));

                }
                menu.addMenuItem(newSubmenu);
            }
            else if (menuAdapter==null)
                menu.addMenuItem(new BottomSheetMenuItem(menu,
                        item.getItemId(),item.getTitle().toString(),item.isVisible(),item.getIcon()));
            else
                menu.addMenuItem(new BottomSheetMenuItem(menu,
                        item.getItemId(),item.getTitle().toString(),item.isVisible(),menuAdapter.getDrawableRes(item)));
        }

        return menu;
    }

    public void inflateAdapter(BottomSheetAdapter adapter) {
        setAdapter(adapter);
        List<BottomSheetMenuItem> items=mItems;
        mItems=new ArrayList<>();
        for (BottomSheetMenuItem i:items) {
            addMenuItem(i);
        }
    }

    public List<BottomSheetMenuItem> getMenuItems() {
        return mItems;
    }

    BottomSheetMenuItem getItemAfter(BottomSheetMenuItem item) {
        try {
            return mItems.get(mItems.indexOf(item)+1);
        }
        catch (Exception e) {
            if (mParent!=null)
                return mParent.getItemAfter(this);
            else
                return null;
        }
    }

    BottomSheetMenuItem getItemBefore(BottomSheetMenuItem item) {
        try {
            return mItems.get(mItems.indexOf(item)-1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            if (mParent!=null)
                return mParent.getItemBefore(this);
            else
                return null;
        }
    }

    public BottomSheetMenuItem findItemByTitle(String s) {
        for (BottomSheetMenuItem i:mItems)
            if (i.getTitle().compareTo(s)==0)
                return i;
            else if (i instanceof  BottomSheetMenu) {
                BottomSheetMenuItem item=((BottomSheetMenu)i).findItemByTitle(s);
                if (item!=null) return item;
            }
        if (mParent!=null)
            return null;
        else
            throw new Resources.NotFoundException("Item not found with title: "+s);
    }

    public BottomSheetMenuItem findItemByID(int itemID) {
        for (BottomSheetMenuItem i:mItems)
            if (i.getItemId()==itemID)
                return i;
            else if (i instanceof  BottomSheetMenu) {
                BottomSheetMenuItem item = ((BottomSheetMenu) i).findItemByID(itemID);
                if (item!=null) return item;
            }
        if (mParent!=null)
            return null;
        else
            throw new Resources.NotFoundException("Item not found with id: "+ itemID);
    }

    @Override
    public BottomSheetMenuItem clone(IdProvider provider) {
        BottomSheetMenu item=new BottomSheetMenu(provider.getUniqueId(),mTitle,true);
        item.mIconRes=mIconRes;
        item.mIconPath=mIconPath;
        item.mIcon=mIcon;
        item.mParent=mParent;

        for (BottomSheetMenuItem i:mItems)
            item.mItems.add(i.clone(provider,item));
        return item;
    }
}
