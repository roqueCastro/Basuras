package com.example.basuras.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basuras.R;
import com.example.basuras.model.Reporte;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.lang.reflect.Type;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.makeramen.roundedimageview.RoundedDrawable.drawableToBitmap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.backgroundColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.backgroundOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOpacity;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        MapboxMap.OnMapClickListener
{

    private MaterialToolbar toolbar;
    private Drawable upArrow;
    private MapView mapView;
    private MapboxMap map;

    private GeoJsonSource geoJsonSource;
    private String id_img_icon = "id_Icon";
    private String id_geo_JsonSource= "id_geoJsonSource";
    private String id_contenedor_marcador= "id_layer";

    private LatLng ubicacionActual = new LatLng(1.967983, -75.921382);
    private TextView mTitle;
    private static final String PROFILE_NAME = "marker";
    private FeatureCollection feactureCollection;

    private ArrayList<Reporte> reportes;
    private Gson json = new Gson();
    private Reporte reporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        Mapbox.getInstance(this, getString(R.string.key_Token));
        //
        setContentView(R.layout.activity_map);

        //Toolbar
        toolbar =  findViewById(R.id.map_toolBar);

        setSupportActionBar(toolbar);
        mTitle = findViewById(R.id.toolbar_title);
        mTitle.setText("Ubicacion");
        toolbar.setTitle(mTitle.getText());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //tint 3 ponits menu white
        upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        upArrow.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setOverflowIcon(upArrow);
        toolbar.setNavigationIcon(upArrow);

        //getEXtra intent latLng
        String j = getIntent().getExtras().getString("json");
        Type dataAlType = new TypeToken<ArrayList<Reporte>>(){}.getType();
        reportes = json.fromJson(j,dataAlType);
        reporte = reportes.get(0);

        ubicacionActual = new LatLng(Float.parseFloat(reporte.getLat()), Float.parseFloat(reporte.getLng()));

        //MAP
        mapView = findViewById(R.id.mapViewMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    //MAP READY
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.map = mapboxMap;

        geoJsonSource = new GeoJsonSource("id_geoJsonSource",
                Feature.fromGeometry(Point.fromLngLat(ubicacionActual.getLongitude(),
                        ubicacionActual.getLatitude())));

        styleMap(Style.OUTDOORS);
    }

    private void styleMap(String Style){
        map.setStyle(Style, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                inicioDeAnadirMarcador(style);
            }
        });
    }

    //CLICK MAP
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return handleClickIcon(map.getProjection().toScreenLocation(point));
    }

    //INICIO MARKER ADD
    private void inicioDeAnadirMarcador(@NonNull Style style) {

        Drawable image  = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_place);
        Bitmap bitmap =drawableToBitmap(image);
        style.addImage(id_img_icon, bitmap);

        Feature ubiFeature = Feature.fromGeometry(Point.fromLngLat(
                ubicacionActual.getLongitude(),
                ubicacionActual.getLatitude()));
        ubiFeature.addStringProperty(PROFILE_NAME, "Dirección de Sr@: " + reporte.getUsuario());


        feactureCollection = FeatureCollection.fromFeatures(new Feature[] {
                ubiFeature
        });
        geoJsonSource = new GeoJsonSource(id_geo_JsonSource, feactureCollection);

        style.addSource(geoJsonSource);

        style.addLayer(new SymbolLayer(id_contenedor_marcador, id_geo_JsonSource).withProperties(
                iconImage(id_img_icon),
                iconAllowOverlap(true),
                iconSize(1f),
                textField(get(PROFILE_NAME)),
                textIgnorePlacement(true),
                textAllowOverlap(true),
                textOffset(new Float[] {0f, -2f}),
                PropertyFactory.textHaloBlur(10f),
                PropertyFactory.textOpacity(0.1f),
                PropertyFactory.rasterContrast(2f)

        ));

        //
        animarCamara(ubicacionActual);

        map.addOnMapClickListener(MapActivity.this);
    }

    //CAMERA MARKER ANIMATE POSITION
    private void animarCamara(LatLng posisionMarkerCamera) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(posisionMarkerCamera)) // Sets the new camera position
                .zoom(11) // Sets the zoom
                .bearing(0) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 2000);
    }


    //
    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = map.queryRenderedFeatures(screenPoint, id_contenedor_marcador);
        if (!features.isEmpty()) {
            String name = features.get(0).getStringProperty(PROFILE_NAME);
            List<Feature> featureList = feactureCollection.features();
            if (featureList != null) {
                for (int i = 0; i < featureList.size(); i++) {
                    if (featureList.get(i).getStringProperty(PROFILE_NAME).equals(name)) {
                        String m = reporte.getCaneca() + ".\n";
                        m += "Usuario: " + reporte.getUsuario() + "\n";
                        m += "fecha: " + reporte.getFecha_update();
                        Toast.makeText(getApplicationContext(),m, Toast.LENGTH_LONG).show();
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
