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
import com.mbappsoftware.aprotadm.adapter.ListaComprovanteAdapter;
import com.mbappsoftware.aprotadm.config.ConfiguracaoFirebase;
import com.mbappsoftware.aprotadm.helper.RecyclerItemClickListener;
import com.mbappsoftware.aprotadm.helper.UsuarioFirebase;
import com.mbappsoftware.aprotadm.model.Comprovante;
import com.mbappsoftware.aprotadm.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListaComprovanteActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<Comprovante> comprovanteList = new ArrayList<>();
    private ListaComprovanteAdapter adapter;
    private RecyclerView recyclerComprovante;
    private Usuario funcionario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_comprovante);

        db = ConfiguracaoFirebase.getfirebaseFirestore();

        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (getIntent().getExtras().containsKey("funcionarioList"))) {

            funcionario = (Usuario) extras.getSerializable("funcionarioList");

            Log.i("sdfdsfd", "HOME 1 > " + funcionario.getUid() + " > " + funcionario.getNome());
        }

        iniciaComponentes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recProjetos();

    }

    private void iniciaComponentes() {
        //configurando toobar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("LISTA DE PROJETOS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerComprovante = findViewById(R.id.listaComprovante_recycler);
        //tvResultado = findViewById(R.id.requisicoes_tv_aguardandoReq);

        //configurar recyclerview
        recyclerComprovante.setLayoutManager(new LinearLayoutManager(this));
        recyclerComprovante.setHasFixedSize(true);
        adapter = new ListaComprovanteAdapter(comprovanteList);
        recyclerComprovante.setAdapter(adapter);

        recyclerComprovante.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerComprovante,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Comprovante comprovante = comprovanteList.get(position);

                        Intent i = new Intent(ListaComprovanteActivity.this, ComprovanteActivity.class);
                        i.putExtra("comprovanteList", comprovante);
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

    private void recProjetos() {

        db.collection("comprovante")
                .whereEqualTo("uidFuncionario", funcionario.getUid())
                .orderBy("qtdNotas", Query.Direction.DESCENDING)
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

    @Override
    public boolean onSupportNavigateUp() {
        if (funcionario != null) {
            Intent i = new Intent(ListaComprovanteActivity.this, DadosFuncionarioActivity.class);
            i.putExtra("funcionarioList", funcionario);
            startActivity(i);
        }
        return false;

    }
}