package com.limeri.leon;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;
import com.limeri.leon.Models.Profesional;

import java.util.ArrayList;
import java.util.List;

public class SelecPacienteActivity extends AppCompatActivity {


    private String mNombre, mApellido, mDNI, mFechaNac, mProfPassword, mProfCorreo, mProfNombre ,mProfMatricula;
    AlertDialog dialog;
    private ListView lvPacientes;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selec_paciente);

        lvPacientes = (ListView) findViewById(R.id.listPacientes);
        Button btnAdd = (Button) findViewById(R.id.buttonAddPaciente);
        ImageButton btnSearch = (ImageButton) findViewById(R.id.btn_search);
        final EditText searchString = (EditText) findViewById(R.id.et_search);
        Button btnMostrarTodo = (Button) findViewById(R.id.btn_mostrarTodos);

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.action_bar_main,
                null);


        getSupportActionBar().setCustomView(actionBarLayout);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        adapter = new ArrayAdapter<String>(this, R.layout.paciente_item);
        adapter.clear();

        for (Paciente paciente : Paciente.getCuentas()) {
            adapter.add(paciente.getNombreCompleto());
        }
        lvPacientes.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.paciente_item);
                for (Paciente paciente : Paciente.getCuentasByName(searchString.getText().toString())) {
                    if (paciente.getNombreCompleto() != "") {
                        adapter.add(paciente.getNombreCompleto());
                    }
                }
                if (adapter.getCount() != 0) {
                    lvPacientes.setAdapter(adapter);
                    lvPacientes.invalidateViews();
                }
            }
        });

        btnMostrarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.paciente_item);
                for (Paciente paciente : Paciente.getCuentas()) {
                    adapter.add(paciente.getNombreCompleto());
                }
                if (adapter.getCount() != 0) {
                    lvPacientes.setAdapter(adapter);
                    lvPacientes.invalidateViews();
                }
            }
        });

        lvPacientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView paciente = (TextView) view.findViewById(R.id.tvLinea);
                //Guardar el paciente seleccionado para usarlo an toda la aplicacion
                Paciente.setSelectedPaciente(Paciente.getCuentaByName(paciente.getText().toString()));
                Navegacion.irA(SelecPacienteActivity.this,MainActivity.class);
            }
        });
    }

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.crear_paciente_popup, (ViewGroup) this
                .findViewById(android.R.id.content), false);
        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        final EditText input2 = (EditText) viewInflated.findViewById(R.id.inputApellido);
        final EditText input3 = (EditText) viewInflated.findViewById(R.id.inputDNI);
        //TODO: Corregir el campo FECHA DE NACIMIENTO, para que sea un campo solo de tipo FECHA. Que valide el tipo de dato.
        final EditText input4 = (EditText) viewInflated.findViewById(R.id.inputFechaNac);
        //   final Spinner spin1 = (Spinner) viewInflated.findViewById(R.id.inputProvider);


//        final ListView lView = (ListView) viewInflated.findViewById(R.id.listPacientes);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cuenta_item);


        final List<Paciente> mPacientes = Paciente.getCuentas();

