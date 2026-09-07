package com.example.taksmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private List<Tarea> listaTareas;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Tarea tarea);
    }

    public TareaAdapter(List<Tarea> listaTareas, OnItemClickListener listener) {
        this.listaTareas = listaTareas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = listaTareas.get(position);
        holder.bind(tarea, listener);
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public void updateList(List<Tarea> newList) {
        this.listaTareas = newList;
        notifyDataSetChanged();
    }

    static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCategoria;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvItemNombre);
            tvCategoria = itemView.findViewById(R.id.tvItemCategoria);
        }

        public void bind(final Tarea tarea, final OnItemClickListener listener) {
            tvNombre.setText(tarea.getNombre());
            tvCategoria.setText(tarea.getCategoria());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(tarea);
                }
            });
        }
    }
}
