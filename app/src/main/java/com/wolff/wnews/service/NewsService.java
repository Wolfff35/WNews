package com.wolff.wnews.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;

import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.utils.WriteNewsToLocalBD;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wolff on 07.07.2017.
 */

public class NewsService extends Service {
    public static final String TAG ="SERVICE";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getAllNews();
        deleteOldNews();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void deleteOldNews(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final int days = Integer.valueOf(preferences.getString("deleteNewsPeriod_days","0"));
        if(days>0) {
            final ScheduledExecutorService scheduler =
                    Executors.newScheduledThreadPool(3);
            scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    if (!isOnline()) {
                        return;
                    }
                    DataLab.get(getApplicationContext()).deleteOldNews(days);
                }
            }, 0, 60, TimeUnit.MINUTES);
        }
    }
    private void getAllNews(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(3);
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                if(!isOnline()){
                    return;
                }
                 ArrayList<WChannel> channels = DataLab.get(getApplicationContext()).getWChannelsList();
                for(WChannel item:channels) {
                    WriteNewsToLocalBD task = new WriteNewsToLocalBD(getApplicationContext());
                    task.readNewsFromChannelAndWriteToLocalBD(item);
                }
             }
    },0, Integer.valueOf(preferences.getString("updatePeriod_minutes","5")), TimeUnit.MINUTES);
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
