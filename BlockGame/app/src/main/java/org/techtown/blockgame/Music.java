package org.techtown.blockgame;

import android.media.MediaPlayer;

class Music {
    private int position;
    private MediaPlayer player;
    private String musicString=null;

    public Music() {

    }

    public Music(String musicString) {
        this.musicString = musicString;
    }

    public void setMusicString(String musicString) {
        this.musicString = musicString;
    }

    public void playAudio() {
        killPlayer();
        try {
            if(musicString!=null) {
                int i =MainActivity.mContext.getResources().getIdentifier("@raw/" + musicString, "raw", MainActivity.mContext.getPackageName());
                player = MediaPlayer.create(MainActivity.mContext, i);
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        player.release();
                    }
                });
                //player.prepare(); // 재생 준비

                player.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void killPlayer() {
        if (player != null)
            try {
                player.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void stopAudio() {
        if (player != null) {
            try {
                player.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pauseAudio() {
        if (player != null) {
            try {
                position = player.getCurrentPosition();
                player.pause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void resumeAudio() {
        if (player != null && !player.isPlaying()) {
            try {
                player.start();
                player.seekTo(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}