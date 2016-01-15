package ru.whalemare.database.Fragments;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOError;
import java.util.ArrayList;

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
     *  В нем каждому объекту view ставится в соответствие ссылка на объект из layout файлам
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
                if (name.equals("") || name.equals(null)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Сначала введите данные", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Произведем поиск по БД и найдем запись с таким же ключом
                Cursor cursor = db.rawQuery("SELECT name FROM mytable WHERE name=\'" + name +"\'", new String[]{});
                if (searchOnDB(cursor, name)) // если запись в БД найдена
                    return; // то запись не добавим

                Log.d(TAG, ">> Производим запись.");
                cv.put("name", name);
                cv.put("data", "Данные: " + name.concat(name));
                long rowID = db.insert("mytable", null, cv);
                Log.d(TAG, "ID столбца: " + rowID);
                Toast.makeText(getActivity().getApplicationContext(), "Данные успешно добавлены в БД", Toast.LENGTH_SHORT).show();
            }
        };

        View.OnLongClickListener longClickAdd = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String name = editText.getText().toString();
                if (name.equals("") || name.equals(null)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Сначала введите данные для добавления 100 записей", Toast.LENGTH_SHORT).show();
                    return true;
                }

                Cursor cursor = db.rawQuery("SELECT name FROM mytable WHERE name=\'" + name +"\'", new String[]{});
                if (searchOnDB(cursor, name)) // если запись найдена
                    return true; // заканчиваем клик

                // добавим запись без редактирования
                cv.put("name", name);
                cv.put("data", "Данные: " + name.concat(name));
                long rowID = db.insert("mytable", null, cv);
                Log.d(TAG, "ID столбца: " + rowID);

                final int NUMBER_OF_ENTRYES = 1000; // число записей в БД
                Integer i = 1;
                while (i <= NUMBER_OF_ENTRYES) {
                    Log.d(TAG, ">> Производим запись.");
                    cv.put("name", name + i.toString());
                    cv.put("data", "Данные: " + name.concat(name) + i.toString());
                    rowID = db.insert("mytable", null, cv);
                    Log.d(TAG, "ID столбца: " + rowID);
                    i++;
                }

                Toast.makeText(getActivity().getApplicationContext(), "Данные 100 последовательных записей успешно добавлены в БД", Toast.LENGTH_SHORT).show();
                return true;
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
                    String queryFind = "SELECT name FROM mytable WHERE name = \'" + name +"\'";
                    Cursor cursor = db.rawQuery(queryFind, new String[] {});

                    ArrayList<String> strings;
                    strings = logCursor(cursor); // выводим все в лог

                    // Вывод диалога
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.list_view, null);
                    alertDialog.setView(convertView);
                    alertDialog.setTitle("Данные по поиску");
                    ListView lv = (ListView) convertView.findViewById(R.id.listview);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.list_item, strings);
                    lv.setAdapter(adapter);

                    // хак костыль и вообще
                    try {


                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    alertDialog.show();

                    cursor.close();
                }
            }
        };

        View.OnClickListener clickDelete = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();

                if (name.equals("") || name.equals(null))
                    Toast.makeText(getActivity().getApplicationContext(), "Введите данные для удаления", Toast.LENGTH_SHORT).show();

                try {
                    db.delete("mytable", "name = \'" + name + "\'", null);
                    Toast.makeText(getActivity().getApplicationContext(), "Данные успешно удалены", Toast.LENGTH_SHORT).show();
                } catch (IOError e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Удаление невозможно", Toast.LENGTH_SHORT).show();
                }
            }
        };

        fabAdd.setOnClickListener(clickAdd); // устанавливаем обычный клик добавления
        fabAdd.setOnLongClickListener(longClickAdd); // устанавливаем длинный клик добавления
        fabSearch.setOnClickListener(clickSearch); // клик поиска
        fabDelete.setOnClickListener(clickDelete); // клик удаления
    }

    /**
     * Функция поиска записи в БД.
     * @param cursor - курсор со значениями в которых ищем.
     * @param wordSearch - само слово для поиска.
     * @return true - если запись найдена. false - если такой записи в БД нет
     */
    private boolean searchOnDB(Cursor cursor, String wordSearch) {
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                String string;
                do {
                    string = "";
                    for (String cn : cursor.getColumnNames())
                        string = "Данные: " + cursor.getString(cursor.getColumnIndex(cn));

                    if (("Данные: " + wordSearch).equals(string))
                    {
                        Log.d(TAG, "Такой ключ записи в БД уже есть, ввод невозможен");
                        Toast.makeText(getActivity().getApplicationContext(), "Такой ключ записи в БД уже есть, ввод невозможен", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                } while (cursor.moveToNext());
            }
        }
        return false;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    /**
     * Функция для удобного вывода в лог информации по курсору. + возвращает строки являющиеся данными
     * индексов.
     * @param c курсор. С его помощью осуществляется получение данных из SQLite
     * @return strings - список строк с данными.
     */
    private ArrayList<String> logCursor(Cursor c){
        ArrayList<String> strings = new ArrayList<>();
        if (c != null){
            if (c.moveToFirst()) {
                String string;
                do {
                    string = "";
                    for (String cn : c.getColumnNames())
                        string = "Данные: " + c.getString(c.getColumnIndex(cn));
                    Log.d(TAG, string);
                    strings.add(string);
                } while (c.moveToNext());
            }
        } else
            Log.d(TAG, "Курсор пустой");
        return strings;
    }
}
