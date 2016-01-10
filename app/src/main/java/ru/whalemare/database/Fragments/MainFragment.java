package ru.whalemare.database.Fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ru.whalemare.database.DBhelper;
import ru.whalemare.database.R;

public class MainFragment extends Fragment{

    private final String TAG = "WHALETAG";

    EditText editText;
    FloatingActionButton fabAdd, fabSearch, fabDelete;
    DBhelper dbHelper;

    public MainFragment() {}

    @Override
    public void onStart() {
        super.onStart();
        editText = (EditText) getActivity().findViewById(R.id.editText);
        fabAdd = (FloatingActionButton) getActivity().findViewById(R.id.fab_add);
        fabSearch = (FloatingActionButton) getActivity().findViewById(R.id.fab_search);
        fabDelete = (FloatingActionButton) getActivity().findViewById(R.id.fab_delete);


        dbHelper = new DBhelper(getActivity().getApplicationContext());
        final ContentValues cv = new ContentValues(); // объект для данных
        final SQLiteDatabase db = dbHelper.getWritableDatabase(); // подключаемся к бд

        View.OnClickListener clickAdd = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                if (name.equals("") || name.equals(null))
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Сначала введите данные", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, ">> Производим запись.");
                cv.put("name", name);
                long rowID = db.insert("mytable", null, cv);
                Log.d(TAG, "ID столбца: " + rowID);
                Toast.makeText(getActivity().getApplicationContext(), name, Toast.LENGTH_SHORT).show();
            }
        };

        View.OnClickListener clickSearch = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, ">> Производим поиск.");
                Toast.makeText(getActivity().getApplicationContext(), "Поиск по БД", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity().getApplicationContext(), "Таблица пуста", Toast.LENGTH_SHORT).show();
                }



                cursor.close();
            }
        };

        View.OnClickListener clickDelete = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Удаление", Toast.LENGTH_SHORT).show();
            }
        };

        fabAdd.setOnClickListener(clickAdd);
        fabSearch.setOnClickListener(clickSearch);
        fabDelete.setOnClickListener(clickDelete);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }
}
