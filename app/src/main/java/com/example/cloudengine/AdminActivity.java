package com.example.cloudengine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cloudengine.data.model.Persona;
import com.example.cloudengine.ui.login.LoginActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminActivity extends AppCompatActivity {

    private List<Persona> listGame = new ArrayList<>();
    ArrayAdapter<Persona> arrayAdapterGame;

    private Button logout;

    EditText nomP, descripcionP, passwordP, precioP, claveP;
    ListView listV_personas;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Persona gameSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        nomP = findViewById(R.id.txt_nombrePersona);
        descripcionP = findViewById(R.id.txt_descripcionPersona);
        passwordP = findViewById(R.id.txt_passwordPersona);
        precioP = findViewById(R.id.txt_precioPersona);
        claveP = findViewById(R.id.txt_videoPersona);

        listV_personas = findViewById(R.id.lv_datosPersonas);
        inicializarFirebase();
        listarDatos();

        listV_personas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gameSelected = (Persona) parent.getItemAtPosition(position);
                nomP.setText(gameSelected.getNombre());
                descripcionP.setText(gameSelected.getDescripcion());
                passwordP.setText(gameSelected.getPassword());
                precioP.setText(gameSelected.getPrecio());
                claveP.setText(gameSelected.getVideo());
            }
        });

        logout = (Button) findViewById(R.id.signOut);

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            }
        });

    }

    private void listarDatos() {
        databaseReference.child("Videojuegos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listGame.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Persona p = objSnaptshot.getValue(Persona.class);
                    listGame.add(p);

                    arrayAdapterGame = new ArrayAdapter<Persona>(AdminActivity.this, android.R.layout.simple_list_item_1, listGame);
                    listV_personas.setAdapter(arrayAdapterGame);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String nombre = nomP.getText().toString();
        String descripcion = descripcionP.getText().toString();
        String password = passwordP.getText().toString();
        String precio = precioP.getText().toString();
        String clave = claveP.getText().toString();
        switch (item.getItemId()){
            case R.id.icon_add:{
                if (nombre.equals("")||descripcion.equals("")||password.equals("")){
                    validacion();
                }
                else {
                    Persona p = new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setDescripcion(descripcion);
                    p.setPassword(password);
                    p.setPrecio(precio);
                    p.setVideo(clave);
                    databaseReference.child("Videojuegos").child(p.getUid()).setValue(p);
                    Toast.makeText(this, "Agregar", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }
            case R.id.icon_save:{
                Persona p = new Persona();
                p.setUid(gameSelected.getUid());
                p.setNombre(nomP.getText().toString().trim());
                p.setDescripcion(descripcionP.getText().toString().trim());
                p.setPassword(passwordP.getText().toString().trim());
                p.setPrecio(precioP.getText().toString().trim());
                p.setVideo(claveP.getText().toString().trim());
                databaseReference.child("Videojuegos").child(p.getUid()).setValue(p);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.icon_delete:{
                Persona p = new Persona();
                p.setUid(gameSelected.getUid());
                databaseReference.child("Videojuegos").child(p.getUid()).removeValue();
                Toast.makeText(this, "Eliminar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }
            default:break;
        }
        return true;
    }

    private void limpiarCajas() {
        nomP.setText("");
        descripcionP.setText("");
        passwordP.setText("");
        precioP.setText("");
        claveP.setText("");
    }

    private void validacion() {
        String nombre = nomP.getText().toString();
        String descripcion = descripcionP.getText().toString();
        String password = passwordP.getText().toString();
        String precio = precioP.getText().toString();
        String clave = claveP.getText().toString();

        if (nombre.equals("")){
            nomP.setError("Required");
        }
        else if (descripcion.equals("")){
            descripcionP.setError("Required");
        }
        else if (password.equals("")){
            passwordP.setError("Required");
        }
        else if (precio.equals("")){
            precioP.setError("Required");
        }
        else if (clave.equals("")){
            claveP.setError("Required");
        }
    }
}