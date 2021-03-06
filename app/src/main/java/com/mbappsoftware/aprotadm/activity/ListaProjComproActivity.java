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

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.adapter.ListaComprovanteAdapter;
import com.mbappsoftware.aprotadm.adapter.ListaProjComproAdapter;
import com.mbappsoftware.aprotadm.adapter.ListaProjetosAdapter;
import com.mbappsoftware.aprotadm.config.ConfiguracaoFirebase;
import com.mbappsoftware.aprotadm.helper.Constant;
import com.mbappsoftware.aprotadm.helper.RecyclerItemClickListener;
import com.mbappsoftware.aprotadm.model.Comprovante;
import com.mbappsoftware.aprotadm.model.Projeto;

import java.util.ArrayList;
import java.util.List;

public class ListaProjComproActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<Comprovante> comprovanteList = new ArrayList<>();
    private ListaProjComproAdapter adapter;
    private RecyclerView recyclerComprovante;
    private String nomeProjeto;
    private int numTela;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_proj_compro);

        db = ConfiguracaoFirebase.getfirebaseFirestore();

        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (getIntent().getExtras().containsKey("pesq_txNomeProjeto"))) {
            numTela = extras.getInt("numTela");

            if (numTela == Constant.NUM_OPS_6){
                nomeProjeto = extras.getString("pesq_txNomeProjeto");
            }

        }

        iniciaComponentes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recProjetoPesquisa();
    }

    private void recProjetoPesquisa() {

        db.collection("comprovante")
                .orderBy("nomeProjeto")
                //.orderBy("nomeFuncionario")
                .startAt(nomeProjeto)
                .endAt(nomeProjeto + "\uf8ff")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        comprovanteList.clear();
                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> listaDocumento = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : listaDocumento) {
                                Comprovante comprovanteRec = d.toObject(Comprovante.class);

                                comprovanteList.add(comprovanteRec);
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
        toolbar.setTitle("COMPROVANTES");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerComprovante = findViewById(R.id.listaProjCompro_recycler);
        //tvResultado = findViewById(R.id.requisicoes_tv_aguardandoReq);

        //configurar recyclerview
        recyclerComprovante.setLayoutManager(new LinearLayoutManager(this));
        recyclerComprovante.setHasFixedSize(true);
        adapter = new ListaProjComproAdapter(comprovanteList);
        recyclerComprovante.setAdapter(adapter);

        recyclerComprovante.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerComprovante,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Comprovante comprovante = comprovanteList.get(position);

                        Intent i = new Intent(ListaProjComproActivity.this, ComprovanteActivity.class);
                        i.putExtra("comprovante", comprovante);
                        i.putExtra("pesq_txNomeProjeto", nomeProjeto);
                        i.putExtra("numTela", numTela);
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