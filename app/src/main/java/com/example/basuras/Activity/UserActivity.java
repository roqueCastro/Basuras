package com.example.basuras.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.basuras.Adapter.AdapterCaneca;
import com.example.basuras.Adapter.AdapterUser;
import com.example.basuras.R;
import com.example.basuras.Utilidades.SnackBarClass;
import com.example.basuras.Utilidades.Utilidades_Proceso;
import com.example.basuras.Utilidades.Utilidades_Request;
import com.example.basuras.model.Caneca;
import com.example.basuras.model.Model;
import com.example.basuras.model.Reporte;
import com.example.basuras.model.Usuario;
import com.example.basuras.volley.ApiVolley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserActivity extends AppCompatActivity implements AdapterUser.OnCallBackAdapterUser,
        ApiVolley.TaskCallbacks
{

    private MaterialToolbar toolbar;
    private ApiVolley apiVolley;
    private Utilidades_Proceso utilidadesProceso = new Utilidades_Proceso(this);

    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private String id;
    private String id_delete;
    private RecyclerView recyclerView;
    private AdapterUser adapter;
    private String url;
    private Gson json = new Gson();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        toolbar =  findViewById(R.id.user_toolBar);
        toolbar.setTitle("Usuarios");
        setSupportActionBar(toolbar);
        //tint 3 ponits menu white
        final Drawable homeArrow = getResources().getDrawable(R.drawable.ic_home);
        homeArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(homeArrow);

        recyclerView = findViewById(R.id.reclerViewUser);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );

        registerForContextMenu(recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter = new AdapterUser(this, (AdapterUser.OnCallBackAdapterUser) this);
        recyclerView.setAdapter(adapter);

        apiVolley = new ApiVolley((ApiVolley.TaskCallbacks) this, this);
    }


    //RECYCLER


    //ALL DATA
    private void initAllData() {
        url = Utilidades_Request.HTTP+ Utilidades_Request.IP+Utilidades_Request.CARPETA+"ws_basura.php?";

        apiVolley.allData(url, "all", id, "","","");
    }

    //INSERT DATA
    private void insertData(ArrayList<Model> datosResponse){
        //
        usuarios.clear();
        Type dataAlTypeU = new TypeToken<ArrayList<Usuario>>(){}.getType();
        usuarios = json.fromJson(datosResponse.get(0).getUsuarios(),dataAlTypeU);

        //
        adapter.adicionarElementos(usuarios);

        Log.d("","");
    }

    //CALLBACK VOLLEY
    @Override
    public void onError(String nameP, String response) {
        Log.d(nameP, response);
        if (dialog != null){
            if (dialog.isShowing()){
                dialog.cancel();
            }
        }
        msj_snackbar(response, 2);//mensaje
    }

    @Override
    public void onSucess(String nameP, String response) {
        switch (nameP){
            case "all":
                Type dataAlType = new TypeToken<ArrayList<Model>>(){}.getType();
                ArrayList<Model> datosResponse = json.fromJson(response,dataAlType);
                insertData(datosResponse);
                Log.d("", "");
                break;
            case "delete_usuario":
                dialog.cancel();
                if (response.equals("")){
                    msj_snackbar("No se elimino", 2);
                }else if(response.equals("true")){
                    msj_snackbar("Registro exitoso.", 1);
                    initAllData();
                }else{
                    msj_snackbar(response, 2);
                }

                Log.d("", "");
                break;
        }
    }

    // //CALLBACK ADAPTER
    @Override
    public void onClickAdapter(int position, Usuario usuario, View v) {



        Intent intent = new Intent(this, RegisterUActivity.class);
        intent.putExtra("operation", 1);

        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario",usuario);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onClickAdapterMore(int position, Usuario usuario, View v) {
        id_delete = usuario.getIdusuario();
        v.showContextMenu();
    }

    //ONCREATE MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_reg, menu);

        MenuItem drawablePerson = menu.findItem(R.id.action_menu_item_enable);
        drawablePerson.setIcon(getResources().getDrawable(R.drawable.ic_person_add));

        Drawable drawable = drawablePerson.getIcon();
        drawable.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_menu_item_enable:
                Intent i = new Intent(this, RegisterUActivity.class);
                i.putExtra("operation", 0);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("","");
        return super.onPrepareOptionsMenu(menu);

    }

    //CONTEXT MENU
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_delete, menu); //infla el menu
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_delete:
                //
                dialog = new ProgressDialog(this);
//            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMessage("Eliminado...");
                dialog.setIndeterminate(false);
//            dialog.setMax(100);
                dialog.setCancelable(false);
                dialog.show();
                //
                apiVolley.usuarioAction(url, "delete_usuario", id_delete,
                        "","","","",""
                );

                return true;
            default:
                Log.d("","");
                return false;
        }
    }



    //MENSAJES
    private void msj_snackbar(String msj, int process){
//        final Snackbar snack = Snackbar.make(findViewById(android.R.id.content),  snackMsg, Snackbar.LENGTH_INDEFINITE);
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  "", Snackbar.LENGTH_LONG);
        //
        SnackBarClass.imprimir_messge_snackbar(snackbar, msj , process, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String response = utilidadesProceso.read_id_preference();
        if (response == ""){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }else{
            String[] parts = response.split(",");
            id = parts[0]; // idusu

            initAllData();
        }
    }
}
