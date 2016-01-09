package ru.whalemare.database;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabMode(tabLayout.MODE_SCROLLABLE);

        tabLayout.addTab(tabLayout.newTab().setText("Главная"));

        editText = (EditText) findViewById(R.id.editText); // найдем место с вводимым текстом
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.fab_add:
                Toast.makeText(getApplicationContext(), editText.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

}

