package androidlabs.com.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by badal on 2018-03-01.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper{

    static String DATABASE_NAME = "Messages.db";
    static int VERSION_NUM = 4;
    static final String KEY_ID = "_id", KEY_MESSAGE = "message";
    public static final String TABLE_NAME = "tbl_message";

    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("ChatDatabaseHelper", "Calling onCreate");
        db.execSQL("create table " + TABLE_NAME + " ( " + KEY_ID + " integer primary key autoincrement, " + KEY_MESSAGE + " text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + i + " newVersion=" + i1);
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int i, int i1) {
        Log.i("ChatDatabaseHelper", "Calling onDowngrade, oldVersion=" + i + " newVersion=" + i1);
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

}
