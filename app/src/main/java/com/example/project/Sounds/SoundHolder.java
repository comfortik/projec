package com.example.project.Sounds;

import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.databinding.DropDownBinding;


public class SoundHolder  extends RecyclerView.ViewHolder {
    private DropDownBinding binding;
    private Sound sound;



    public SoundHolder(DropDownBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        binding.imgButton.setOnClickListener(v -> {
            if (sound.getMediaPlayer().isPlaying()) {
                sound.getMediaPlayer().pause();
            } else {
                sound.getMediaPlayer().start();
                sound.getMediaPlayer().setLooping(true);
            }
        });
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if(progress!=0){
                        float volume = progress/100f;
                        sound.getMediaPlayer().setVolume(volume, volume);
                    }

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void bind(Sound sound) {
        this.sound = sound;
        binding.tv.setText(sound.getName());
        binding.imgButton.setBackgroundResource(sound.getImg());
    }
    public  void resetSeek(){

        binding.seekBar.setProgress(0);
    }



}
