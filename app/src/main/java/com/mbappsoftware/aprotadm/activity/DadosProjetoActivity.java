package com.mbappsoftware.aprotadm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.model.Comprovante;
import com.mbappsoftware.aprotadm.model.Projeto;
import com.mbappsoftware.aprotadm.model.Usuario;

public class DadosProjetoActivity extends AppCompatActivity {

    private Projeto projeto;
    private TextView nomeProjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_projeto);

        nomeProjeto = findViewById(R.id.dados_tv_nomeProjeto);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Projeto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (getIntent().getExtras().containsKey("projetoList"))) {

            projeto = (Projeto) extras.getSerializable("projetoList");
            nomeProjeto.setText(projeto.getNomeProjeto());
            toolbar.setTitle("Projeto - " + projeto.getNomeProjeto());
            //Log.i("sdfdsfd", "HOME 1 > " + funcionario.getUid() + " > " + funcionario.getNome());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_deleta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_deletar:
                deletarProjeto();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletarProjeto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("DESEJA DELETAR?")
                .setMessage("Após confirmar, o projeto sera deletado!")
                .setCancelable(false)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        projeto.deletaFirestoreProjeto();
                        Toast.makeText(DadosProjetoActivity.this, "PROJETO REMOVIDO!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DadosProjetoActivity.this, ListaProjetoActivity.class));

                    }
                }).setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}