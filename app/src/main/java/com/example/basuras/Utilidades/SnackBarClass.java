package com.example.basuras.Utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.example.basuras.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;

public class SnackBarClass {
    public static void imprimir_messge_snackbar(Snackbar snackbar, String msj, int process, Context context){
        View view = snackbar.getView();

//        TextView tv = (TextView) view.findViewById((com.google.android.material.R.id.snackbar_text));
//
//        tv.setBackgroundColor(Color.RED);
//        tv.setLines(12);

        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.height=120;
//        params.bottomMargin=10;


        View custom = LayoutInflater.from(context).inflate(R.layout.view_snackbar, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarlayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarlayout.setPadding(0,0,0,0);


        TextView message = custom.findViewById(R.id.textview_message);

        message.setText(msj);
        if (process==1){
            message.setBackgroundColor(context.getResources().getColor(R.color.Verde_Claro));
        }else if(process == 2){
            message.setBackgroundColor(context.getResources().getColor(R.color.design_default_color_error));
        }

        message.setTextColor(context.getResources().getColor(android.R.color.white));

        snackbarlayout.addView(custom, params);
        snackbar.show();

    }
    /*COlor*/
    public static ColorStateList getColorStateList(Resources resources, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return resources.getColorStateList(id, null);
        } else {
            return resources.getColorStateList(id);
        }
    }

    /*preferences select*/


}
