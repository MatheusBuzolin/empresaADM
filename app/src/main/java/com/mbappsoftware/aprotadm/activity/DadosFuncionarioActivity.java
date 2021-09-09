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
    private String txNomeFuncionario;

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

            if (getIntent().getExtras().containsKey("pesq_txNomeFunc")){
                txNomeFuncionario = extras.getString("pesq_txNomeFunc");
            }

            funcionario = (Usuario) extras.getSerializable("funcionarioList");
            nomeFuncionario.setText(funcionario.getNome());
            toolbar.setTitle("Funcionario: " + funcionario.getNome());
            Log.i("sdfdsfd", "HOME 1 > " + funcionario.getUid() + " > " + funcionario.getNome());
        }
    }

    public void comprovante(View view) {
        if (funcionario != null) {
            Intent i = new Intent(DadosFuncionarioActivity.this, ListaComprovanteActivity.class);
            if (txNomeFuncionario != null){
                i.putExtra("pesq_txNomeFunc", txNomeFuncionario);
            }
            i.putExtra("funcionarioList", funcionario);
            startActivity(i);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (txNomeFuncionario != null) {
            Intent i = new Intent(DadosFuncionarioActivity.this, ListaFuncionarioActivity.class);
            i.putExtra("pesq_txNomeFunc", txNomeFuncionario);
            startActivity(i);

        }else{
            startActivity(new Intent(DadosFuncionarioActivity.this, ListaFuncionarioActivity.class));
        }
        return false;

    }


}