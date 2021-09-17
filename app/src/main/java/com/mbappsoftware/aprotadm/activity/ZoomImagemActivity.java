package com.mbappsoftware.aprotadm.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.helper.Constant;
import com.mbappsoftware.aprotadm.model.Comprovante;
import com.mbappsoftware.aprotadm.model.Usuario;

public class ZoomImagemActivity extends AppCompatActivity {

    private ImageView iVFoto;
    private Usuario funcionario;
    private Comprovante comprovante;
    private String txNomeFuncionario, nomeProjeto, txData;
    private int numTela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_imagem);
        //configurando toobar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("COMPROVANTE");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iVFoto = findViewById(R.id.pv_image);

        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (getIntent().getExtras().containsKey("numTela"))) {
            numTela = extras.getInt("numTela");

            if (numTela == Constant.NUM_OPS_1){
                txNomeFuncionario = extras.getString("pesq_txNomeProjetoNome");
                nomeProjeto = extras.getString("pesq_txNomeProjetoProjeto");
                comprovante = (Comprovante) extras.getSerializable("comprovante");

            }else if (numTela == Constant.NUM_OPS_2 || numTela == Constant.NUM_OPS_3){
                funcionario = (Usuario) extras.getSerializable("funcionario");
                txNomeFuncionario = extras.getString("pesq_txNomeProjetoNome");
                comprovante = (Comprovante) extras.getSerializable("comprovante");

            }else if (numTela == Constant.NUM_OPS_4){
                comprovante = (Comprovante) extras.getSerializable("comprovante");

            }else if (numTela == Constant.NUM_OPS_5){
                comprovante = (Comprovante) extras.getSerializable("comprovante");
                txNomeFuncionario = extras.getString("pesq_txNomeProjetoNome");
                nomeProjeto = extras.getString("pesq_txNomeProjetoProjeto");
                txData = extras.getString("pesq_txNomeProjetoData");

            }

            carregarFotoString(comprovante.getUrlImagem());

            //Log.i("funcks", "ZoomImagemActivity - FUNCIONARIO > " + numReceb);
        }
    }

    private void carregarFotoString(String foto) {
        Uri uri = Uri.parse(foto);

        Glide.with(ZoomImagemActivity.this).asBitmap().load(uri).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                Toast.makeText(ZoomImagemActivity.this, "ERRO AO CARREGAR FOTO MOTORISTA", Toast.LENGTH_SHORT).show();
                //progressBarCriminal.setVisibility(View.GONE);
                iVFoto.setImageResource(R.drawable.padrao);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                //progressBarCriminal.setVisibility(View.GONE);
                return false;
            }
        }).into(iVFoto);
    }

    private void carregarFotoUri(Uri uri) {


        Glide.with(ZoomImagemActivity.this).asBitmap().load(uri).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                Toast.makeText(ZoomImagemActivity.this, "ERRO AO CARREGAR FOTO MOTORISTA", Toast.LENGTH_SHORT).show();
                //progressBarCriminal.setVisibility(View.GONE);
                iVFoto.setImageResource(R.drawable.padrao);

                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                //progressBarCriminal.setVisibility(View.GONE);

                return false;
            }
        }).into(iVFoto);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (numTela == Constant.NUM_OPS_1){
            Intent i = new Intent(ZoomImagemActivity.this, ComprovanteActivity.class);
            i.putExtra("pesq_txNomeProjetoNome", txNomeFuncionario);
            i.putExtra("pesq_txNomeProjetoProjeto", nomeProjeto);
            i.putExtra("comprovante", comprovante);
            i.putExtra("numTela", numTela);
            startActivity(i);

        }else if (numTela == Constant.NUM_OPS_2 || numTela == Constant.NUM_OPS_3){
            Intent i = new Intent(ZoomImagemActivity.this, ComprovanteActivity.class);
            i.putExtra("pesq_txNomeProjetoNome", txNomeFuncionario);
            i.putExtra("comprovante", comprovante);
            i.putExtra("funcionario", funcionario);
            i.putExtra("numTela", numTela);
            startActivity(i);

        }else if (numTela == Constant.NUM_OPS_4){
            Intent i = new Intent(ZoomImagemActivity.this, ComprovanteActivity.class);
            i.putExtra("comprovante", comprovante);
            i.putExtra("numTela", numTela);
            startActivity(i);

        }else if (numTela == Constant.NUM_OPS_5){
            Intent i = new Intent(ZoomImagemActivity.this, ComprovanteActivity.class);
            i.putExtra("comprovante", comprovante);
            i.putExtra("pesq_txNomeProjetoNome", txNomeFuncionario);
            i.putExtra("pesq_txNomeProjetoProjeto", nomeProjeto);
            i.putExtra("pesq_txNomeProjetoData", txData);
            i.putExtra("numTela", numTela);
            startActivity(i);

        }


        return false;

    }
}