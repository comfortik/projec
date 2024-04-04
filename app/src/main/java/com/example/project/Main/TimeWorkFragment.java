package com.example.project.Main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.example.project.databinding.FragmentTimeWorkBinding;

public class TimeWorkFragment extends Fragment {
    String name;
    boolean isInterval;
    FragmentTimeWorkBinding binding;

    private TimeWorkViewModel mViewModel;

    public static TimeWorkFragment newInstance(String name, boolean isInterval) {
        TimeWorkFragment timeWorkFragment = new TimeWorkFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putBoolean("isInterval", isInterval);
        timeWorkFragment.setArguments(args);
        return timeWorkFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTimeWorkBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Bundle args = getArguments();
        name  = args.getString("name");
        isInterval = args.getBoolean("isInterval");
        binding.fragmentTimePicker.setIs24HourView(true);
        binding.fragmentTimePicker.setHour(0);
        binding.fragmentTimePicker.setMinute(10);
        if(isInterval){
            binding.btnFragmentCreateType.setVisibility(View.VISIBLE);
            binding.strelka.setVisibility(View.GONE);
        }
        else binding.strelka.setVisibility(View.VISIBLE);
        binding.tv.setText(name);
        binding.btnFragmentCreateType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeWork = (binding.fragmentTimePicker.getHour()*3600+binding.fragmentTimePicker.getMinute()*60)*1000;
                try{
                    ((post)getActivity()).postpost(name, timeWork);
                }catch (ClassCastException ignored){}

                getActivity().getSupportFragmentManager().beginTransaction().remove(TimeWorkFragment.this).commit();

            }
        });
        binding.strelka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeWork = (binding.fragmentTimePicker.getHour()*3600+binding.fragmentTimePicker.getMinute()*60)*1000;
                TimeRestFragment timeRestFragment= TimeRestFragment.newInstance(name, timeWork);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, timeRestFragment)
                        .addToBackStack(null)
                        .commit();
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.fragment, fragmentTimeRest)
//                        .addToBackStack(null)
//                        .commit();
            }
        });
        binding.strelkaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TimeWorkViewModel.class);
        // TODO: Use the ViewModel
    }

}