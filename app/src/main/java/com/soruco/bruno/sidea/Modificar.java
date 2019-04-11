package com.soruco.bruno.sidea;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Modificar extends AppCompatActivity {

    EditText et_nombre, et_apellido, et_tel;
    Button bt_modificar, bt_borrar;
    int id;
    String nombre, apellido, tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        //Con el Bundle recibo los datos de la actividad ListaContactos, donde se encuentra el listado
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            // Obtengo los datos contenidos en el bundle, para luego asignarles en la vista
            id = bundle.getInt("Id");
            nombre = bundle.getString("Nombre");
            apellido = bundle.getString("Apellido");
            tel = bundle.getString("Tel");
        }

        // Declaro los objetos de la vista en variables
        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_apellido = (EditText) findViewById(R.id.et_apellido);
        et_tel = (EditText) findViewById(R.id.et_tel);
        // Para darle una mascara de input phone
        et_tel.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        bt_modificar = (Button) findViewById(R.id.bt_modificar);
        bt_borrar = (Button) findViewById(R.id.bt_borrar);

        // Le asigno los datos a la vista para mostrar obtenidos del bundle
        et_nombre.setText(nombre);
        et_apellido.setText(apellido);
        et_tel.setText(tel);

        // Cuando hacen clic en el boton MODIFICAR
        bt_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Llamo al metodo Modificar con los valores editados de la vista, menos el Id
                Modificar(id,et_nombre.getText().toString(),et_apellido.getText().toString(),et_tel.getText().toString());
                //Vuelvo hacia atras
                onBackPressed();
            }
        });

        // Cuando hacen clilc en el boton BORRAR
        bt_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eliminar(id);
                //Vuelvo hacia atras
                onBackPressed();
            }
        });

        // Para los botones de adelante y retroceso
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void Modificar(int Id, String Nombre, String Apellido, String Tel){
        try {
            BaseHelper baseHelper = new BaseHelper(this,"DEMOBD",null,1);
            SQLiteDatabase db = baseHelper.getWritableDatabase();
            if (db!=null){
                ContentValues registroModificado = new ContentValues();
                registroModificado.put("Nombre", Nombre);
                registroModificado.put("Apellido", Apellido);
                registroModificado.put("Tel", Tel);
                // Se hace la modificacion del registro
                long i = db.update("Contactos",registroModificado,"Id="+Id,null);
                if (i>0){
                    Toast.makeText(this,"Contacto modificado",Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void Eliminar(int Id){
        try {
            BaseHelper baseHelper = new BaseHelper(this,"DEMOBD",null,1);
            SQLiteDatabase db = baseHelper.getWritableDatabase();
            if (db!=null){
                // Se hace la eliminacion del registro
                long i = db.delete("Contactos","Id="+Id,null);
                if (i>0){
                    Toast.makeText(this,"Contacto eliminado",Toast.LENGTH_SHORT).show();
                }
            }
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
