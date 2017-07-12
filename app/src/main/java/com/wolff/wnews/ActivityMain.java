package com.wolff.wnews;

import android.content.Intent;
import android.os.Bundle;
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

import com.wolff.wnews.fragments.News_list_fragment;
import com.wolff.wnews.model.WNews;
import com.wolff.wnews.service.NewsService;
import com.wolff.wnews.utils.CreateMenu;
import com.wolff.wnews.utils.TestData;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,News_list_fragment.News_list_fragment_listener {

    private int mCurrentChannelId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        startService(new Intent(this, NewsService.class));
        new CreateMenu().createMenu(getApplicationContext(),navigationView.getMenu());
        //TEST
        TestData testData = new TestData();
        testData.fillTestData(getApplicationContext());
        News_list_fragment fragment = News_list_fragment.newInstance(0);
        displayFragment(fragment);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();
        switch (id){
            case R.id.action_exit:{
                //stopService(new Intent(this,NewsService.class));
                break;
            }
            case R.id.action_switch_theme:{
                break;
            }
            case R.id.action_settings:{
                break;
            }
            case R.id.action_edit_groups:{
                break;
            }
              default:
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        mCurrentChannelId = id;
        Log.e("SELECT","Channel id = "+mCurrentChannelId);
        News_list_fragment fragment = News_list_fragment.newInstance(mCurrentChannelId);
        displayFragment(fragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void displayFragment(android.support.v4.app.Fragment fragment) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frafment_container_main, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onNewsSelected(WNews news) {
        Log.e("onNewsSelected",""+news.getTitle());
    }
}
