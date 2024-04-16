package com.example.project.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {
    private String[] items;
    private String[] intervals;
    private String[] timeWork;
    private String [] timeRest;

    public CustomAdapter(Context context, String[] items, String[] intervals, String[] timeWork, String [] timeRest) {
        super(context, R.layout.drop_down_item, items);
        if (items != null && intervals != null && items.length == intervals.length) {
            this.items = items;
            this.intervals = intervals;
            this.timeRest=timeRest;
            this.timeWork= timeWork;
        } else  {
            this.items = new String[1];
            this.intervals = new String[1];
            this.timeWork=new String[1];
            this.timeRest = new String[1];
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.drop_down_item, parent, false);
        }

        TextView mainText = convertView.findViewById(R.id.mainText);
        TextView sideSimbol = convertView.findViewById(R.id.sideSymbol);
        TextView timeWor = convertView.findViewById(R.id.timeWork);
        TextView timeRes = convertView.findViewById(R.id.timeRest);
        mainText.setText(items[position]);
        sideSimbol.setText(intervals[position]);
        timeWor.setText(timeWork[position]);
        timeRes.setText(timeRest[position]);
        return convertView;
    }
}