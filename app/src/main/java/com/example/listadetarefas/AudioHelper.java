package com.example.listadetarefas;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.content.pm.PackageManager;

public class AudioHelper {
    private Context context;
    private AudioManager audioManager;

    public AudioHelper(Context context) {
        this.context = context;
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public boolean audioOutputAvailable(int type) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
            return false;
        }
        AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
        for (AudioDeviceInfo device : devices) {
            if (device.getType() == type) {
                return true;
            }
        }
        return false;
    }
}