package com.wolff.wnews.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wolff.wnews.localdb.DataLab;
import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.utils.MySettings;
import com.wolff.wnews.utils.WriteNewsToLocalBD;

import java.util.ArrayList;
import java.util.Date;
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
        //Log.e(TAG,"Bind");
        return null;
    }

    @Override
    public void onCreate() {
        //Log.e(TAG,"Create");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.e(TAG,"Start command");
        getAllNews();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //Log.e(TAG,"Destroy");
        super.onDestroy();
    }
    private void getAllNews(){
        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(3);
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //Log.e("TASK","RUN "+new Date()+"    online = "+isOnline());
                if(!isOnline()){
                    //Log.e("INTERNET","NOT ONLINE");
                    return;
                }
                ArrayList<WChannel> channels = DataLab.get(getApplicationContext()).getWChannelsList();
                for(WChannel item:channels) {
                   // Log.e("CHANNELS",""+item.getLink());
                    WriteNewsToLocalBD task = new WriteNewsToLocalBD(getApplicationContext());
                    task.readNewsFromChannelAndWriteToLocalBD(item);
                }
             }
    },0, new MySettings().UPDATE_PERIOD_MINUTES, TimeUnit.MINUTES);
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
