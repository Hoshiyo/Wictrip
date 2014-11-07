package com.darzul.hoshiyo.wictrip.fragment;

/**
 * Created by hoshiyo on 01/11/14.
 */
public class NavDrawerItem {
    private String header;
    private int icon;
    private String itemName;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public NavDrawerItem(){}

    public NavDrawerItem(String header) {
        this.header = header;
    }

    public NavDrawerItem(String itemName, int icon) {
        this.itemName = itemName;
        this.icon = icon;
    }

    public NavDrawerItem(String itemName, int icon, boolean isCounterVisible, String count){
        this.itemName = itemName;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getHeader(){
        return this.header;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getItemName(){
        return this.itemName;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setHeader(String header){
        this.header = header;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
}
