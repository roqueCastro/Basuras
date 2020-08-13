package com.example.basuras.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.basuras.R;
import com.example.basuras.Utilidades.SnackBarClass;
import com.example.basuras.Utilidades.Utilidades_Proceso;
import com.example.basuras.Utilidades.Utilidades_Request;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;



public class LoginActivity extends AppCompatActivity {

    EditText _email, _pass;
    Button _signl;
    TextInputLayout _inputPass;

    private RequestQueue request;
    private Context context;
    private StringRequest stringRequest;

    private Utilidades_Proceso utilidadesProceso = new Utilidades_Proceso(this);
    private ProgressDialog progreso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        request = Volley.newRequestQueue(getApplicationContext());

        _email = findViewById(R.id.edittext_email);
        _pass = findViewById(R.id.edittext_pass);
        _inputPass = findViewById(R.id.textInputPass);
        _signl = findViewById(R.id.btn_signupl);
        context = this;

        _signl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void C_W_S_Content(final String ema, final String pass) {
        progreso_carga("Cargando...","create");//crear progres dialog
        String url = Utilidades_Request.HTTP+ Utilidades_Request.IP+Utilidades_Request.CARPETA+"ws_basura.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso_carga("Cargando informacion","delete");//crear progres dialog

                if(response.trim().equals("")){
                    msj_snackbar("Contraseña o correo electronico incorrecto!.", 2);
                    response = "0";
                }else{
                    _email.setText("");
                    _pass.setText("");
                    msj_snackbar("Exitoso ",1);


                    utilidadesProceso.update_id_preference(response);
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso_carga("Cargando informacion","delete");//crear progres dialog
                //mensajeAlertaTextViewError("Ocurrio un error en el servidor ", 3000);
                Log.i("TAGV", error.toString());
                msj_snackbar("Error al consultar informacion.", 2);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> paramentros = new HashMap<>();
                paramentros.put("Operacion", "logueo");
                paramentros.put("usu", ema);
                paramentros.put("pass", pass);
                return paramentros;
            }
        };
        request.add(stringRequest);
    }



    //click btn en registro
    public void signUp() {
//        Log.d(TAG, "Signup");

        if(!validate()){
            onSignupFailed();
            return;
        }

        String ema = _email.getText().toString();
        String pass = _pass.getText().toString();

        C_W_S_Content(ema, pass);

    }

    public void onSignupFailed() {
        enabledbtn();
    }

    /*VALIDATION*/
    @SuppressLint("ResourceAsColor")
    public boolean validate() {
        boolean valid = true;

        String ema = _email.getText().toString();
        String pass = _pass.getText().toString();

        //|| !Patterns.EMAIL_ADDRESS.matcher(ema).matches()
        if (ema.isEmpty() ) {
            _email.setError("Requerido");
            valid = false;
        } else {
            _email.setError(null);
        }

        if (pass.isEmpty()) {
//            _pass.setError("Requerido");
            _inputPass.setHelperTextEnabled(true);
            _inputPass.setHelperText("Requerido");

            ColorStateList color = utilidadesProceso.getColor();

            _inputPass.setHelperTextColor(color);
            valid = false;
        } else {
            _pass.setError(null);
            _inputPass.setHelperTextEnabled(false);
            _inputPass.setHelperText("");
        }



        return valid;
    }

    /*
     *
     *
     *
     ******************FUntion para mensaje Toask*****************
     *
     *
     *
     */
    private void msj_toast(String msj){
        Toast.makeText(getApplicationContext(), msj,Toast.LENGTH_SHORT).show();
    }

    /*mensaje snackbar*/
    private void msj_snackbar(String msj, int process){
//        final Snackbar snack = Snackbar.make(findViewById(android.R.id.content),  snackMsg, Snackbar.LENGTH_INDEFINITE);
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  "", Snackbar.LENGTH_LONG);

        //

        SnackBarClass.imprimir_messge_snackbar(snackbar, msj , process, context);
    }

    /*
     *
     * *******FUntion para mensaje progress******
     */
    private void progreso_carga(String msj, String action){

        if (action.equals("create")){
            progreso= new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
            progreso.setMessage(msj);
            progreso.setCanceledOnTouchOutside(false);
            progreso.show();
        }else if(action.equals("delete")){
            progreso.hide();
        }
    }

    /*
     *
     *
     * BUTTON STATE
     *
     * */
    private void disablebtn(){
        _signl.setText("Guardando...");
        _signl.setEnabled(false);
    }
    private void enabledbtn(){
        _signl.setEnabled(true);
        _signl.setText("Guardar");
    }

}
