package com.example.project.Sounds;


import static java.security.AccessController.getContext;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.project.R;
import com.example.project.databinding.DropDownBinding;

import java.util.ArrayList;
import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {
    List<Sound> sounds;
    Context context;
    List<SoundHolder> soundHolders;

    public SoundAdapter(List<Sound> sounds, Context context){
        this.sounds = sounds;
        this.context  = context;
        this.soundHolders = new ArrayList<>();
    }

    @NonNull
    @Override
    public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        DropDownBinding binding = DropDownBinding.inflate(layoutInflater, parent, false);
        SoundHolder soundHolder = new SoundHolder(binding, context);
        soundHolders.add(soundHolder);
        return soundHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SoundHolder holder, int position) {
        Sound sound = sounds.get(position);
        holder.bind(sound);
    }

    @Override
    public int getItemCount() {
        return sounds.size();
    }

    public void resetSeekBars(){
        for(int i = 0; i < soundHolders.size(); i++){ // перебираем все SoundHolder в списке
            SoundHolder soundHolder = soundHolders.get(i);
            soundHolder.resetSeek(); // сбрасываем SeekBar для каждого SoundHolder
        }
    }
}



