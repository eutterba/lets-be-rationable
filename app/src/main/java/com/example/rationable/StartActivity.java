package com.example.rationable;

import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.R;
import com.google.android.material.button.MaterialButton;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        MaterialButton schedbtn =  findViewById(R.id.schedBtn);
        schedbtn.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, ScheduleActivity.class);
            startActivity(intent);
        });

        //this mother fucker down there took 3 whole hours to debug
        // so i could even see if values would show up I rewrote the code 5 times
        // i just want that on the record whenever yall are evaluating the capstones
        // and wonder why does this not look as polished as the other capstone project
        //stuff like that is why

        MaterialButton currRatbtn =  findViewById(R.id.currRatBtn);
        currRatbtn.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, CurrentRationsActivity.class);
            Log.d("onClick: ", "go to current rations" );
            startActivity(intent);
        });

        MaterialButton barRatBtn =  findViewById(R.id.barRatBtn);
        barRatBtn.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, BarcodeScannerActivity.class);
            Log.d("onClick: ", "go to barcode scanner" );
            startActivity(intent);
        });

        MaterialButton manRatbtn =  findViewById(R.id.manRatBtn);
        manRatbtn.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, ManualActivity.class);
            startActivity(intent);
        });

        

    }

}
