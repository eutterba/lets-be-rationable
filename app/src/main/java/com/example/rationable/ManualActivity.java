package com.example.rationable;





import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.R;
import com.google.android.material.button.MaterialButton;


public class ManualActivity extends AppCompatActivity {


    private EditText foodNameEdt, calPerServEdt, barcodeEdt, servPerItemEdt, totalAmountEdt;
    private Button addFoodBtn;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        MaterialButton homeManBtn =  findViewById(R.id.homeManBtn);
        homeManBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ManualActivity.this, StartActivity.class);
            startActivity(intent);
        });


        //init variables
        foodNameEdt = findViewById(R.id.idEdtFoodName);
        calPerServEdt = findViewById(R.id.idEdtCaloriesPerServing);
        barcodeEdt = findViewById(R.id.idEdtFoodBarcode);
        servPerItemEdt = findViewById(R.id.idEdtServingsPerItem);
        totalAmountEdt = findViewById(R.id.idEdtTotalAmount);
        addFoodBtn = findViewById(R.id.idBtnAddFood);
        dbHandler = new DBHandler(ManualActivity.this);

        //add on click listener for add food
        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data from all the edit fields
                String foodName = foodNameEdt.getText().toString();
                float calPerServ = Float.parseFloat(calPerServEdt.getText().toString());
                String barcode = barcodeEdt.getText().toString();
                float servPerItem = Float.parseFloat(servPerItemEdt.getText().toString());
                float totalAmount = Float.parseFloat(totalAmountEdt.getText().toString());

                if (foodName.isEmpty() || calPerServ == 0 || servPerItem == 0 || totalAmount == 0) {
                    Toast.makeText(ManualActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (barcode.isEmpty()) {
                    barcode = "barcode na";
                }

                //call method to add food to sqlite database and pass the data
                dbHandler.AddNewFood(foodName, calPerServ, barcode, servPerItem, totalAmount);
                //display message to confirm that food has been added
                Toast.makeText(ManualActivity.this, "Food Added..", Toast.LENGTH_SHORT).show();
                //clear all the edit fields
                foodNameEdt.setText("");
                calPerServEdt.setText("");
                barcodeEdt.setText("");
                servPerItemEdt.setText("");
                totalAmountEdt.setText("");
            }
        });



    }
}
