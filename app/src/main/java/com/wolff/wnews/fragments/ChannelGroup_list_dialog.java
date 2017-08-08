package com.wolff.wnews.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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

import com.wolff.wnews.R;
import com.wolff.wnews.activities.ChannelGroup_item_activity;
import com.wolff.wnews.adapters.ChannelGroup_list_adapter;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannelGroup;

import java.util.ArrayList;

/**
 * Created by wolff on 13.07.2017.
 */

public class ChannelGroup_list_dialog extends DialogFragment {
    public static final String GROUP_ID = "Group_ID";
    //private ArrayList<WChannelGroup> mGroupList = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<WChannelGroup> groupList = DataLab.get(mContext).getWChannelGroupsList();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_fragment,null);
        ListView lvItemList = (ListView)view.findViewById(R.id.lvListMain);
        ChannelGroup_list_adapter adapter = new ChannelGroup_list_adapter(mContext, groupList);
        lvItemList.setAdapter(adapter);
        lvItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(GROUP_ID,position);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
    }
     @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
  }
