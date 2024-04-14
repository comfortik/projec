package com.example.project.Sounds;

import android.media.MediaPlayer;

public class Sound {
    MediaPlayer mediaPlayer;
    String name;
    int img;
    public Sound(MediaPlayer mediaPlayer, String name, int img){
        this.mediaPlayer=mediaPlayer;
        this.name= name;
        this.img= img;
    }
    public Sound(){}

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }



}
