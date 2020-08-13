package com.example.basuras.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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
    private RecyclerView recyclerView;
    private AdapterUser adapter;
    private String url;
    private Gson json = new Gson();

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

        apiVolley = new ApiVolley((ApiVolley.TaskCallbacks) this, this);
    }


    //RECYCLER
    private void initRecycler() {
        recyclerView = findViewById(R.id.reclerViewUser);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter = new AdapterUser(this, (AdapterUser.OnCallBackAdapterUser) this);
        recyclerView.setAdapter(adapter);

        initAllData();
    }

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
            case "insert_reporte":
//                Log.d("", "");
                break;
        }
    }

    // //CALLBACK ADAPTER
    @Override
    public void onClickAdapter(int position, Usuario usuario, View v) {

    }

    @Override
    public void onLongClickAdapter(int position, Usuario usuario, View v) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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

            initRecycler();
        }
    }
}
