package com.wolff.wnews.localdb;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WChannelGroup;
import com.wolff.wnews.model.WNews;
import com.wolff.wnews.utils.DateUtils;

/**
 * Created by wolff on 23.05.2017.
 */

public class DbCursorWrapper extends CursorWrapper {

      public DbCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public WChannel getWChannel(boolean isMenu){
        WChannel channel = new WChannel();
        channel.setId(getInt(getColumnIndex(DbSchema.BaseColumns.ID)));
        channel.setName(getString(getColumnIndex(DbSchema.BaseColumns.NAME)));
        channel.setPubDate(new DateUtils().dateFromString(getString(getColumnIndex(DbSchema.BaseColumns.PUB_DATE)),DateUtils.DATE_FORMAT_SAVE));
        channel.setTitle(getString(getColumnIndex(DbSchema.BaseColumns.TITLE)));
        channel.setLink(getString(getColumnIndex(DbSchema.BaseColumns.LINK)));
        channel.setDescription(getString(getColumnIndex(DbSchema.BaseColumns.DESCRIPTION)));
        channel.setLanguage(getString(getColumnIndex(DbSchema.Table_Channel.Cols.LANGUAGE)));
        channel.setImage(getString(getColumnIndex(DbSchema.Table_Channel.Cols.IMAGE)));
        channel.setRating(getString(getColumnIndex(DbSchema.Table_Channel.Cols.RATING)));
        channel.setIdGroup(getInt(getColumnIndex(DbSchema.Table_Channel.Cols.ID_GROUP)));

        //menu
        if(isMenu) {
            channel.setMenu_items_all(getLong(getColumnIndex(DbSchema.Table_Channel.Cols.MENU_ITEMS_ALL)));
            channel.setMenu_items_read(getLong(getColumnIndex(DbSchema.Table_Channel.Cols.MENU_ITEMS_READ)));
        }
        return channel;
     }
    public WChannelGroup getWChannelGroup(){
        WChannelGroup channelGroup = new WChannelGroup();
        channelGroup.setId(getInt(getColumnIndex(DbSchema.BaseColumns.ID)));
        channelGroup.setName(getString(getColumnIndex(DbSchema.BaseColumns.NAME)));
        return channelGroup;
    }

    public WNews getWNews(){
        WNews news = new WNews();
        news.setId(getInt(getColumnIndex(DbSchema.BaseColumns.ID)));
        news.setName(getString(getColumnIndex(DbSchema.BaseColumns.NAME)));
        news.setPubDate(new DateUtils().dateFromString(getString(getColumnIndex(DbSchema.BaseColumns.PUB_DATE)),DateUtils.DATE_FORMAT_SAVE));
        news.setTitle(getString(getColumnIndex(DbSchema.BaseColumns.TITLE)));
        news.setLink(getString(getColumnIndex(DbSchema.BaseColumns.LINK)));
        news.setDescription(getString(getColumnIndex(DbSchema.BaseColumns.DESCRIPTION)));

        news.setGuid(getString(getColumnIndex(DbSchema.Table_News.Cols.GUID)));
        news.setEnclosure(getString(getColumnIndex(DbSchema.Table_News.Cols.ENCLOSURE)));
        news.setEnclosure_type(getString(getColumnIndex(DbSchema.Table_News.Cols.ENCLOSURE_TYPE)));
        news.setIdChannel(getInt(getColumnIndex(DbSchema.Table_News.Cols.ID_CHANNEL)));
        news.setReaded(getInt(getColumnIndex(DbSchema.Table_News.Cols.IS_READ))==1);
        if(news.getPubDate()==null) {
            Log.e("GET NEWS", "------------------------------------------------------------------------------------------------------------------------------------");
            Log.e("GET NEWS", "Date = " + new DateUtils().dateFromString(getString(getColumnIndex(DbSchema.BaseColumns.PUB_DATE)), DateUtils.DATE_FORMAT_SAVE));
            Log.e("GET DATE", "GUID = " + news.getGuid());
            //Log.e("GET DATE","DATE = "+news.getPubDate());
            Log.e("GET DATE", "===================================================================================================================================");
        }
            return news;
    }
 }
