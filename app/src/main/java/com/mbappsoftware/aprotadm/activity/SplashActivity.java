package com.mbappsoftware.aprotadm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.config.ConfiguracaoFirebase;
import com.mbappsoftware.aprotadm.helper.UsuarioFirebase;
import com.mbappsoftware.aprotadm.model.Usuario;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    }

    @Override
    protected void onStart() {
        super.onStart();
        abrirTelaHome();
    }

    private void abrirTelaHome(){

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checandoConexao() && verificarUsuarioLogado()) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }else{
                    //abrirTelaHome();
                    abrirTelaLogin();
                    Toast.makeText(SplashActivity.this, "Sem Conexão!", Toast.LENGTH_LONG).show();
                }

            }
        }, 3000);
    }

    public boolean verificarUsuarioLogado(){
        //autenticacao.signOut();
        if (autenticacao.getCurrentUser() != null) {
            return true;
        }else{
            return false;
        }
    }

    private void abrirTelaLogin(){
        Intent i = new Intent(this,LoginActivity.class);
        //i.putExtra("funcionario", usuario);
        startActivity(i);
        finish();
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
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("gggchecandoConexao", "checandoConexao-> DESCONECTADO : ");
            return false;
        }
    }

    private void deslogarUsuario(){
        try{
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}