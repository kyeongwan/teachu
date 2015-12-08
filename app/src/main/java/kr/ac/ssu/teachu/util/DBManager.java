package kr.ac.ssu.teachu.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {

    static DBManager g_this;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        g_this = this;
    }

    public static DBManager getInstance() {
        if(g_this == null)
            Log.e("DBManager", "Instance NULL");
        return g_this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS user_info(" +
                        "token TEXT PRIMARY KEY, " +
                        "device_id TEXT, " +
                        "user_id TEXT, " +
                        "gcm_key TEXT);";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS schedule(" +
                "schedule_id TEXT PRIMARY KEY UNIQUE, " +
                "title TEXT, " +
                "time TEXT, " +
                "date datetime," +
                "context text);"; // boolean처럼 사용할 것. 앱 등록 여부.
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS chat_info(" +
                "channel_id TEXT PRIMARY KEY UNIQUE, " +
                "public_onoff TEXT, " +
                "channel_limit INTEGER, " +
                "channel_cate TEXT, " +
                "app_id TEXT, " +
                "channel_name TEXT, " +
                "chief_id TEXT, " +
                "user_color TEXT," +
                "check_favorite INTEGER);";

        db.execSQL(sql);

        Log.e("DBManager", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void write(String _query) {
        Log.e("DBManager", _query);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void select(String query, OnSelect cb) {
        SQLiteDatabase db = getReadableDatabase();
        Log.e("DBManager SELECT query", query);
        try {
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                cb.onSelect(cursor);
            }
            cb.onComplete(cursor.getCount());
        }catch (Exception e){
            cb.onErrorHandler(e);
        }
    }

    public static interface OnSelect {
        public void onSelect(Cursor cursor);
        public void onComplete(int cnt);
        public void onErrorHandler(Exception e);
    }
}