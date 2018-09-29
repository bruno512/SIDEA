package com.soruco.bruno.sidea;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaContactos extends AppCompatActivity {

    ListView lista;
    ArrayList<String> listado;
    ArrayList<Category> category = new ArrayList<Category>();

    // Para actualizar el listado una vez que se modificó
    @Override
    protected void onPostResume() {
        super.onPostResume();
        CargarListado2();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        lista = (ListView) findViewById(R.id.lista);
        category = ListarContactos();
        AdapterItem adapter = new AdapterItem(this, category);

        lista.setAdapter(adapter);
        // Para ingresar al listado desde el menu:
        //Cargar();
        //PAra ingresar al listado desde el boton:
        //CargarListado();

        // Para capturar el clic en cada elemento del listado
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String clave = category.get(i).getCategoryId();
                String nombre = category.get(i).getNombre();
                String apellido = category.get(i).getApellido();
                String tel = category.get(i).getDescription();

                // Para pasar los datos a la actividad Modificar
                Intent intent = new Intent(ListaContactos.this,Modificar.class);
                int c = Integer.parseInt(clave);
                intent.putExtra("Id", c);
                intent.putExtra("Nombre", nombre);
                intent.putExtra("Apellido", apellido);
                intent.putExtra("Tel", tel);
                startActivity(intent);
            }
        });

        // Para los botones de adelante y retroceso
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    // Forma vieja con lista simple
    private void CargarListado(){
        listado = ListarPersonas();
        ArrayAdapter<String> adapater = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listado);
        lista.setAdapter(adapater);
    }

    private void CargarListado2(){
        category = ListarContactos();
        AdapterItem adapter = new AdapterItem(this, category);
        lista.setAdapter(adapter);
    }

    private ArrayList<String> ListarPersonas(){
        ArrayList<String> datos = new ArrayList<String>();
        BaseHelper baseHelper = new BaseHelper(this,"DEMOBD",null,1);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        try {
            String sql = "SELECT Id,Nombre,Apellido,Tel FROM Contactos";
            Cursor c = db.rawQuery(sql,null);
            if (c.moveToFirst()){
                do{
                    String linea = c.getInt(0)+" "+c.getString(1)+" "+c.getString(2)+" "+ c.getInt(3);
                    datos.add(linea);
                }while (c.moveToNext()); // Repito mientras el sig registro exista
            }
            db.close();
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return datos;
    }

    private ArrayList<Category> ListarContactos(){
        ArrayList<Category> datos = new ArrayList<Category>();
        BaseHelper baseHelper = new BaseHelper(this,"DEMOBD",null,1);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        try {
            String sql = "SELECT Id,Nombre,Apellido,Tel FROM Contactos";
            Cursor c = db.rawQuery(sql,null);
            if (c.moveToFirst()){
                do{
                    Category item = new Category(c.getString(0), " ",c.getString(3), getResources().getDrawable(R.drawable.persona),c.getString(1),c.getString(2));
                    datos.add(item);
                }while (c.moveToNext()); // Repito mientras el sig registro exista
            }
            db.close();
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return datos;
    }

    public void Cargar(){
        BaseHelper baseHelper = new BaseHelper(this,"DEMOBD",null,1);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        if (db!=null){
            Cursor c = db.rawQuery("SELECT * FROM Contactos",null);
            int cantidad = c.getCount();
            int i = 0;
            String[] arreglo = new String[cantidad];
            // Pregunto si puedo estar en el 1er registro
            if (c.moveToFirst()){
                do{
                    String linea = c.getInt(0)+" "+c.getString(1)+" "+c.getString(2)+" "+ c.getInt(3);

                    arreglo[i] = linea;
                    i++;

                }while (c.moveToNext()); // Repito mientras el sig registro exista
            }
            // Creo un adapatador para comunicar el origen de datos con un elemento de la lista
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arreglo);
            ListView lista = (ListView) findViewById(R.id.lista);
            //Ya tenemos listo el ListView para pasarle el adaptador
            lista.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Si apretan el boton hacia atras
        if (id == android.R.id.home){
            // Cierro esta actividad
            finish();
        }
        if (id == R.id.addContact){
            Intent intent = new Intent(this,Create.class);
            this.startActivity(intent);
        }
        if (id == R.id.home){
            Intent intent = new Intent(this,MainActivity.class);
            this.startActivity(intent);
        }

        //Devuelvo el item seleccionado
        return super.onOptionsItemSelected(item);
    }
}
