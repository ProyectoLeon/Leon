package com.limeri.leon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.limeri.leon.Models.Cuenta;


public class MainActivity extends AppCompatActivity {

    Button buttonTest;
    Button buttonJuegos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtPaciente = (TextView) findViewById(R.id.txtPaciente);

        if(Cuenta.getSelectedCuenta() != null) {
            txtPaciente.setText(txtPaciente.getText().toString() + " " + Cuenta.getSelectedCuenta().getUsuario() + " " + Cuenta.getSelectedCuenta().getContraseña());
        }

        buttonTest = (Button) findViewById(R.id.buttonTest);
        buttonJuegos = (Button) findViewById(R.id.buttonJuegos);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, ExamenActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent mainIntent = new Intent(MainActivity.this, SelecPacienteActivity.class);
        MainActivity.this.startActivity(mainIntent);
        MainActivity.this.finish();
    }
}
