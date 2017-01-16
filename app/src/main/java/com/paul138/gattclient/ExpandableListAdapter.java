package com.paul138.gattclient;

/**
 * Created by 138 on 1/15/2017.
 */

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> listGroups; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> listChildMap; //group then list data

    public ExpandableListAdapter(Context context, List<String> listGroups,
                                 HashMap<String, List<String>> listChildMap) {
        this._context = context;
        this.listGroups = listGroups;
        this.listChildMap = listChildMap;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listChildMap.get(this.listGroups.get(groupPosition))
                .get(childPosititon);
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

        TextView listItem = (TextView) convertView
                .findViewById(R.id.listItem);

        listItem.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChildMap.get(this.listGroups.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listGroups.size();
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
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView listGroup = (TextView) convertView
                .findViewById(R.id.listGroup);
        listGroup.setTypeface(null, Typeface.BOLD);
        listGroup.setText(headerTitle);

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

