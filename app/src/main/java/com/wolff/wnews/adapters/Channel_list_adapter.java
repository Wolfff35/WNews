package com.wolff.wnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wolff.wnews.R;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WChannelGroup;

import java.util.ArrayList;

/**
 * Created by wolff on 13.07.2017.
 */

public class Channel_list_adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<WChannel> mChannelList;

    public Channel_list_adapter(Context context, ArrayList<WChannel>channelList){
        mChannelList = channelList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mChannelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mChannelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view=mInflater.inflate(R.layout.channel_list_item_adapter,parent,false);
        }
        TextView tvTitleChannel = (TextView)view.findViewById(R.id.tvTitleChannel);
        WChannel channel = (WChannel) getItem(position);
        tvTitleChannel.setText(channel.getName());
        return view;
    }

}
