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
        boolean valid = true;
        Usuario oldUsuario = oldUsuarios.get(oldItemPosition);
        Usuario newUsuario = newUsuarios.get(newItemPosition);

        if (!oldUsuario.getNombre_usu().equals(newUsuario.getNombre_usu())){
            valid = false;
        }
        if (!oldUsuario.getCorreo_usu().equals(newUsuario.getCorreo_usu())){
            valid = false;
        }
        if (!oldUsuario.getPassword().equals(newUsuario.getPassword())){
            valid = false;
        }
        if (!oldUsuario.getTelefono().equals(newUsuario.getTelefono())){
            valid = false;
        }
        if (!oldUsuario.getFecha_update().equals(newUsuario.getFecha_update())){
            valid = false;
        }
        if (!oldUsuario.getRol_idrol().equals(newUsuario.getRol_idrol())){
            valid = false;
        }
        return valid;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
