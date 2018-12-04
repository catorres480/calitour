package com.proyecto.sena.calitour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class OperadorTuristico extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operadorturistico);

        //Activar icono en action bar.

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

    }

    //Evento para invocar la Activity Visualizar
    public void visualizar(View v){
        Intent activityVisualizar = new Intent(this, Visualizar.class);
        activityVisualizar.putExtra("coleccion","operador_turistico");
        activityVisualizar.putExtra("titulo","Ver Operador turistico");

        startActivity(activityVisualizar);
    }

    //Evento para invocar la Activity Agregar
    public void agregar(View v){
        Intent activityAgregar = new Intent(this, Agregar.class);
        activityAgregar.putExtra("coleccion","operador_turistico");
        activityAgregar.putExtra("titulo","Agregar Operador turistico");
        startActivity(activityAgregar);
    }

    //Evento para invocar la Activity Borrar
    public void borrar(View v){
        Intent activityBorrar = new Intent(this, Borrar.class);
        activityBorrar.putExtra("coleccion","operador_turistico");
        activityBorrar.putExtra("titulo","Borrar Operador turistico");
        startActivity(activityBorrar);
    }

    //Evento para invocar la Activity editar
    public void editar(View v){
        Intent activityEditar = new Intent(this, Editar.class);
        activityEditar.putExtra("coleccion","operador_turistico");
        activityEditar.putExtra("titulo","Editar Operador turistico");
        startActivity(activityEditar);
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    //Boton regreso action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent home = new Intent(OperadorTuristico.this, HomeActivity.class);
        startActivity(home);

        return super.onContextItemSelected(item);

    }

}
