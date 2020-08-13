package com.example.basuras.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.basuras.Adapter.AdapterCaneca;
import com.example.basuras.R;
import com.example.basuras.Utilidades.LocationChangeListeningActivityLocationCallback;
import com.example.basuras.Utilidades.SnackBarClass;
import com.example.basuras.Utilidades.Utilidades_Proceso;
import com.example.basuras.Utilidades.Utilidades_Request;
import com.example.basuras.model.Caneca;
import com.example.basuras.model.Model;
import com.example.basuras.model.Reporte;
import com.example.basuras.model.Usuario;
import com.example.basuras.volley.ApiVolley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements ApiVolley.TaskCallbacks,
        AdapterCaneca.onClickListenerAdapterCaneca,
        PopupMenu.OnMenuItemClickListener,
        LocationListener
{

    private static final String TAG = "ActivityMain";
    private static final int LOCATION_SETTINGS_REQUEST =  12122;
    private String id, rol;

    private MaterialToolbar toolbar;
    private Context context =  this;
    private Utilidades_Proceso utilidadesProceso = new Utilidades_Proceso(this);
    private ApiVolley apiVolley;
    private Gson json = new Gson();

    private ArrayList<Caneca> canecas = new ArrayList<>();
    private ArrayList<Reporte> reportes = new ArrayList<>();
    private ArrayList<Usuario> usuarios = new ArrayList<>();

    private RecyclerView recyclerView;
    private boolean notificationbooleand;
    private AdapterCaneca adapter;
    private String operacion;


    private LocationManager manager;
    private PermissionsManager permissionsManager;
    private boolean gpsE = true;
    private Timer timer;
    private ProgressDialog dialog;
    private String lat, lng;
    private AlertDialog.Builder builder;
    private AlertDialog dialogg;
    private boolean enablingGps = false;

    //location gps enabled
    private LocationRequest locationRequest;
    private String url;
    private String cane;
    private boolean dataServer = false;
    private String nameUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        toolbar =  findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        //tint 3 ponits menu white
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_more);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setOverflowIcon(upArrow);

        apiVolley = new ApiVolley((ApiVolley.TaskCallbacks) this, this);



        //Reclycler




    }

    //RECYCLER
    private void initRecycler() {
        recyclerView = findViewById(R.id.reclerViewMain);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter = new AdapterCaneca(canecas, this, notificationbooleand, (AdapterCaneca.onClickListenerAdapterCaneca) this);
        recyclerView.setAdapter(adapter);

        initAllData();
    }

    //ALL DATA
    private void initAllData() {
        url = Utilidades_Request.HTTP+ Utilidades_Request.IP+Utilidades_Request.CARPETA+"ws_basura.php?";

        apiVolley.allData(url, operacion, id, "","","");
    }

    //INSERT DATA
    private void insertData(ArrayList<Model> datosResponse){
        //
        Type dataAlTypeC = new TypeToken<ArrayList<Caneca>>(){}.getType();
        canecas = json.fromJson(datosResponse.get(0).getCanecas(),dataAlTypeC);
        //
        Type dataAlTypeR = new TypeToken<ArrayList<Reporte>>(){}.getType();
        reportes = json.fromJson(datosResponse.get(0).getReporte(),dataAlTypeR);
        //
        adapter.adicionarElementos(canecas);

        Log.d("","");
    }

    //INSERT DATA SERVER
    private void enviarinfoserver() {
        if (!dataServer) {
            dataServer = true;
            dialog.setMessage("Enviando información...");
            apiVolley.allData(url, "insert_reporte", id, cane, lat, lng);
        }
    }

    //INITIAL REPORTING
    private void actionInitReporte() {
        int iff = 0;
        for (Reporte r: reportes){
            if (r.getUsuario().equals(id) && r.getCaneca().equals(cane)){
                if (r.getEstado().equals("0")){
                    iff = 1;
                }
            }
        }

        if (iff != 1){
            gpsE = true; //return time
            dataServer = false; //return data insert
            time();
            gpsEnaDis();
            //progreso
            dialog = new ProgressDialog(this);
//            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMessage("Capturando ubicación...");
            dialog.setIndeterminate(false);
//            dialog.setMax(100);
            dialog.setCancelable(false);
            dialog.show();
        }else{
            messageToast("Esta en proceso... Pronto llegaremos!.");
        }
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
            case "all_user":
                Type dataAlTypes = new TypeToken<ArrayList<Model>>(){}.getType();
                ArrayList<Model> datosResponses = json.fromJson(response,dataAlTypes);
                insertData(datosResponses);
                Log.d("", "");
                break;
            case "insert_reporte":
                dialog.cancel();
                destruirProcesos();
                if (response.equals("")){
                    messageToast("No se registro");
                }else if(response.equals("true")){
                    messageToast("Registro exitoso.");
                    initAllData();
                }
                Log.d("", "");
                break;
        }
    }

    //CALLBACK ADAPTER
    @Override
    public void onClickListener(int position, Caneca caneca, View view) {
//        Toast.makeText(getApplicationContext(), caneca.getNombre_caneca(), Toast.LENGTH_LONG).show();
        if(notificationbooleand){
            Intent intent = new Intent(this, ReporteActivity.class);
            intent.putExtra("caneca", caneca.getIdcaneca());
            startActivity(intent);
        }else{
            cane = caneca.getIdcaneca();
            showMenu(view);
        }
    }

    @Override
    public void onLongClickListener(int position, Caneca caneca, View view) {
//        showMenu(view);
        if (!notificationbooleand){
//            messageToast("Inicio gps");
//            time();
//            gpsEnaDis();
//            //progreso
//            dialog = new ProgressDialog(this);
////            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            dialog.setMessage("Capturando ubicación...");
//            dialog.setIndeterminate(false);
////            dialog.setMax(100);
//            dialog.setCancelable(false);
//            dialog.show();
        }
    }

    //MENSAJES
    private void msj_snackbar(String msj, int process){
//        final Snackbar snack = Snackbar.make(findViewById(android.R.id.content),  snackMsg, Snackbar.LENGTH_INDEFINITE);
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  "", Snackbar.LENGTH_LONG);

        //

        SnackBarClass.imprimir_messge_snackbar(snackbar, msj , process, context);
    }

    //OPTIONS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //ICON TINT
        Drawable drawablePerson = menu.findItem(R.id.menu_main_person).getIcon();
        drawablePerson.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        menu.findItem(R.id.menu_main_person).setIcon(drawablePerson);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_main_salir:
                utilidadesProceso.update_id_preference("");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.menu_main_person:
                if (notificationbooleand){
                    startActivity(new Intent(this, UserActivity.class));
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //SHOW POPUP MENU
    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) this);
        popup.inflate(R.menu.menu_item_botton);
        popup.show();
    }

    ////////////////////######LOCATION################///////////////////////
    private void gpsEnaDis() {
        //
        if (manager == null){
            manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        //

        //
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (!enablingGps){
                OnGPSENABLING();
            }
        } else {
            //
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                    != PackageManager.PERMISSION_GRANTED && 
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) 
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                return;
            }else{
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,  this);
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            }
        }
    }

    //
    private void OnGPSENABLING() {

        enablingGps = true; // saber si tiene o no que volver a lanzar el dialog activar gps

         locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        settingsBuilder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(settingsBuilder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response =
                            task.getResult(ApiException.class);
                } catch (ApiException ex) {
                    //#############################################################################
                    switch (ex.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            messageToast("Ubicacion inhabilitada cumple");
                            try {
                                ResolvableApiException resolvableApiException =
                                        (ResolvableApiException) ex;
                                resolvableApiException
                                        .startResolutionForResult(MainActivity.this,
                                                LOCATION_SETTINGS_REQUEST);
                            } catch (IntentSender.SendIntentException e) {
                                Log.d(TAG,"Error activar ubi dialog:  " + e.getMessage());
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            messageToast("Ubicacion inhabilitada no cumple");
                            break;
                    }
                    //##############################################################################
                }
            }
        });
    }

    // LISTENER LOCATION AND PERMISION RESULT CALLBACK
    @Override
    public void onLocationChanged(Location location) {

        if (gpsE){
            timer.cancel();
            gpsE = false;
        }

        lat = String.valueOf(location.getLatitude());
        lng = String.valueOf(location.getLongitude());
        if (lat.length() > 7){
            if (dialog != null){
                if (dialog.isShowing()){
                    //destruir procesos y enviar info
                    enviarinfoserver();
                    destruirProcesos();
                }
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {
            if (grantResults.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gpsEnaDis();
                    return;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_SETTINGS_REQUEST){
            enablingGps = false;
        }
    }

    //////////////////////////////////////////////////////////

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_solicitar:
                actionInitReporte();
                return true;
            default:
                return false;
        }
    }



    private void messageToast(String msj){
        Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_LONG).show();
    }

    //time
    public void time(){
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        // update UI here
                        Log.d(TAG, "initTimer");
                        if (gpsE){
                            gpsEnaDis();
                        }
                        //
                    }
                });
            }
        },0, 14000);
    }

    private void destruirProcesos() {
        if (timer != null){
            timer.cancel();
        }
        if (manager != null){

            manager.removeUpdates(this);
        }
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
            id = parts[0]; // 19
            rol = parts[1]; // 19-A

            if (rol.equals("1")){
                notificationbooleand = true;
                operacion = "all";
            }else{
                notificationbooleand = false;
                operacion = "all_user";
            }

            initRecycler();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

//        MainActivity.this.unregisterReceiver(locationSwitchStateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        destruirProcesos();

    }
}
