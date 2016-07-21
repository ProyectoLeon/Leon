package com.limeri.leon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExamenActivity extends AppCompatActivity {
    Button buttonFiguraInc;
    Button buttonClaves;
    Button buttonInformacion;
    Button buttonMatrices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);

        buttonInformacion = (Button) findViewById(R.id.buttonInformacion);
        buttonMatrices = (Button) findViewById(R.id.buttonMatrices);

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


    }
}