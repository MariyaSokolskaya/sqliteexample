package com.example.sqliteexample;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddActivity extends AppCompatActivity {
    Spinner spinner;
    String listCategory[] = {"Продукты","Транспорт","Развлечения",
            "Коммунальные платежи","Обучение","Лечение"};
    OpenHelper openHelper;
    SQLiteDatabase sdb;
    EditText purchaseText, moneyText, commentText, dateText;
    AppCompatButton addButton;
    ArrayAdapter<String> adapter;
    String userCategory = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add);

        spinner = findViewById(R.id.cat_list);
        purchaseText = findViewById(R.id.purchase_text);
        moneyText = findViewById(R.id.money_text);
        commentText = findViewById(R.id.comment_text);
        addButton = findViewById(R.id.add_button);
        dateText = findViewById(R.id.date_text);

        openHelper = new OpenHelper(this);
        sdb = openHelper.getWritableDatabase();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategory);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userCategory = listCategory[i];
                Log.d("CHECK", userCategory);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                userCategory = listCategory[0];
                Log.d("CHECK", userCategory);

            }
        });
//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                userCategory = listCategory[i];
//                Log.d("CHECK", userCategory);
//            }
//        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //добавление категории в таблицу, если её там нет
                //шаг 1. Запрос к БД на извлечение нужной категории
                String query = "SELECT * FROM " + OpenHelper.DATABASE_TABLE_CATEGORY +
                        " WHERE " + OpenHelper.CATEGORY_CAT + "=\'" + userCategory
                        +"\';";
                Cursor answerCategory = sdb.rawQuery(query, null);
                int count = answerCategory.getCount();
                int id_cat = 0;
                Log.d("CHECK", answerCategory.toString());
                if (count == 0){
                    //шаг 2. Если категории нет - добавить
                    String addCatQuery = "INSERT INTO " + OpenHelper.DATABASE_TABLE_CATEGORY +
                            "(" + OpenHelper.CATEGORY_CAT + ") VALUES (\'" +
                            userCategory + "\');";
                    sdb.execSQL(addCatQuery);
                    //шаг 3. Для добавленной категории получить id
                    String getCatQuery = "SELECT _id FROM " + OpenHelper.DATABASE_TABLE_CATEGORY +
                            " WHERE " + OpenHelper.CATEGORY_CAT + "=\'" + userCategory + "\';";
                    Cursor idCatCursor = sdb.rawQuery(getCatQuery, null);
                    Log.d("CHECK", idCatCursor.toString());
                    //SELECT _id FROM category WHERE category_purchase='userCategory'
                    idCatCursor.moveToFirst();
                    int idx = idCatCursor.getColumnIndex(OpenHelper.CATEGORY_ID);
                    id_cat = idCatCursor.getInt(idx);
                }else{
                    answerCategory.moveToFirst();
                    int idx = answerCategory.getColumnIndex(
                            OpenHelper.CATEGORY_ID);
                    id_cat = answerCategory.getInt(idx);
                }
                answerCategory.close();

                String purchase = purchaseText.getText().toString();
                String money = moneyText.getText().toString();
                String date = dateText.getText().toString();
                String comment = commentText.getText().toString();
                if(comment.isEmpty()){
                    comment = "NULL";
                }
                //TODO переписать запрос, добавив внешний ключ
                String insertQuery = "INSERT INTO " +
                        OpenHelper.DATABASE_TABLE + "(" + OpenHelper.BUDGET_PURCHASE +
                        ", " + OpenHelper.BUDGET_MONEY + ", " +
                        OpenHelper.BUDGET_DATE + ", " + OpenHelper.BUDGET_COMMENT +
                        ", " + OpenHelper.BUDGET_CATEGORY_ID +
                        ") " + "VALUES (\'" + purchase + "\', " +
                        Double.parseDouble(money) + ", \'" + date + "\', \'" +
                        comment + "\', " + id_cat + ");";
                sdb.execSQL(insertQuery);
                Toast.makeText(getBaseContext(), "Запись добавлена",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!sdb.isOpen()){
            sdb = openHelper.getWritableDatabase();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sdb.isOpen()){
            sdb.close();
        }
    }
}