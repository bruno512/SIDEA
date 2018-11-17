package com.soruco.bruno.sidea;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.os.IBinder;
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
    MQTTHerper mqttHelper;
    Context thisContext=this;
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
        //Toast.makeText(this, "Servicio creado", Toast.LENGTH_LONG).show();
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
        Toast.makeText(this, "Servicio iniciado", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }
    private void startMqtt() {
        mqttHelper = new MQTTHerper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
            }
            @Override
            public void connectionLost(Throwable throwable) {
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

                    numeroCelular="3875744803";//datos.get(0);
                    numeroCelular2="3884307596";
                    //enviarMensaje("hOLA","3875744803");
                    nombrePersona="Gabriel Roldan";
                    mensaje = nombrePersona+" y/o su familia estan en riesgo por gases toxicos presentes en su hogar, por favor contactase lo antes posible con el o alguien cercano";
                    //ArrayList<String> datos = Numeros();
                    //enviarMensaje(mensaje,datos.get(0));
                    //enviarMensaje(mensaje,datos.get(1));
                    //Manda un mensaje luego de 10 segundos si no contesta
                    //startService(new Intent(thisContext, ServicioTimer.class));
                    /*new CountDownTimer(10000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            //Se puede colocar un mensaje cada segundo
                        }
                        public void onFinish() {
                            //Obtengo los numeros de telefono de los contactos de emergencia
                            //ArrayList<String> datos = Numeros();
                            for (int i=0;i<datos.size();i++){
                                Toast.makeText(thisContext, "Dato "+i+datos.get(i), Toast.LENGTH_SHORT).show();
                            }

                            //enviarMensaje(mensaje,"3875744803");
                            //enviarMensaje(mensaje2,numeroCelular2);
                            //enviarMensaje(mensaje3,numeroCelular3);
                            //.... Se agregan las necesarias
                        }
                    }.start();*/
                }
                Toast.makeText(thisContext, "Llego: " + mqttMessage.toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });
    }
    public void enviarMensaje(String men,String nroCel){
        Toast.makeText(thisContext,"Entro a enviar mensaje",Toast.LENGTH_SHORT).show();
        // Etiqueta utilizada para cancelar la petición
        String  tag_string_req = "string_req";
        String url = "http://servicio.smsmasivos.com.ar/enviar_sms.asp?api=1&usuario=SMSDEMO50725&clave=SMSDEMO50725734&tos="+nroCel+"&texto="+men;
        StringRequest strReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(VolleyLog.TAG, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(VolleyLog.TAG, "Error: " + error.getMessage());
            }
        }
        );
        // Añadimos la petición a la cola de peticiones de Volley
        AppControllerSMS.getInstance().addToRequestQueue(strReq, tag_string_req);
        Toast.makeText(thisContext,"Fin de enviar mensaje",Toast.LENGTH_SHORT).show();
    }

    public ArrayList<String> Numeros(){
        ArrayList<String> datos = new ArrayList<String>();
        BaseHelper baseHelper = new BaseHelper(this,"DEMOBD",null,1);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        try {
            String sql = "SELECT Tel FROM Contactos";
            Cursor c = db.rawQuery(sql,null);
            if (c.moveToFirst()){
                do{
                    String linea = c.getString(0);
                    datos.add(linea);
                }while (c.moveToNext()); // Repito mientras el sig registro exista
            }
            db.close();
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return datos;
    }
}