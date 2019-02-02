package com.soruco.bruno.sidea;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Objects;
public class ServiceMQTT extends Service {
    Context thisContext=this;
    MQTTHerper mqttHelper;
    NotificationCompat.Builder notificacion;
    String nombrePersona,mensaje,numeroCelular,nombrePersona2,mensaje2,numeroCelular2,nombrePersona3,mensaje3,numeroCelular3;
    private static final int idUnica=333;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificacion = new NotificationCompat.Builder(this);
        notificacion.setAutoCancel(true);
        notificacion.setTimeoutAfter(3000);
        Toast.makeText(this, "Servicio creado", Toast.LENGTH_LONG).show();
        //startMqtt();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Servicio destruido", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess) {
        startMqtt();
        Toast.makeText(this, "Se ejecuto onStartComand", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    private void startMqtt() {
        mqttHelper = new MQTTHerper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Toast.makeText(thisContext, "Conexion completa", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void connectionLost(Throwable throwable) {
                Toast.makeText(thisContext, "Conexion perdida", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {
                Log.w("Debug", mqttMessage.toString());
                if (Objects.equals(mqttMessage.toString(), "1")) {
                    Toast.makeText(thisContext, "Llego: " + mqttMessage.toString(), Toast.LENGTH_SHORT).show();

                    //Creo la notificacion
                    notificacion.setSmallIcon(R.mipmap.ic_launcher);
                    notificacion.setTicker("Nueva Alarma");
                    notificacion.setPriority(Notification.PRIORITY_HIGH);
                    notificacion.setWhen(System.currentTimeMillis());
                    notificacion.setContentTitle("PELIGRO GASES TOXICOS");
                    notificacion.setContentText("Presione para eliminar alarma");

                    //Ejecuto la alarma para que suene aunque se lance la notificacion
                    Intent intent = new Intent(ServiceMQTT.this, Alarma.class);
                    startActivity(intent);

                    //Espero a que el usuario toque la notificacion y luego se abre la alarma
                    PendingIntent pendingIntent = PendingIntent.getActivity(thisContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    notificacion.setContentIntent(pendingIntent);
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(idUnica,notificacion.build());

                }
                Toast.makeText(thisContext, "Llego: " + mqttMessage.toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });
    }
}