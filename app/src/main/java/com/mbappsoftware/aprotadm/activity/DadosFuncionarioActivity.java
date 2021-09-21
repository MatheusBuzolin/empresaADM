package com.mbappsoftware.aprotadm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.helper.Constant;
import com.mbappsoftware.aprotadm.model.Comprovante;
import com.mbappsoftware.aprotadm.model.Usuario;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

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
                deletarFuncionario();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletarFuncionario() {

        Toast.makeText(this, "DELETA FUNCIONARIO", Toast.LENGTH_LONG).show();
    }

    private void inicializaComponentes() {

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Funcionario");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomeFuncionario = findViewById(R.id.dados_tv_nomeFunc);
    }

    public void gerarPlanilha(View view) {
        GeraPlanilha geraPlanilha = new GeraPlanilha();
        //downloadTask.execute(comprovante.getUrlImagem());
        geraPlanilha.execute();
    }

    public void modificaExcel(View view){
        File pathF = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);

        File xlsFile = new File(pathF, "RDV.xls");

        try {
            Workbook wbb = Workbook.getWorkbook(xlsFile);
            WritableWorkbook copy = Workbook.createWorkbook(new File(String.valueOf(xlsFile)),wbb);
            WritableSheet shett = copy.getSheet(0);
            Label digitado = new Label(0, 60, "TESTE 10");
            Label digitado1 = new Label(1, 60, "TESTE 11");
            Label digitado2 = new Label(2, 60, "TESTE 12");
            Label digitado3 = new Label(3, 60, "TESTE 13");
            Label digitado4 = new Label(4, 60, "TESTE 14");
            Label digitado5 = new Label(5, 60, "TESTE 15");
            Label digitado6 = new Label(6, 60, "TESTE 16");
            Label digitado7 = new Label(7, 60, "TESTE 17");
            Label digitado8 = new Label(8, 60, "TESTE 18");
            Label digitado9 = new Label(9, 60, "TESTE 19");
            Label digitado10 = new Label(10, 60, "TESTE 20");
            Label digitado11 = new Label(11, 60, "TESTE 21");

            shett.addCell(digitado);
            shett.addCell(digitado1);
            shett.addCell(digitado2);
            shett.addCell(digitado3);
            shett.addCell(digitado4);
            shett.addCell(digitado5);
            shett.addCell(digitado6);
            shett.addCell(digitado7);
            shett.addCell(digitado8);
            shett.addCell(digitado9);
            shett.addCell(digitado10);
            shett.addCell(digitado11);

            copy.write();
            copy.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "erro 1 -> " + e.toString(), Toast.LENGTH_LONG).show();
            Log.i("fgdf", " erro 1 -> " + e.toString());
        } catch (BiffException e) {
            e.printStackTrace();
            Toast.makeText(this, "erro 2 -> " + e.toString(), Toast.LENGTH_LONG).show();
            Log.i("fgdf", " erro 2 -> " + e.toString());
        } catch (RowsExceededException e) {
            e.printStackTrace();
            Toast.makeText(this, "erro 3 -> " + e.toString(), Toast.LENGTH_LONG).show();
        } catch (WriteException e) {
            e.printStackTrace();
            Toast.makeText(this, "erro 4 ->  " + e.toString(), Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(this, "Cliclado", Toast.LENGTH_SHORT).show();
    }

    class GeraPlanilha extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DadosFuncionarioActivity.this);
            progressDialog.setTitle("Gerando Excel");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            File pathF = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);


            /*File pathF = new File(Environment.getExternalStorageDirectory() + "/APROT", " excel.xls");
            FileOutputStream fileOutputStream = null;
            String nomePasta = "APROT";
            File f = new File(Environment.getExternalStorageDirectory() + "/" + nomePasta, " excel.xls");*/

            File file = new File(pathF, "excel.xls");

            //File file = new File(getExternalFilesDir(null), "excel.xls");
            WritableWorkbook wb = null;
            try {

                wb = Workbook.createWorkbook(file);

            } catch (IOException e) {
                e.printStackTrace();
            }

            wb.createSheet("planilha", 0);

            WritableSheet plan = wb.getSheet(0);
            //r -> linha
            //c -> coluna
            Label label = new Label(0, 0, "Primeira célula");

            try {
                plan.addCell(label);
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }

            try {
                wb.write();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }


            return "Excel Completo";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.hide();
            Toast.makeText(DadosFuncionarioActivity.this, result, Toast.LENGTH_LONG).show();
            //String path = "Download/" + comprovante.getUidComprovante();
            //imagem.setImageDrawable(Drawable.createFromPath(path));
        }
    }
}