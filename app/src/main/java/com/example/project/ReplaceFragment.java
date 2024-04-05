package com.example.project;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ReplaceFragment {
    public ReplaceFragment(){
    }
    public void replaceFragment(FragmentManager fragmentManager, Fragment fragment){
        fragmentManager.beginTransaction().replace(R.id.fragmentView, fragment).commit();
    }
}
