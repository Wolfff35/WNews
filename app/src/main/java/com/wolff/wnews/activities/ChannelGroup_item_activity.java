package com.wolff.wnews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.wolff.wnews.R;
import com.wolff.wnews.fragments.ChannelGroup_item_fragment;
import com.wolff.wnews.model.WChannelGroup;

/**
 * Created by wolff on 13.07.2017.
 */

public class ChannelGroup_item_activity extends AppCompatActivity {
    private WChannelGroup mGroupItem;
    public static final String EXTRA_CHANNELGROUP_ITEM = "ChannelGroupItem";

    public static Intent newIntent(Context context, WChannelGroup item){
        Intent intent = new Intent(context,ChannelGroup_item_activity.class);
        intent.putExtra(EXTRA_CHANNELGROUP_ITEM,item);
        return intent;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isLightTheme = preferences.getBoolean("isLightTheme",false);
        if(isLightTheme){
            setTheme(R.style.AppThemeLight);
        }else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.item_activity);
        mGroupItem = (WChannelGroup) getIntent().getSerializableExtra(EXTRA_CHANNELGROUP_ITEM);
        ChannelGroup_item_fragment group_itemFragment = ChannelGroup_item_fragment.newIntance(mGroupItem);

        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.item_container, group_itemFragment);
        fragmentTransaction.commit();


    }

}


