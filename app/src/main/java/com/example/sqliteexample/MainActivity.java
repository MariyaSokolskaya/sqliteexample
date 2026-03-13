package com.example.sqliteexample;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase sdb;
    OpenHelper openHelper;
    AppCompatButton addActiveButton, getActiveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addActiveButton = findViewById(R.id.addActivButton);
        getActiveButton = findViewById(R.id.getActivButton);

        openHelper = new OpenHelper(this);
        sdb = openHelper.getWritableDatabase();

        addActiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!sdb.isOpen())
            sdb = openHelper.getWritableDatabase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sdb.close();
    }
}