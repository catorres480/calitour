package com.proyecto.sena.calitour;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Borrar extends AppCompatActivity {
    //myDB variable para referenciar la base de datos en FireBase
    FirebaseFirestore myDB;

    //Variables para referenciar los compontes graficos en el Layout
    List<String> listaBuscar;
    int id=0;
    EditText txtNombre;
    EditText txtDireccion;
    EditText txtSitioWeb;
    EditText txtTelefonoFijo;
    EditText txtCorreoElectronico;
    EditText txtCelular;
    TextView txtBuscar;
    Spinner listBuscar;
    TextView twTitulo;



    //Button btnBorrar;

   Bundle datos;
    //Colección (Tabla) a crear en FireBase
    String coleccion ="x" ;
    String titulo= "Promotor";

    //ProgressDialog para mostrar un mensaje de espera al usuario
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar);

        //Activar icono en action bar.

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        FirebaseApp.initializeApp(this);

        myDB = FirebaseFirestore.getInstance();


        txtNombre = findViewById(R.id.txtNombre);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtTelefonoFijo = findViewById(R.id.txtTelefonoFijo);
        txtCorreoElectronico = findViewById(R.id.txtCorreoElectronico);
        txtSitioWeb = findViewById(R.id.txtSitioWeb);
        txtCelular = findViewById(R.id.txtCelular);
        //btnBorrar = findViewById(R.id.btnBorrar);


        twTitulo = findViewById(R.id.lblTitulo);

        datos = getIntent().getExtras();
        titulo= datos.getString("titulo");
        coleccion = datos.getString("coleccion");

        twTitulo.setText(titulo);


        txtBuscar = findViewById(R.id.txtBuscar);
        listBuscar = findViewById(R.id.listBuscar);

        dialog = new ProgressDialog(this);

        listaBuscar = new ArrayList<String>();
        listarBuscar();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listaBuscar);
        listBuscar.setAdapter(adapter);

     /*   btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                borrar(v);
            }
        });*/


        //Escucha 0 y cuando se selecciona llena los dados.
        listBuscar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id1)
            {
                Toast. makeText (getApplicationContext(), "Seleccionado: " +listaBuscar.get(position),
                        Toast. LENGTH_SHORT ).show();
                String[] seleccion = listaBuscar.get(position).split("-");
                Log.d("tam ",""+seleccion.length);
                for (int i=0;i<seleccion.length; i++)
                {
                    Log.d("arreglo "+i , seleccion[i]);
                }

                String valor=  seleccion[1];
                id = Integer.parseInt(valor);
                Log.d("",""+id);
                // listBuscar = null;
                buscar(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });



    }

    //Método para borrar registro en la BD
    public void borrar(View v) {

        //Inicio del ProgressDialog
        dialog.setMessage("Espere un momento");
        dialog.show();

        //Referencia al documento a borrar
        myDB.collection(coleccion).document(""+id)
                .delete()  //Borrado del documento
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) { //Listener en caso de exito
                        Toast.makeText(getApplication(), "Documento borrado correctamente", Toast.LENGTH_LONG).show();
                        //Limpiar las cajas de texto sino el registro a buscar no existe
                      //  txtId.setText("");
                        txtDireccion.setText("");
                        txtNombre.setText("");
                        txtTelefonoFijo.setText("");
                        txtCelular.setText("");
                        txtSitioWeb.setText("");
                        txtCorreoElectronico.setText("");


                        dialog.dismiss(); //Cierre del ProgressDialog
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) { //Listener en caso de error
                        Toast.makeText(getApplication(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        dialog.dismiss(); //Cierre del ProgressDialog
                    }
                });


    }
    public void listarBuscar(){
        final Task<QuerySnapshot> nombre;
        nombre = myDB.collection(coleccion).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String text = "";
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        listaBuscar.add(document.getString("nombre")+"-"+document.getString("id"));
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
    }
    public void buscar(View v) {

        //Inicio del ProgressDialog
        dialog.setMessage("Espere un momento");
        dialog.show();

        //Referencia a la colección (tabla) en FireBase y al respectivo documento (el documento tiene como id la cedula del paciente)
        DocumentReference docRef = myDB.collection(coleccion).document(""+id);
        docRef.get() //Busqueda del documento
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() { //Listener para gestionar los resultados de la busqueda
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            //Copia del documento buscado
                            DocumentSnapshot doc = task.getResult(); //Resultados de la busqueda

                            if (doc.exists()) { //En caso tal la busqueda sea exitosa

                                id = Integer.parseInt(doc.getString("id"));

                                txtDireccion.setText(doc.getString("direccion"));
                                txtNombre.setText(doc.getString("nombre"));
                                txtTelefonoFijo.setText(doc.getString("telefono_fijo"));
                                txtCelular.setText(doc.getString("celular"));
                                txtSitioWeb.setText(doc.getString("sitio_web"));
                                txtCorreoElectronico.setText(doc.getString("correo_electronico"));

                                dialog.dismiss();

                            } else { //En caso tal la busqueda no sea exitosa
                                Toast.makeText(getApplication(), "No existe", Toast.LENGTH_LONG).show();
                                //Limpiar las cajas de texto sino el registro a buscar no existe

                                txtDireccion.setText("");
                                txtNombre.setText("");
                                txtTelefonoFijo.setText("");
                                txtCelular.setText("");
                                txtSitioWeb.setText("");
                                txtCorreoElectronico.setText("");


                                dialog.dismiss(); //Cierre del ProgressDialog
                            }
                        } else {

                            Toast.makeText(getApplication(), "Error: " + task.getException(), Toast.LENGTH_LONG).show();
                            dialog.dismiss(); //Cierre del ProgressDialog
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) { //Listener para gestionar posibles fallas
                        Toast.makeText(getApplication(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        dialog.dismiss(); //Cierre del ProgressDialog
                    }
                });

    }

    public void btnRegresar(View view) {
        Intent home = new Intent(getBaseContext(), HomeActivity.class);
        startActivity(home);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    //Boton regreso action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent home = new Intent(Borrar.this, HomeActivity.class);
        startActivity(home);

        return super.onContextItemSelected(item);

    }




}
