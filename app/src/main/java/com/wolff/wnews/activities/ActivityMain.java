package com.wolff.wnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wolff.wnews.R;
import com.wolff.wnews.fragments.ChannelGroup_list_fragment;
import com.wolff.wnews.fragments.Channel_list_fragment;
import com.wolff.wnews.fragments.News_list_fragment;
import com.wolff.wnews.fragments.Settings_fragment;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WChannelGroup;
import com.wolff.wnews.model.WNews;
import com.wolff.wnews.service.NewsService;
import com.wolff.wnews.utils.CreateMenu;
import com.wolff.wnews.utils.MySettings;
import com.wolff.wnews.utils.TestData;

import java.util.ArrayList;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,News_list_fragment.News_list_fragment_listener,
        ChannelGroup_list_fragment.ChannelGroup_list_fragment_listener,Channel_list_fragment.Channel_list_fragment_listener {

    private int mCurrentChannelId;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TEST
        TestData testData = new TestData();
        testData.fillTestData(getApplicationContext());

        setTheme(new MySettings().CURRENT_THEME);
        setContentView(R.layout.activity_main);
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
         News_list_fragment fragment = News_list_fragment.newInstance(0);
        displayFragment(fragment);

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
        if(mCurrentChannelId==0){
            News_list_fragment fragment = News_list_fragment.newInstance(0);
            displayFragment(fragment);
        }else {
            News_list_fragment fragment = News_list_fragment.newInstance(mCurrentChannelId);
            displayFragment(fragment);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
     private void displayFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_main, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onNewsSelected(ArrayList<WNews>listNews,WNews news) {
        Intent intent = News_item_activity.newIntent(getApplicationContext(),listNews,news);
        startActivity(intent);
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
}
