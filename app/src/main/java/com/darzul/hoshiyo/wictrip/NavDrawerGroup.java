package com.darzul.hoshiyo.wictrip;

import java.util.Collection;
import java.util.List;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 07/11/2014.
 */
public class NavDrawerGroup {
    String text;
    int iconLeft;
    List<NavDrawerChild> children;

    public NavDrawerGroup(String text, int iconLeft, List<NavDrawerChild> children) {
        this.text = text;
        this.iconLeft = iconLeft;
        this.children = children;
    }

    public String getText() {
        return text;
    }

    public int getIconLeft() {
        return iconLeft;
    }

    public Collection<NavDrawerChild> getChildren() {
        return children;
    }

    public NavDrawerChild getChild(int childPosition) {
        return children.get(childPosition);
    }
}
