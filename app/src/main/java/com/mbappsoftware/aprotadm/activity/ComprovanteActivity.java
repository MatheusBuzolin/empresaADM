package com.mbappsoftware.aprotadm.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.config.ConfiguracaoFirebase;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ComprovanteActivity extends AppCompatActivity {

    private Comprovante comprovante;
    private Usuario funcionario;
    private CircleImageView imagem;
    private TextView idProjeto, dia, valor, status, despesa;
    private ProgressBar progressBarMotorista;
    private StorageReference storage;
    private String urlWeb;
    private int home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprovante);

        storage = ConfiguracaoFirebase.getFirebaseStorage();

        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (getIntent().getExtras().containsKey("comprovanteList"))) {

            comprovante = (Comprovante) extras.getSerializable("comprovanteList");
            funcionario = (Usuario) extras.getSerializable("funcionarioList");
            home = extras.getInt("home");

            Log.i("funcks", "FUNCIONARIO > " + comprovante.getDiaDaNota());
        }


        StorageReference ref = storage.child("funcionario")
                .child("comprovante")
                .child(comprovante.getUidComprovante() + ".jpeg" );

        ref.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("dccdc", "URL 1-> " + uri.toString());
                        urlWeb = String.valueOf(uri);
                    }
                });

        iniciaComponentes();
    }

    private void iniciaComponentes() {

        //configurando toobar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Comprovante");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagem = findViewById(R.id.compro_fotoMotorista);
        idProjeto = findViewById(R.id.compro_tv_idComprovante);
        valor = findViewById(R.id.compro_tv_valor);
        dia = findViewById(R.id.compro_tv_dia);
        status = findViewById(R.id.compro_tv_status);
        progressBarMotorista = findViewById(R.id.progressBar_fotoComprovante);
        despesa = findViewById(R.id.compro_tv_despesa);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (comprovante != null) {
            carregaFoto();
        }
    }

    private void carregaFoto() {

        String strValorNota = String.valueOf(comprovante.getValorNota());

        idProjeto.setText(comprovante.getNomeProjeto());
        valor.setText(strValorNota);
        dia.setText(comprovante.getDiaDaNota());
        status.setText(comprovante.getStatus());
        despesa.setText(comprovante.getTipoComprovante());

        Glide.with(ComprovanteActivity.this).asBitmap().load(comprovante.getUrlImagem()).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                Toast.makeText(ComprovanteActivity.this, "ERRO AO CARREGAR FOTO MOTORISTA", Toast.LENGTH_SHORT).show();
                progressBarMotorista.setVisibility(View.GONE);
                imagem.setImageResource(R.drawable.padrao);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                progressBarMotorista.setVisibility(View.GONE);
                return false;
            }
        }).into(imagem);
    }

    public void abrirFotoMotoristaGrande(View view) {

        if (comprovante.getUrlImagem() != null) {
            Intent i = new Intent(ComprovanteActivity.this, ZoomImagemActivity.class);
            i.putExtra("urlComprovanteZoom", comprovante.getUrlImagem());
            i.putExtra("comprovanteList", comprovante);
            i.putExtra("funcionarioList", funcionario);
            startActivity(i);
        }
    }

    public void baixarImagem(View view) {

        DownloadTask downloadTask = new DownloadTask();
        //downloadTask.execute(comprovante.getUrlImagem());
        downloadTask.execute(urlWeb);
    }

    public void mudaStatus(View view) {

        if (comprovante.getStatus().equals(Comprovante.STATUS_ANALISE)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("ATUALIZAR STATUS?")
                    .setCancelable(false)
                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            comprovante.setStatus(Comprovante.STATUS_CONFIRMADO);
                            comprovante.updateStatus();
                            Toast.makeText(ComprovanteActivity.this, "Status atualizado!!", Toast.LENGTH_SHORT).show();
                            status.setText(Comprovante.STATUS_CONFIRMADO);

                        }
                    }).setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Toast.makeText(ComprovanteActivity.this, "Status ja foi atualizado!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (funcionario != null && home == 1) {
            startActivity(new Intent(this, HomeActivity.class));
        }else if(funcionario != null){
            Intent i = new Intent(ComprovanteActivity.this, ListaComprovanteActivity.class);
            i.putExtra("funcionarioList", funcionario);
            startActivity(i);
        }
        return false;

    }

    class DownloadTask extends AsyncTask<String, Integer, String>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ComprovanteActivity.this);
            progressDialog.setTitle("Baixando Comprovante");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String path = params[0];
            int file_lengt = 0;

            //um exemplo
            String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";

            //outro exemplo
            File pathF = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File file = new File(pathF, "DemoPicture.jpg");

            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_lengt = urlConnection.getContentLength();
                //File new_folfer = new File("sdcard/Download"); -> NO MEU CELULAR
                //File new_folfer = new File("/Download");

                if(!file.exists()){
                    file.mkdir();
                }

                File input_file = new File(file, imageFileName + funcionario.getNomePesquisa() + ".jpg");
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;
                OutputStream outputStream = new FileOutputStream(input_file);
                while ((count = inputStream.read(data)) != -1){
                    total += count;
                    outputStream.write(data,0, count);
                    int progress = (int) total+100/file_lengt;
                    publishProgress(progress);

                }
                inputStream.close();
                outputStream.close();


            } catch (MalformedURLException e) {
                Log.i("fefefe", "1 ->>> " + e);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("fefefe", "2 ->>> " + e);
            }



            return "Download Completo";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.hide();
            Toast.makeText(ComprovanteActivity.this, result, Toast.LENGTH_LONG).show();
            String path = "Download/" + comprovante.getUidComprovante();
            //imagem.setImageDrawable(Drawable.createFromPath(path));
        }
    }
}