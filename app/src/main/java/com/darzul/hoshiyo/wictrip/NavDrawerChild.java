package com.darzul.hoshiyo.wictrip;

import android.view.View;

/**
 * Created by hoshiyo on 01/11/14.
 */
public class NavDrawerChild {

    String text;
    int icoLeft;
    int icoRight;
    View.OnClickListener listener;

    public NavDrawerChild(String text, int icoLeft, int icoRight, View.OnClickListener listener) {
        this.text = text;
        this.icoLeft = icoLeft;
        this.icoRight = icoRight;
        this.listener = listener;
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

    public View.OnClickListener getListener() {
        return listener;
    }
}
