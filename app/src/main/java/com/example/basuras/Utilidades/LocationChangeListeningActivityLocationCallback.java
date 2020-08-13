package com.example.basuras.Utilidades;

import android.app.Activity;
import android.location.Location;

import androidx.annotation.NonNull;

import com.example.basuras.Activity.MainActivity;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.lang.ref.WeakReference;

public class LocationChangeListeningActivityLocationCallback implements LocationEngineCallback<LocationEngineResult> {



    private OnCallBackResultLocaiton call;



    @Override
    public void onSuccess(LocationEngineResult result) {
        Location location = result.getLastLocation();

        if (location == null) {
            return;
        }

        LatLng latLng = new LatLng(
                result.getLastLocation().getLatitude(),
                result.getLastLocation().getLongitude()
        );

        if (call != null){
            call.onSuccess(latLng);
        }

    }

    @Override
    public void onFailure(@NonNull Exception exception) {
        if (call != null){
            call.onFailure(exception.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public interface OnCallBackResultLocaiton{
        void onSuccess(LatLng coordenadas);
        void onFailure(String error);
    }
}
