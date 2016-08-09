package com.limeri.leon;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.limeri.leon.Models.Paciente;

public class ExamenActivity extends AppCompatActivity {
    Button buttonFiguraInc;
    Button buttonClaves;
    Button buttonInformacion;
    Button buttonMatrices;
    Button buttonSimbolos;
    Button buttonVocabulario;
    Button buttonAdivinanzas;
    Button buttonComprension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);

        buttonInformacion = (Button) findViewById(R.id.buttonInformacion);
        buttonMatrices = (Button) findViewById(R.id.buttonMatrices);
        buttonFiguraInc = (Button) findViewById(R.id.buttonFiguraInc);
        buttonClaves = (Button) findViewById(R.id.buttonClaves);
        buttonAdivinanzas = (Button) findViewById(R.id.buttonAdivinanzas);
        buttonSimbolos = (Button) findViewById(R.id.buttonSimbolos);
        buttonVocabulario = (Button) findViewById(R.id.buttonVocabulario);
        buttonComprension = (Button) findViewById(R.id.buttonComprension);

        ActionBar AB = getSupportActionBar();
        AB.setTitle(Paciente.getmSelectedPaciente().getNombreCompleto());

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


        buttonFiguraInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ExamenActivity.this, FiguraIncompletaActivity.class);
                ExamenActivity.this.startActivity(mainIntent);
                ExamenActivity.this.finish();
            }
        });

        buttonClaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonSimbolos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ExamenActivity.this, BqSimbolosActivity.class);
                ExamenActivity.this.startActivity(mainIntent);
                ExamenActivity.this.finish();

            }
        });

        buttonVocabulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ExamenActivity.this, VocabularioActivity.class);
                ExamenActivity.this.startActivity(mainIntent);
                ExamenActivity.this.finish();

            }
        });

        buttonComprension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ExamenActivity.this, ComprensionActivity.class);
                ExamenActivity.this.startActivity(mainIntent);
                ExamenActivity.this.finish();

            }
        });

        buttonAdivinanzas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ExamenActivity.this, AdivinanzasActivity.class);
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