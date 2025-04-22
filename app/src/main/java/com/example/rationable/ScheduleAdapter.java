package com.example.rationable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.barcodescanner.R;

import java.util.ArrayList;

public class ScheduleAdapter extends ArrayAdapter<String[]> {

    private final Context context;
    private final ArrayList<String[]> foods;


    public ScheduleAdapter(Context context, ArrayList<String[]> foods) {
        super(context, R.layout.schedule_item_layout, foods);
        this.context = context;
        this.foods = foods;

    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = convertView;

        if (rowView == null) {
            rowView = inflater.inflate(R.layout.schedule_item_layout, parent, false);
        }

        TextView line1 = rowView.findViewById(R.id.line1);
        TextView line2 = rowView.findViewById(R.id.line2);
        TextView line3 = rowView.findViewById(R.id.line3);

        String[] item = foods.get(position);
        String displayText = item[0] + "   " + item[1] + "servs " + item[2] + " cal";

        if (item.length > 0) line1.setText(item[0]);
        if (item.length > 1) line2.setText(item[1]);
        if (item.length > 2) line3.setText(item[2]);

        return rowView;
    }

}
