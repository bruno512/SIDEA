package com.soruco.bruno.sidea;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaContactosUtiles extends AppCompatActivity {

    ListView lista;
    ArrayList<Category> category = new ArrayList<Category>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos_utiles);

        // Para mostrar todos sus elementos en el ListView de nuestro “activity_main.xml” tenemos que crear el adaptador:
        lista = (ListView) findViewById(R.id.listView);
        // Cargo el ArrayList
        Category itemCEN = new Category("1", "Central de Emergencias Nacional", "911", getResources().getDrawable(R.drawable.emergency),"","");
        Category itemE = new Category("2", "Emergencias médicas", "107", getResources().getDrawable(R.drawable.hospital),"","");
        Category itemEA = new Category("2", "Emergencia Ambiental", "105", getResources().getDrawable(R.drawable.ambiental),"","");
        Category itemB = new Category("2", "Bomberos", "100", getResources().getDrawable(R.drawable.firefighter),"","");
        Category itemDC = new Category("2", "Defensa civil", "103", getResources().getDrawable(R.drawable.defensa),"","");
        Category itemCNT = new Category("2", "Centro Nacional de Intoxicaciones", "0 800 333 0160", getResources().getDrawable(R.drawable.toxic),"","");
        Category itemHN = new Category("2", "Hospital Materno Infantil", "3884245005", getResources().getDrawable(R.drawable.hospitalninos),"","");
        Category itemP = new Category("2", "Gabriel", "3875744803", getResources().getDrawable(R.drawable.persona),"","");
        // https://www.argentina.gob.ar/tema/emergencias
        category.add(itemCEN);
        category.add(itemE);
        category.add(itemEA);
        category.add(itemB);
        category.add(itemDC);
        category.add(itemCNT);
        category.add(itemHN);
        category.add(itemP);

        AdapterItem adapter = new AdapterItem(this, category);

        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                String title = category.get(position).getTitle();
                String categoryId = category.get(position).getCategoryId();
                String description = category.get(position).getDescription();

                // Para realizar la llamada:
                // IMPORTANTE: En el dispositivo donde se va a usar hay q darle permisos a la app para hacer llamadas desde la configuracion del dispositivo
                Intent llamar = new Intent(android.content.Intent.ACTION_CALL,
                        Uri.parse("tel:"+description));
                if (ActivityCompat.checkSelfPermission(ListaContactosUtiles.this, Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(llamar);

            }
        });

        // Para los botones de adelante y retroceso
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        if (id == R.id.home){
            Intent intent = new Intent(this,MainActivity.class);
            this.startActivity(intent);
        }

        //Devuelvo el item seleccionado
        return super.onOptionsItemSelected(item);
    }
}
