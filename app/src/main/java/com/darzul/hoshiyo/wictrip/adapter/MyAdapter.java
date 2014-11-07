package com.darzul.hoshiyo.wictrip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.darzul.hoshiyo.wictrip.NavDrawerChild;
import com.darzul.hoshiyo.wictrip.NavDrawerGroup;
import com.example.hoshiyo.wictrip.R;

import java.util.Collection;
import java.util.List;

public class MyAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<NavDrawerGroup> navDrawerGroups;

    public MyAdapter(Context context, List<NavDrawerGroup> navDrawerGroups) {
        this.mContext = context;
        this.navDrawerGroups = navDrawerGroups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.navDrawerGroups.get(groupPosition).getChild(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_child, null);
        }

        NavDrawerChild navDrawerChild = navDrawerGroups.get(groupPosition).getChild(childPosition);

        ImageView icoLeft = (ImageView) v.findViewById(R.id.ico_left);
        icoLeft.setImageResource(navDrawerChild.getIcoLeft());

        TextView text = (TextView) v.findViewById(R.id.text);
        text.setText(navDrawerChild.getText());

        ImageView icoRight = (ImageView) v.findViewById(R.id.ico_right);
        int icoRes = navDrawerChild.getIcoRight();
        if(icoRes == -1) {
            //TODO cas pas d'icone
        } else {
            icoRight.setImageResource(icoRes);
        }

        return v;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Collection<NavDrawerChild> children = this.navDrawerGroups.get(groupPosition).getChildren();

        if(children == null) {
            return 0;
        }
        return children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.navDrawerGroups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.navDrawerGroups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_group, null);
        }

        NavDrawerGroup navDrawerGroup = navDrawerGroups.get(groupPosition);

        ImageView ico = (ImageView) v.findViewById(R.id.ic_left);
        int icoRes = navDrawerGroup.getIconLeft();
        if(icoRes == -1) {
            //TODO cas pas d'image
        } else {
            ico.setImageResource(icoRes);
        }

        if(navDrawerGroup.getChildren() == null) {
            //TODO enleve indicator
        }

        TextView text = (TextView) v.findViewById(R.id.text);
        text.setText(navDrawerGroup.getText());

        return v;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}