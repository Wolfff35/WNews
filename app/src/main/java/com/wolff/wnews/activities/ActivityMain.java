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

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,News_list_fragment_viewPager.News_list_fragment_listener,
        ChannelGroup_list_fragment.ChannelGroup_list_fragment_listener,Channel_list_fragment.Channel_list_fragment_listener {

    private int mCurrentChannelId;
    private DrawerLayout drawer;

    private LinearLayout mMainContainer;
    private ViewPager mViewPager_News;
    private int mCurrentNewsScreen=0;//текущий экран новостей

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TEST
        TestData testData = new TestData();
        testData.fillTestData(getApplicationContext());

        setTheme(new MySettings().CURRENT_THEME);
        setContentView(R.layout.activity_main);
         mMainContainer = (LinearLayout) findViewById(R.id.fragment_container_main);
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
        displayFragment(null);

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
                Settings_fragment fragment = Settings_fragment.newInstance();
                displayFragment(fragment);
                break;
            }
            case R.id.action_edit_groups:{
                ChannelGroup_list_fragment fragment = ChannelGroup_list_fragment.newInstance();
                displayFragment(fragment);
                break;
            }
            case R.id.action_edit_channels:{
                Channel_list_fragment fragment = Channel_list_fragment.newInstance();
                displayFragment(fragment);
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
        displayFragment(null);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
     private void displayFragment(Fragment fragment) {
         Log.e("displayFragment","DISPLAY");
         if(fragment==null){
             mMainContainer.setVisibility(View.INVISIBLE);
             mMainContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
             mViewPager_News.setVisibility(View.VISIBLE);
             LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
             mViewPager_News.setLayoutParams(params);
             DataLab dataLab = DataLab.get(getApplicationContext());
             final ArrayList<WNews> allNews = dataLab.getWNewsList(mCurrentChannelId);
             mViewPager_News.setPageTransformer(true, new ZoomOutPageTransformer());
             mViewPager_News.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        mCurrentNewsScreen = position;
                        ArrayList<WNews> partNews = getPartNews(allNews,mCurrentNewsScreen);
                        return News_list_fragment_viewPager.newInstance(partNews,mCurrentChannelId);
                    }

                    @Override
                    public int getCount() {
                        int l = allNews.size();
                        if(l<=MySettings.NEWS_PER_SCREEN){
                            return 1;
                        }else {
                            return ((l - l % MySettings.NEWS_PER_SCREEN) / MySettings.NEWS_PER_SCREEN);
                        }
                    }
                });
         }else {
             mMainContainer.setVisibility(View.VISIBLE);
             mMainContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
             mViewPager_News.setVisibility(View.INVISIBLE);
             LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
             mViewPager_News.setLayoutParams(params);
             FragmentTransaction fragmentTransaction;
             fragmentTransaction = getSupportFragmentManager().beginTransaction();
             fragmentTransaction.replace(R.id.fragment_container_main, fragment);
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
