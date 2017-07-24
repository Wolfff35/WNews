package com.wolff.wnews.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.util.ArrayList;

/**
 * Created by wolff on 11.07.2017.
 */

public class News_list_fragment_viewPager extends Fragment {
    private News_list_fragment_listener listener;
    private ArrayList<WNews> mNewsList = new ArrayList<>();
    private ArrayList<WChannel> mChannelList = new ArrayList<>();
    public static final String ID_PARTNEWS = "ID_PARTNEWS";
    public static final String ID_CHANNEL = "ID_CHANNEL";
    public static final String ID_SCREEN = "ID_SCREEN";
    private ListView mNewsListViewMain;
    private long mIdCurrentChannel;
    private int mCurrentNewsScreen;
    private Menu mOptionsMenu;
    private News_list_adapter mAdapter;

    public interface News_list_fragment_listener{
        void onNewsSelected_vp(ArrayList<WNews> newsList, WNews news);
    }
    public static News_list_fragment_viewPager newInstance(ArrayList<WNews> partNews,long idChannel,int currentPage){
        News_list_fragment_viewPager fragment = new News_list_fragment_viewPager();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ID_PARTNEWS,partNews);
        bundle.putLong(ID_CHANNEL,idChannel);
        bundle.putInt(ID_SCREEN,currentPage);
        fragment.setArguments(bundle);
        return fragment;
    }

     @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mIdCurrentChannel = getArguments().getLong(ID_CHANNEL);
         mNewsList = (ArrayList<WNews>) getArguments().getSerializable(ID_PARTNEWS);
         mChannelList = DataLab.get(getContext()).getWChannelsList();
         mCurrentNewsScreen=getArguments().getInt(ID_SCREEN);
         setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.mOptionsMenu = menu;
        inflater.inflate(R.menu.menu_list_news, mOptionsMenu);
        super.onCreateOptionsMenu(mOptionsMenu,inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
     mAdapter.notifyDataSetChanged();
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
        mAdapter = new News_list_adapter(getContext(),mNewsList,mChannelList);
        mNewsListViewMain.setAdapter(mAdapter);
        mNewsListViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onNewsSelected_vp(mNewsList,mNewsList.get(position));
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
        listener = (News_list_fragment_listener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

}
