package com.example.project;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.databinding.FragmentChooseBinding;

import java.sql.Time;

public class ChooseFragment extends Fragment {

    private ChooseViewModel mViewModel;
    String name;
    FragmentChooseBinding binding;

    public static ChooseFragment newInstance(String name) {
        ChooseFragment fragment = new ChooseFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= FragmentChooseBinding.inflate(inflater, container,false);
        View view = binding.getRoot();
        Bundle args = getArguments();
        name = args.getString("name");
        binding.strelka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked= binding.aSwitch.isChecked();
            TimeWorkFragment timeWorkFragment= TimeWorkFragment.newInstance(name, isChecked);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, timeWorkFragment)
                    .addToBackStack(null)
                    .commit();
            }
        });
        binding.strelkaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }


}