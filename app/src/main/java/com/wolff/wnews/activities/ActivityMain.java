package com.wolff.wnews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wolff.wnews.R;
import com.wolff.wnews.fragments.ChannelGroup_list_fragment;
import com.wolff.wnews.fragments.Channel_list_fragment;
import com.wolff.wnews.fragments.News_list_fragment_viewPager;
import com.wolff.wnews.fragments.Settings_fragment;
import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WChannelGroup;
import com.wolff.wnews.model.WNews;
import com.wolff.wnews.service.NewsService;
import com.wolff.wnews.utils.CreateMenu;
import com.wolff.wnews.utils.ZoomOutPageTransformer;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,News_list_fragment_viewPager.News_list_fragment_listener,
        ChannelGroup_list_fragment.ChannelGroup_list_fragment_listener,Channel_list_fragment.Channel_list_fragment_listener {

    private DrawerLayout drawer;

    private Fragment mMainFragment;
    private Fragment mOldFragment;

    private LinearLayout mMainContainer;
    private LinearLayout mPagerContainer;
    private ViewPager mViewPager_News;

    private boolean mMarkAsREadIfSwap;
    private int mPreviousPageNumber;
    private int mCountNewsScreen;// количество страниц/экранов
    private int mCurrentChannelId=0;
    private int mCountNewsPerScreen;//новостей на странице/экране
    private ArrayList<WNews> mAllNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTheme();
        setContentView(R.layout.activity_main);
        mMainContainer = (LinearLayout) findViewById(R.id.fragment_container_main);
        mPagerContainer = (LinearLayout) findViewById(R.id.fragment_container_pager);
        mViewPager_News = (ViewPager) findViewById(R.id.viewPager_news_container);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mCountNewsPerScreen = Integer.valueOf(preferences.getString("countNewsPerScreen","5"));
        mMarkAsREadIfSwap = preferences.getBoolean("markAsReadIfSwap",false);
        mPreviousPageNumber=0;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
                public void onDrawerOpened(View drawerView){
                    super.onDrawerOpened(drawerView);
                    new CreateMenu().createMenu(getApplicationContext(),navigationView.getMenu());
                }
          };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        startService(new Intent(this, NewsService.class));
        displayFragment();
        setWindowTitle();

    }
     @Override
    protected void onResume() {
        super.onResume();
        mFragmentStatePagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
         return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem_action_mark_all_as_read = menu.findItem(R.id.action_mark_all_as_read);
        if(menuItem_action_mark_all_as_read!=null) {
            if (mMainFragment == null) {
                menuItem_action_mark_all_as_read.setVisible(true);
            } else {
                menuItem_action_mark_all_as_read.setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();
        switch (id){
            case R.id.action_exit:{
                stopService(new Intent(this,NewsService.class));
                finish();
                break;
            }
            case R.id.action_settings:{
                mOldFragment=mMainFragment;
                mMainFragment = Settings_fragment.newInstance();
                displayFragment();
                break;
            }
            case R.id.action_edit_groups:{
                mOldFragment=mMainFragment;
                mMainFragment = ChannelGroup_list_fragment.newInstance();
                displayFragment();
                break;
            }
            case R.id.action_edit_channels:{
                mOldFragment=mMainFragment;
                mMainFragment = Channel_list_fragment.newInstance();
                displayFragment();
                break;
            }
            case R.id.action_delete_all:{

                DataLab dataLab = DataLab.get(getApplicationContext());
                dataLab.deleteOldNews(0);
                dataLab.deleteChannels();
                dataLab.deleteGroups();
                break;
            }
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        mCurrentChannelId = item.getItemId();
        mOldFragment=mMainFragment;
        mMainFragment=null;
        displayFragment();
        setWindowTitle();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
     private void displayFragment() {
         if(mOldFragment!=null){
             FragmentTransaction fragmentTransaction;
             fragmentTransaction = getSupportFragmentManager().beginTransaction();
             fragmentTransaction.remove(mOldFragment);
             fragmentTransaction.commit();
             mOldFragment=null;
         }
         if(mMainFragment==null){
             changeLayouts(true);
             DataLab dataLab = DataLab.get(getApplicationContext());
             mAllNews = dataLab.getWNewsList(mCurrentChannelId);
             mViewPager_News.setPageTransformer(true, new ZoomOutPageTransformer());
             mViewPager_News.setAdapter(mFragmentStatePagerAdapter);
             mViewPager_News.addOnPageChangeListener(onPageChangeListener);
         }else {
             changeLayouts(false);
             FragmentTransaction fragmentTransaction;
             fragmentTransaction = getSupportFragmentManager().beginTransaction();
             fragmentTransaction.replace(R.id.fragment_container_main, mMainFragment);
             fragmentTransaction.commit();
         }
         invalidateOptionsMenu();
    }
    private ArrayList<WNews> getPartNews(ArrayList<WNews> allNews,int currentScreen){
         ArrayList<WNews> partNews = new ArrayList<>(mCountNewsPerScreen);
        for(int i=currentScreen*mCountNewsPerScreen;i<currentScreen*mCountNewsPerScreen+mCountNewsPerScreen;i++){
            if(i<allNews.size()) {
                partNews.add(allNews.get(i));
            }
        }
    return partNews;
    }

    private void changeLayouts(boolean isViewPager){
        if(isViewPager){
            mMainContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mMainContainer.setVisibility(View.GONE);
            mPagerContainer.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            mPagerContainer.setVisibility(View.VISIBLE);

        }else {
            mMainContainer.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            mMainContainer.setVisibility(View.VISIBLE);
            mPagerContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mPagerContainer.setVisibility(View.GONE);
            mMainContainer.invalidate();
            mPagerContainer.invalidate();
        }
    }

    private FragmentStatePagerAdapter mFragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            ArrayList<WNews> partNews = getPartNews(mAllNews,position);
            return News_list_fragment_viewPager.newInstance(partNews,mCurrentChannelId,position,mCountNewsScreen);
        }

        @Override
        public int getItemPosition(Object object) {
             super.getItemPosition(object);
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            int l = mAllNews.size();
            if(l<=mCountNewsPerScreen){
                mCountNewsScreen=1;
            }else {
                mCountNewsScreen = ((l - l % mCountNewsPerScreen) / mCountNewsPerScreen);
                if(l%mCountNewsScreen!=0){
                    mCountNewsScreen = mCountNewsScreen+1;
                }
            }
            return mCountNewsScreen;
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
             if(mMarkAsREadIfSwap){
                 mPreviousPageNumber=position-1;
                 if(mPreviousPageNumber<0) mPreviousPageNumber=0;
                 ArrayList<WNews> partNews = getPartNews(mAllNews,mPreviousPageNumber);
                for(WNews item:partNews){
                    if(!item.isReaded()){
                        item.setReaded(true);
                        DataLab.get(getApplicationContext()).news_update(item);
                    }
                }
                mFragmentStatePagerAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    //----------------------------------------------------------------------------------------------
    @Override
    public void onChannelGroupSelected(WChannelGroup group) {
        Intent intent = ChannelGroup_item_activity.newIntent(getApplicationContext(),group);
        startActivity(intent);
    }

    @Override
    public void onChannelSelected(WChannel channel) {
        Intent intent = Channel_item_activity.newIntent(getApplicationContext(),channel);
        startActivity(intent);

    }

    @Override
    public void onNewsSelected_vp(ArrayList<WNews> newsList, WNews news) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean markAsReadIfOpen = preferences.getBoolean("markAsReadIfOpen",false);
        Intent intent = News_item_activity.newIntent(getApplicationContext(),newsList,news);
        if(markAsReadIfOpen&&!news.isReaded()){
            news.setReaded(true);
            DataLab.get(getApplicationContext()).news_update(news);
        }
        startActivity(intent);
    }
    //----------------------------------------------------------------------------------------------
    private void setWindowTitle(){
        if(mCurrentChannelId==0){
            setTitle(getResources().getString(R.string.app_name)+" Все новости ");
        }else {
            setTitle(getResources().getString(R.string.app_name)+" "+DataLab.get(getApplicationContext()).findChannelById(mCurrentChannelId,
                    DataLab.get(getApplicationContext()).getWChannelsList()).getName());
        }
    }
    private void setActivityTheme(){
        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isLightTheme = preferences.getBoolean("isLightTheme",false);
        if(isLightTheme){
            setTheme(R.style.AppThemeLight_NoActionBar);
        }else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
    }
}