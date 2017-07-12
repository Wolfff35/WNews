package com.wolff.wnews.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by wolff on 23.05.2017.
 */

    public class DbHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, DbSchema.DATABASE_NAME, null, VERSION);
    }

    @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DbSchema.CREATE_CHANNELGROUP_TABLE);
            db.execSQL(DbSchema.CREATE_CHANNEL_TABLE);
            db.execSQL(DbSchema.CREATE_NEWS_TABLE);
            Log.e("CREATE TABLES","CREATED!");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
}