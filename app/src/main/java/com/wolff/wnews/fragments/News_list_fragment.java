package com.wolff.wnews.fragments;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.wolff.wnews.adapters.News_list_adapter;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WNews;

import java.util.ArrayList;

/**
 * Created by wolff on 11.07.2017.
 */

public class News_list_fragment extends ListFragment{
    private News_list_fragment_listener listener;
    private ArrayList<WNews> mNewsList = new ArrayList<>();
    private ArrayList<WChannel> mChannelList = new ArrayList<>();
    public static final String ID_CHANNEL = "ID_CHANNEL";

    public interface News_list_fragment_listener{
        void onNewsSelected(WNews news);
    }
    public static News_list_fragment newInstance(long idChannel){
        News_list_fragment fragment = new News_list_fragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ID_CHANNEL,idChannel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        long idChannel = getArguments().getLong(ID_CHANNEL);
        mNewsList = DataLab.get(getContext()).getWNewsList(idChannel);
        mChannelList = DataLab.get(getContext()).getWChannelsList();
       //WChannel currentChannel = DataLab.get(getContext()).findChannelById(idChannel,mChannelList);
        News_list_adapter adapter = new News_list_adapter(getContext(),mNewsList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onNewsSelected(mNewsList.get(position));
            }
        });
     }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (News_list_fragment_listener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

}
