package com.wolff.wnews.localdb;

/**
 * Created by wolff on 23.05.2017.
 */

public class DbSchema {

    //==========================================================================================
    public static final String DATABASE_NAME = "wmoney.db";

    public static final String CREATE_CHANNEL_TABLE = "CREATE TABLE "+ Table_Channel.TABLE_NAME+" ("+
            BaseColumns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            BaseColumns.NAME+" TEXT, "+
            BaseColumns.pubDate+" TEXT, "+
            BaseColumns.title+" TEXT, "+
            BaseColumns.link+" TEXT, "+
            BaseColumns.description+" TEXT, "+
            Table_Channel.Cols.language+" TEXT, "+
            Table_Channel.Cols.copyright+" TEXT, "+
            Table_Channel.Cols.managingEditor+" TEXT, "+
            Table_Channel.Cols.webMaster+" TEXT, "+
            Table_Channel.Cols.lastBuildDate+" TEXT, "+
            Table_Channel.Cols.category+" TEXT, "+
            Table_Channel.Cols.generator+" TEXT, "+
            Table_Channel.Cols.docs+" TEXT, "+
            Table_Channel.Cols.cloud+" TEXT, "+
            Table_Channel.Cols.ttl+" TEXT, "+
            Table_Channel.Cols.image+" TEXT, "+
            Table_Channel.Cols.rating+" TEXT, "+
            Table_Channel.Cols.textInput+" TEXT, "+
            Table_Channel.Cols.skipDays+" TEXT, "+
            Table_Channel.Cols.skipHours+" TEXT, "+
            Table_Channel.Cols.idGroup+" INTEGER, "+
            "FOREIGN KEY ("+Table_Channel.Cols.idGroup+") REFERENCES "+Table_ChannelGroup.TABLE_NAME+"("+BaseColumns.ID+")"+
            ")";

    public static final String CREATE_NEWS_TABLE = "CREATE TABLE "+ Table_News.TABLE_NAME+" ("+
            BaseColumns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            BaseColumns.NAME+" TEXT, "+
            Table_News.Cols.idChannel+" INTEGER, "+
            BaseColumns.pubDate+" TEXT, "+
            BaseColumns.title+" TEXT, "+
            BaseColumns.link+" TEXT, "+
            BaseColumns.description+" TEXT, "+
            Table_News.Cols.guid+" TEXT, "+
            Table_News.Cols.author+" TEXT, "+
            Table_News.Cols.category+" TEXT, "+
            Table_News.Cols.comments+" TEXT, "+
            Table_News.Cols.enclosure+" TEXT, "+
            Table_News.Cols.source+" TEXT, "+
            Table_News.Cols.isRead+" INTEGER "+
            ")";

    public static final String CREATE_CHANNELGROUP_TABLE = "CREATE TABLE "+ Table_ChannelGroup.TABLE_NAME+" ("+
            BaseColumns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            BaseColumns.NAME+" TEXT "+
                  ")";

    //==================================================================================================
    public static final class BaseColumns{
        public static final String ID = "_id";
        public static final String NAME = "_name";
        public static final String pubDate = "pubDate";
        public static final String title = "title";
        public static final String link = "link";
        public static final String description = "description";
    }
     public static final class Table_Channel{

         public static final String TABLE_NAME = "table_channel";

         public static final class Cols{
              public static final String language = "language";
             public static final String copyright = "copyright";
             public static final String managingEditor = "managingEditor";
             public static final String webMaster = "webMaster";
             public static final String lastBuildDate = "lastBuildDate";
             public static final String category = "category";
             public static final String generator = "generator";
             public static final String docs = "docs";
             public static final String cloud = "cloud";
             public static final String ttl = "ttl";
             public static final String image = "image";
             public static final String rating = "rating";
             public static final String textInput = "textInput";
             public static final String skipHours = "skipHours";
             public static final String skipDays = "skipDays";

             public static final String idGroup = "_idGroup";
         }

     }
    public static final class Table_ChannelGroup{

        public static final String TABLE_NAME = "table_channel_group";

    }
     public static final class Table_News{

        public static final String TABLE_NAME = "table_news";

        public static final class Cols{
            public static final String idChannel = "id_channel";
            public static final String guid = "guid";
            public static final String author = "author";
            public static final String category = "category";
            public static final String comments = "comments";
            public static final String enclosure = "enclosure";
            public static final String source = "source";
            public static final String isRead = "isRead";

        }
    }
}
