package com.vann.csdn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * author： bwl on 2016-03-28.
 * email: bxl049@163.com
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "csdn";
    public static final String TABLE_CSDN = "tb_csdn";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_CSDN
                + "(id integer primary key autoincrement, title,text,link text,"
                + "imgLink text,content text,date text,newsType integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("drop table if exists " + TABLE_CSDN);
            onCreate(db);
        }
    }
}
