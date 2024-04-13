package com.example.project.Main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.databinding.FragmentTimeRestBinding;

public class TimeRestFragment extends Fragment {
    String name;
    long timeWork;
    FragmentTimeRestBinding binding;
    private TimeRestViewModel mViewModel;

    public static TimeRestFragment newInstance(String name, long timeWork) {
        TimeRestFragment timeRestFragment = new TimeRestFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putLong("timeWork", timeWork);
        timeRestFragment.setArguments(args);
        return timeRestFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTimeRestBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Bundle args = getArguments();
        name = args.getString("name");
        timeWork = args.getLong("timeWork");
        binding.fragmentTimePicker.setIs24HourView(true);
        binding.fragmentTimePicker.setHour(0);
        binding.fragmentTimePicker.setMinute(15);
        binding.strelkaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        binding.btnFragmentCreateType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.fragmentTimePicker.getMinute()==0){
                    Toast.makeText(getContext(), "Выбрано 0 минут", Toast.LENGTH_SHORT).show();
                }else{
                    long timeRest = (binding.fragmentTimePicker.getHour()*3600+binding.fragmentTimePicker.getMinute()*60)*1000;
                    try{
                        ((post)getActivity()).postpost(name, timeWork, timeRest);
                    }catch (ClassCastException ignored){}

                    getActivity().getSupportFragmentManager().beginTransaction().remove(TimeRestFragment.this).commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TimeRestViewModel.class);
        // TODO: Use the ViewModel
    }

}