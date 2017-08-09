package com.wolff.wnews.fragments;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wolff.wnews.R;
import com.wolff.wnews.adapters.News_list_adapter;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WNews;

import java.util.ArrayList;

/**
 * Created by wolff on 11.07.2017.
 */

public class News_list_fragment_viewPager extends Fragment {
    private News_list_fragment_listener listener;
    private ArrayList<WNews> mNewsList = new ArrayList<>();
    private  static final String ID_PARTNEWS = "ID_PARTNEWS";
    private  static final String ID_CHANNEL = "ID_CHANNEL";
    private  static final String ID_SCREEN = "ID_SCREEN";
    private  static final String ID_COUNTPAGE = "ID_COUNTPAGE";
    private ListView mNewsListViewMain;

    private TextView tvPageNumber;
    private int mCurrentNewsScreen;
    private int mCountNewsScreen;
   // private Menu mOptionsMenu;
    private News_list_adapter mAdapter;

    public interface News_list_fragment_listener{
        void onNewsSelected_vp(ArrayList<WNews> newsList, WNews news);
    }
    public static News_list_fragment_viewPager newInstance(ArrayList<WNews> partNews,long idChannel,int currentPage,int countPage){
        News_list_fragment_viewPager fragment = new News_list_fragment_viewPager();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ID_PARTNEWS,partNews);
        bundle.putLong(ID_CHANNEL,idChannel);
        bundle.putInt(ID_SCREEN,currentPage);
        bundle.putInt(ID_COUNTPAGE,countPage);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mNewsList = (ArrayList<WNews>) getArguments().getSerializable(ID_PARTNEWS);
         mCurrentNewsScreen=getArguments().getInt(ID_SCREEN);
         mCountNewsScreen=getArguments().getInt(ID_COUNTPAGE);
         setHasOptionsMenu(true);
       // SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
       // boolean mark_auto = preferences.getBoolean("markAsReadIfSwap",false);
        //if (mark_auto){
        //    markNewsAsRead();
        //}

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_news, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_mark_all_as_read: {
                markNewsAsRead();
                mAdapter.notifyDataSetChanged();
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
        tvPageNumber = (TextView)view.findViewById(R.id.tvPageNumber);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new News_list_adapter(getContext(),mNewsList);
        tvPageNumber.setText("Page "+(mCurrentNewsScreen+1)+" from "+mCountNewsScreen);
        mNewsListViewMain.setAdapter(mAdapter);
        mNewsListViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onNewsSelected_vp(mNewsList,mNewsList.get(position));
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
    private void markNewsAsRead(){
        DataLab dataLab = DataLab.get(getContext());
        for(WNews newsItem:mNewsList){
            if(!newsItem.isReaded()){
                newsItem.setReaded(true);
                dataLab.news_update(newsItem);
            }
        }
    }

}
