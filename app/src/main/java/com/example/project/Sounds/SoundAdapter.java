package com.example.project.Sounds;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.project.databinding.DropDownBinding;

import java.util.ArrayList;
import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {
    List<Sound> sounds;
    List<SoundHolder> soundHolders; // список для хранения всех SoundHolder

    public SoundAdapter(List<Sound> sounds){
        this.sounds = sounds;
        this.soundHolders = new ArrayList<>(); // инициализируем список SoundHolder
    }

    @NonNull
    @Override
    public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        DropDownBinding binding = DropDownBinding.inflate(layoutInflater, parent, false);
        SoundHolder soundHolder = new SoundHolder(binding);
        soundHolders.add(soundHolder); // добавляем созданный SoundHolder в список
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



