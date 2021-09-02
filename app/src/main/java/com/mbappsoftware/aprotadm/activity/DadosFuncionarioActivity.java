package com.mbappsoftware.aprotadm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.model.Usuario;

public class DadosFuncionarioActivity extends AppCompatActivity {

    private Usuario funcionario;
    private TextView nomeFuncionario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_funcionario);

        nomeFuncionario = findViewById(R.id.dados_tv_nomeFunc);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Funcionarios");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (getIntent().getExtras().containsKey("funcionarioList"))) {

            funcionario = (Usuario) extras.getSerializable("funcionarioList");
            nomeFuncionario.setText(funcionario.getNome());
            toolbar.setTitle("Funcionario: " + funcionario.getNome());
            Log.i("sdfdsfd", "HOME 1 > " + funcionario.getUid() + " > " + funcionario.getNome());
        }
    }

    public void comprovante(View view) {
        if (funcionario != null) {
            Intent i = new Intent(DadosFuncionarioActivity.this, ListaComprovanteActivity.class);
            i.putExtra("funcionarioList", funcionario);
            startActivity(i);
        }
    }


}