package com.limeri.leon;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
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
    Button buttonSemejanzas;
    Button buttonAritmetica;
    Button buttonDigitos;
    Button buttonLetrasYNumeros;
    Button buttonAnimales;
    private Paciente paciente;

    //TODO: Que para los juegos libres, no se muestre la opción activar juego alternativo
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
        buttonSemejanzas = (Button) findViewById(R.id.buttonSemejanzas);
        buttonAritmetica = (Button) findViewById(R.id.buttonAritmetica);
        buttonDigitos = (Button) findViewById(R.id.buttonDigitos);
        buttonLetrasYNumeros = (Button) findViewById(R.id.buttonLetrasYNumeros);
        buttonAnimales = (Button) findViewById(R.id.buttonAnimales);

        AdministradorJuegos.setContext(getApplicationContext());
        paciente = Paciente.getSelectedPaciente();
        ActionBar AB = getSupportActionBar();
        if (AB != null) {
            AB.setTitle(paciente.getNombreCompleto());
        }
        paciente.agregarEvaluacion(new Evaluacion());

        buttonInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Información","","InformacionActivity",null));
                Navegacion.irA(ExamenActivity.this, InformacionActivity.class);
            }
        });

        buttonMatrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Matrices","","MatricesActivity",null));
                Navegacion.irA(ExamenActivity.this, MatricesActivity.class);
            }
        });


        buttonFiguraInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Figuras Incompletas","","FiguraIncompletaActivity",null));
                Navegacion.irA(ExamenActivity.this, FiguraIncompletaActivity.class);
            }
        });

        buttonSimbolos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Búsqueda de Símbolos","","BqSimbolosActivity",null));
                Navegacion.irA(ExamenActivity.this, BqSimbolosActivity.class);

            }
        });

        buttonVocabulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Vocabulario","","VocabularioActivity",null));
                Navegacion.irA(ExamenActivity.this, VocabularioActivity.class);

            }
        });

        buttonComprension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Comprensión","","ComprensionActivity",null));
                Navegacion.irA(ExamenActivity.this, ComprensionActivity.class);

            }
        });

        buttonAdivinanzas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Adivinanzas","","AdivinanzasActivity",null));
                Navegacion.irA(ExamenActivity.this, AdivinanzasActivity.class);

            }
        });

        buttonSemejanzas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Semejanzas","","SemejanzasActivity",null));
                Navegacion.irA(ExamenActivity.this, SemejanzasActivity.class);

            }
        });
        buttonClaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Claves","","ClavesActivity",null));
                Navegacion.irA(ExamenActivity.this, ClavesActivity.class);

            }
        });
        buttonAritmetica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Aritmetica","","AritmeticaActivity",null));
                Navegacion.irA(ExamenActivity.this, AritmeticaActivity.class);

            }
        });
        buttonDigitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Digitos","","DigitosActivity",null));
                Navegacion.irA(ExamenActivity.this, DigitosActivity.class);

            }
        });
        buttonLetrasYNumeros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Letras y Numeros","","LetrasYNumerosActivity",null));
                Navegacion.irA(ExamenActivity.this, LetrasYNumerosActivity.class);

            }
        });
        buttonAnimales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionActual().agregarJuego(new Juego("Animales","","AnimalesActivity",null));
                Navegacion.irA(ExamenActivity.this, AnimalesActivity.class);

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Navegacion.irA(ExamenActivity.this, MainActivity.class);
    }


}