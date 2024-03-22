package com.example.project;

import static android.content.Intent.getIntent;

import android.app.FragmentContainer;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

public class fragmentNewType extends Fragment {
    String newName;


    public fragmentNewType(){
        super(R.layout.fragment_new_type);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        newName=null;
        ImageButton btn= view.findViewById(R.id.strelka);
        ImageButton btnBack = view.findViewById(R.id.strelka_back);
        EditText etName = view.findViewById(R.id.etFragmentCreateType);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName= etName.getText().toString();
                Fragment chooseFragment= new FragmentChoose(newName);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, chooseFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE||event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                    btn.performClick();
                    return true;
                }
                return false;
            }
        });

    }
}
