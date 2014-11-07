package com.darzul.hoshiyo.wictrip;

/**
 * Created by hoshiyo on 01/11/14.
 */
public class NavDrawerChild {

    String text;
    int icoLeft;
    int icoRight;

    public NavDrawerChild(String text, int icoLeft, int icoRight) {
        this.text = text;
        this.icoLeft = icoLeft;
        this.icoRight = icoRight;
    }

    public int getIcoLeft() {
        return icoLeft;
    }

    public String getText() {
        return text;
    }

    public int getIcoRight() {
        return icoRight;
    }
}
