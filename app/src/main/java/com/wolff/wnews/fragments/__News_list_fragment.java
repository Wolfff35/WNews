package com.wolff.wnews.fragments;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wolff.wnews.R;
import com.wolff.wnews.adapters.News_list_adapter;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WNews;
import com.wolff.wnews.service.NewsService;

import java.util.ArrayList;

/**
 * Created by wolff on 11.07.2017.
 */

public class __News_list_fragment extends Fragment {
    private __News_list_fragment_listener listener;
    private ArrayList<WNews> mNewsList = new ArrayList<>();
    private ArrayList<WChannel> mChannelList = new ArrayList<>();
    public static final String ID_CHANNEL = "ID_CHANNEL";
    private ListView mNewsListViewMain;
    private long mIdCurrentChannel;

    public interface __News_list_fragment_listener{
        void onNewsSelected(ArrayList<WNews> newsList,WNews news);
    }
    public static __News_list_fragment newInstance(long idChannel){
        __News_list_fragment fragment = new __News_list_fragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ID_CHANNEL,idChannel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIdCurrentChannel = getArguments().getLong(ID_CHANNEL);
        mNewsList = DataLab.get(getContext()).getWNewsList(mIdCurrentChannel);
        mChannelList = DataLab.get(getContext()).getWChannelsList();

    }
    @Override
    public void onResume() {
        super.onResume();
        mNewsList = DataLab.get(getContext()).getWNewsList(mIdCurrentChannel);
        mChannelList = DataLab.get(getContext()).getWChannelsList();
        onActivityCreated(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.list_fragment,container,false);
        mNewsListViewMain = (ListView)view.findViewById(R.id.lvListMain);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         News_list_adapter adapter = new News_list_adapter(getContext(),mNewsList,mChannelList);
        mNewsListViewMain.setAdapter(adapter);
        mNewsListViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onNewsSelected(mNewsList,mNewsList.get(position));
            }
        });
        mNewsListViewMain.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Log.e("SCROLL","firstVisibleItem = "+firstVisibleItem+"; visibleItemCount = "+visibleItemCount+"; totalItemCount = "+totalItemCount);
            }
        });
        if(mIdCurrentChannel==0){
            getActivity().setTitle(getResources().getString(R.string.app_name)+" Все новости");
        }else {
            getActivity().setTitle(getResources().getString(R.string.app_name)+" "+DataLab.get(getContext()).findChannelById(mIdCurrentChannel, mChannelList).getName());
        }
     }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (__News_list_fragment_listener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

}