package com.wolff.wnews.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wolff.wnews.R;
import com.wolff.wnews.activities.ChannelGroup_item_activity;
import com.wolff.wnews.activities.Channel_item_activity;
import com.wolff.wnews.adapters.Channel_list_adapter;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannel;

import java.util.ArrayList;

/**
 * Created by wolff on 13.07.2017.
 */

public class Channel_list_fragment extends Fragment {
    private Channel_list_fragment_listener listener1;
    private ArrayList<WChannel> mChannelList = new ArrayList<>();
    public static final String ID_CHANNEL = "ID_CHANNEL";
    private ListView mChannelListViewMain;
    private Menu mOptionsMenu;

    public interface Channel_list_fragment_listener{
        void onChannelSelected(WChannel channel);
    }
    public static Channel_list_fragment newInstance(){
        Channel_list_fragment fragment = new Channel_list_fragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onResume() {
        super.onResume();
        mChannelList = DataLab.get(getContext()).getWChannelsList();
        onActivityCreated(null);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.list_fragment,container,false);
        mChannelListViewMain = (ListView)view.findViewById(R.id.lvListMain);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mChannelList = DataLab.get(getContext()).getWChannelsList();
        Channel_list_adapter adapter = new Channel_list_adapter(getContext(),mChannelList);

        mChannelListViewMain.setAdapter(adapter);
        mChannelListViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener1.onChannelSelected(mChannelList.get(position));
            }
        });
        getActivity().setTitle(getString(R.string.menu_nav_header_channels));
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener1 = (Channel_list_fragment_listener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener1=null;
    }
    //=================
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.mOptionsMenu = menu;
        inflater.inflate(R.menu.menu_list_actions, mOptionsMenu);
        super.onCreateOptionsMenu(mOptionsMenu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add_item:{
                Intent intent = Channel_item_activity.newIntent(getContext(),null);
                startActivity(intent);
                break;
            }
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}
