package com.example.basuras.DiffUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;



import com.example.basuras.model.Reporte;

import java.util.ArrayList;

public class ReporteDiffUtil extends DiffUtil.Callback {

    private ArrayList<Reporte> oldReporte;
    private ArrayList<Reporte> newReporte;

    public ReporteDiffUtil(ArrayList<Reporte> oldReporte, ArrayList<Reporte> newReporte) {
        this.oldReporte = oldReporte;
        this.newReporte = newReporte;
    }

    @Override
    public int getOldListSize() {
        return oldReporte.size();
    }

    @Override
    public int getNewListSize() {
        return newReporte.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldReporte.get(oldItemPosition).getIdreporte().equals(newReporte.get(newItemPosition).getIdreporte());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldReporte.get(oldItemPosition).getEstado().equals(newReporte.get(newItemPosition).getEstado());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
