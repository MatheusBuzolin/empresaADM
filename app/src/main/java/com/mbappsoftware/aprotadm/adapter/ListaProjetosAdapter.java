package com.mbappsoftware.aprotadm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.model.Projeto;
import com.mbappsoftware.aprotadm.model.Usuario;

import java.util.List;

public class ListaProjetosAdapter extends RecyclerView.Adapter<ListaProjetosAdapter.MyViewHolder>{

    private List<Projeto> projetoList;


    public ListaProjetosAdapter(List<Projeto> projetoList) {
        this.projetoList = projetoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_projeto, parent, false);
        return new ListaProjetosAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaProjetosAdapter.MyViewHolder holder, int position) {

        Projeto projeto = projetoList.get(position);
        holder.nome.setText(projeto.getNomeProjeto());
    }

    @Override
    public int getItemCount() {
        return projetoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.adRequi_tv_nomeProjeto);
        }
    }
}
