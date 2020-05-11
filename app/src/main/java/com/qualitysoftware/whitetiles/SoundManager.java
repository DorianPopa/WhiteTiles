package com.qualitysoftware.whitetiles;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;

public class SoundManager {
    Context context;

    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap = null;

    private SoundManager(){}

    public SoundManager(Context _context){
        context = _context;
        init();
    }

    private void init(){
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();

        loadSounds();
    }

    private void loadSounds(){
        if(soundPoolMap != null){
            soundPoolMap.put(soundPoolMap.size() + 1, soundPool.load(context, R.raw.piano_c4, 1));
        }
    }

    public void playSound(int soundId){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(audioManager != null){
            float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float leftVolume = curVolume/maxVolume;
            float rightVolume = curVolume/maxVolume;
            int priority = 1;
            int no_loop = 0;
            float normal_playback_rate = 1f;

            try {
                soundPool.play(soundPoolMap.get(soundId), leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
            }
            catch(NullPointerException npe){
                Log.e("SoundManager", "Tried playing an invalid sound");
                npe.printStackTrace();
            }
        }
    }

}
