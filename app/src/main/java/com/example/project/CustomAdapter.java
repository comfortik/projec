package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    public String[] items;
    public String[] intervals;
    public CustomAdapter(Context context, String[] items, String[] intervals) {
        super(context, R.layout.drop_down_item, items);
        this.items = items;
        this.intervals = intervals;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.drop_down_item, parent, false);
        }
        TextView mainText = convertView.findViewById(R.id.mainText);
        TextView sideSimbol = convertView.findViewById(R.id.sideSymbol);
        mainText.setText(items[position]);
        sideSimbol.setText(intervals[position]);
        return convertView;
    }
}
