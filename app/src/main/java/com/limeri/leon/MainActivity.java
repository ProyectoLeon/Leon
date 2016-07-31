package com.limeri.leon;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
            txtPaciente.setText(txtPaciente.getText().toString() + Cuenta.getSelectedCuenta().getNombreCompleto());
        }

        ActionBar AB = getSupportActionBar();
        AB.setTitle(txtPaciente.getText());

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
}
