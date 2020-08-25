package com.example.basuras.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.impl.utils.futures.FluentFuture;
import androidx.collection.ArraySet;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basuras.R;
import com.example.basuras.Utilidades.SnackBarClass;
import com.example.basuras.Utilidades.Utilidades_Proceso;
import com.example.basuras.Utilidades.Utilidades_Request;
import com.example.basuras.model.Model;
import com.example.basuras.model.Reporte;
import com.example.basuras.model.Usuario;
import com.example.basuras.volley.ApiVolley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class RegisterUActivity extends AppCompatActivity implements ApiVolley.TaskCallbacks
{

    private Utilidades_Proceso utilidadesProceso = new Utilidades_Proceso(this);
    private MaterialToolbar toolbar;
    private ApiVolley apiVolley;
    private int actionOperation;
    private String id_rol;

    //
    @BindView(R.id.edittext_name)  EditText _name;
    @BindView(R.id.edittext_email) EditText _email;
    @BindView(R.id.edittext_pass) EditText _pass;
    @BindView(R.id.edittext_phone) EditText _phone;
    @BindView(R.id.edittext_direcciona) EditText _direccion;

    @BindView(R.id.btn_signupR) Button _btnregistro;

    @BindView(R.id.textInputPass) TextInputLayout _inputPass;

    @BindView(R.id.autoCompleteRol)    AutoCompleteTextView rol;

    @BindViews({ R.id.edittext_name, R.id.edittext_email, R.id.edittext_pass, R.id.edittext_phone, R.id.edittext_direcciona }) List<EditText> nameViews;
    private Menu menuGlobal;
    private ArrayAdapter<CharSequence> adapter;
    private Usuario usuario;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_u);

        //
        ButterKnife.bind(this);


        //getExtra intent
        actionOperation  = getIntent().getExtras().getInt("operation");

        toolbar =  findViewById(R.id.register_toolBar);
        if (actionOperation != 1) {
            toolbar.setTitle("Registro usuarios");
            enabledAllEdittext();
        }else{

            toolbar.setTitle("Actualizar usuarios");
            disabledAllEdittext();

            Bundle objetivoEnviado = getIntent().getExtras();
            if(objetivoEnviado != null){
                usuario = (Usuario) objetivoEnviado.getSerializable("usuario");
            }
            Log.d("","");
        }
        setSupportActionBar(toolbar);
        //tint 3 ponits menu white
        final Drawable homeArrow = getResources().getDrawable(R.drawable.selector_home);
        homeArrow.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(homeArrow);

        apiVolley = new ApiVolley((ApiVolley.TaskCallbacks) this, this);

        //LLENAR ROL
        rol.setInputType(InputType.TYPE_NULL);
        rol.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long arg3) {
                id_rol = String.valueOf(adapterView.getAdapter().getItemId(position) + 1);
//                textView.setText("");
            }
        });

        llenarRol();

    }

    private void llenarRol() {
        if (usuario != null){
            _name.setText(usuario.getNombre_usu());
            _email.setText(usuario.getCorreo_usu());
            _pass.setText(usuario.getPassword());
            _phone.setText(usuario.getTelefono());
            _direccion.setText(usuario.getDireccion());
            id_rol = usuario.getRol_idrol();
            if (usuario.getRol_idrol().equals("1")){
                rol.setText("Administrador");
            }else{
                rol.setText("Usuario");
            }
        }

        ArrayList<String> listaEventos = new ArrayList<String>();
        listaEventos.add(/*encuestass.get(i).getId_encuesta() + " - " +*/ "Administrador");
        listaEventos.add(/*encuestass.get(i).getId_encuesta() + " - " +*/ "Usuario");


        adapter= new ArrayAdapter(this, android.R.layout.simple_spinner_item,listaEventos);
        rol.setAdapter(adapter);
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
        String usu = _email.getText().toString();
        String pass = _pass.getText().toString();
        String tele = _phone.getText().toString();
        String direccion = _direccion.getText().toString();
        String rol = id_rol;

        dialog = new ProgressDialog(this);
//            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("Registrando...");
        dialog.setIndeterminate(false);
//            dialog.setMax(100);
        dialog.setCancelable(false);
        dialog.show();

        String url = Utilidades_Request.URL;
        switch (actionOperation){
            case 22:
                if(validarUpdate(name, usu, pass, tele, direccion,  rol)){
                    dialog.cancel();
                    msj_snackbar("No tienes ningun dato modificado...", 2);
                    enabledAllEdittext();
                    return;
                }
                //########
                disabledAllEdittext();
                apiVolley.usuarioAction(url, "update_usuario", usuario.getIdusuario(),
                        name,
                        usu,
                        pass,
                        tele,
                        direccion,
                        rol
                );
                ///////////////////////////////////////////////////////////////////////////
                break;
            default:
                apiVolley.usuarioAction(url, "insert_usuario", "",
                        name,
                        usu,
                        pass,
                        tele,
                        direccion,
                        rol
                );
        }
        Log.d("", "");

//        C_W_S_Content(ema, pass);

    }

    public void onSignupFailed() {
        enabledAllEdittext();
    }

    //VALIDATION EDITTEX
    @SuppressLint("ResourceAsColor")
    public boolean validate() {
        boolean valid = true;

        String name = _name.getText().toString();
        String ema = _email.getText().toString();
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

        if (phone.isEmpty() ) {
            _phone.setError("Requerido");
            valid = false;
        } else {
            _phone.setError(null);
        }

        if (direccion.isEmpty() ) {
            _direccion.setError("Requerido");
            valid = false;
        } else {
            _direccion.setError(null);
        }

        if (id_rol == null) {
            rol.setError("Requerido");
            valid = false;
        } else {
            rol.setError(null);
        }



        return valid;
    }

    //VALIDATION UPDATE
    private boolean validarUpdate(String name, String usu, String pass, String tele, String direccion, String rol) {
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
        if (!usuario.getRol_idrol().equals(rol)){
            valid = false;
        }
        Log.d("","");

        return  valid;
    }

    //ENABLED EDTTEXT
    private void disabledAllEdittext() {
        for (EditText editText: nameViews){
            editText.setEnabled(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }

        rol.setEnabled(false);
        rol.setFocusable(false);
        rol.setFocusableInTouchMode(false);
        _btnregistro.setEnabled(false);
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

        rol.setEnabled(true);
        _btnregistro.setEnabled(true);
    }

    //CALLBACK VOLLEY
    @Override
    public void onError(String nameP, String response) {
        if (dialog != null){
            if (dialog.isShowing()){
                dialog.cancel();
            }
        }
        Log.d(nameP, response);
        msj_snackbar(response, 2);//mensaje
    }

    @Override
    public void onSucess(String nameP, String response) {
        switch (nameP){
            case "insert_usuario":

                dialog.cancel();
                if (response.equals("")){
                    msj_snackbar("No se registro: " + response, 2);
                }else if(response.equals("true")){
                    msj_snackbar("Registro exitoso.", 1);
                    clearEdittext();
                }

                Log.d("", "");
                break;
            case "update_usuario":
                dialog.cancel();
                if (response.equals("")){
                    msj_snackbar("No se registro: " + response, 2);
                }else if(response.equals("true")){
                    msj_snackbar("Registro exitoso.", 1);
                    clearEdittext();
                    finish();
                }

//                Log.d("", "");
                break;
        }
    }

    //CLEAR IMPUTS
    private void clearEdittext() {
        enabledAllEdittext();
        
        for (EditText editText : nameViews){
            editText.setText("");
        }
        
        rol.setText("");
    }

    //CLICK REGISTER
    @OnClick({R.id.btn_signupR})
    void bottonClick(){
//        Toast.makeText(getApplicationContext(),"Click btn", Toast.LENGTH_SHORT).show();
        signUp();
    }

    //ONCREATE MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_reg, menu);

        if (actionOperation != 0){
            menuGlobal = menu;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_menu_item_enable:
                if (actionOperation != 1){
                    actionOperation = 1;
                    disabledAllEdittext();
                }else{
                    actionOperation = 22;
                    enabledAllEdittext();
                }
                onPrepareOptionsMenu(menuGlobal);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (actionOperation == 0){
            MenuItem drawablePerson = menu.findItem(R.id.action_menu_item_enable);
            drawablePerson.setVisible(false);

        }else{
            if (menu != null){
                MenuItem drawablePerson = menu.findItem(R.id.action_menu_item_enable);
                if (actionOperation != 1){
                    drawablePerson.setIcon(getResources().getDrawable(R.drawable.ic_person));
                }else{
                    drawablePerson.setIcon(getResources().getDrawable(R.drawable.ic_person_disabled));
                }

                Drawable drawable = drawablePerson.getIcon();
                drawable.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }



        return super.onPrepareOptionsMenu(menu);
    }


    //MENSAJES
    private void msj_snackbar(String msj, int process){
//        final Snackbar snack = Snackbar.make(findViewById(android.R.id.content),  snackMsg, Snackbar.LENGTH_INDEFINITE);
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  "", Snackbar.LENGTH_LONG);
        //
        SnackBarClass.imprimir_messge_snackbar(snackbar, msj , process, this);
    }


}