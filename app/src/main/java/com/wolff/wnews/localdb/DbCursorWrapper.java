package com.wolff.wnews.localdb;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

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
        channel.setPubDate(new DateUtils().dateFromString(getString(getColumnIndex(DbSchema.BaseColumns.pubDate)),DateUtils.DATE_FORMAT_SAVE));
        channel.setTitle(getString(getColumnIndex(DbSchema.BaseColumns.title)));
        channel.setLink(getString(getColumnIndex(DbSchema.BaseColumns.link)));
        channel.setDescription(getString(getColumnIndex(DbSchema.BaseColumns.description)));
        channel.setLanguage(getString(getColumnIndex(DbSchema.Table_Channel.Cols.language)));
        channel.setCopyright(getString(getColumnIndex(DbSchema.Table_Channel.Cols.copyright)));
        channel.setManagingEditor(getString(getColumnIndex(DbSchema.Table_Channel.Cols.managingEditor)));
        channel.setWebMaster(getString(getColumnIndex(DbSchema.Table_Channel.Cols.webMaster)));
        channel.setLastBuildDate(getString(getColumnIndex(DbSchema.Table_Channel.Cols.lastBuildDate)));
        channel.setCategory(getString(getColumnIndex(DbSchema.Table_Channel.Cols.category)));
        channel.setGenerator(getString(getColumnIndex(DbSchema.Table_Channel.Cols.generator)));
        channel.setDocs(getString(getColumnIndex(DbSchema.Table_Channel.Cols.docs)));
        channel.setCloud(getString(getColumnIndex(DbSchema.Table_Channel.Cols.cloud)));
        channel.setTtl(getInt(getColumnIndex(DbSchema.Table_Channel.Cols.ttl)));
        channel.setImage(getString(getColumnIndex(DbSchema.Table_Channel.Cols.image)));
        channel.setRating(getString(getColumnIndex(DbSchema.Table_Channel.Cols.rating)));
        channel.setTextInput(getString(getColumnIndex(DbSchema.Table_Channel.Cols.textInput)));
        channel.setSkipDays(getString(getColumnIndex(DbSchema.Table_Channel.Cols.skipDays)));
        channel.setSkipHours(getString(getColumnIndex(DbSchema.Table_Channel.Cols.skipHours)));
        channel.setIdGroup(getInt(getColumnIndex(DbSchema.Table_Channel.Cols.idGroup)));
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
        news.setPubDate(new DateUtils().dateFromString(getString(getColumnIndex(DbSchema.BaseColumns.pubDate)),DateUtils.DATE_FORMAT_SAVE));
        news.setTitle(getString(getColumnIndex(DbSchema.BaseColumns.title)));
        news.setLink(getString(getColumnIndex(DbSchema.BaseColumns.link)));
        news.setDescription(getString(getColumnIndex(DbSchema.BaseColumns.description)));

        news.setGuid(getString(getColumnIndex(DbSchema.Table_News.Cols.guid)));
        news.setAuthor(getString(getColumnIndex(DbSchema.Table_News.Cols.author)));
        news.setCategory(getString(getColumnIndex(DbSchema.Table_News.Cols.category)));
        news.setComments(getString(getColumnIndex(DbSchema.Table_News.Cols.comments)));
        news.setEnclosure(getString(getColumnIndex(DbSchema.Table_News.Cols.enclosure)));
        news.setSource(getString(getColumnIndex(DbSchema.Table_News.Cols.source)));
        news.setIdChannel(getInt(getColumnIndex(DbSchema.Table_News.Cols.idChannel)));
        news.setReaded(getInt(getColumnIndex(DbSchema.Table_News.Cols.isRead))==1);

        return news;
    }
 }
