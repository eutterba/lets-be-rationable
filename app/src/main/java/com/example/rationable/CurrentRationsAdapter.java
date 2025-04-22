package com.example.rationable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.barcodescanner.R;

public class CurrentRationsAdapter extends CursorAdapter {

    //private ListView userListView;
    
    public CurrentRationsAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }
    
    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = view.findViewById(R.id.rationName);
        TextView caloriesTextView = view.findViewById(R.id.rationCalories);
        TextView barcodeTextView = view.findViewById(R.id.rationBarcode);
        TextView servingsTextView = view.findViewById(R.id.rationServings);
        TextView totalTextView = view.findViewById(R.id.rationTotal);

        //userListView = view.findViewById(R.id.userListView);

        // Ensure column indexes are correctly retrieved
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String calories = cursor.getString(cursor.getColumnIndexOrThrow("calories"));
        String barcode = cursor.getString(cursor.getColumnIndexOrThrow("barcode"));
        String servings = cursor.getString(cursor.getColumnIndexOrThrow("servings"));
        String total = cursor.getString(cursor.getColumnIndexOrThrow("total"));
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));


        // Assign data to TextViews
        nameTextView.setText(name);
        caloriesTextView.setText(calories);
        barcodeTextView.setText(barcode);
        servingsTextView.setText(servings);
        totalTextView.setText(total);
        
        
    }
    
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.ration_item_layout, parent, false);
    }
    
    
}
