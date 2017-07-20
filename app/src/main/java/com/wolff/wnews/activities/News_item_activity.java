package com.wolff.wnews.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.wolff.wnews.R;
import com.wolff.wnews.fragments.News_item_fragment;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_item_activity);
        mViewPager = (ViewPager)findViewById(R.id.viewPager_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mNewsList = (List<WNews>) getIntent().getSerializableExtra(EXTRA_WNEWSLIST);
        mCurrentNews = (WNews)getIntent().getSerializableExtra(EXTRA_WNEWS);
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
        for (int i=0;i<mNewsList.size();i++){
            if(mNewsList.get(i).getId()==mCurrentNews.getId()){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}