package com.wolff.wnews.service;

import android.os.AsyncTask;

import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.utils.WriteChannelToLocalDB;

/**
 * Created by wolff on 18.07.2017.
 */

public class GetChannelInfo_Task extends AsyncTask<String,Void,WChannel> {
    @Override
    protected WChannel doInBackground(String... params) {
        WriteChannelToLocalDB ww = new WriteChannelToLocalDB();
        return ww.readChannelFromInet(params[0]);

    }

    @Override
    protected void onPostExecute(WChannel channel) {
        super.onPostExecute(channel);
    }
}
