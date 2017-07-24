package com.wolff.wnews.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.wolff.wnews.utils.MySettings;
import com.wolff.wnews.utils.TestData;
import com.wolff.wnews.utils.ZoomOutPageTransformer;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,News_list_fragment_viewPager.News_list_fragment_listener,
        ChannelGroup_list_fragment.ChannelGroup_list_fragment_listener,Channel_list_fragment.Channel_list_fragment_listener {

    private int mCurrentChannelId;
    private DrawerLayout drawer;

    private LinearLayout mMainContainer;
    private LinearLayout mPagerContainer;
    private ViewPager mViewPager_News;
    private int mCurrentNewsScreen=0;//текущий экран новостей
    private ArrayList<WNews> mAllNews;
    private Fragment mMainFragment;
    private Fragment mOldFragment;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TEST
        TestData testData = new TestData();
        testData.fillTestData(getApplicationContext());

        setTheme(new MySettings().CURRENT_THEME);
        setContentView(R.layout.activity_main);
         mMainContainer = (LinearLayout) findViewById(R.id.fragment_container_main);
         mPagerContainer = (LinearLayout) findViewById(R.id.fragment_container_pager);
         mViewPager_News = (ViewPager) findViewById(R.id.viewPager_news_container);

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
        mMainFragment=null;
         mOldFragment=null;
        displayFragment();

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
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();
        switch (id){
            case R.id.action_exit:{
                stopService(new Intent(this,NewsService.class));
                finish();
                break;
            }
            case R.id.action_switch_theme:{
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
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        mCurrentChannelId = id;
        mCurrentNewsScreen=1;
        mOldFragment=mMainFragment;
        mMainFragment=null;
        displayFragment();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
     private void displayFragment() {
         Log.e("displayFragment","DISPLAY");
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
             mViewPager_News.setAdapter(fragmentStatePagerAdapter);

             mViewPager_News.setCurrentItem(mCurrentNewsScreen);
         }else {
             changeLayouts(false);

             FragmentTransaction fragmentTransaction;
             fragmentTransaction = getSupportFragmentManager().beginTransaction();
             fragmentTransaction.replace(R.id.fragment_container_main, mMainFragment);
             fragmentTransaction.commit();
         }

    }
    private ArrayList<WNews> getPartNews(ArrayList<WNews> allNews,int currentScreen){
        ArrayList<WNews> partNews = new ArrayList<>(MySettings.NEWS_PER_SCREEN);
        for(int i=currentScreen*MySettings.NEWS_PER_SCREEN;i<currentScreen*MySettings.NEWS_PER_SCREEN+MySettings.NEWS_PER_SCREEN;i++){
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
    private FragmentStatePagerAdapter fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            mCurrentNewsScreen = position;
            ArrayList<WNews> partNews = getPartNews(mAllNews,mCurrentNewsScreen);
            return News_list_fragment_viewPager.newInstance(partNews,mCurrentChannelId,mCurrentNewsScreen);
        }

        @Override
        public int getCount() {
            int l = mAllNews.size();
            if(l<=MySettings.NEWS_PER_SCREEN){
                return 1;
            }else {
                return ((l - l % MySettings.NEWS_PER_SCREEN) / MySettings.NEWS_PER_SCREEN);
            }
        }
    };
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
        Intent intent = News_item_activity.newIntent(getApplicationContext(),newsList,news);
        if(MySettings.MARK_AS_READ_IF_OPEN&&!news.isReaded()){
            news.setReaded(true);
            DataLab.get(getApplicationContext()).news_update(news);
        }
        startActivity(intent);

    }
}
