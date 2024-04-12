package com.example.project.Sounds;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.project.Emotion.EmotionDiaryFragment;
import com.example.project.OnHideFragmentContainerListener;
import com.example.project.Profile.ProfileFragment;
import com.example.project.R;
import com.example.project.ReplaceFragment;
import com.example.project.Settings.SettingsFragment;
import com.example.project.databinding.FragmentSoundBinding;
import com.google.android.material.navigation.NavigationBarView;

public class SoundFragment extends Fragment {

    private SoundViewModel mViewModel;
    FragmentSoundBinding binding;
    private OnHideFragmentContainerListener hideFragmentContainerListener;
    private MediaPlayer mediaPlayer1, mediaPlayer2, mediaPlayer3, mediaPlayer4;




    public static SoundFragment newInstance() {
        return new SoundFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSoundBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Context context= getContext();
        mediaPlayer1 = MediaPlayer.create(context, R.raw.sound1);
        mediaPlayer2 = MediaPlayer.create(context, R.raw.sound1);
        mediaPlayer3 = MediaPlayer.create(context, R.raw.sound1);
        mediaPlayer4 = MediaPlayer.create(context, R.raw.sound1);


        binding.buttonSound1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mediaPlayer1 != null) {
                    if (mediaPlayer1.isPlaying()) {
                        mediaPlayer1.stop();
                        mediaPlayer1.release();
                        mediaPlayer1 = null;
                    } else {
                        mediaPlayer1 = MediaPlayer.create(context, R.raw.sound1);
                        mediaPlayer1.start();
                    }
                } else {
                    mediaPlayer1 = MediaPlayer.create(context, R.raw.sound1);
                    mediaPlayer1.start();
                }

            }
        });
        binding.buttonSound2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mediaPlayer2 != null) {
                    if (mediaPlayer2.isPlaying()) {
                        mediaPlayer2.stop();
                        mediaPlayer2.release();
                        mediaPlayer2 = null;
                    } else {
                        mediaPlayer2 = MediaPlayer.create(context, R.raw.sound2);
                        mediaPlayer2.start();
                    }
                } else {
                    mediaPlayer2 = MediaPlayer.create(context, R.raw.sound2);
                    mediaPlayer2.start();
                }
            }
        });
        binding.buttonSound3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mediaPlayer3 != null) {
                    if (mediaPlayer3.isPlaying()) {
                        mediaPlayer3.stop();
                        mediaPlayer3.release();
                        mediaPlayer3 = null;
                    } else {
                        mediaPlayer3 = MediaPlayer.create(context, R.raw.sound3);
                        mediaPlayer3.start();
                    }
                } else {
                    mediaPlayer3 = MediaPlayer.create(context, R.raw.sound3);
                    mediaPlayer3.start();
                }
            }
        });
        binding.buttonSound4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mediaPlayer4 != null) {
                    if (mediaPlayer4.isPlaying()) {
                        mediaPlayer4.stop();
                        mediaPlayer4.release();
                        mediaPlayer4 = null;
                    } else {
                        mediaPlayer4 = MediaPlayer.create(context, R.raw.sound4);
                        mediaPlayer4.start();
                    }
                } else {
                    mediaPlayer4 = MediaPlayer.create(context, R.raw.sound4);
                    mediaPlayer4.start();
                }
            }
        });
        binding.seekbarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress!=0) {
                    float volume = progress / 100f;
                    mediaPlayer1.setVolume(volume, volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.seekbarVolume2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress!=0) {
                    float volume = progress / 100f;
                    mediaPlayer2.setVolume(volume, volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.seekbarVolume3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress!=0) {
                    float volume = progress / 100f;
                    mediaPlayer3.setVolume(volume, volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.seekbarVolume4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress!=0){
                    float volume = progress/100f;
                    mediaPlayer4.setVolume(volume, volume);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });





        return view ;
    }
    public void stopMedia(){
        if(mediaPlayer1!=null){
            mediaPlayer1.stop();
            mediaPlayer1.release();
        }
        if(mediaPlayer2!=null){
            mediaPlayer2.stop();
            mediaPlayer2.release();
        }
        if(mediaPlayer3!=null){
            mediaPlayer3.stop();
            mediaPlayer3.release();
        }
        if(mediaPlayer4!=null){
            mediaPlayer4.stop();
            mediaPlayer4.release();
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SoundViewModel.class);
        // TODO: Use the ViewModel
    }
    public void setOnHideFragmentContainerListener(OnHideFragmentContainerListener listener) {
        this.hideFragmentContainerListener = listener;
    }

}