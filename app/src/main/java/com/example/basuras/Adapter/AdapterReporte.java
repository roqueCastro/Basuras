package com.example.basuras.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basuras.DiffUtils.CanecaDiffUtil;
import com.example.basuras.DiffUtils.ReporteDiffUtil;
import com.example.basuras.R;
import com.example.basuras.model.Caneca;
import com.example.basuras.model.Reporte;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterReporte extends RecyclerView.Adapter<AdapterReporte.ViewHolder> {

    public static final int VIEW_TYPE_N = 0;
    public static final int VIEW_TYPE_F = 1;
    private int VIEW_STATE;
    private int archivados;
    private ArrayList<Reporte> reportes;
    private CallbackOnClickListener onClickListener;

    public interface CallbackOnClickListener{
        void onClickAdapterReporte(int position, Reporte reporte, View v, int state);
        void onLongClickAdapterReporte(int position, Reporte reporte, View v);
    }

    public AdapterReporte(CallbackOnClickListener onClickListener) {
        this.reportes = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public AdapterReporte.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Log.d("","");
        if (VIEW_TYPE_N == i){
            VIEW_STATE = 1;
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nn, parent, false);
            return new ViewHolder(view);
        }else if(VIEW_TYPE_F == i){
            VIEW_STATE = 2;
            View views = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add, parent, false);
            return new ViewHolder(views);
        }else {
            VIEW_STATE = 0;
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReporte.ViewHolder holder, int position) {
        Reporte reporte = reportes.get(position);
        try {
            holder.llenarinputs(reporte);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.onClicklistener(position,reporte);
    }

    @Override
    public int getItemCount() {
        return reportes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout container;
        TextView evento, person, latLng, date, add;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if (VIEW_STATE == 1) {
                evento = itemView.findViewById(R.id.textViewReporte);
                person = itemView.findViewById(R.id.textViewNamePerson);
                latLng = itemView.findViewById(R.id.textViewlatlng);
                date = itemView.findViewById(R.id.textViewdate);
                container = itemView.findViewById(R.id.lyt_parent);
            }else if (VIEW_STATE == 2) {
                add = itemView.findViewById(R.id.textViewAdd);
            }
        }

        void llenarinputs(Reporte reporte) throws ParseException {
            if (VIEW_STATE == 1){
                if (reporte.getIdreporte() != "10000"){
                    evento.setText(reporte.getCaneca());
                    person.setText(reporte.getUsuario());
                    latLng.setText(reporte.getLat() + ", " + reporte.getLng());

                    SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
                    Date myDate = dt.parse(reporte.getFecha_update());

                    String m = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK).format(myDate);
                    date.setText(m);
                }else{
                    add.setText("Reportes archivados (" + archivados + ").");
                }

            }else if(VIEW_STATE ==2){
                add.setText("Reportes archivados (" + archivados + ").");
            }
        }

        void onClicklistener(final int position, final Reporte reporte){
            if (VIEW_STATE == 1){
                if (!reporte.getIdreporte().equals("10000") ){
                    //##
                    container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onClickListener != null)
                                onClickListener.onClickAdapterReporte(position,reporte,v, 1);
                        }
                    });
                    //##
                    container.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (onClickListener != null)
                                onClickListener.onLongClickAdapterReporte(position, reporte, v);
                            return true;
                        }
                    });
                    //##
                }

            }else if(VIEW_STATE ==2){
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener != null)
                            onClickListener.onClickAdapterReporte(position,reporte,v, 2);
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Reporte reporte = reportes.get(position);
        if (reporte.getIdreporte().equals("10000")){
            return VIEW_TYPE_F;
        }else{
            return VIEW_TYPE_N;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public void adicionarElementos(ArrayList<Reporte> list, int archivados) {
        if (archivados!= 0){
            this.archivados = archivados;
        }

        //diff utils
        ReporteDiffUtil reporteDiffUtil = new ReporteDiffUtil(this.reportes, list);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(reporteDiffUtil);

        this.reportes.clear();
        this.reportes.addAll(list);
        diffResult.dispatchUpdatesTo(this);
    }

}
