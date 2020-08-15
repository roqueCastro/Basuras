package com.example.basuras.DiffUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.basuras.model.Caneca;

import java.util.ArrayList;

public class CanecaDiffUtil extends DiffUtil.Callback {

    private ArrayList<Caneca> oldCanecas;
    private ArrayList<Caneca> newCanecas;

    public CanecaDiffUtil(ArrayList<Caneca> oldCanecas, ArrayList<Caneca> newCanecas) {
        this.oldCanecas = oldCanecas;
        this.newCanecas = newCanecas;
    }

    @Override
    public int getOldListSize() {
        return oldCanecas.size();
    }

    @Override
    public int getNewListSize() {
        return newCanecas.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCanecas.get(oldItemPosition).getIdcaneca().equals(newCanecas.get(newItemPosition).getIdcaneca());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//        return oldCanecas.get(oldItemPosition).getNombre_caneca().equals(newCanecas.get(newItemPosition).getNombre_caneca());
        return oldCanecas.get(oldItemPosition).getCant_reportes() == newCanecas.get(newItemPosition).getCant_reportes();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
