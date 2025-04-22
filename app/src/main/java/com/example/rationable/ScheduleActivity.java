package com.example.rationable;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class ScheduleActivity extends AppCompatActivity {

    private int CALORIE_REQUIREMENTS = 2100;



    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        dbHandler = new DBHandler(this);

        ListView scheduleView = findViewById(R.id.scheduleListView);
        ArrayList<String[]> foods =  dbHandler.getSchedule(0, CALORIE_REQUIREMENTS);
        if (foods == null || foods.isEmpty()) {
            Log.d("ScheduleActivity", "No foods returned from getSchedule");
        } else {
            Log.d("ScheduleActivity", "Loaded " + foods.size() + " food items");
        }

        ScheduleAdapter adapter = new ScheduleAdapter(this, foods);
        scheduleView.setAdapter(adapter);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        MaterialButton homeSchedBtn =  findViewById(R.id.homeSchedBtn);
        homeSchedBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ScheduleActivity.this, StartActivity.class);
            startActivity(intent);
        });

        MaterialButton makeSchedBtn = findViewById(R.id.makeSchedBtn);
        makeSchedBtn.setOnClickListener(v -> {
            ArrayList<String[]> updatedFoods = dbHandler.getSchedule(0, CALORIE_REQUIREMENTS);
            if (updatedFoods == null || updatedFoods.isEmpty()) {
                Log.d("ScheduleActivity", "No foods returned from getSchedule");
            } else {
                Log.d("ScheduleActivity", "Updated " + updatedFoods.size() + " food items");
            }

            adapter.clear();
            adapter.addAll(updatedFoods);
            adapter.notifyDataSetChanged();
        });



    }




}
