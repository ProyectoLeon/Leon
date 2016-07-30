package com.limeri.leon;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.limeri.leon.Models.Cuenta;

import java.util.List;

public class SelecPacienteActivity extends AppCompatActivity {


    String mUser, mContr, mProv;
    AlertDialog dialog;
    ListView pacientes;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selec_paciente);
        //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);

        pacientes = (ListView) findViewById(R.id.listPacientes);
        Button btnAdd = (Button) findViewById(R.id.buttonAddPaciente);
        ImageButton btnSearch = (ImageButton) findViewById(R.id.btn_search);
        final EditText searchString = (EditText) findViewById(R.id.et_search);
        Button btnMostrarTodo = (Button) findViewById(R.id.btn_mostrarTodos);


        adapter = new ArrayAdapter<String>(this, R.layout.paciente_item);
        adapter.clear();

        for (int i = 0; i < Cuenta.getCuentas().size(); i++) {

            String nombreApellidoCuenta = Cuenta.getCuentas().get(i).getNombre() + " " + Cuenta.getCuentas().get(i).getApellido();

               adapter.add(nombreApellidoCuenta);
            }


         pacientes.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.paciente_item);

                for (int i = 0; i < Cuenta.getCuentasByName(searchString.getText().toString()).size(); i++) {

                    String nombre = Cuenta.getCuentasByName(searchString.getText().toString()).get(i).getNombre();
                    String apellido = Cuenta.getCuentasByName(searchString.getText().toString()).get(i).getApellido();

                    if (nombre != "" || apellido != "") {
                        adapter.add(nombre + " " + apellido);
                    }

                }

                if (adapter.getCount() != 0) {
                    pacientes.setAdapter(adapter);
                    pacientes.invalidateViews();

                }

            }
        });

        btnMostrarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.paciente_item);

                for (int i = 0; i < Cuenta.getCuentas().size(); i++) {
                adapter.add(Cuenta.getCuentas().get(i).getNombre() + " " + Cuenta.getCuentas().get(i).getApellido());

                }

                if (adapter.getCount() != 0) {
                    pacientes.setAdapter(adapter);
                    pacientes.invalidateViews();

                }

            }
        });

        pacientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView paciente = (TextView) view.findViewById(R.id.tvLinea);

                //Guardar el paciente seleccionado para usarlo an toda la aplicacion
                Cuenta.setSelectedCUenta(Cuenta.getCuentaByName(paciente.getText().toString()));

                Intent mainIntent = new Intent(SelecPacienteActivity.this, MainActivity.class);
                SelecPacienteActivity.this.startActivity(mainIntent);
                SelecPacienteActivity.this.finish();

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
        final EditText input2 = (EditText) viewInflated.findViewById(R.id.inputPassword);
        //   final Spinner spin1 = (Spinner) viewInflated.findViewById(R.id.inputProvider);


//        final ListView lView = (ListView) viewInflated.findViewById(R.id.listPacientes);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cuenta_item);


        List<Cuenta> mCuentas = Cuenta.getCuentas();

//        for (int i = 0; i < mCuentas.size(); i++) {

        //    adapter.add(mCuentas.get(i).getNombre().toString());

        // }

        // if (adapter.getCount() != 0) {
        //    lView.setAdapter(adapter);
        // }

        builder.setView(viewInflated);


        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

        // Set up the buttons

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mUser = input.getText().toString();
                mContr = input2.getText().toString();
                //         mProv = spin1.getSelectedItem().toString();

                Cuenta cuenta = new Cuenta();

                cuenta.setApellido(mContr);
                cuenta.setNombre(mUser);
                cuenta.setProvider(mProv);

                Cuenta.add(cuenta);

                adapter.add(cuenta.getNombre() + " " + cuenta.getApellido());
                pacientes.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pacientes.invalidateViews();

                Cuenta.saveCuenta(SelecPacienteActivity.this, cuenta);

                try {


                } catch (Exception ex) {
                    Log.d("Fullfill", "Fullfill error" + ex.toString());
                }

                // callAdapter();
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
            ((ArrayAdapter<String>)pacientes.getAdapter()).clear();
            pacientes.invalidateViews();
            pacientes = new ListView(this);
        }

        Intent mainIntent = new Intent(SelecPacienteActivity.this, LoginActivity.class);
        SelecPacienteActivity.this.startActivity(mainIntent);
        SelecPacienteActivity.this.finish();
    }
}

