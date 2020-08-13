package com.example.basuras.DiffUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.basuras.model.Usuario;

import java.util.ArrayList;

public class UsuarioDiffUtil extends DiffUtil.Callback {

    private ArrayList<Usuario> oldUsuarios;
    private ArrayList<Usuario> newUsuarios;

    public UsuarioDiffUtil(ArrayList<Usuario> oldUsuarios, ArrayList<Usuario> newUsuarios) {
        this.oldUsuarios = oldUsuarios;
        this.newUsuarios = newUsuarios;
    }

    @Override
    public int getOldListSize() {
        return oldUsuarios.size();
    }

    @Override
    public int getNewListSize() {
        return newUsuarios.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldUsuarios.get(oldItemPosition).getIdusuario().equals(newUsuarios.get(newItemPosition).getIdusuario());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldUsuarios.get(oldItemPosition).getNombre_usu().equals(newUsuarios.get(newItemPosition).getNombre_usu());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
