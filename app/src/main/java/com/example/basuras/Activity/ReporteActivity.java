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
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import com.example.basuras.Adapter.AdapterCaneca;
import com.example.basuras.Adapter.AdapterReporte;
import com.example.basuras.R;
import com.example.basuras.Utilidades.SnackBarClass;
import com.example.basuras.Utilidades.Utilidades_Request;
import com.example.basuras.model.Caneca;
import com.example.basuras.model.Model;
import com.example.basuras.model.Reporte;
import com.example.basuras.volley.ApiVolley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReporteActivity extends AppCompatActivity implements ApiVolley.TaskCallbacks,
        AdapterReporte.CallbackOnClickListener,
        View.OnClickListener
{

    private MaterialToolbar toolbar;
    private ApiVolley apiVolley;
    private String id_caneca;
    private RecyclerView recyclerView;
    private AdapterReporte adapter;
    private Gson json = new Gson();
    private ArrayList<Reporte> reportes =  new ArrayList<>();
    private Drawable homeArrow,  backArrow;
    private boolean botom_home =  false;
    private String id_reporte;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        //Toolbar
        toolbar =  findViewById(R.id.reporte_toolBar);
        toolbar.setTitle("Reportes");//
        setSupportActionBar(toolbar);

        //tint home arrow toolbar
        homeArrow = getResources().getDrawable(R.drawable.ic_home);
        homeArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        backArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        backArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationIcon(homeArrow);

        toolbar.setNavigationOnClickListener(this);


        id_caneca = getIntent().getExtras().getString("caneca");

        apiVolley = new ApiVolley((ApiVolley.TaskCallbacks) this, this);

        initRecycler();
    }

    //RECYCLER
    private void initRecycler() {
        recyclerView = findViewById(R.id.reclerViewReporte);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );

        registerForContextMenu(recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter = new AdapterReporte((AdapterReporte.CallbackOnClickListener) this);
        recyclerView.setAdapter(adapter);

        initAllData();
    }

    //ALL DATA
    private void initAllData() {
        String url = Utilidades_Request.HTTP+ Utilidades_Request.IP+Utilidades_Request.CARPETA+"ws_basura.php?";

        apiVolley.allData(url, "reportes", id_caneca, "", "", "");
    }

    //INSERT DATA
    private void insertData(ArrayList<Reporte> newReportes) {
        ArrayList<Reporte> reportesD = new ArrayList<>();
        int e = 0;
        for (Reporte r: newReportes){
            if (r.getEstado().equals("0")){
                reportesD.add(r);
            }else{
                e = 1;
            }
        }
        if (e == 1){
            reportesD.add(new Reporte("10000",
                    "",
                    "",
                    "","","", ""));
        }

        adapter.adicionarElementos(reportesD);
        if (reportes.size() == 0){
            finish();
        }
    }

    //ARCHIVADOS
    private void initArchivadosReportes() {
        botom_home = true;
        ArrayList<Reporte> newRe = new ArrayList<>();
        toolbar.setTitle("Reportes Archivados");
        toolbar.setNavigationIcon(backArrow);

        for (Reporte r: reportes){
            if (r.getEstado().equals("0")){

            }else {
                newRe.add(r);
            }
        }
        adapter.adicionarElementos(newRe);
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
            case "reportes":
                Type dataAlType = new TypeToken<ArrayList<Reporte>>(){}.getType();
                reportes = json.fromJson(response,dataAlType);
                insertData(reportes);
                Log.d("", "");
                break;
            case "finalizador":
                dialog.cancel();
                if (response.equals("")){
                    msj_snackbar("No se actualizo", 2);
                }else if(response.equals("true")){
                    msj_snackbar("Actualizacion exitosa.", 1);
                    initAllData();
                }else{
                    msj_snackbar(response, 2);
                }

                Log.d("", "");
                break;
        }
    }

    //CALLBACK ONITEMCLICK LISTENER
    @Override
    public void onClickAdapterReporte(int position, Reporte reporte, View v, int state) {
        if (state == 1){
//            msj_snackbar("Click adapter reporte " + reporte.getCaneca() + " state " + String.valueOf(state), 2);
            ArrayList<Reporte> r = new ArrayList<>();
            r.add(reporte);
            String responseDatos = json.toJson(r);//convertir en jsonString
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("json", responseDatos);
            startActivity(intent);
        }else if(state == 2){
            initArchivadosReportes();
        }

    }

    @Override
    public void onLongClickAdapterReporte(int position, Reporte reporte, View v) {
        if (reporte.getEstado().equals("0")){
            id_reporte = reporte.getIdreporte();
            v.showContextMenu();
        }
    }

    //ON CLICK
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reporte_toolBar:
                msj_snackbar("nn",1);
                break;
            default:
                if (botom_home){
                    toolbar.setTitle("Reportes");
                    toolbar.setNavigationIcon(homeArrow);
                    botom_home = false;
                    insertData(reportes);
                }else{
                    finish();
                }
        }
    }

    //menu context recycler
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_delete, menu);

        MenuItem itemm = menu.findItem(R.id.action_menu_delete);
        itemm.setTitle("Finalizado");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_menu_delete:
                //
                dialog = new ProgressDialog(this);
//            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMessage("Finalizando...");
                dialog.setIndeterminate(false);
//            dialog.setMax(100);
                dialog.setCancelable(false);
                dialog.show();
                //
                apiVolley.usuarioAction(Utilidades_Request.URL, "finalizador", id_reporte,
                        "","","","",""
                );
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    //MENSAJES
    /*mensaje snackbar*/
    private void msj_snackbar(String msj, int process){
//        final Snackbar snack = Snackbar.make(findViewById(android.R.id.content),  snackMsg, Snackbar.LENGTH_INDEFINITE);
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  "", Snackbar.LENGTH_LONG);

        //

        SnackBarClass.imprimir_messge_snackbar(snackbar, msj , process, this);
    }


}