//        for (int i = 0; i < mPacientes.size(); i++) {

        //    adapter.agregarPaciente(mPacientes.get(i).getNombre().toString());

        // }

        // if (adapter.getCount() != 0) {
        //    lView.setAdapter(adapter);
        // }
        //TODO: Setear un nuevo layout, para reutilizar y definir un diseño de pantalla acorde al modelo.
        builder.setView(viewInflated);


        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

        // Set up the buttons

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mNombre = input.getText().toString();
                mApellido = input2.getText().toString();
                mDNI = input3.getText().toString();
                mFechaNac = input4.getText().toString();

                Paciente paciente = new Paciente();

                paciente.setApellido(mApellido);
                paciente.setNombre(mNombre);
                paciente.setDni(mDNI);
                paciente.setFechaNac(mFechaNac);

                adapter.add(paciente.getNombreCompleto());
                lvPacientes.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lvPacientes.invalidateViews();

                Paciente.saveCuenta(SelecPacienteActivity.this, paciente);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });


        dialog = builder.create();
        dialog.show();

        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (button != null) {
            button.setBackgroundColor(getResources().getColor(R.color.black));
            button.setTextColor(getResources().getColor(R.color.black));
            button.setGravity(Gravity.RIGHT);
            button.setGravity(Gravity.CENTER_VERTICAL);
            button.setBackground(getResources().getDrawable(R.drawable.button));
        }

        Button button2 = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (button2 != null) {
            button2.setBackgroundColor(getResources().getColor(R.color.black));
            button2.setTextColor(getResources().getColor(R.color.black));
            button2.setGravity(Gravity.LEFT);
            button2.setBackground(getResources().getDrawable(R.drawable.button));
            button2.setGravity(Gravity.CENTER_VERTICAL);
        }

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(adapter != null) {
            adapter.clear();
            ((ArrayAdapter<String>) lvPacientes.getAdapter()).clear();
            lvPacientes.invalidateViews();
            lvPacientes = new ListView(this);
        }
        Navegacion.irA(this, LoginActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selec_paciente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.action_settings: {
                showDialog_Prof();
                break;
            }
            case R.id.action_signout:{
                Navegacion.irA(this, LoginActivity.class);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //POPUP PARA EDITAR DATOS PROFESIONAL
    private void showDialog_Prof() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.datos_profesional_popup, (ViewGroup) this
                .findViewById(android.R.id.content), false);
        // Set up the input
        final EditText nombre = (EditText) viewInflated.findViewById(R.id.inputNombre);
        final EditText matricula = (EditText) viewInflated.findViewById(R.id.inputMatricula);
        final EditText password = (EditText) viewInflated.findViewById(R.id.inputPassword);
        final EditText confPassword = (EditText) viewInflated.findViewById(R.id.inputPassword2);
        final EditText correo = (EditText) viewInflated.findViewById(R.id.inputCorreo);

        nombre.setText(Profesional.getProfesional().getNombre());
        nombre.setEnabled(false);
        matricula.setText(Profesional.getProfesional().getmMatricula());
        matricula.setEnabled(false);
        password.setText(Profesional.getProfesional().getmPassword());
        correo.setText(Profesional.getProfesional().getmCorreo());


        builder.setView(viewInflated);


        // Set up the buttons

        builder.setPositiveButton("Actualizar datos profesional", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mProfPassword = password.getText().toString();
                mProfCorreo = correo.getText().toString();
                mProfNombre = nombre.getText().toString();
                mProfMatricula = matricula.getText().toString();

                if (confPassword.getText().toString().isEmpty()) {
                    confPassword.setError("Repita contraseña");
                } else if (!password.getText().toString().equals(confPassword.getText().toString()))
                {
                    password.setError("Contraseñas no coinciden");
                    confPassword.setError("Contraseñas no coinciden");
                }else {
                    Profesional profesional = Profesional.getProfesional();
                    Profesional.borrarCuentaBase(SelecPacienteActivity.this, profesional);

                    // Profesional profesional = new Profesional();
                    profesional.setNombre(mProfNombre);
                    profesional.setmCorreo(mProfCorreo);
                    profesional.setmPassword(mProfPassword);
                    profesional.setmMatricula(mProfMatricula);

                    Profesional.saveProfesional(SelecPacienteActivity.this, profesional);
                    //GRABAR ACTUALIZACION O BORRAR Y VOLVER A CREAR
                }
                // callAdapter();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
           }


        });

        dialog = builder.create();


        dialog.show();


        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        if (button != null) {
            button.setBackgroundColor(getResources().getColor(R.color.black));
            button.setTextColor(getResources().getColor(R.color.black));
            button.setGravity(Gravity.END);
            button.setGravity(Gravity.CENTER_VERTICAL);
            button.setBackground(getResources().getDrawable(R.drawable.button));
            button.setPadding(10, 0, 10, 0);
        }

        Button button2 = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        if (button2 != null) {
            button2.setBackgroundColor(getResources().getColor(R.color.black));
            button2.setTextColor(getResources().getColor(R.color.black));
            button2.setGravity(Gravity.START);
            button2.setBackground(getResources().getDrawable(R.drawable.button));
            button2.setGravity(Gravity.CENTER_VERTICAL);
            button2.setPadding(10, 0, 10, 0);
        }

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);

    }


}