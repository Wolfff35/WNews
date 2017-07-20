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
           Table_Channel.Cols.IMAGE+" TEXT, "+
            Table_Channel.Cols.RATING+" TEXT, "+
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
            Table_News.Cols.ENCLOSURE+" TEXT, "+
            Table_News.Cols.ENCLOSURE_TYPE+" TEXT, "+
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
             public static final String IMAGE = "image";
             public static final String RATING = "rating";

             public static final String ID_GROUP = "_idGroup";
             public static final String MENU_ITEMS_ALL = "_all_items";
             public static final String MENU_ITEMS_READ = "_unread_items";
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
            public static final String ENCLOSURE = "enclosure";
            public static final String ENCLOSURE_TYPE = "enclosure_type";
            public static final String IS_READ = "_isRead";

        }
    }
}
