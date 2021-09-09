package com.mbappsoftware.aprotadm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.model.Usuario;

import java.util.List;

public class PesquisaAdapter extends RecyclerView.Adapter<PesquisaAdapter.MyViewHolder>{

    private List<Usuario> funcionarioList;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;


    public PesquisaAdapter(List<Usuario> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_funci, parent, false);
        return new PesquisaAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull PesquisaAdapter.MyViewHolder holder, int position) {

        Usuario funcionario = funcionarioList.get(position);
        holder.nome.setText(funcionario.getNomePesquisa());
    }

    @Override
    public int getItemCount() {
        return funcionarioList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome        = itemView.findViewById(R.id.adRequi_tv_nome);
        }
    }
}
