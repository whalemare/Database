package ru.whalemare.database;

import android.util.Log;

public class Item {

    String TAG = "WHALETAG";

    public String name = null; // сама запись
    public int key; // порядковый номер записи == ее ключ == id

    Item(String name)
    {
        this.name = name;
        this.key++;
        Log.d(TAG, "Ид = " + this.key);
    }

}
