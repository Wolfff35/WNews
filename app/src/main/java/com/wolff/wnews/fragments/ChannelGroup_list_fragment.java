package com.wolff.wnews.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wolff.wnews.R;
import com.wolff.wnews.adapters.ChannelGroup_list_adapter;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannelGroup;

import java.util.ArrayList;

/**
 * Created by wolff on 13.07.2017.
 */

public class ChannelGroup_list_fragment extends Fragment {
    private ChannelGroup_list_fragment_listener listener;
    private ArrayList<WChannelGroup> mGroupList = new ArrayList<>();
    public static final String ID_CHANNELGROUP = "ID_CHANNELGROUP";
    private ListView mGroupListViewMain;

    public interface ChannelGroup_list_fragment_listener{
        void onChannelGroupSelected(WChannelGroup group);
    }
    public static ChannelGroup_list_fragment newInstance(){
        ChannelGroup_list_fragment fragment = new ChannelGroup_list_fragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.list_fragment,container,false);
        mGroupListViewMain = (ListView)view.findViewById(R.id.lvListMain);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mGroupList = DataLab.get(getContext()).getWChannelGroupsList();
        ChannelGroup_list_adapter adapter = new ChannelGroup_list_adapter(getContext(),mGroupList);

        mGroupListViewMain.setAdapter(adapter);
        mGroupListViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onChannelGroupSelected(mGroupList.get(position));
            }
        });
            getActivity().setTitle("Группы новостных каналов ");
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ChannelGroup_list_fragment_listener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

}
