package com.soruco.bruno.sidea;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

public class Alarma extends AppCompatActivity {
    long tiempo=10000; //Tiempo inicial del temporizador
    private CountDownTimer timer;
    TextView numTemp,descTemp;
    Context thisContext=this;
    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    Button btn_apagado;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma);
        btn_apagado = findViewById(R.id.btn_apagado);
        numTemp=findViewById(R.id.textViewTemporizador);
        descTemp=findViewById(R.id.textViewDescripcion);
        //stopService(new Intent(this,ServiceMQTT.class));
        encender();
    }
    public void encender(){
        //Iniciamos temporizador
         timer = new CountDownTimer(tiempo, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempo=tiempo-1000;
                actualizarTiempo();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                descTemp.setText("Se enviaron mensajes de texto alerta a los contactos de emergencia");
                descTemp.setTextColor(Color.parseColor("#F7A232"));
                numTemp.setVisibility(View.INVISIBLE);
                Toast.makeText(thisContext, "Teoricamente se envio el mensaje", Toast.LENGTH_SHORT).show();
                String numeroCelular = "3875744803";
                //datos.get(0);
                //numeroCelular2="3884307596";

                String nombrePersona="Gabriel Roldan";
                String mensaje = nombrePersona+" y/o su familia estan en riezgo por gases toxicos presentes en su hogar, por favor contactase lo antes posible con el o alguien cercano";
                //ArrayList<String> datos = Numeros();
                //enviarMensaje(mensaje,datos.get(0));
                //enviarMensaje(mensaje,datos.get(1));
                //enviarMensaje("hola3","3875744803");

                //Manda un mensaje luego de 10 segundos si no contesta
                //Obtengo los numeros de telefono de los contactos de emergencia
                //ArrayList<String> datos = Numeros();
                //for (int i=0;i<datos.size();i++){
                // Toast.makeText(thisContext, "Dato "+i+datos.get(i), Toast.LENGTH_SHORT).show();
                //}

                //enviarMensaje(mensaje,"3875744803");
                //enviarMensaje(mensaje2,numeroCelular2);
                //enviarMensaje(mensaje3,numeroCelular3);
                //.... Se agregan las necesarias
            }
        };
        timer.start();
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

    public void actualizarTiempo(){
        int minutos = (int) tiempo / 60000;
        int segundos = (int) tiempo % 60000 / 1000;
        String tiempotxt = "";
        if (minutos<60) tiempotxt += "0";
        tiempotxt += minutos + ":";
        if (segundos<10) tiempotxt += "0";
            tiempotxt += segundos;
        numTemp.setText(tiempotxt);
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

    public void apagar(View view) {
        //Cancelamos el temporizador
        timer.cancel();
        //Apagamos sonido
        mediaPlayer.stop();
        mediaPlayer.release();
        //Apagamos vibracion
        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vi.cancel();
        //startService(new Intent(this,ServiceMQTT.class));
        //stopService(new Intent(this,ServiceMQTT.class));
        this.finish();
    }

    public void esperarysonar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,10, 0);
            }
        }, milisegundos);
    }


}