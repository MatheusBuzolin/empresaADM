package com.mbappsoftware.aprotadm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.adapter.ListaComprovanteAdapter;
import com.mbappsoftware.aprotadm.config.ConfiguracaoFirebase;
import com.mbappsoftware.aprotadm.helper.Permissoes;
import com.mbappsoftware.aprotadm.helper.RecyclerItemClickListener;
import com.mbappsoftware.aprotadm.helper.UsuarioFirebase;
import com.mbappsoftware.aprotadm.model.Comprovante;
import com.mbappsoftware.aprotadm.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DatabaseReference firebaseRef;
    private FirebaseFirestore db;
    private FirebaseAuth autenticacao;

    private Usuario usuario;
    private Comprovante comprovante;
    private List<Comprovante> comprovanteList = new ArrayList<>();
    private ListaComprovanteAdapter adapter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerComprovante;
    private RecuperaDadosFunc runRecDadosFunc = new RecuperaDadosFunc();
    private RecListComprovantes runListaComprovantes = new RecListComprovantes();
    private VerificaConexao runVerificaConexao = new VerificaConexao();
    private ConnectivityManager conexRun;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private int conta = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //validar permissoes
        Permissoes.validarPermissoes(permissoes, this, 1);

        firebaseRef = ConfiguracaoFirebase.getFirebase();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        db = ConfiguracaoFirebase.getfirebaseFirestore();

        conexRun = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        iniciaComponentes();

        new Thread(runListaComprovantes).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(runVerificaConexao).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        new Thread(runVerificaConexao).interrupt();
    }

    private void iniciaComponentes() {

        //configurando toobar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Bem Vindo!");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerComprovante = findViewById(R.id.home_rv_comprovantes);

        drawerLayout = findViewById(R.id.drawerLayoutId);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigationViewId);
        navigationView.setNavigationItemSelectedListener(this);

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
                        if (checandoConexao()) {
                            comprovante = comprovanteList.get(position);
                            Toast.makeText(HomeActivity.this, "Aguarde!", Toast.LENGTH_SHORT).show();
                            new Thread(runRecDadosFunc).start();
                        }else{
                            Toast.makeText(HomeActivity.this, "Verifique sua conexão!", Toast.LENGTH_LONG).show();
                        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_listaFuncionarios: {
                startActivity(new Intent(this, ListaFuncionarioActivity.class));
                break;
            }
            case R.id.menu_lista_projetos: {
                startActivity(new Intent(this, ListaProjetoActivity.class));
                break;
            }
            case R.id.menu_cadastroFuncionario: {
                startActivity(new Intent(this, CadastroFuncionarioActivity.class));
                break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "PRECIONOU", Toast.LENGTH_SHORT).show();
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_passageiro_sai:
                deslogarUsuario();
                break;
            case R.id.menu_adicionar:
                addProjetos();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addProjetos() {
        Intent i = new Intent(HomeActivity.this, AddProjetoActivity.class);
        startActivity(i);
    }

    private void deslogarUsuario() {
        try {
            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            autenticacao.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class RecListComprovantes implements Runnable {

        @Override
        public void run() {
            if (checandoConexao()) {
                db.collection("comprovante")
                        .whereEqualTo("status", Comprovante.STATUS_ANALISE)
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

            }else{
                Toast.makeText(HomeActivity.this, "Conexão perdida!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class RecuperaDadosFunc implements Runnable {

        @Override
        public void run() {
            if (checandoConexao()) {
                db.collection("funcionario")
                        .document(comprovante.getUidFuncionario())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    int home = 1;
                                    //Toast.makeText(SplashActivity.this, "IF 1", Toast.LENGTH_SHORT).show();

                                    if (document.exists()) {
                                        usuario = document.toObject(Usuario.class);

                                        Intent i = new Intent(HomeActivity.this, ComprovanteActivity.class);
                                        i.putExtra("comprovanteList", comprovante);
                                        i.putExtra("funcionarioList", usuario);
                                        i.putExtra("home", home);
                                        startActivity(i);

                                    }

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }else{
                Toast.makeText(HomeActivity.this, "Conexão perdida!", Toast.LENGTH_SHORT).show();

            }
        }
    }

    class VerificaConexao implements Runnable {

        @Override
        public void run() {

            while(conta == 0) {

                checandoConexao();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean checandoConexao() {
        ConnectivityManager conex = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Network informa = conex.getAllNetworks();  // ConnectivityManager.NetworkCallback informa;
        //NetworkInfo networkInfos = conex.getActiveNetworkInfo();

        Network network = conex.getActiveNetwork();
        NetworkCapabilities capabilities = conex.getNetworkCapabilities(network);

        if (capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))) {
            Log.i("gggchecandoConexao", "checandoConexao-> CONECTADO WIFI: ");
            return true;
        } else {
            Log.i("gggchecandoConexao", "checandoConexao-> DESCONECTADO : ");
            return false;
        }
    }
}