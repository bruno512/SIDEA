package com.soruco.bruno.sidea;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Auto extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Lanzar Servicio

        //Intent serviceIntent = new Intent();
        // serviceIntent.setAction("exemple.com.intentomqtt.ServiceMQTT");
        //context.startService(serviceIntent);
        Intent i = new Intent(context, MainActivity.class);
        // Cuando se intenta abrir una Activity desde un componente que no sea una actividad
        // se debe de avisar mediante un Flag que está siendo iniciada por un componente alternativo.
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
