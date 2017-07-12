package com.wolff.wnews.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wolff.wnews.R;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WNews;
import com.wolff.wnews.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wolff on 11.07.2017.
 */

public class News_list_adapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<WNews> mNewsList;
    private ArrayList<WChannel> mChannelList;
    //private WChannel mCurrentChannel;

    public News_list_adapter(Context context, ArrayList<WNews> newsList,ArrayList<WChannel>channelList){
        mContext=context;
        mNewsList = newsList;
        //mCurrentChannel = currentChannel;
        mChannelList = channelList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mNewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view=mInflater.inflate(R.layout.news_list_item_adapter,parent,false);
        }
        ImageView ivPictureNews = (ImageView)view.findViewById(R.id.ivPictureNews);
        TextView tvTitleNews = (TextView)view.findViewById(R.id.tvTitleNews);
        TextView tvDatePubNews = (TextView)view.findViewById(R.id.tvDatePubNews);
        WNews news = (WNews)getItem(position);
        tvTitleNews.setText(news.getTitle());
        DateUtils dateUtils = new DateUtils();
        //
        String time_interval;
        Date now = new Date();
        long sec = (now.getTime()-news.getPubDate().getTime())/1000;
        if(sec<60){
            time_interval=""+sec+" sec";
        }else if(sec<3600){
            time_interval = ""+sec/60+" min";
        }else if(sec<24*3600){
            time_interval = ""+sec/3600+" hours";
        }else {
            time_interval = ""+(sec/(24*3600))+" days";
        }
        //
        String currChannel="";
        DataLab dataLab = DataLab.get(mContext);
        WChannel ch = dataLab.findChannelById(news.getIdChannel(),mChannelList);
        if(ch!=null) {
            currChannel = ch.getName();
        }
        tvDatePubNews.setText(dateUtils.dateToString(news.getPubDate(),DateUtils.DATE_FORMAT_VID)+" - "+time_interval+" - "+currChannel);
        if(!news.getEnclosure().isEmpty()) {
            Picasso.with(mContext)
                    .load(news.getEnclosure())
                    .placeholder(R.drawable.ic_download_black)
                    .error(R.drawable.ic_error_black)
                    .into(ivPictureNews);
        }
        return view;
    }
}
