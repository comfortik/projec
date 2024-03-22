package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class fragmentTimeWork extends Fragment {


    Activity activity;

    String name;
    boolean isChecked;
    post post;
    ImageButton strelka;
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            activity = (Activity)context;
        }
    }


    public fragmentTimeWork(String name, boolean isChecked){
        super(R.layout.fragment_time_work);
        this.name = name;
        this.isChecked= isChecked;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        strelka= view.findViewById(R.id.strelka);
        Button btn = view.findViewById(R.id.btnFragmentCreateType);
        ImageButton btnBack = view. findViewById(R.id.strelka_back);
        TimePicker timePicker= view.findViewById(R.id.fragmentTimePicker);
        TextView tv = view.findViewById(R.id.tv);
        timePicker.setIs24HourView(true);
        timePicker.setHour(0);
        timePicker.setMinute(15);
        if(isChecked){
            btn.setVisibility(View.VISIBLE);
            strelka.setVisibility(View.GONE);
        }
        else strelka.setVisibility(View.VISIBLE);
        tv.setText(name);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeWork = (timePicker.getHour()*3600+timePicker.getMinute()*60)*1000;
                try{
                    ((post)activity).postpost(name, timeWork);
                }catch (ClassCastException ignored){}
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragmentTimeWork.this).commit();
            }
        });
        strelka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeWork = (timePicker.getHour()*3600+timePicker.getMinute()*60)*1000;
                fragmentTimeRest fragmentTimeRest = new fragmentTimeRest(name, timeWork);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, fragmentTimeRest)
                        .addToBackStack(null)
                        .commit();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

}
