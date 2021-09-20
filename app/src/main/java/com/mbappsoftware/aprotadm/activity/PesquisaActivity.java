package com.mbappsoftware.aprotadm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.helper.Constant;
import com.santalu.maskedittext.MaskEditText;

public class PesquisaActivity extends AppCompatActivity {

    private MaskEditText data;
    private TextInputEditText nomeFuncionario, nomeProjeto;
    private int numTela;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        iniciaComponentes();
    }

    public void btPesquisar(View view) {

        String txNomeFunc = nomeFuncionario.getText().toString().trim().toUpperCase();
        String txNomeProjeto = nomeProjeto.getText().toString().trim().toUpperCase();
        String txData = data.getText().toString().trim();

        if (!txNomeFunc.isEmpty() ){
            if (!txNomeProjeto.isEmpty() && !txData.isEmpty()){//RECUPERA LISTA DE -> NOME + PROJETO + DATA
                recNomeProjetoData(txNomeFunc, txNomeProjeto, txData);

            }else if(!txNomeProjeto.isEmpty()){//RECUPERA LISTA DE -> NOME + PROJETO
                recNomeProjeto(txNomeFunc, txNomeProjeto);

            }else if (!txData.isEmpty()){
                recNomeFunData(txNomeFunc, txData);

            }else{//RECUPERA SO LISTA DE -> NOME
                recNome(txNomeFunc);

            }

        }else if(!txNomeProjeto.isEmpty()){
            if (!txData.isEmpty()){ //RECUPERA LISTA DE -> PROJETO + DATA
                recProjetoData(txNomeProjeto, txData);

            }else{//RECUPERA SO LISTA DE -> PROJETO
                recProjeto(txNomeProjeto);

            }

        }else if (!txData.isEmpty()){//RECUPERA SO LISTA DE -> DATA
            recData(txData);

        }else{
            nomeFuncionario.setError("!");
            nomeProjeto.setError("!");
            data.setError("!");
            Toast.makeText(this, "Preecha os dados", Toast.LENGTH_SHORT).show();
        }
    }

    private void recNomeProjetoData(String txNomeFunc, String txNomeProjeto, String txData) {
        Intent i = new Intent(PesquisaActivity.this, ListaNomeProjetoActivity.class);
        numTela = Constant.NUM_OPS_5;
        i.putExtra("pesq_txNomeProjetoProjeto", txNomeProjeto);
        i.putExtra("pesq_txNomeProjetoNome", txNomeFunc);
        i.putExtra("pesq_txNomeProjetoData", txData);
        i.putExtra("numTela", numTela);
        startActivity(i);
    }

    private void recNomeProjeto(String txNomeFunc, String txNomeProjeto) {
        Intent i = new Intent(PesquisaActivity.this, ListaNomeProjetoActivity.class);
        numTela = Constant.NUM_OPS_1;
        i.putExtra("pesq_txNomeProjetoProjeto", txNomeProjeto);
        i.putExtra("pesq_txNomeProjetoNome", txNomeFunc);
        i.putExtra("numTela", numTela);
        startActivity(i);
    }

    private void recProjetoData(String txNomeProjeto, String txData) {
        /*Intent i = new Intent(PesquisaActivity.this, ListaFuncionarioActivity.class);
        numTela = Constant.NUM_OPS_6;
        i.putExtra("pesq_txNomeFunc", txNomeFunc);
        i.putExtra("numTela", numTela);
        startActivity(i);**/
    }

    private void recNomeFunData(String txNomeFunc, String txData) {


    }

    private void recNome(String txNomeFunc) {
        Intent i = new Intent(PesquisaActivity.this, ListaFuncionarioActivity.class);
        numTela = Constant.NUM_OPS_2;
        i.putExtra("pesq_txNomeProjetoNome", txNomeFunc);
        i.putExtra("numTela", numTela);
        startActivity(i);
    }

    private void recProjeto(String txNomeProjeto) {
        Intent i = new Intent(PesquisaActivity.this, ListaProjComproActivity.class);
        numTela = Constant.NUM_OPS_6;
        i.putExtra("pesq_txNomeProjeto", txNomeProjeto);
        i.putExtra("numTela", numTela);
        startActivity(i);
    }

    private void recData(String txData) {


    }

    private void iniciaComponentes() {

        //configurando toobar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("PESQUISA");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomeFuncionario = findViewById(R.id.pesq_et_nomeFunc);
        nomeProjeto = findViewById(R.id.pesq_et_projeto);
        data = findViewById(R.id.pesq_et_dia);

    }
}