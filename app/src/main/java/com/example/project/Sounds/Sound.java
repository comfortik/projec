package com.example.project.Sounds;

import android.media.MediaPlayer;

public class Sound {
    MediaPlayer mediaPlayer;
    String name;
    int img;


    int imgPause;
    public Sound(MediaPlayer mediaPlayer, String name, int img, int imgPause){
        this.mediaPlayer=mediaPlayer;
        this.name= name;
        this.img= img;
        this.imgPause = imgPause;
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
    public int getImgPause() {
        return imgPause;
    }

    public void setImgPause(int imgPause) {
        this.imgPause = imgPause;
    }



}
