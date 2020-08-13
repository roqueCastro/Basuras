package com.example.basuras.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basuras.DiffUtils.CanecaDiffUtil;
import com.example.basuras.DiffUtils.UsuarioDiffUtil;
import com.example.basuras.R;
import com.example.basuras.model.Caneca;
import com.example.basuras.model.Usuario;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> {

    private ArrayList<Usuario> usuarios;
    Context context;
    private OnCallBackAdapterUser callBack;

    public interface  OnCallBackAdapterUser{
        void onClickAdapter(int position, Usuario usuario, View v);
        void onLongClickAdapter(int position, Usuario usuario, View v);
    }

    public AdapterUser(Context context, OnCallBackAdapterUser callBack) {
        this.usuarios = new ArrayList<>();
        this.context = context;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public AdapterUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_users_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.ViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);
        holder.imprimirVector(usuario);
        try {
            holder.llenarInputs(usuario);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.onClickListenerAdapter(position, usuario);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView vector;
        private TextView name, user, telefono, date;
        private LinearLayout container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            vector = itemView.findViewById(R.id.imageViewDrawableUser);
            name = itemView.findViewById(R.id.textViewNameUser);
            user = itemView.findViewById(R.id.textViewCorUser);
            telefono = itemView.findViewById(R.id.textViewTeleUser);
            date = itemView.findViewById(R.id.textViewdateUser);
            container = itemView.findViewById(R.id.lyt_parentU);
        }

        void imprimirVector(Usuario usuario){
            if (usuario.getRol_idrol().equals("1")){
                vector.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_settings));
            }else{
                vector.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person));
            }
        }

        void llenarInputs(Usuario usuario) throws ParseException {
            name.setText(usuario.getNombre_usu());
            user.setText(usuario.getCorreo_usu());
            telefono.setText(usuario.getTelefono());

            SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
            Date myDate = dt.parse(usuario.getFecha_update());
            String m = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK).format(myDate);
            date.setText(m);
        }

        void onClickListenerAdapter(int position, Usuario usuario){
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack!= null)
                        callBack.onClickAdapter(position, usuario, v);

                }
            });
            //////
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (callBack!= null)
                        callBack.onLongClickAdapter(position, usuario, v);
                    return true;
                }
            });
        }
    }

    ////////////////////////////////////////////////////////////////

    public void adicionarElementos(ArrayList<Usuario> list) {
        //diff utils
        UsuarioDiffUtil usuarioDiffUtil = new UsuarioDiffUtil(this.usuarios, list);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(usuarioDiffUtil);

        this.usuarios.clear();
        this.usuarios.addAll(list);
        diffResult.dispatchUpdatesTo(this);
    }
}
