package com.darzul.hoshiyo.wictrip.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.darzul.hoshiyo.wictrip.fragment.NavDrawerItem;
import com.example.hoshiyo.wictrip.R;

public class MyAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public MyAdapter(Context context, List<String> listDataHeader,
                     HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (this._listDataHeader == null) {
            Log.e("Debug", "mListDataHeader is null.");
            return 0;
        } else if (groupPosition < 0 || groupPosition >= this._listDataHeader.size()) {
            Log.e("Debug", "position invalid: " + groupPosition);
            return 0;
        } else if (this._listDataHeader.get(groupPosition) == null) {
            Log.e("Debug", "Value of mListDataHeader at position is null: " + groupPosition);
            return 0;
        } else if (this._listDataChild == null) {
            Log.e("Debug", "mListDataChild is null.");
            return 0;
        } else if (!this._listDataChild.containsKey(this._listDataHeader.get(groupPosition))) {
            Log.e("Debug", "No key: " + this._listDataHeader.get(groupPosition));
            return 0;
        } else if (this._listDataChild.get(this._listDataHeader.get(groupPosition)) == null) {
            Log.e("Debug", "Value at key is null: " + this._listDataHeader.get(groupPosition));
            return 0;
        } else {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_group, null);
        }

        /*ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.itemName);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        imgIcon.setImageResource(_listDataHeader.get(groupPosition).getIcon());
        txtTitle.setText(_listDataHeader.get(groupPosition).getItemName());

        // displaying count
        // check whether it set visible or not
        if(_listDataHeader.get(groupPosition).getCounterVisibility()){
            txtCount.setText(_listDataHeader.get(groupPosition).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }*/

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
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