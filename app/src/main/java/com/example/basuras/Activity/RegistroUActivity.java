package com.example.basuras.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.basuras.R;
import com.example.basuras.Utilidades.SnackBarClass;
import com.example.basuras.Utilidades.Utilidades_Proceso;
import com.example.basuras.Utilidades.Utilidades_Request;
import com.example.basuras.model.Usuario;
import com.example.basuras.volley.ApiVolley;
import com.google.android.gms.common.api.Api;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistroUActivity extends AppCompatActivity implements ApiVolley.TaskCallbacks {

    @BindView(R.id.edittext_name_u) EditText _name;
    @BindView(R.id.edittext_email_u) EditText _correo;
    @BindView(R.id.edittext_pass_u) EditText _pass;
    @BindView(R.id.edittext_phone_u) EditText _phone;
    @BindView(R.id.edittext_direccion) EditText _direccion;
    @BindView(R.id.textInputPass_u)   TextInputLayout _inputPass;
    @BindView(R.id.btn_signupRe) Button _registro;
    @BindViews({ R.id.edittext_name_u, R.id.edittext_email_u, R.id.edittext_pass_u, R.id.edittext_phone_u, R.id.edittext_direccion }) List<EditText> nameViews;
    @BindView(R.id.linearLayout) LinearLayout contentLayout;

    private Utilidades_Proceso utilidades_proceso;

    private ApiVolley apiVolley;
    private ProgressDialog dialog;

    private boolean proceso;
    private MaterialToolbar toolbar;
    private Usuario usuario;
    private String id_usu = "";

    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;
    private Drawable upArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_u);


        //
        ButterKnife.bind(this);


        proceso = getIntent().getExtras().getBoolean("proceso");

        //Toolbar
        toolbar =  findViewById(R.id.registro_toolBar);

        if (proceso){
            //Toolbar title
            toolbar.setTitle("Actualizar usuarios");
            //tint 3 ponits menu white
            upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
            upArrow.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);

            Bundle objetivoEnviado = getIntent().getExtras();
            if(objetivoEnviado != null){
                usuario = (Usuario) objetivoEnviado.getSerializable("usuario");
            }

            llenarInputs();
            disabledAllEdittext();
        }else{
            toolbar.setTitle("Registro usuario");
        }

        setSupportActionBar(toolbar);

        if (upArrow != null){
            toolbar.setOverflowIcon(upArrow);
            toolbar.setNavigationIcon(upArrow);
        }

        utilidades_proceso = new Utilidades_Proceso(this);
        apiVolley =  new ApiVolley((ApiVolley.TaskCallbacks) this, this);
    }

    private void llenarInputs() {
        _name.setText(usuario.getNombre_usu());
        _correo.setText(usuario.getCorreo_usu());
        _pass.setText(usuario.getPassword());
        _phone.setText(usuario.getTelefono());
        _direccion.setText(usuario.getDireccion());

        id_usu = usuario.getIdusuario();

    }

    //CLICK REGISTRO
    @OnClick({R.id.btn_signupRe})
    void bottonClick(){
        signUp();
    }

    //ClICK CONTENT
    @OnClick({R.id.linearLayout})
    void onclickContent(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            //super.onBackPressed();
            enabledAllEdittext();
            return;
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }

    //BTN REGISTRO
    public void signUp() {
//        Log.d(TAG, "Signup");

        _inputPass.setHelperTextEnabled(false);
        _inputPass.setHelperText("");

        if(!validate()){
            onSignupFailed();
            return;
        }

        disabledAllEdittext();
        String name = _name.getText().toString();
        String usu = _correo.getText().toString();
        String pass = _pass.getText().toString();
        String tele = _phone.getText().toString();
        String direccion = _direccion.getText().toString();

        msj_snackbar("Exitosamente. ", 1);
//
        dialog = new ProgressDialog(this);
//            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("Registrando...");
        dialog.setIndeterminate(false);
//            dialog.setMax(100);
        dialog.setCancelable(false);
        dialog.show();

        String url = Utilidades_Request.URL;

        if (proceso){

                if(validarUpdate(name, usu, pass, tele, direccion)){
                    dialog.cancel();
                    msj_snackbar("No tienes ningun dato modificado...", 2);
                    enabledAllEdittext();
                    return;
                }
                //########
                disabledAllEdittext();
                apiVolley.usuario_uAction(url, "update_usuario", id_usu,
                        name,
                        usu,
                        pass,
                        tele,
                        direccion,
                        "2"
                );
                ///////////////////////////////////////////////////////////////////////////
        }else{
            apiVolley.usuario_uAction(url, "insert_usuario_usu", "",
                    name,
                    usu,
                    pass,
                    tele,
                    direccion,
                    "2"
            );
        }



        Log.d("", "");

//        C_W_S_Content(ema, pass);

    }

    //VALIDATION UPDATE
    private boolean validarUpdate(String name, String usu, String pass, String tele, String direccion) {
        boolean valid = true;

        if (!usuario.getNombre_usu().equals(name)){
            valid = false;
        }
        if (!usuario.getCorreo_usu().equals(usu)){
            valid = false;
        }
        if (!usuario.getPassword().equals(pass)){
            valid = false;
        }
        if (!usuario.getTelefono().equals(tele)){
            valid = false;
        }
        if (!usuario.getDireccion().equals(direccion)){
            valid = false;
        }
        Log.d("","");

        return  valid;
    }

    public void onSignupFailed() {
        enabledAllEdittext();
    }

    //VALIDATION EDITTEX
    @SuppressLint("ResourceAsColor")
    public boolean validate() {
        boolean valid = true;

        String name = _name.getText().toString();
        String ema = _correo.getText().toString();
        String pass = _pass.getText().toString();
        String phone = _phone.getText().toString();
        String direccion = _direccion.getText().toString();

        //|| !Patterns.EMAIL_ADDRESS.matcher(ema).matches()
        if (name.isEmpty() ) {
            _name.setError("Requerido");
            valid = false;
        } else {
            _name.setError(null);
        }

        if (ema.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(ema).matches() ) {
            _correo.setError("Introduzca una dirección de correo electrónico válida");
            valid = false;
        } else {
            _correo.setError(null);
        }

        if (pass.isEmpty()) {
//            _pass.setError("Requerido");
            _inputPass.setHelperTextEnabled(true);
            _inputPass.setHelperText("Requerido");

            ColorStateList color = utilidades_proceso.getColor();

            _inputPass.setHelperTextColor(color);
            valid = false;
        } else {
            _pass.setError(null);
            _inputPass.setHelperTextEnabled(false);
            _inputPass.setHelperText("");
        }

        if (phone.isEmpty() ) {
            _phone.setError("Requerido");
            valid = false;
        } else {
            _phone.setError(null);
        }

        if (direccion.isEmpty()) {
            _direccion.setError("Requerido");
            valid = false;
        } else {
            _direccion.setError(null);
        }



        return valid;
    }

    //ENABLED EDTTEXT
    private void disabledAllEdittext() {
        for (EditText editText: nameViews){
            editText.setEnabled(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }


        _registro.setEnabled(false);
    }

    //DISABLED EDTTEXT
    private void enabledAllEdittext() {
        for (EditText editText: nameViews){
            editText.setEnabled(true);

            int i = 0;
            if (i == 0){
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                i++;
            }
        }


        _registro.setEnabled(true);
    }

    //CALLBACK VOLLEY
    @Override
    public void onError(String nameP, String response) {
        if (dialog != null){
            if (dialog.isShowing()){
                dialog.cancel();
            }
        }
//        Log.d(nameP, response);
        msj_snackbar(response, 2);//mensaje
    }

    @Override
    public void onSucess(String nameP, String response) {
        switch (nameP){
            case "insert_usuario_usu":

                dialog.cancel();
                if (response.equals("")){
                    msj_snackbar("No se registro: " + response, 2);
                    enabledAllEdittext();
                }else if (response.equals("correo")){
                    msj_snackbar("Correo electrónico existente. Cambialo e intentelo de nuevo.: " + response, 2);
                    enabledAllEdittext();
                }else{
                    msj_snackbar("Registro exitoso... " + response, 1);
                    utilidades_proceso.update_id_preference(response);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }

                Log.d("", "");
                break;

            case "update_usuario":

                dialog.cancel();
                if (response.equals("")){
                    msj_snackbar("No se actualizo: " + response, 2);
                    enabledAllEdittext();
                }else{
                    msj_snackbar("Actualizo exitosamente...", 1);
                }

                Log.d("", "");
                break;
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}