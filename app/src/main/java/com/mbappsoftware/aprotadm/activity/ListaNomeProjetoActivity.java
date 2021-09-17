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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.adapter.ListaComprovanteAdapter;
import com.mbappsoftware.aprotadm.adapter.ListaProjComproAdapter;
import com.mbappsoftware.aprotadm.config.ConfiguracaoFirebase;
import com.mbappsoftware.aprotadm.helper.Constant;
import com.mbappsoftware.aprotadm.helper.RecyclerItemClickListener;
import com.mbappsoftware.aprotadm.model.Comprovante;
import com.mbappsoftware.aprotadm.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListaNomeProjetoActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<Comprovante> comprovanteList = new ArrayList<>();
    private ListaProjComproAdapter adapter;
    private RecyclerView recyclerComprovante;
    private String txNomeFuncionario, txNomeProjeto, txData;
    private int numTela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_nome_projeto);

        db = ConfiguracaoFirebase.getfirebaseFirestore();

        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (getIntent().getExtras().containsKey("numTela"))) {
            numTela = extras.getInt("numTela");

            if (numTela == Constant.NUM_OPS_1){
                txNomeProjeto = extras.getString("pesq_txNomeProjetoProjeto");
                txNomeFuncionario = extras.getString("pesq_txNomeProjetoNome");

            }else if (numTela == Constant.NUM_OPS_5){
                txNomeProjeto = extras.getString("pesq_txNomeProjetoProjeto");
                txNomeFuncionario = extras.getString("pesq_txNomeProjetoNome");
                txData = extras.getString("pesq_txNomeProjetoData");
            }
            //Log.i("sdfdsfd", "ListaNomeProjetoActivity 1 > " + txNomeProjeto + " > " + txNomeFuncionario);
        }

        iniciaComponentes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recProjetos();

    }

    private void recProjetos() {

        db.collection("comprovante")
                //.orderBy("nomeProjeto")
                .orderBy("nomeFuncionario")
                .startAt(txNomeFuncionario)
                .endAt(txNomeFuncionario + "\uf8ff")
                //.orderBy("qtdNotas", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        comprovanteList.clear();
                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> listaDocumento = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : listaDocumento) {
                                Comprovante comprovanteRec = d.toObject(Comprovante.class);

                                if (comprovanteRec.getNomeProjeto().equals(txNomeProjeto)){

                                    if (numTela == Constant.NUM_OPS_5 && comprovanteRec.getDiaDaNota().equals(txData)){
                                        comprovanteList.add(comprovanteRec);

                                    }else{
                                        comprovanteList.add(comprovanteRec);
                                    }

                                    Log.i("cscsc", " -> " + comprovanteRec);
                                }


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
        toolbar.setTitle("LISTA NOME FUN. + PROJETO");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerComprovante = findViewById(R.id.listaNomeProjeto_recycler);
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

                        Intent i = new Intent(ListaNomeProjetoActivity.this, ComprovanteActivity.class);
                        i.putExtra("pesq_txNomeProjetoNome", txNomeFuncionario);
                        i.putExtra("pesq_txNomeProjetoProjeto", txNomeProjeto);
                        if (numTela == Constant.NUM_OPS_5){
                            i.putExtra("pesq_txNomeProjetoData", txData);
                        }
                        i.putExtra("numTela", numTela);
                        i.putExtra("comprovante", comprovante);
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