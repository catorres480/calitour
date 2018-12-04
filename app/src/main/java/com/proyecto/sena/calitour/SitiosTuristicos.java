package com.proyecto.sena.calitour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SitiosTuristicos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitiosturisticos);

        //Activar icono en action bar.

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    //Evento para invocar la Activity Visualizar
    public void visualizar(View v){
        Intent activityVisualizar = new Intent(this, Visualizar.class);
        activityVisualizar.putExtra("coleccion","sitio_turistico");
        activityVisualizar.putExtra("titulo","Ver Sitio Turistico");
        startActivity(activityVisualizar);
    }

    //Evento para invocar la Activity Agregar
    public void agregar(View v){
        Intent activityAgregar = new Intent(this, Agregar.class);
        activityAgregar.putExtra("coleccion","sitio_turistico");
        activityAgregar.putExtra("titulo","Agregar Sitio Turistico");
        startActivity(activityAgregar);
    }

    //Evento para invocar la Activity Borrar
    public void borrar(View v){
        Intent activityBorrar = new Intent(this, Borrar.class);
        activityBorrar.putExtra("coleccion","sitio_turistico");
        activityBorrar.putExtra("titulo","Borrar Sitio Turistico");
        startActivity(activityBorrar);
    }

    //Evento para invocar la Activity editar
    public void editar(View v){
        Intent activityEditar = new Intent(this, Editar.class);
        activityEditar.putExtra("coleccion","sitio_turistico");
        activityEditar.putExtra("titulo","Editar Sitio Turistico");
        startActivity(activityEditar);
    }
}
