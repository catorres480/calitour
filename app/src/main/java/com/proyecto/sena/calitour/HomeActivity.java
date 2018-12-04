package com.proyecto.sena.calitour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Activar icono en action bar.

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }







   public void btnSitiosTuristicos(View view) {
        Intent sitiosturisticos = new Intent(HomeActivity.this, SitiosTuristicos.class);
        startActivity(sitiosturisticos);
    }
    public void btnHoteles(View view) {
        Intent hoteles = new Intent(getBaseContext(), Hoteles.class);
        startActivity(hoteles);
    }
    public void btnOperadores(View view) {
        Intent Operadorturistico = new Intent(getBaseContext(), OperadorTuristico.class);
        Operadorturistico.putExtra("titulo","Ver Operador turistico");
        startActivity(Operadorturistico);
    }
}
