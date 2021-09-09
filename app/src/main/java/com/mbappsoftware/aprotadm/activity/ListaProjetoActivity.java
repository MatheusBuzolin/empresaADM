package com.mbappsoftware.aprotadm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.adapter.ListaFunciAdapter;
import com.mbappsoftware.aprotadm.adapter.ListaProjetosAdapter;
import com.mbappsoftware.aprotadm.config.ConfiguracaoFirebase;
import com.mbappsoftware.aprotadm.helper.RecyclerItemClickListener;
import com.mbappsoftware.aprotadm.model.Projeto;
import com.mbappsoftware.aprotadm.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListaProjetoActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<Projeto> projetoList = new ArrayList<>();
    private ListaProjetosAdapter adapter;
    private RecyclerView recyclerProjeto;
    private String nomeProjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_projeto);

        db = ConfiguracaoFirebase.getfirebaseFirestore();


        iniciaComponentes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recProjetos();

    }

    private void recProjetoPesquisa() {

        db.collection("comprovante")
                .orderBy("nomeProjeto")
                .startAt(nomeProjeto)
                .endAt(nomeProjeto + "\uf8ff")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        projetoList.clear();
                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> listaDocumento = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : listaDocumento) {
                                Projeto projetoRec = d.toObject(Projeto.class);

                                projetoList.add(projetoRec);
                            }
                            //Collections.reverse(carroList);
                            adapter.notifyDataSetChanged();

                        }

                    }
                });
    }

    private void recProjetos() {

        db.collection("projeto")
                .orderBy("nomePesquisaProj", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        projetoList.clear();
                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> listaDocumento = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : listaDocumento) {
                                Projeto projetoRec = d.toObject(Projeto.class);

                                projetoList.add(projetoRec);
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
        toolbar.setTitle("LISTA DE PROJETOS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerProjeto = findViewById(R.id.listaProjeto_recycler);
        //tvResultado = findViewById(R.id.requisicoes_tv_aguardandoReq);

        //configurar recyclerview
        recyclerProjeto.setLayoutManager(new LinearLayoutManager(this));
        recyclerProjeto.setHasFixedSize(true);
        adapter = new ListaProjetosAdapter(projetoList);
        recyclerProjeto.setAdapter(adapter);

        recyclerProjeto.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerProjeto,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Projeto projeto = projetoList.get(position);

                        Intent i = new Intent(ListaProjetoActivity.this, DadosProjetoActivity.class);
                        i.putExtra("projetoList", projeto);
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