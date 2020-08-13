package com.example.basuras.Utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;

import static android.content.Context.MODE_PRIVATE;

public class Utilidades_Proceso {

    private Context context;

    public Utilidades_Proceso(Context context) {
        this.context = context;
    }

    //COLOR LIST
    public ColorStateList getColor(){
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                Color.RED,
                Color.GRAY,
                Color.GRAY,
                Color.BLACK
        };

        ColorStateList myList = new ColorStateList(states, colors);
        return myList;
    }

    //PREFERENCE
    public String read_id_preference(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilidades_Request.NAME_PREFERENCE, MODE_PRIVATE);
        String id = sharedPreferences.getString("id_rol", "");

        return id;
    }

    public void update_id_preference(String id ){
        SharedPreferences.Editor editor = context.getSharedPreferences(Utilidades_Request.NAME_PREFERENCE, MODE_PRIVATE).edit();
        editor.putString("id_rol", id);

        editor.apply();
    }
}
