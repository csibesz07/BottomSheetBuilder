package com.github.rubensousa.bottomsheetbuilder.sample;

import com.github.rubensousa.bottomsheetbuilder.menu.BottomSheetMenu;
import com.github.rubensousa.bottomsheetbuilder.menu.BottomSheetMenuItem;

interface BottomSheetViewHolder extends BottomSheetMenuItem.IdProvider {
    BottomSheetMenu getMenu();
    int getId();

    @Override
    int getUniqueId();
}