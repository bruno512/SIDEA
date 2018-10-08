package com.soruco.bruno.sidea;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class ServicioTimer extends Service {

    String FORMAT = "%02d:%02d:%02d";
    private static String TAG = "Servicio";
    public static final String PAQUETE = "com.soruco.bruno.sidea"; //ejemplo com.proyecto.MainActivity
    Intent bi = new Intent(PAQUETE);

    CountDownTimer cdt = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Comienza el timer...");
        cdt = new CountDownTimer(9000, 1000) {
            public void onTick(long millisUntilFinished) {
                String tiempo = ""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                //con esto se envia el tiempo
                bi.putExtra("Tiempo", tiempo);
                sendBroadcast(bi);
            }
            public void onFinish() {
                //se envia el tiempo finalizado
                bi.putExtra("Fin", "Tiempo terminado!");
                sendBroadcast(bi);
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        cdt.cancel();
        Log.i(TAG, "Timer cancelado");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}