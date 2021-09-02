package com.mbappsoftware.aprotadm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.mbappsoftware.aprotadm.R;
import com.mbappsoftware.aprotadm.model.Comprovante;

import java.util.List;

public class ListaComprovanteAdapter extends RecyclerView.Adapter<ListaComprovanteAdapter.MyViewHolder>{

    private List<Comprovante> comprovanteList;
    private static final int HOME_COMPROVANTE = 0;
    private static final int FUNC_COMPROVANTE = 1;

    public ListaComprovanteAdapter(List<Comprovante> comprovanteList) {
        this.comprovanteList = comprovanteList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_comprovante, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Comprovante comprovante = comprovanteList.get(position);
        holder.dia.setText(comprovante.getDiaDaNota());
        holder.nome.setText(comprovante.getNomeFuncionario().toUpperCase());
    }

    /*@Override
    public int getItemViewType(int position) {
        Comprovante comprovante = comprovanteList.get(position);

        if (comprovante.getStatus().equals(Comprovante.STATUS_ANALISE)){
            return HOME_COMPROVANTE;
        }else {
            return FUNC_COMPROVANTE;
        }
    }*/

    @Override
    public int getItemCount() {
        return comprovanteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dia, nome;

        public MyViewHolder(View itemView) {
            super(itemView);

            dia = itemView.findViewById(R.id.adRequi_tv_dia);
            nome = itemView.findViewById(R.id.adRequi_tv_nome);
        }
    }
}
