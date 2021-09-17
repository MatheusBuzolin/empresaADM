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
import com.mbappsoftware.aprotadm.helper.Constant;
import com.mbappsoftware.aprotadm.model.Comprovante;
import com.mbappsoftware.aprotadm.model.Usuario;

public class DadosFuncionarioActivity extends AppCompatActivity {

    private Usuario funcionario;
    private TextView nomeFuncionario;
    private String nomeFuncionarioPesquisa;
    private int numTela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_funcionario);

        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (getIntent().getExtras().containsKey("numTela"))) {
            numTela = extras.getInt("numTela");

            if (numTela == Constant.NUM_OPS_2){
                nomeFuncionarioPesquisa = extras.getString("pesq_txNomeProjetoNome");
                funcionario = (Usuario) extras.getSerializable("funcionario");

            }else if (numTela == Constant.NUM_OPS_3){
                funcionario = (Usuario) extras.getSerializable("funcionario");
                nomeFuncionarioPesquisa = funcionario.getNomePesquisa();
            }

            Log.i("dfdf", " - > " + nomeFuncionarioPesquisa);

        }

        inicializaComponentes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        carregaDadosFuncionario();
    }

    private void carregaDadosFuncionario() {
        nomeFuncionario.setText(funcionario.getNome());

    }

    public void comprovante(View view) {
        if (funcionario != null){
            Intent i = new Intent(DadosFuncionarioActivity.this, ListaComprovanteActivity.class);
            i.putExtra("numTela", numTela);
            i.putExtra("funcionario", funcionario);
            i.putExtra("pesq_txNomeProjetoNome", nomeFuncionarioPesquisa);
            startActivity(i);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (numTela == Constant.NUM_OPS_2){
            Intent i = new Intent(DadosFuncionarioActivity.this, ListaFuncionarioActivity.class);
            i.putExtra("pesq_txNomeProjetoNome", nomeFuncionarioPesquisa);
            i.putExtra("numTela", numTela);
            startActivity(i);
        }else{
            startActivity(new Intent(DadosFuncionarioActivity.this, ListaFuncionarioActivity.class));
        }
        return false;

    }

    private void inicializaComponentes() {

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Funcionario");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomeFuncionario = findViewById(R.id.dados_tv_nomeFunc);
    }
}