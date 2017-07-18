package com.wolff.wnews.localdb;

import android.content.Context;
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
    public WChannel getWChannel(){
        WChannel channel = new WChannel();
        channel.setId(getInt(getColumnIndex(DbSchema.BaseColumns.ID)));
        channel.setName(getString(getColumnIndex(DbSchema.BaseColumns.NAME)));
        channel.setPubDate(new DateUtils().dateFromString(getString(getColumnIndex(DbSchema.BaseColumns.PUB_DATE)),DateUtils.DATE_FORMAT_SAVE));
        channel.setTitle(getString(getColumnIndex(DbSchema.BaseColumns.TITLE)));
        channel.setLink(getString(getColumnIndex(DbSchema.BaseColumns.LINK)));
        channel.setDescription(getString(getColumnIndex(DbSchema.BaseColumns.DESCRIPTION)));
        channel.setLanguage(getString(getColumnIndex(DbSchema.Table_Channel.Cols.LANGUAGE)));
        channel.setCopyright(getString(getColumnIndex(DbSchema.Table_Channel.Cols.COPYRIGHT)));
        channel.setManagingEditor(getString(getColumnIndex(DbSchema.Table_Channel.Cols.MANAGING_EDITOR)));
        channel.setWebMaster(getString(getColumnIndex(DbSchema.Table_Channel.Cols.WEB_MASTER)));
        channel.setLastBuildDate(getString(getColumnIndex(DbSchema.Table_Channel.Cols.LAST_BUILD_DATE)));
        channel.setCategory(getString(getColumnIndex(DbSchema.Table_Channel.Cols.CATEGORY)));
        channel.setGenerator(getString(getColumnIndex(DbSchema.Table_Channel.Cols.GENERATOR)));
        channel.setDocs(getString(getColumnIndex(DbSchema.Table_Channel.Cols.DOCS)));
        channel.setCloud(getString(getColumnIndex(DbSchema.Table_Channel.Cols.CLOUD)));
        channel.setTtl(getInt(getColumnIndex(DbSchema.Table_Channel.Cols.TTL)));
        channel.setImage(getString(getColumnIndex(DbSchema.Table_Channel.Cols.IMAGE)));
        channel.setRating(getString(getColumnIndex(DbSchema.Table_Channel.Cols.RATING)));
        channel.setTextInput(getString(getColumnIndex(DbSchema.Table_Channel.Cols.TEXT_INPUT)));
        channel.setSkipDays(getString(getColumnIndex(DbSchema.Table_Channel.Cols.SKIP_DAYS)));
        channel.setSkipHours(getString(getColumnIndex(DbSchema.Table_Channel.Cols.SKIP_HOURS)));
        channel.setIdGroup(getInt(getColumnIndex(DbSchema.Table_Channel.Cols.ID_GROUP)));
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
        news.setAuthor(getString(getColumnIndex(DbSchema.Table_News.Cols.AUTHOR)));
        news.setCategory(getString(getColumnIndex(DbSchema.Table_News.Cols.CATEGORY)));
        news.setComments(getString(getColumnIndex(DbSchema.Table_News.Cols.COMMENTS)));
        news.setEnclosure(getString(getColumnIndex(DbSchema.Table_News.Cols.ENCLOSURE)));
        news.setSource(getString(getColumnIndex(DbSchema.Table_News.Cols.SOURCE)));
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
