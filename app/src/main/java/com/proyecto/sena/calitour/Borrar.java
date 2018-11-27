package com.proyecto.sena.calitour;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    EditText txtBuscar;
    ListView listBuscar;
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


        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        listBuscar.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long ide) {

                Toast. makeText (getApplicationContext(), "Seleccionadó: " +listaBuscar.get(posicion),
                        Toast. LENGTH_SHORT ).show();
                id= posicion+1;
                listBuscar.setAdapter(null);
                buscar(view);

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
                        listaBuscar.add(document.getString("nombre"));
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


}
