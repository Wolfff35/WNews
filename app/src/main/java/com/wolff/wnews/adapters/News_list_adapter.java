package com.wolff.wnews.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wolff.wnews.R;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WNews;
import com.wolff.wnews.utils.DateUtils;

import java.util.ArrayList;

/**
 * Created by wolff on 11.07.2017.
 */

public class News_list_adapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<WNews> mNewsList;
    private boolean mIsLightTheme;

    public News_list_adapter(Context context, ArrayList<WNews> newsList){
        mContext=context;
        mNewsList = newsList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mIsLightTheme = preferences.getBoolean("isLightTheme",false);
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
        if(mIsLightTheme){
            if(news.isReaded()){
                tvTitleNews.setTextColor(ContextCompat.getColor(mContext,R.color.color_readed_news_light));
            }else {
                tvTitleNews.setTextColor(ContextCompat.getColor(mContext,R.color.color_unreaded_news_light));
            }
        }else {
            if(news.isReaded()){
                tvTitleNews.setTextColor(ContextCompat.getColor(mContext,R.color.color_readed_news_dark));
            }else {
                tvTitleNews.setTextColor(ContextCompat.getColor(mContext,R.color.color_unreaded_news_dark));
            }
        }
        String time_interval = dateUtils.calculateInterval(news.getPubDate());
        DataLab dataLab = DataLab.get(mContext);
        WChannel ch = dataLab.findChannelById(news.getIdChannel(),dataLab.getWChannelsList());
        String currChannel="";
        if(ch!=null) {
            currChannel = ch.getName();
        }
        tvDatePubNews.setText(dateUtils.dateToString(news.getPubDate(),DateUtils.DATE_FORMAT_VID)+" - "+time_interval+" - "+currChannel);
        if(!news.getEnclosure().isEmpty()) {
             Glide.with(mContext)
                    .load(news.getEnclosure())
                    .into(ivPictureNews);
        }
        return view;
    }
}
