package com.proyecto.sena.calitour;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Visualizar extends AppCompatActivity {
    //myDB variable para referenciar la base de datos en FireBase
    FirebaseFirestore myDB;

    //Variables para referenciar los compontes graficos en el Layout
    List<String> listaBuscar;
    EditText txtBuscar;
    ListView listBuscar;

    int id=0;
    EditText txtNombre;
    EditText txtDireccion;
    EditText txtSitioWeb;
    EditText txtTelefonoFijo;
    EditText txtCorreoElectronico;
    EditText txtCelular;
    TextView twTitulo;

    Button btnRegresar;

    Bundle datos;

    int cont =0;

    //Colección (Tabla) a crear en FireBase
    String coleccion ="x";
    String titulo= "Promotor";

    //ProgressDialog para mostrar un mensaje de espera al usuario
    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);

        FirebaseApp.initializeApp(this);

        myDB = FirebaseFirestore.getInstance();


        txtNombre = findViewById(R.id.txtNombre);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtTelefonoFijo = findViewById(R.id.txtTelefonoFijo);
        txtCorreoElectronico = findViewById(R.id.txtCorreoElectronico);
        txtSitioWeb = findViewById(R.id.txtSitioWeb);
        txtCelular = findViewById(R.id.txtCelular);
        twTitulo = findViewById(R.id.lblTitulo);
        btnRegresar = findViewById(R.id.btnVisualizar);



        datos = getIntent().getExtras();
        titulo= datos.getString("titulo");
        coleccion = datos.getString("coleccion");

        twTitulo.setText(titulo);


        txtBuscar = findViewById(R.id.txtBuscar);
        listBuscar = findViewById(R.id.listBuscar);

        dialog = new ProgressDialog(this);

      /*  btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buscar(v);
            }
        });*/

        listaBuscar = new ArrayList<String>();
        listarBuscar();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listaBuscar);
        listBuscar.setAdapter(adapter);




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
        //Escucha listview y cuando se selecciona llena los dados.
        listBuscar.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long ide) {

                Toast. makeText (getApplicationContext(), "Seleccionadó: " +listaBuscar.get(posicion),
                        Toast. LENGTH_SHORT ).show();
                id= posicion+1;
                //listBuscar.setAdapter(null);
               listBuscar.setAdapter(null);
                buscar(view);

            }
        });


    }
    //Metodo onClick para enviar E-mail
    public void ibtnEmail(View view) {


        String mail = "http://"+txtCorreoElectronico.getText().toString();

        enviarEmail(mail, "Peticion de informacion", "Buen dia le desea Cali Tour");
    }

    //Metodo onClick para hacer llamada
    public void ibtnLlamada(View view) {

        dialog.setMessage("Espere un momento");
        dialog.show();

        String num = "032"+txtTelefonoFijo.getText().toString();
        hacerLlamada(num);

    }

    public void ibtnCell(View view) {

        dialog.setMessage("Espere un momento");
        dialog.show();

        String num = txtCelular.getText().toString();
        hacerLlamada(num);
    }
    //acceder a la pagina web
    public void ibtnUrl(View view) {

        dialog.setMessage("Espere un momento");
        dialog.show();

        String url = "http://"+txtSitioWeb.getText().toString();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

    }



    //Método para buscar  en la BD
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
    //Metodo para llenar el listview con la base de datos Firebase
    public void listarBuscar(){


        myDB.collection(coleccion).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String text="";
                    for(QueryDocumentSnapshot document : task.getResult()){
                        listaBuscar.add(document.getString("nombre"));
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
    }

    //Metodo para enviar un E-mail
    public void enviarEmail(String destino, String asunto, String mensaje) {

        dialog.setMessage("Espere un momento");
        dialog.show();

        //Intent para enviar Email
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{destino});
        email.putExtra(Intent.EXTRA_SUBJECT, asunto);
        email.putExtra(Intent.EXTRA_TEXT, mensaje);

        //requiero para hacer prompts email
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Seleccione un cliente de correo:"));

    }
    //Metodo para hacer llamadas
    public void hacerLlamada(String numero) {

        dialog.setMessage("Espere un momento");
        dialog.show();

        try {

            //Verificación del API, si el API es mayor a 22 se debe solicitar explicitamente el
            //permiso para hacer llamadas Manifest.permission.CALL_PHONE
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    //Solicitud del permiso
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);

                    return;
                }

            }

            //Intent para hacer llamadas
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + numero));
            startActivity(callIntent);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




}
