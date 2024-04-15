package com.example.project.Sounds;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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

import java.util.ArrayList;
import java.util.List;

public class SoundFragment extends Fragment {

    private SoundViewModel mViewModel;
    FragmentSoundBinding binding;
    SoundAdapter soundAdapter;
    List<Sound> soundList;

    public static SoundFragment newInstance() {
        return new SoundFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= FragmentSoundBinding.inflate(inflater, null, false);
        View view = binding.getRoot();
        soundList = new ArrayList<>();
        soundList.add(new Sound(MediaPlayer.create(getContext(), R.raw.sound1), "Сверчки", R.drawable.ic_launcher_background ));
        soundList.add(new Sound(MediaPlayer.create(getContext(), R.raw.sound2), "Звуки волн", R.drawable.ic_launcher_background));
        soundList.add(new Sound(MediaPlayer.create(getContext(), R.raw.sound3), "Sound 3", R.drawable.ic_launcher_background));
        soundList.add(new Sound(MediaPlayer.create(getContext(), R.raw.sound4), "Sound 4", R.drawable.ic_launcher_background));
        soundAdapter = new SoundAdapter(soundList, getContext());
        binding.recycler.setAdapter(soundAdapter);
        binding.btn.setOnClickListener(v -> {
            for (Sound sound : soundList) {
                MediaPlayer mediaPlayer = sound.getMediaPlayer();
                if (mediaPlayer.isPlaying()) {
                    sound.getMediaPlayer().pause();
                    sound.getMediaPlayer().seekTo(0);
                }
            }
            soundAdapter.resetSeekBars();
            soundAdapter.notifyDataSetChanged();

        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SoundViewModel.class);
        // TODO: Use the ViewModel
    }
    public void releaseMedia(){
        for (int i=0; i<soundList.size(); i++){
            soundList.get(i).getMediaPlayer().release();
        }
    }


}