package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentChoose extends Fragment {
    String newName;
    Switch aSwitch;
    public FragmentChoose(String name){
        super(R.layout.fragment_choose);
        this.newName=name;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton btn= view.findViewById(R.id.strelka);
        ImageButton btnBack = view.findViewById(R.id.strelka_back);
        aSwitch= view.findViewById(R.id.aSwitch);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked= aSwitch.isChecked();
                Fragment fragmentTimeWork = new fragmentTimeWork(newName, isChecked);
                FragmentPost.FragmentPost(fragmentTimeWork, requireActivity());
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.fragment, fragmentTimeWork)
//                        .addToBackStack(null)
//                        .commit();
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
