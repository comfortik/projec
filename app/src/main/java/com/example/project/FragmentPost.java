package com.example.project;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.project.R;
import androidx.appcompat.app.AppCompatActivity;

public class FragmentPost {
    public static void FragmentPost(Fragment f, FragmentActivity a){
        FragmentManager fragmentManager = a.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, f)
                .addToBackStack(null)
                .commit();
    }
}
