package com.mbappsoftware.aprotadm.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.helper.Permissoes;
import com.mbappsoftware.aprotadm.helper.UsuarioFirebase;
import com.mbappsoftware.aprotadm.model.Comprovante;
import com.mbappsoftware.aprotadm.model.Projeto;
import com.mbappsoftware.aprotadm.model.Usuario;

import java.io.IOException;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class AddProjetoActivity extends AppCompatActivity {

    private static final int SELECAO_GALERIA = 200;
    private static final int SELECAO_CAMERA = 100;

    private StorageReference storage;
    private Usuario funcionario;

    private TextInputEditText tagProjeto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_projeto);

        iniciaComponentes();
    }

    public void salvar(View view) {
        String idProjeto = UUID.randomUUID().toString();

        String txTagProjeto = tagProjeto.getText().toString().trim();

        if (txTagProjeto != null) {

            Projeto projeto= new Projeto();
            projeto.setNomeProjeto(txTagProjeto.toUpperCase());
            projeto.setNomePesquisaProj(txTagProjeto.toUpperCase());
            projeto.setUidProjeto(idProjeto);
            projeto.salvarFirestoreProjeto(projeto);

            Toast.makeText(this, "PROJETO SALVO!!", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Colocar a TAG do PROJETO", Toast.LENGTH_LONG).show();
        }
    }

    private void iniciaComponentes(){

        //configurando toobar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("NOVO PROJETO");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tagProjeto = findViewById(R.id.add_et_tagProjeto);

    }

    /*@Override
    public boolean onSupportNavigateUp() {
        return false;

    }*/
}