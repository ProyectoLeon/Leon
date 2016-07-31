package com.limeri.leon;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.limeri.leon.Models.Cuenta;

public class ExamenActivity extends AppCompatActivity {
    Button buttonFiguraInc;
    Button buttonClaves;
    Button buttonInformacion;
    Button buttonMatrices;
    Button buttonSimbolos;
    Button buttonVocabulario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);

        buttonInformacion = (Button) findViewById(R.id.buttonInformacion);
        buttonMatrices = (Button) findViewById(R.id.buttonMatrices);

        ActionBar AB = getSupportActionBar();
        AB.setTitle(Cuenta.getSelectedCuenta().getNombreCompleto());

        buttonInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ExamenActivity.this, InformacionActivity.class);
                ExamenActivity.this.startActivity(mainIntent);
                ExamenActivity.this.finish();
            }
        });

        buttonMatrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ExamenActivity.this, MatricesActivity.class);
                ExamenActivity.this.startActivity(mainIntent);
                ExamenActivity.this.finish();
            }
        });

        buttonFiguraInc = (Button) findViewById(R.id.buttonFiguraInc);
        buttonFiguraInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ExamenActivity.this, FiguraIncompletaActivity.class);
                ExamenActivity.this.startActivity(mainIntent);
                ExamenActivity.this.finish();
            }
        });

        buttonClaves = (Button) findViewById(R.id.buttonClaves);
        buttonClaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonSimbolos = (Button) findViewById(R.id.buttonSimbolos);
        buttonSimbolos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ExamenActivity.this, BqSimbolosActivity.class);
                ExamenActivity.this.startActivity(mainIntent);
                ExamenActivity.this.finish();

            }
        });

        buttonVocabulario = (Button) findViewById(R.id.buttonVocabulario);
        buttonVocabulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ExamenActivity.this, VocabularioActivity.class);
                ExamenActivity.this.startActivity(mainIntent);
                ExamenActivity.this.finish();

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent mainIntent = new Intent(ExamenActivity.this, MainActivity.class);
        ExamenActivity.this.startActivity(mainIntent);
        ExamenActivity.this.finish();
    }


}