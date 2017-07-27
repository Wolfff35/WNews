package com.wolff.wnews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wolff.wnews.R;
import com.wolff.wnews.fragments.News_item_fragment;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WNews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wolff on 19.07.2017.
 */

public class News_item_activity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<WNews> mNewsList;
    private WNews mCurrentNews;
    public static final String EXTRA_WNEWSLIST = "W_NewsList";
    public static final String EXTRA_WNEWS = "W_News";

    public static Intent newIntent(Context context, ArrayList<WNews> wNewsList,WNews currentNews){
        Intent intent = new Intent(context,News_item_activity.class);
        intent.putExtra(EXTRA_WNEWSLIST,  wNewsList);
        intent.putExtra(EXTRA_WNEWS,currentNews);
        return intent;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mCurrentNews",mCurrentNews);
        //Log.e("onSaveInstanceState","onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentNews = (WNews)savedInstanceState.getSerializable("mCurrentNews");
        //Log.e("onRestoreInstanceState","onRestoreInstanceState");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isLightTheme = preferences.getBoolean("isLightTheme",false);
        if(isLightTheme){
            setTheme(R.style.AppThemeLight);
        }else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.news_item_activity);
        mViewPager = (ViewPager)findViewById(R.id.viewPager_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mNewsList = (List<WNews>) getIntent().getSerializableExtra(EXTRA_WNEWSLIST);

        if(savedInstanceState!=null){
            mCurrentNews = (WNews)savedInstanceState.getSerializable("mCurrentNews");
        }else {
            mCurrentNews = (WNews) getIntent().getSerializableExtra(EXTRA_WNEWS);
        }

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                WNews item = mNewsList.get(position);
                return News_item_fragment.newIntance(item);
            }
            @Override
            public int getCount() {
                return mNewsList.size();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("onPageSelected","PAGE = "+position);
                WNews item = mNewsList.get(position);
                 //if(&&!item.isReaded()){
                 //    item.setReaded(true);
                 //    DataLab.get(getApplicationContext()).news_update(item);
                 //}

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for (int i=0;i<mNewsList.size();i++){
            if(mNewsList.get(i).getId()==mCurrentNews.getId()){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
