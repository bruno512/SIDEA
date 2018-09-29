package com.soruco.bruno.sidea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

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
