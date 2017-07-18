package com.wolff.wnews.localdb;

/**
 * Created by wolff on 23.05.2017.
 */

public class DbSchema {

    //==========================================================================================
    public static final String DATABASE_NAME = "wnews.db";

    public static final String CREATE_CHANNEL_TABLE = "CREATE TABLE "+ Table_Channel.TABLE_NAME+" ("+
            BaseColumns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            BaseColumns.NAME+" TEXT, "+
            BaseColumns.PUB_DATE+" TEXT, "+
            BaseColumns.TITLE+" TEXT, "+
            BaseColumns.LINK+" TEXT, "+
            BaseColumns.DESCRIPTION+" TEXT, "+
            Table_Channel.Cols.LANGUAGE+" TEXT, "+
            Table_Channel.Cols.COPYRIGHT+" TEXT, "+
            Table_Channel.Cols.MANAGING_EDITOR+" TEXT, "+
            Table_Channel.Cols.WEB_MASTER+" TEXT, "+
            Table_Channel.Cols.LAST_BUILD_DATE+" TEXT, "+
            Table_Channel.Cols.CATEGORY+" TEXT, "+
            Table_Channel.Cols.GENERATOR+" TEXT, "+
            Table_Channel.Cols.DOCS+" TEXT, "+
            Table_Channel.Cols.CLOUD+" TEXT, "+
            Table_Channel.Cols.TTL+" TEXT, "+
            Table_Channel.Cols.IMAGE+" TEXT, "+
            Table_Channel.Cols.RATING+" TEXT, "+
            Table_Channel.Cols.TEXT_INPUT+" TEXT, "+
            Table_Channel.Cols.SKIP_DAYS+" TEXT, "+
            Table_Channel.Cols.SKIP_HOURS+" TEXT, "+
            Table_Channel.Cols.ID_GROUP+" INTEGER, "+
            "FOREIGN KEY ("+Table_Channel.Cols.ID_GROUP+") REFERENCES "+Table_ChannelGroup.TABLE_NAME+"("+BaseColumns.ID+")"+
            ")";

    public static final String CREATE_NEWS_TABLE = "CREATE TABLE "+ Table_News.TABLE_NAME+" ("+
            BaseColumns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            BaseColumns.NAME+" TEXT, "+
            Table_News.Cols.ID_CHANNEL+" INTEGER, "+
            BaseColumns.PUB_DATE+" TEXT, "+
            BaseColumns.TITLE+" TEXT, "+
            BaseColumns.LINK+" TEXT, "+
            BaseColumns.DESCRIPTION+" TEXT, "+
            Table_News.Cols.GUID+" TEXT, "+
            Table_News.Cols.AUTHOR+" TEXT, "+
            Table_News.Cols.CATEGORY+" TEXT, "+
            Table_News.Cols.COMMENTS+" TEXT, "+
            Table_News.Cols.ENCLOSURE+" TEXT, "+
            Table_News.Cols.SOURCE+" TEXT, "+
            Table_News.Cols.IS_READ+" INTEGER "+
            ")";

    public static final String CREATE_CHANNELGROUP_TABLE = "CREATE TABLE "+ Table_ChannelGroup.TABLE_NAME+" ("+
            BaseColumns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            BaseColumns.NAME+" TEXT "+
                  ")";

    //==================================================================================================
    public static final class BaseColumns{
        public static final String ID = "_id";
        public static final String NAME = "_name";
        public static final String PUB_DATE = "pubDate";
        public static final String TITLE = "title";
        public static final String LINK = "link";
        public static final String DESCRIPTION = "description";
    }
     public static final class Table_Channel{

         public static final String TABLE_NAME = "table_channel";

         public static final class Cols{
              public static final String LANGUAGE = "language";
             public static final String COPYRIGHT = "copyright";
             public static final String MANAGING_EDITOR = "managingEditor";
             public static final String WEB_MASTER = "webMaster";
             public static final String LAST_BUILD_DATE = "lastBuildDate";
             public static final String CATEGORY = "category";
             public static final String GENERATOR = "generator";
             public static final String DOCS = "docs";
             public static final String CLOUD = "cloud";
             public static final String TTL = "ttl";
             public static final String IMAGE = "image";
             public static final String RATING = "rating";
             public static final String TEXT_INPUT = "textInput";
             public static final String SKIP_HOURS = "skipHours";
             public static final String SKIP_DAYS = "skipDays";

             public static final String ID_GROUP = "_idGroup";
         }

     }
    public static final class Table_ChannelGroup{

        public static final String TABLE_NAME = "table_channel_group";

    }
     public static final class Table_News{

        public static final String TABLE_NAME = "table_news";

        public static final class Cols{
            public static final String ID_CHANNEL = "_id_channel";
            public static final String GUID = "guid";
            public static final String AUTHOR = "author";
            public static final String CATEGORY = "category";
            public static final String COMMENTS = "comments";
            public static final String ENCLOSURE = "enclosure";
            public static final String SOURCE = "source";
            public static final String IS_READ = "_isRead";

        }
    }
}
