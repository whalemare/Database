package ru.whalemare.database.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import ru.whalemare.database.Adapters.ListAdapter;
import ru.whalemare.database.DBhelper;
import ru.whalemare.database.R;

public class ListFragment extends Fragment {
    private final String TAG = "WHALETAG";
    private DBhelper dbHelper;

    public RecyclerView recyclerView;
    public ListAdapter adapter;

    public ListFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        FloatingActionButton fabRefresh = (FloatingActionButton) getActivity().findViewById(R.id.fab_list_refresh);
        FloatingActionButton fabDelete = (FloatingActionButton) getActivity().findViewById(R.id.fab_list_delete);
        FloatingActionButton fabShuffle = (FloatingActionButton) getActivity().findViewById(R.id.fab_shuffle);

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper = new DBhelper(getActivity().getApplicationContext());
                final SQLiteDatabase db = dbHelper.getWritableDatabase(); // подключаемся к бд

                Log.d(TAG, "Обновление данных");
                ArrayList<String> names = new ArrayList<>();

                Cursor cursor = db.query("mytable", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {// ставим позицию курсора на 1 строку выборки. Если строк нет = false
                    // определеяем  номера столбцов по имени
                    int idColumnIndex = cursor.getColumnIndex("id");
                    int nameColumnIndex = cursor.getColumnIndex("name");

                    do { // получаем значения по номерам столбцов
                        Log.d(TAG, "ID = " + cursor.getInt(idColumnIndex) + "; " +
                            "Key = " + cursor.getString(nameColumnIndex));
                        names.add(cursor.getString(nameColumnIndex)); // добавили строку в список
                    } while (cursor.moveToNext());
                } else {
                    Log.d(TAG, "0 столбцов таблицы");
                    Toast.makeText(getActivity().getApplicationContext(), "Таблица пуста", Toast.LENGTH_SHORT).show();
                }

                adapter.setItems(names);
                cursor.close();
            }
        });
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SQLiteDatabase db = dbHelper.getWritableDatabase(); // подключаемся к бд
                Toast.makeText(getActivity().getApplicationContext(), "Удаляем все записи в БД", Toast.LENGTH_SHORT).show();
                db.delete("mytable", null, null);
            }
        });
        fabShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.shuffle();
            }
        });

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        adapter = new ListAdapter(getActivity().getApplicationContext(), new ArrayList<String>(), recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }
}

//TODO scrollingViewBehavior