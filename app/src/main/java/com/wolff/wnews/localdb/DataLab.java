package com.wolff.wnews.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.wolff.wnews.model.WChannel;
import com.wolff.wnews.model.WChannelGroup;
import com.wolff.wnews.model.WNews;
import com.wolff.wnews.utils.DateUtils;

import java.util.ArrayList;

/**
 * Created by wolff on 23.05.2017.
 */

public class DataLab {
    private static DataLab sDataLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private DataLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new DbHelper(mContext).getWritableDatabase();
    }
    public static DataLab get(Context context){
        if(sDataLab==null){
            sDataLab = new DataLab(context);
        }
        return sDataLab;
    }

private DbCursorWrapper queryWNews(long idChannel){
    String selection;
    String[] selectionArgs;
    String[] columns = null;
    String groupBy = null;
    String having = null;
    String orderBy = DbSchema.BaseColumns.PUB_DATE+" DESC";
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    boolean onlyUnreadedNews = preferences.getBoolean("onlyUnreadedNews",false);
    if(onlyUnreadedNews){
        if (idChannel == 0) {
            selection = DbSchema.Table_News.Cols.IS_READ + " = ?";
            selectionArgs = new String[]{"0"};
        } else {
            selection = DbSchema.Table_News.Cols.ID_CHANNEL + " = ? AND "+DbSchema.Table_News.Cols.IS_READ + " = ?";
            selectionArgs = new String[]{"" + idChannel,"0"};
        }

    }else {
        if (idChannel == 0) {
            selection = null;
            selectionArgs = null;
        } else {
            selection = DbSchema.Table_News.Cols.ID_CHANNEL + " = ?";
            selectionArgs = new String[]{"" + idChannel};
        }
    }
     Cursor cursor = mDatabase.query(DbSchema.Table_News.TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            groupBy,
            having,
            orderBy);
    return new DbCursorWrapper(cursor);
}
    private DbCursorWrapper queryWNewsByGuid(String guid){
        String selection;
        String[] selectionArgs;
        String[] columns = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        selection = DbSchema.Table_News.Cols.GUID+" = ?";
        selectionArgs = new String[]{""+guid};
        Cursor cursor = mDatabase.query(DbSchema.Table_News.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);
        return new DbCursorWrapper(cursor);
    }
    public WNews getWNewsByGuid(String guid){
        DbCursorWrapper cursorWrapper = queryWNewsByGuid(guid);
        if(cursorWrapper.getCount()>0) {
            cursorWrapper.moveToFirst();
            WNews news = cursorWrapper.getWNews();
            cursorWrapper.close();
            return news;
        }else {
            return null;
        }
    }

    public ArrayList<WNews> getWNewsList(long idChannel){
        DbCursorWrapper cursorWrapper = queryWNews(idChannel);
        ArrayList<WNews> newsList = new ArrayList<>();
        cursorWrapper.moveToFirst();
        while (!cursorWrapper.isAfterLast()) {
            WNews news = cursorWrapper.getWNews();
            newsList.add(news);
            //Log.e("GUID",""+news.getGuid());
            cursorWrapper.moveToNext();
        }
        cursorWrapper.close();
        //Log.e("READ NEWS","FROM LOCAL DB; Channel = "+idChannel+"; Количество = "+newsList.size());
        return newsList;
    }
    private static ContentValues getContentValues_WNews(WNews news){
        ContentValues values = new ContentValues();
        values.put(DbSchema.BaseColumns.NAME,news.getName());
        values.put(DbSchema.BaseColumns.DESCRIPTION,news.getDescription());
        values.put(DbSchema.BaseColumns.PUB_DATE,new DateUtils().dateToString(news.getPubDate(),DateUtils.DATE_FORMAT_SAVE));
        values.put(DbSchema.BaseColumns.LINK,news.getLink());
        values.put(DbSchema.BaseColumns.TITLE,news.getTitle());
        values.put(DbSchema.Table_News.Cols.ID_CHANNEL,news.getIdChannel());
        values.put(DbSchema.Table_News.Cols.ENCLOSURE,news.getEnclosure());
        values.put(DbSchema.Table_News.Cols.ENCLOSURE_TYPE,news.getEnclosure_type());
        values.put(DbSchema.Table_News.Cols.GUID,news.getGuid());
        if(news.isReaded()) {
            values.put(DbSchema.Table_News.Cols.IS_READ, 1);
        }else {
            values.put(DbSchema.Table_News.Cols.IS_READ, 0);
        }
        //Log.e("getContentValues_WNews","GUID = "+news.getGuid());
        //Log.e("getContentValues_WNews","ID CHANNEL = "+news.getIdChannel());
        //Log.e("getContentValues_WNews",""+new DateUtils().dateToString(news.getPubDate(),DateUtils.DATE_FORMAT_SAVE));
        //Log.e("getContentValues_WNews","=============================================================================");

        return values;
    }

     public void news_add(WNews news){
        ContentValues values = getContentValues_WNews(news);
        mDatabase.insert(DbSchema.Table_News.TABLE_NAME,null,values);
        //Log.e("add news","Success   DATE = "+news.getPubDate()+";    "+news.getTitle());
    }
    public void news_update(WNews news){
        ContentValues values = getContentValues_WNews(news);
        String table = DbSchema.Table_News.TABLE_NAME;
        mDatabase.update(
                table,
                values,
                DbSchema.BaseColumns.ID+" = ?",
                new String[]{String.valueOf(news.getId())}
        );
        //Log.e("update news"," Success");
    }

    public void news_delete(WNews news){
        mDatabase.delete(
                DbSchema.Table_News.TABLE_NAME,
                DbSchema.BaseColumns.ID+" =?",
                new String[]{String.valueOf(news.getId())}
        );
        //Log.e("delete news","Success");
    }
