package com.example.project.Main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.databinding.FragmentNewTypeBinding;

public class NewTypeFragment extends Fragment {
    String newName;
    private NewTypeViewModel viewModel;
    private FragmentNewTypeBinding binding;
    public static NewTypeFragment newInstance() {
        return new NewTypeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewTypeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.strelka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName= binding.etFragmentCreateType.getText().toString();
                ChooseFragment chooseFragment = ChooseFragment.newInstance(newName);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, chooseFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        binding.etFragmentCreateType.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    binding.strelka.performClick();
                    return true;
                }
                return false;
            }
        });

        return view;
    }


}