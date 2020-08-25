package com.example.basuras.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basuras.DiffUtils.CanecaDiffUtil;
import com.example.basuras.R;
import com.example.basuras.model.Caneca;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AdapterCaneca extends RecyclerView.Adapter<AdapterCaneca.ViewHolder> {

    private ArrayList<Caneca> canecas;
    private Context context;
    private boolean notification;
    private onClickListenerAdapterCaneca onClick;

    public AdapterCaneca(ArrayList<Caneca> canecas, Context context, boolean notification, onClickListenerAdapterCaneca onClickListenerAdapterCaneca) {
        this.canecas = canecas;
        this.context = context;
        this.notification = notification;
        this.onClick = onClickListenerAdapterCaneca;
    }

    @NonNull
    @Override
    public AdapterCaneca.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_canecas , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCaneca.ViewHolder holder, int position) {
        Caneca caneca = canecas.get(position);

        holder.setContenido(caneca);
        if (notification){
            holder.setNotification(caneca);
        }

        holder.setOnClickListener(position,caneca);
    }

    @Override
    public int getItemCount() {
        return canecas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView imageView;
        private TextView tv_title, tv_contenido, tv_noti;
        private RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewCaneca);
            tv_title = itemView.findViewById(R.id.textViewTitle);
            tv_contenido = itemView.findViewById(R.id.textViewContenido);
            tv_noti = itemView.findViewById(R.id.textViewNotification);
            container = itemView.findViewById(R.id.relativeLayoutContainer);
        }

        //
        void setContenido(Caneca caneca){
            tv_title.setText(caneca.getNombre_caneca());
            tv_contenido.setText(caneca.getDescripcion());
        }
        //
        void setNotification(Caneca caneca){
            if ((caneca.getCant_reportes()) > 0){
                tv_noti.setVisibility(View.VISIBLE);
                tv_noti.setText(String.valueOf(caneca.getCant_reportes()));
            }else{
                tv_noti.setVisibility(View.GONE);
            }

        }

        void setOnClickListener(final int position, final Caneca caneca) {
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClick != null){
                        onClick.onClickListener(position, caneca, v);
                    }
                }
            });
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onClick != null)
                        onClick.onLongClickListener(position, caneca, v);
                    return true;
                }
            });
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void adicionarElementos(ArrayList<Caneca> list) {
        //diff utils
        CanecaDiffUtil canecaDiffUtil = new CanecaDiffUtil(this.canecas, list);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(canecaDiffUtil);

        this.canecas.clear();
        this.canecas.addAll(list);
        diffResult.dispatchUpdatesTo(this);
    }

    public interface onClickListenerAdapterCaneca {
        void onClickListener(int position, Caneca caneca, View v);
        void onLongClickListener(int position, Caneca caneca, View view);
    }
}
