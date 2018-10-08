package com.soruco.bruno.sidea;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class Alarma extends AppCompatActivity {
    TextView dato;
    private CameraManager mCameraManager;
    private String mCameraId=null;
    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    Button btn_apagado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma);
        dato = findViewById(R.id.txt_dato);
        btn_apagado = (Button) findViewById(R.id.btn_apagado);
        stopService(new Intent(this,ServiceMQTT.class));
        encender();
    }
    public void encender(){
        //Encendemos el sonido
        mediaPlayer = MediaPlayer.create(getApplication(), R.raw.sonido_humocorto);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 3, 0);
        //Encendemos la vibracion
        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 1000};
        vi.vibrate(pattern, 0);
        //Aumentamos el volumen en 4 seg
        //esperarysonar(4000);
    }
    public void esperarysonar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,10, 0);
            }
        }, milisegundos);
    }
    public void apagar(View view) {
        //Apagamos sonido
        mediaPlayer.stop();
        mediaPlayer.release();
        //Apagamos vibracion
        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vi.cancel();
        startService(new Intent(this,ServiceMQTT.class));
        finish();
    }
}