package com.example.project;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
        EditText etName = view.findViewById(R.id.etFragmentCreateType);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName= etName.getText().toString();
                Fragment chooseFragment= new FragmentChoose(newName);
                FragmentActivity a = requireActivity();
                FragmentPost.FragmentPost(chooseFragment, a);
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.fragment, chooseFragment)
//                        .addToBackStack(null)
//                        .commit();
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
