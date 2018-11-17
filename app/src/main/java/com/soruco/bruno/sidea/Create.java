package com.soruco.bruno.sidea;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Create extends AppCompatActivity {

    EditText et_nombre, et_apellido, et_tel;

    @Override
    // hola
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_apellido = (EditText) findViewById(R.id.et_apellido);
        et_tel = (EditText)findViewById(R.id.et_tel);
        // Para darle una mascara de input phone
        et_tel.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // Para los botones de adelante y retroceso
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void GuardarDatos(View view){
        try {
            String nombre = et_nombre.getText().toString();
            String apellido = et_apellido.getText().toString();
            String tel = et_tel.getText().toString();
            BaseHelper baseHelper = new BaseHelper(this,"DEMOBD",null,1);
            SQLiteDatabase db = baseHelper.getWritableDatabase();
            if (db!=null){
                ContentValues registroNuevo = new ContentValues();
                registroNuevo.put("Nombre", nombre);
                registroNuevo.put("Apellido", apellido);
                registroNuevo.put("Tel", tel);
                // Se hace la inserccion del registro
                long i = db.insert("Contactos",null, registroNuevo);
                if (i>0){
                    Toast.makeText(this,"Registro insertado",Toast.LENGTH_SHORT).show();
                }
            }
            //Vuelvo hacia atras
            onBackPressed();
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Si apretan el boton hacia atras
        if (id == android.R.id.home){
            // Cierro esta actividad
            finish();
        }
        //Devuelvo el item seleccionado
        return super.onOptionsItemSelected(item);
    }
}
