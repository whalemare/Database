package ru.whalemare.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    DBhelper dbHelper;
    final String TAG = "WHALETAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabMode(tabLayout.MODE_SCROLLABLE);

        tabLayout.addTab(tabLayout.newTab().setText("Главная"));
        tabLayout.addTab(tabLayout.newTab().setText("неглавная"));

        editText = (EditText) findViewById(R.id.editText); // найдем место с вводимым текстом
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        FloatingActionButton fabSearch = (FloatingActionButton) findViewById(R.id.fab_search);
        FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.fab_delete);

        dbHelper = new DBhelper(this);
    }

    public void onClick(View view){

        ContentValues cv = new ContentValues(); // объект для данных

        SQLiteDatabase db = dbHelper.getWritableDatabase(); // подключаемся к бд



        switch(view.getId()){
            case R.id.fab_add:
                String name = editText.getText().toString();

                if (name.equals("") || name.equals(null))
                {
                    Toast.makeText(getApplicationContext(), "Сначала введите данные", Toast.LENGTH_SHORT).show();
                    break;
                }

                Log.d(TAG, ">> Производим запись.");

                cv.put("name", name);
                long rowID = db.insert("mytable", null, cv);
                Log.d(TAG, "ID столбца: " + rowID);
                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_search:
                Log.d(TAG, ">> Производим поиск.");
                Toast.makeText(getApplicationContext(), "Поиск по БД", Toast.LENGTH_SHORT).show();
                // делаем запрос всех данных из mytable и получаем Cursor
                Cursor cursor = db.query("mytable", null, null, null, null, null, null);

                if (cursor.moveToFirst()) // ставим позицию курсора на 1 строку выборки. Если строк нет = false
                {
                    // определеяем  номера столбцов по имени
                    int idColumnIndex = cursor.getColumnIndex("id");
                    int nameColumnIndex = cursor.getColumnIndex("name");

                    do { // получаем значения по номерам столбцов
                        Log.d(TAG, "ID = " + cursor.getInt(idColumnIndex) + "; " +
                                    "Key = " + cursor.getString(nameColumnIndex));
                    } while (cursor.moveToNext());

                }
                else
                {
                    Log.d(TAG, "0 столбцов таблицы");
                    Toast.makeText(getApplicationContext(), "Таблица пуста", Toast.LENGTH_SHORT);
                }

                cursor.close();
                break;
        }
    }

}

