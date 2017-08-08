package com.wolff.wnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wolff.wnews.R;
import com.wolff.wnews.model.WChannelGroup;

import java.util.ArrayList;

/**
 * Created by wolff on 13.07.2017.
 */

public class ChannelGroup_list_adapter extends BaseAdapter {
    //private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<WChannelGroup> mGroupList;

    public ChannelGroup_list_adapter(Context context,ArrayList<WChannelGroup>groupList){
        //mContext=context;
        mGroupList = groupList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGroupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view=mInflater.inflate(R.layout.channelgroup_list_item_adapter,parent,false);
        }
        TextView tvTitleGroup = (TextView)view.findViewById(R.id.tvTitleGroup);
        WChannelGroup group = (WChannelGroup) getItem(position);
        tvTitleGroup.setText(group.getName());
        return view;
    }

}
