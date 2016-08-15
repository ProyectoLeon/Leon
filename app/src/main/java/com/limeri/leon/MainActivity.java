package com.limeri.leon;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;
import com.limeri.leon.Models.Profesional;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    String mNombre, mApellido, mDNI, mFechaNac, mProfPassword, mProfCorreo, mProfNombre, mProfMatricula;
    AlertDialog dialog;
    private TextView txtPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            txtPaciente = (TextView) findViewById(R.id.txtPaciente);
            Paciente paciente = Paciente.getSelectedPaciente();
            if(paciente != null) {
                actualizarNombrePaciente();
            }

            ActionBar AB = getSupportActionBar();
            if (AB != null) {
                AB.setTitle(txtPaciente.getText());
            }

            Button buttonTest = (Button) findViewById(R.id.buttonTest);
            if (buttonTest != null) {
                if (paciente.tieneEvaluacionIniciada()) {
                    buttonTest.setText("Continuar Evaluación");
                }
                buttonTest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navegacion.irA(MainActivity.this, InicioJuegoActivity.class);
                    }
                });
            }

            Button buttonJuegos = (Button) findViewById(R.id.buttonJuegos);
            if (buttonJuegos != null) {
                buttonJuegos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navegacion.irA(MainActivity.this, ExamenActivity.class);
                    }
                });
            }

            Button buttonEstadisticas = (Button) findViewById(R.id.buttonEstadisticas);
            if (buttonEstadisticas != null) {
                if (paciente.tieneEvaluacionFinalizada()) {
                    buttonEstadisticas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Navegacion.irA(MainActivity.this, ValorExamenActivity.class);
                        }
                    });
                } else {
                    buttonEstadisticas.setEnabled(false);
                }
            }

            Button buttonEditarPaciente = (Button) findViewById(R.id.buttonEditarPaciente);
            if (buttonEditarPaciente != null) {
                buttonEditarPaciente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog();
                    }
                });
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void actualizarNombrePaciente() {
        txtPaciente.setText("Paciente: " + Paciente.getSelectedPaciente().getNombreCompleto());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(MainActivity.this, SelecPacienteActivity.class);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.action_settings: {
                showDialog_Prof();
                break;
            }
            case R.id.action_paciente: {
                Navegacion.irA(MainActivity.this, SelecPacienteActivity.class);
                break;
            }
            case R.id.action_signout:{
                Navegacion.irA(MainActivity.this, LoginActivity.class);
                break;
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

        input.setText(Paciente.getSelectedPaciente().getNombre());
        input2.setText(Paciente.getSelectedPaciente().getApellido());
        input3.setText(Paciente.getSelectedPaciente().getDni());
        input4.setText(Paciente.getSelectedPaciente().getFechaNac());

        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mNombre = input.getText().toString();
                mApellido = input2.getText().toString();
                mDNI = input3.getText().toString();
                mFechaNac = input4.getText().toString();

                Paciente paciente = Paciente.getSelectedPaciente();

                paciente.setApellido(mApellido);
                paciente.setNombre(mNombre);
                paciente.setDni(mDNI);
                paciente.setFechaNac(mFechaNac);

                Paciente.saveCuenta(MainActivity.this, paciente);
                actualizarNombrePaciente();
            }
        });

        builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Paciente paciente = Paciente.getSelectedPaciente();
                Paciente.borrarCuenta(MainActivity.this, paciente);
                Paciente.borrarSelectedPaciente();
                dialog.cancel();
                Navegacion.irA(MainActivity.this, SelecPacienteActivity.class);
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
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
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
                Profesional.borrarCuentaBase(MainActivity.this, profesional);

                // Profesional profesional = new Profesional();
                profesional.setNombre(mProfNombre);
                profesional.setmCorreo(mProfCorreo);
                profesional.setmPassword(mProfPassword);
                profesional.setmMatricula(mProfMatricula);

                Profesional.saveProfesional(MainActivity.this, profesional);
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


