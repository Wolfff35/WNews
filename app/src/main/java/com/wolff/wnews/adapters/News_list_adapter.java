package com.wolff.wnews.adapters;

import android.content.Context;
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
import com.wolff.wnews.utils.MySettings;

import java.util.ArrayList;

/**
 * Created by wolff on 11.07.2017.
 */

public class News_list_adapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<WNews> mNewsList;
    private ArrayList<WChannel> mChannelList;

    public News_list_adapter(Context context, ArrayList<WNews> newsList,ArrayList<WChannel>channelList){
        mContext=context;
        mNewsList = newsList;
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

        String time_interval = dateUtils.calculateInterval(news.getPubDate());
        DataLab dataLab = DataLab.get(mContext);
        WChannel ch = dataLab.findChannelById(news.getIdChannel(),mChannelList);
        String currChannel="";
        if(ch!=null) {
            currChannel = ch.getName();
        }
        tvDatePubNews.setText(dateUtils.dateToString(news.getPubDate(),DateUtils.DATE_FORMAT_VID)+" - "+time_interval+" - "+currChannel);
        if(!news.getEnclosure().isEmpty()) {
            Picasso picasso = Picasso.with(mContext);
            if(new MySettings().SHOW_PICASSO_INDICATOR) {
                picasso.setIndicatorsEnabled(true);
            }
            picasso.load(news.getEnclosure())
            //        .placeholder(R.drawable.ic_download_black)
            //        .error(R.drawable.ic_error_black)
                    .into(ivPictureNews);
        }
        return view;
    }
}