//===================================================================================================
private DbCursorWrapper queryWChannels(){
    String selection;
    String[] selectionArgs;
    String[] columns = null;
    String groupBy = null;
    String having = null;
    String orderBy = null;
    selection = null;
    selectionArgs = null;
    Cursor cursor = mDatabase.query(DbSchema.Table_Channel.TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            groupBy,
            having,
            orderBy);
    return new DbCursorWrapper(cursor);
}
     public ArrayList<WChannel> getWChannelsList(){
        DbCursorWrapper cursorWrapper = queryWChannels();
        ArrayList<WChannel> channelsList = new ArrayList<>();
        cursorWrapper.moveToFirst();
        while (!cursorWrapper.isAfterLast()) {
            channelsList.add(cursorWrapper.getWChannel(false));
            cursorWrapper.moveToNext();
        }
        cursorWrapper.close();
        // Log.e("READ CHANNELS","FROM LOCAL DB; Количество = "+channelsList.size());
         return channelsList;
    }
    private static ContentValues getContentValues_WChannels(WChannel channel){
        ContentValues values = new ContentValues();
        values.put(DbSchema.BaseColumns.NAME,channel.getName());
        values.put(DbSchema.BaseColumns.DESCRIPTION,channel.getDescription());
        values.put(DbSchema.BaseColumns.PUB_DATE,new DateUtils().dateToString(channel.getPubDate(),DateUtils.DATE_FORMAT_SAVE));
        values.put(DbSchema.BaseColumns.LINK,channel.getLink());
        values.put(DbSchema.BaseColumns.TITLE,channel.getTitle());
        values.put(DbSchema.Table_Channel.Cols.ID_GROUP,channel.getIdGroup());
        return values;
    }

     public void channel_add(WChannel channel){
        ContentValues values = getContentValues_WChannels(channel);
        mDatabase.insert(DbSchema.Table_Channel.TABLE_NAME,null,values);
        //Log.e("add channel","Success "+channel.getTitle());
    }
    public void channel_update(WChannel channel){
        ContentValues values = getContentValues_WChannels(channel);
        String table = DbSchema.Table_Channel.TABLE_NAME;
        mDatabase.update(
                table,
                values,
                DbSchema.BaseColumns.LINK+" = ?",
                new String[]{String.valueOf(channel.getLink())}
        );
        //Log.e("update channel"," Success");
    }

    public void channel_delete(WChannel channel){
        mDatabase.delete(
                DbSchema.Table_Channel.TABLE_NAME,
                DbSchema.BaseColumns.ID+" =?",
                new String[]{String.valueOf(channel.getId())}
        );
        //Log.e("delete channel","Success");
    }
    public WChannel getWChannelByLink(String link){
        DbCursorWrapper cursorWrapper = queryWChannelByLink(link);
        if(cursorWrapper.getCount()>0) {
            cursorWrapper.moveToFirst();
            WChannel channel = cursorWrapper.getWChannel(false);
            cursorWrapper.close();
            return channel;
        }else {
            return null;
        }
    }
    private DbCursorWrapper queryWChannelByLink(String link){
        String selection;
        String[] selectionArgs;
        String[] columns = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        selection = DbSchema.BaseColumns.LINK+" = ?";
        selectionArgs = new String[]{""+link};
        Cursor cursor = mDatabase.query(DbSchema.Table_Channel.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);
        return new DbCursorWrapper(cursor);
    }
    public WChannel findChannelById(double idChannel, ArrayList<WChannel> channelList){

        for (WChannel item:channelList) {
            if(item.getId()==idChannel){
                return item;
            }
        }
        return null;
    }

    //===================================================================================================
    private DbCursorWrapper queryWChannelGroups(){
        String selection;
        String[] selectionArgs;
        String[] columns = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        selection = null;
        selectionArgs = null;
        Cursor cursor = mDatabase.query(DbSchema.Table_ChannelGroup.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);
        return new DbCursorWrapper(cursor);
    }
    public ArrayList<WChannelGroup> getWChannelGroupsList(){
        DbCursorWrapper cursorWrapper = queryWChannelGroups();
        ArrayList<WChannelGroup> channelGroupsList = new ArrayList<>();
        cursorWrapper.moveToFirst();
        while (!cursorWrapper.isAfterLast()) {
            channelGroupsList.add(cursorWrapper.getWChannelGroup());
            cursorWrapper.moveToNext();
        }
        cursorWrapper.close();
        //Log.e("READ GROUPS","FROM LOCAL DB; Количество = "+channelGroupsList.size());
        return channelGroupsList;
    }
    private static ContentValues getContentValues_WChannelGroups(WChannelGroup channelGroup){
        ContentValues values = new ContentValues();
        values.put(DbSchema.BaseColumns.NAME,channelGroup.getName());
         return values;
    }

    public void channelGroup_add(WChannelGroup channelGroup){
        ContentValues values = getContentValues_WChannelGroups(channelGroup);
        mDatabase.insert(DbSchema.Table_ChannelGroup.TABLE_NAME,null,values);
        //Log.e("add channel group","Success "+channelGroup.getName());
    }
    public void channelGroup_update(WChannelGroup group){
        ContentValues values = getContentValues_WChannelGroups(group);
        String table = DbSchema.Table_ChannelGroup.TABLE_NAME;
        mDatabase.update(
                table,
                values,
                DbSchema.BaseColumns.ID+" = ?",
                new String[]{String.valueOf(group.getId())}
        );
        //Log.e("update channel group"," Success");
    }

    public void channelGroup_delete(WChannelGroup channelGroup){
        mDatabase.delete(
                DbSchema.Table_ChannelGroup.TABLE_NAME,
                DbSchema.BaseColumns.ID+" =?",
                new String[]{String.valueOf(channelGroup.getId())}
        );
        //Log.e("delete channel group","Success");
    }
    public WChannelGroup findGroupById(double idGroup, ArrayList<WChannelGroup> groupList){

        for (WChannelGroup item:groupList) {
            if(item.getId()==idGroup){
                return item;
            }
        }
        return null;
    }

    //=======================================================================================================
    //Main menu creation
    public ArrayList<WChannel> getMenuItems(){
        ArrayList<WChannel> channelsList  = new ArrayList<>();
        String selectQuery = "SELECT COUNT ("+DbSchema.Table_News.TABLE_NAME+"."+DbSchema.Table_News.Cols.IS_READ+") AS "+DbSchema.Table_Channel.Cols.MENU_ITEMS_ALL
                +", SUM("+DbSchema.Table_News.TABLE_NAME+"."+DbSchema.Table_News.Cols.IS_READ+") AS "+DbSchema.Table_Channel.Cols.MENU_ITEMS_READ
                +", "+DbSchema.Table_Channel.TABLE_NAME+".*"
                +" FROM "+DbSchema.Table_News.TABLE_NAME+", "+DbSchema.Table_Channel.TABLE_NAME
                +" WHERE "+DbSchema.Table_News.TABLE_NAME+"."+DbSchema.Table_News.Cols.ID_CHANNEL+" = "
                +DbSchema.Table_Channel.TABLE_NAME+"."+DbSchema.BaseColumns.ID
                +" GROUP BY "+DbSchema.Table_News.TABLE_NAME+"."+DbSchema.Table_News.Cols.ID_CHANNEL;
       // Log.e("SELECT QUERY"," = "+selectQuery);
        Cursor cursor = mDatabase.rawQuery(selectQuery,null);
        DbCursorWrapper cursorWrapper = new DbCursorWrapper(cursor);
        cursorWrapper.moveToFirst();
        while (!cursorWrapper.isAfterLast()) {
            channelsList.add(cursorWrapper.getWChannel(true));
            cursorWrapper.moveToNext();
        }
        cursorWrapper.close();
        cursor.close();

        return channelsList;
    }
public void deleteOldNews(int daysOld){
    String delString;
    if(daysOld>0) {
        delString = "DELETE FROM " + DbSchema.Table_News.TABLE_NAME + " WHERE strftime('"
                + DateUtils.DATE_FORMAT_DELETE + "'," + DbSchema.BaseColumns.PUB_DATE + ")<strftime('" + DateUtils.DATE_FORMAT_DELETE + "','now','-" + daysOld + " day')";
    }else {
        delString = "DELETE FROM " + DbSchema.Table_News.TABLE_NAME;
    }
    mDatabase.execSQL(delString);
}
    public void deleteChannels(){
        String delString = "DELETE FROM "+DbSchema.Table_Channel.TABLE_NAME;
        mDatabase.execSQL(delString);
    }
    public void deleteGroups(){
        String delString = "DELETE FROM "+DbSchema.Table_ChannelGroup.TABLE_NAME;
        mDatabase.execSQL(delString);
    }

 }
