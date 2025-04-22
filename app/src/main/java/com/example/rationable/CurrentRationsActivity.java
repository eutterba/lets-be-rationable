package com.example.rationable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.R;
import com.google.android.material.button.MaterialButton;

//im tempted to rename this to problem child

public class CurrentRationsActivity extends AppCompatActivity {

    private ListView userView;
    private DBHandler dbHandler;
    private CurrentRationsAdapter currentRationsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_rations);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        MaterialButton homeCurrBtn =  findViewById(R.id.homeCurrBtn);
        homeCurrBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CurrentRationsActivity.this, StartActivity.class);
            startActivity(intent);
        });

        userView = findViewById(R.id.userListView);
        dbHandler = new DBHandler(this);

        //dummy rations for testing
//        dbHandler.AddNewFood("test", 100, "1234567891234", 3, 1);
//        dbHandler.AddNewFood("test2", 150, "043600001091", 31, 21);
//        dbHandler.AddNewFood("test3", 130, "987654321012", 4, 11);
//        dbHandler.AddNewFood("test4", 10, "012345678987", 0, 0);

        Cursor cursor = dbHandler.getRations();

        if (cursor != null) {
            currentRationsAdapter = new CurrentRationsAdapter(this, cursor, 0);
            userView.setAdapter(currentRationsAdapter);
        }else{
            Log.e("CurrentRationsActivity", "Cursor is null");
        }

        if (cursor != null) {
            Log.d("CurrentRationsActivity", "Cursor contains " + cursor.getCount() + " rows.");
        } else {
            Log.e("CurrentRationsActivity", "Cursor is null");
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHandler != null) {
            Cursor cursor = dbHandler.getRations();
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }


}
