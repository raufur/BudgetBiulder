package com.epsilon.coders.budgetbiulder.NavDrawer;

import android.graphics.drawable.Drawable;

/**
 * Created by Raufur on 9/12/14.
 */
public class NavDrawerItem {
    public static final int ITEM = 1;
    public static final int DIVIDER = 2;
    public static final int HEADER = 3;

    private Drawable icon;
    private String image;
    private String name;
    private String bank;
    private int type;

    public NavDrawerItem(Drawable icon, String name){
        this.icon = icon;
        this.name = name;
        this.type = ITEM;
    }

    public NavDrawerItem(){
        this.type = DIVIDER;
    }

    public NavDrawerItem(String image, String name, String bank){
        this.image = image;
        this.name = name;
        this.bank = bank;
        this.type = HEADER;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getBank() {
        return bank;
    }

    public String getImage() {
        return image;
    }
}
