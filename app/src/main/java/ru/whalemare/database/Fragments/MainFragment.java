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

    private final String TAG = "WHALETAG"; // строка для отладки, чтобы можно было отделять наши данные от других

    EditText editText; // строка с редактируемым текстом. Сюда будут заноситься данные
    FloatingActionButton fabAdd, fabSearch, fabDelete; // кнопки: добавить, найти, удалить
    DBhelper dbHelper; // объект помощника БД

    public MainFragment() {}

    /**
     *  Метод, который запускается при старте фрагмента.
     *  В нем каждому объекту view ставится в соответствие ссылка на объект из layout файла.
     *
     */
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
                    Toast.makeText(getActivity().getApplicationContext(), "Сначала введите данные", Toast.LENGTH_SHORT).show();
                else {
                    Log.d(TAG, ">> Производим запись.");
                    cv.put("name", name);
                    long rowID = db.insert("mytable", null, cv);
                    Log.d(TAG, "ID столбца: " + rowID);
                    Toast.makeText(getActivity().getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                }
            }
        };

        View.OnClickListener clickSearch = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();

                if (name.equals("") || name.equals(null))
                    Toast.makeText(getActivity().getApplicationContext(), "Введите данные для поиска", Toast.LENGTH_SHORT).show();
                else {
                    Log.d(TAG, ">> Производим поиск по ключу " + name);
                    Toast.makeText(getActivity().getApplicationContext(), "Поиск по БД", Toast.LENGTH_SHORT).show();
                    // делаем запрос всех данных из mytable и получаем Cursor
                    String queryFind = "SELECT name FROM mytable WHERE name LIKE \"%" + name +"%\"";
                    Cursor cursor = db.rawQuery(queryFind, new String[] {});
                    Log.d(TAG, "Поиск по ключу завершен. Начинаем писать в лог");
                    logCursor(cursor); // выводим все в лог
                    Log.d(TAG, "Написали в лог");
                    cursor.close();
                }
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

    private void logCursor(Cursor c){
        if (c != null){
            if (c.moveToFirst()) {
                String string;
                do {
                    string = "";
                    for (String cn : c.getColumnNames())
                        string = string.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    Log.d(TAG, string);
                } while (c.moveToNext());
            }
        } else
            Log.d(TAG, "Курсор пустой");
    }
}
