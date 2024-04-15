package com.example.project.Sounds;

import android.content.Context;
import android.graphics.PorterDuff;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.databinding.DropDownBinding;


public class SoundHolder  extends RecyclerView.ViewHolder {
    private DropDownBinding binding;
    private Sound sound;
    Context context;



    public SoundHolder(DropDownBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        int color = ContextCompat.getColor( context, R.color.seekBar);
        binding.seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        binding.seekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        binding.imgButton.setOnClickListener(v -> {
            if (sound.getMediaPlayer().isPlaying()) {
                sound.getMediaPlayer().pause();
                binding.imgButton.setBackgroundResource(R.drawable.ic_launcher_background);
                binding.seekBar.setProgress(0);
            } else {
                sound.getMediaPlayer().start();
                sound.getMediaPlayer().setLooping(true);
                binding.imgButton.setBackgroundResource(R.drawable.ic_launcher_foreground);
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
        binding.imgButton.setBackgroundResource(R.drawable.ic_launcher_background);
        binding.seekBar.setProgress(0);
    }



}
