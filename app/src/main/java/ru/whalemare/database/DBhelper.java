package ru.whalemare.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBhelper extends SQLiteOpenHelper {

    private static final String TAG = "WHALETAG";
    public static final String DATABASE_NAME = "myDB";
    private static final int DATABASE_VERSION = 3;

    public static final String DATABASE_TABLE = "mytable";
    public static final String NAME_COLUMN = "name";
    public static final String DATA_COLUMN = "data";
    private static final String DATABASE_CREATE_SCRIPT = "create table " + DATABASE_TABLE
        + " ("
        + "id integer primary key autoincrement, "
        + NAME_COLUMN + " text not null, "
        + DATA_COLUMN + " text"
        + ");";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "#Создаем БД#");
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Производится обновление БД с версии " + oldVersion + ", на версию " + newVersion);

        updateDatabase(db, oldVersion, newVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1)
            db.execSQL(DATABASE_CREATE_SCRIPT);

        if (oldVersion < 2)
            db.execSQL("ALTER TABLE mytable ADD COLUMN data TEXT");
    }
}
