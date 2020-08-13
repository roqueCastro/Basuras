package com.example.basuras.volley;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.basuras.Activity.MainActivity;
import com.example.basuras.Utilidades.MySingleton;

import java.util.HashMap;
import java.util.Map;

public class ApiVolley {

    private TaskCallbacks mCallbacks;
    private Context context;
    private RequestQueue request;
    private StringRequest stringRequest;

    public ApiVolley(TaskCallbacks mCallbacks, Context context) {
        this.mCallbacks = mCallbacks;
        this.context = context;
    }

    public interface TaskCallbacks {
        void onError(String nameP, String response);
        void onSucess(String nameP,String response);
    }


    public void allData(
            String url, final String operacion, final String id, final String caneca,
            final String lat, final String lng
                        ){
        request = MySingleton.getInstance(context).getRequestQueue();
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (mCallbacks != null){
                    mCallbacks.onSucess(operacion, response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mCallbacks != null){
                    mCallbacks.onError(operacion, error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> paramentros = new HashMap<>();
                paramentros.put("Operacion", operacion);
                paramentros.put("id", id);
                paramentros.put("caneca", caneca);
                paramentros.put("lat", lat);
                paramentros.put("lng", lng);
                return paramentros;
            }
        };
        request.add(stringRequest);
    }
}
