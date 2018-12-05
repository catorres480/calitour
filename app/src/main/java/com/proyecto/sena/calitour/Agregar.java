package com.proyecto.sena.calitour;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Console;
import java.util.HashMap; import java.util.Map;


public class Agregar extends AppCompatActivity {

    //myDB variable para referenciar la base de datos en FireBase
    FirebaseFirestore myDB;

    //Variables para referenciar los compontes graficos en el Layout
    EditText txtId;
    EditText txtNombre;
    EditText txtDireccion;
    EditText txtSitioWeb;
    EditText txtTelefonoFijo;
    EditText txtCorreoElectronico;
    EditText txtCelular;
    TextView twTitulo;
    int cont =0;

    Bundle datos;

    //Colección (Tabla) a crear en FireBase
    String coleccion ="x" ;
    String titulo= "Promotor";
    //ProgressDialog para mostrar un mensaje de espera al usuario
    private ProgressDialog dialog;

    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        //Activar icono en action bar.

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        FirebaseApp.initializeApp(this);

        myDB = FirebaseFirestore.getInstance();
        /*FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        myDB.setFirestoreSettings(settings);*/

        twTitulo = findViewById(R.id.lblTitulo);
        txtNombre = findViewById(R.id.txtNombre);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtTelefonoFijo = findViewById(R.id.txtTelefonoFijo);
        txtCorreoElectronico = findViewById(R.id.txtCorreoElectronico);
        txtSitioWeb = findViewById(R.id.txtSitioWeb);
        txtCelular = findViewById(R.id.txtCelular);
       // btnGuardar = findViewById(R.id.btnAgregar);

        datos = getIntent().getExtras();
         titulo= datos.getString("titulo");
         coleccion = datos.getString("coleccion");
         twTitulo.setText(titulo);
        //Instancia de la clase ProgressDialog
        dialog = new ProgressDialog(this);

    /*    btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guardar(v);
            }
            });*/

        int id = obtenerId();
    }






    public int obtenerId()
    {
        String id = ""; //Variable para concatenas todos los valores de un documento
        Log.d("entidad",coleccion);
        myDB.collection(coleccion)
                .get() //Obtención de todos los documentos de la colección medicos
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) { //Listener para gestionar la consulta

                        if (task.isSuccessful()) { //Si la busqueda fue exitosa


                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                if(!document.getId().equals(""))
                                {
                                    Log.d("doc",document.getId());
                                    if(cont<(Integer.parseInt(document.getId())))
                                    {
                                        cont = Integer.parseInt(document.getId());
                                    }
                                    Log.d("cont es ",""+cont);
                                }
                            }

                        } else { //Si la busqueda no fue exitosa
                           cont = 0;
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // toastResult("Error while adding the data : " + e.getMessage());
                        Toast.makeText(getApplication(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        dialog.dismiss(); //Cierre del ProgressDialog
                    }
                });

      return  cont;
    }


    //Método para guardar nuevo registro en la BD
    public void guardar(View v)
    {
        if( TextUtils.isEmpty(txtNombre.getText())) {
            txtNombre.setError("El nombre es requerido!");
        }
        else if( TextUtils.isEmpty(txtDireccion.getText())) {
            txtDireccion.setError("La dirección es requerida!");
        }
        else if( TextUtils.isEmpty(txtSitioWeb.getText())) {
            txtSitioWeb.setError("El sitio web es requerido!");
        }
        else if( TextUtils.isEmpty(txtCorreoElectronico.getText())) {
            txtCorreoElectronico.setError("El correo es requerido!");
        }
        else if( TextUtils.isEmpty(txtTelefonoFijo.getText())) {
            txtTelefonoFijo.setError("El telefono es requerido!");
        }
        else if( TextUtils.isEmpty(txtCelular.getText())) {
            txtCelular.setError("El celular es requerido!");
        }
        else {


            //Inicio del ProgressDialog
            dialog.setMessage("Espere un momento");
            dialog.show();

            cont++;
            Log.d("deberia guardar este ", "" + cont);


            //Referencia a la colección en FireBase (sino existe la crea)
            CollectionReference datos = myDB.collection(coleccion);


            //HashMap es una estructura que permite almacenar datos usando la regla, llave => valor
            Map<String, Object> dato = new HashMap<>();
            //Para almacenar valores en un HashMap se utiliza el metodo put (llave, valor)
            dato.put("id", "" + cont);
            dato.put("celular", txtCelular.getText().toString());
            dato.put("correo_electronico", txtCorreoElectronico.getText().toString());
            dato.put("nombre", txtNombre.getText().toString());
            dato.put("direccion", txtDireccion.getText().toString());
            dato.put("telefono_fijo", txtTelefonoFijo.getText().toString());
            dato.put("sitio_web", txtSitioWeb.getText().toString());
            //Creación de un nuevo documento en la colección pacientes
            datos.document("" + cont)
                    .set(dato) //Agregación del nuevo registro
                    .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) { //Listener en caso de exito
                            Toast.makeText(getApplication(), "Registro guardado", Toast.LENGTH_LONG).show();
                            dialog.dismiss(); //cierre del ProgressDialog
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() { //Listener en caso de error
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplication(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            dialog.dismiss(); //cierre del ProgressDialog
                        }
                    });
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    //Boton regreso action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent home = new Intent(Agregar.this, HomeActivity.class);
        startActivity(home);

        return super.onContextItemSelected(item);

    }


}
