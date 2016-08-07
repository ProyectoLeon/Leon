package com.limeri.leon;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.limeri.leon.Models.Paciente;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    Button buttonEditarPaciente;
    String mNombre, mApellido, mDNI, mFechaNac;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtPaciente = (TextView) findViewById(R.id.txtPaciente);

        if(Paciente.getSelectedCuenta() != null) {
            txtPaciente.setText(txtPaciente.getText().toString() + Paciente.getSelectedCuenta().getNombreCompleto());
        }

        ActionBar AB = getSupportActionBar();
        AB.setTitle(txtPaciente.getText());

        findViewById(R.id.buttonTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, InicioJuegoActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        });
        findViewById(R.id.buttonJuegos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, ExamenActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        });
        buttonEditarPaciente = (Button) findViewById(R.id.buttonEditarPaciente);
        buttonEditarPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent mainIntent = new Intent(MainActivity.this, SelecPacienteActivity.class);
        MainActivity.this.startActivity(mainIntent);
        MainActivity.this.finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.action_settings: {
                return true;
            }
            case R.id.action_paciente: {
                Intent mainIntent = new Intent(MainActivity.this, SelecPacienteActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
            case R.id.action_signout:{
                Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }

        return super.onOptionsItemSelected(item);
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
        final EditText input4 = (EditText) viewInflated.findViewById(R.id.inputFechaNac);

        input.setText(Paciente.getSelectedCuenta().getNombre());
        input2.setText(Paciente.getSelectedCuenta().getApellido());
        input3.setText(Paciente.getSelectedCuenta().getmDNI());
        input4.setText(Paciente.getSelectedCuenta().getmFechaNac());

        final List<Paciente> mPacientes = Paciente.getCuentas();

        builder.setView(viewInflated);


        // Set up the buttons

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mNombre = input.getText().toString();
                mApellido = input2.getText().toString();
                mDNI = input3.getText().toString();
                mFechaNac = input4.getText().toString();

                Paciente paciente = Paciente.getSelectedCuenta();
                Paciente.borrarCuentaBase(MainActivity.this,paciente);


                paciente.setApellido(mApellido);
                paciente.setNombre(mNombre);
                paciente.setmDNI(mDNI);
                paciente.setmFechaNac(mFechaNac);

                Paciente.saveCuenta(MainActivity.this, paciente);
                //GRABAR ACTUALIZACION O BORRAR Y VOLVER A CREAR

//                Paciente.saveCuenta(SelecPacienteActivity.this, paciente);

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
                Paciente paciente = Paciente.getSelectedCuenta();
                Paciente.borrarCuentaBase(MainActivity.this,paciente);
                Paciente.borrarCuenta(paciente);
                Paciente.borrarSelectedPaciente();
                dialog.cancel();
                Intent mainIntent = new Intent(MainActivity.this, SelecPacienteActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();

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
}
