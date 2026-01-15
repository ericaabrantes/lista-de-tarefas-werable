package com.example.listadetarefas;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.AudioDeviceCallback;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView statusTextView;
    private AudioHelper audioHelper;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Conecta com o layout
        statusTextView = findViewById(R.id.text_view);

        // Inicializa os ajudantes
        audioHelper = new AudioHelper(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Verifica o áudio assim que abre
        checkAudioOutput();

        // Fica vigiando se conecta fone Bluetooth (Atividade  3)
        audioManager.registerAudioDeviceCallback(new AudioDeviceCallback() {
            @Override
            public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
                super.onAudioDevicesAdded(addedDevices);
                if (audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                    updateStatus("Fone Bluetooth conectado!");
                }
            }

            @Override
            public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
                super.onAudioDevicesRemoved(removedDevices);
                if (!audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                    updateStatus("Fone desconectado.");
                }
            }
        }, null);
    }

    private void checkAudioOutput() {
        boolean hasSpeaker = audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER);
        boolean hasBluetooth = audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP);

        if (hasSpeaker || hasBluetooth) {
            updateStatus("Áudio pronto para uso.");
        } else {
            // Atividade 4: Redirecionar para config se não tiver áudio
            updateStatus("Sem áudio. Abrindo config...");
            Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXTRA_CONNECTION_ONLY", true);
            intent.putExtra("EXTRA_CLOSE_ON_CONNECT", true);
            intent.putExtra("android.bluetooth.devicepicker.extra.FILTER_TYPE", 1);

            // Verifica se existe um app de configuração antes de abrir para não dar erro
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    // Função para atualizar o texto na tela
    private void updateStatus(String message) {
        runOnUiThread(() -> {
            statusTextView.setText(message);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        });
    }
}