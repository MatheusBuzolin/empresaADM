package com.mbappsoftware.aprotadm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.adapter.ListaFunciAdapter;
import com.mbappsoftware.aprotadm.config.ConfiguracaoFirebase;
import com.mbappsoftware.aprotadm.helper.RecyclerItemClickListener;
import com.mbappsoftware.aprotadm.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListaFuncionarioActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<Usuario> funcionarioList = new ArrayList<>();
    private ListaFunciAdapter adapter;
    private RecyclerView recyclerFuncionario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_funcionario);

        db = ConfiguracaoFirebase.getfirebaseFirestore();

        iniciaComponentes();

    }

    @Override
    protected void onStart() {
        super.onStart();
        recFuncionarios();

    }

    private void recFuncionarios() {

        db.collection("funcionario")
                .orderBy("nome", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        funcionarioList.clear();
                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> listaDocumento = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : listaDocumento) {
                                Usuario funcionarioRec = d.toObject(Usuario.class);

                                funcionarioList.add(funcionarioRec);
                            }
                            //Collections.reverse(carroList);
                            adapter.notifyDataSetChanged();

                        }

                    }
                });
    }

    private void iniciaComponentes() {
        //configurando toobar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Funcionarios");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerFuncionario = findViewById(R.id.listaFuncionario_recycler);
        //tvResultado = findViewById(R.id.requisicoes_tv_aguardandoReq);

        //configurar recyclerview
        recyclerFuncionario.setLayoutManager(new LinearLayoutManager(this));
        recyclerFuncionario.setHasFixedSize(true);
        adapter = new ListaFunciAdapter(funcionarioList);
        recyclerFuncionario.setAdapter(adapter);

        recyclerFuncionario.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerFuncionario,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Usuario funcionario = funcionarioList.get(position);

                        Intent i = new Intent(ListaFuncionarioActivity.this, DadosFuncionarioActivity.class);
                        i.putExtra("funcionarioList", funcionario);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));
    }
}