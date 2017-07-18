package com.wolff.wnews.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.wolff.wnews.R;
import com.wolff.wnews.fragments.ChannelGroup_item_fragment;
import com.wolff.wnews.fragments.Channel_item_fragment;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WChannelGroup;

/**
 * Created by wolff on 13.07.2017.
 */

public class Channel_item_activity extends AppCompatActivity {
    private WChannel mChannelItem;
    public static final String EXTRA_CHANNEL_ITEM = "ChannelItem";

    public static Intent newIntent(Context context, WChannel item){
        Intent intent = new Intent(context,Channel_item_activity.class);
        intent.putExtra(EXTRA_CHANNEL_ITEM,item);
        return intent;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);
        mChannelItem = (WChannel) getIntent().getSerializableExtra(EXTRA_CHANNEL_ITEM);
        Channel_item_fragment channel_itemFragment = Channel_item_fragment.newIntance(mChannelItem);

        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.item_container, channel_itemFragment);
        fragmentTransaction.commit();


    }

}


