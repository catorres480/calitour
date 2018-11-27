package com.proyecto.sena.calitour;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.widget.EditText;

public class Hoteles extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoteles);

    }




    //Evento para invocar la Activity Visualizar
    public void visualizar(View v){
        Intent activityVisualizar = new Intent(this, Visualizar.class);
        activityVisualizar.putExtra("coleccion","hotel");
        activityVisualizar.putExtra("titulo","Ver Hotel");
        startActivity(activityVisualizar);
    }

    //Evento para invocar la Activity Agregar
    public void agregar(View v){
        Intent activityAgregar = new Intent(this, Agregar.class);
        activityAgregar.putExtra("coleccion","hotel");
        activityAgregar.putExtra("titulo","Agregar Hotel");
        startActivity(activityAgregar);
    }

    //Evento para invocar la Activity Borrar
    public void borrar(View v){
        Intent activityBorrar = new Intent(this, Borrar.class);
        activityBorrar.putExtra("coleccion","hotel");
        activityBorrar.putExtra("titulo","Borrar Hotel");
        startActivity(activityBorrar);
    }

    //Evento para invocar la Activity editar
    public void editar(View v){
        Intent activityEditar = new Intent(this, Editar.class);
        activityEditar.putExtra("coleccion","hotel");
        activityEditar.putExtra("titulo","Editar Hotel");
        startActivity(activityEditar);
    }


}