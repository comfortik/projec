package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class fragmentTimeRest extends Fragment {
    long timeWork, timeRest;
    String name;
    Activity activity;
    TimePicker timePicker;
    ImageButton btnBack;
    Button btn;

    public fragmentTimeRest(String name, long timeWork){
        super(R.layout.fragment_time_rest);
        this.name= name;
        this.timeWork= timeWork;
    }
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            activity = (Activity)context;
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timePicker= view.findViewById(R.id.fragmentTimePicker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(0);
        timePicker.setMinute(10);
        btnBack= view. findViewById(R.id.strelka_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        btn = view.findViewById(R.id.btnFragmentCreateType);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeRest = (timePicker.getHour()*3600+timePicker.getMinute()*60)*1000;
                try{
                    ((post)activity).postpost(name, timeWork, timeRest);
                }catch (ClassCastException ignored){}
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragmentTimeRest.this).commit();
                Fragment fragmentNewType = new fragmentNewType();
                FragmentPost.FragmentPost(fragmentNewType, requireActivity());
            }
        });


    }
}
