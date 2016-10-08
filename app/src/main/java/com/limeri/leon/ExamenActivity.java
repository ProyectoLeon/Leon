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
    Button buttonCubos;
    Button buttonConceptos;
    Paciente paciente;

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
        buttonCubos = (Button) findViewById(R.id.buttonCubos);
        buttonConceptos = (Button) findViewById(R.id.buttonConceptos);

        AdministradorJuegos.setContext(getApplicationContext());
        paciente = Paciente.getSelectedPaciente();
        ActionBar AB = getSupportActionBar();
        if (AB != null) {
            AB.setTitle(paciente.getNombreCompleto());
        }

        buttonInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Información","Comprensión verbal","InformacionActivity",null,false));
                Navegacion.irA(ExamenActivity.this, InformacionActivity.class);
            }
        });

        buttonMatrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Matrices","Razonamiento Perceptivo","MatricesActivity",null,false));
                Navegacion.irA(ExamenActivity.this, MatricesActivity.class);
            }
        });


        buttonFiguraInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Completamiento de Figuras","Razonamiento Perceptivo","FiguraIncompletaActivity",null,false));
                Navegacion.irA(ExamenActivity.this, FiguraIncompletaActivity.class);
            }
        });

        buttonSimbolos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Búsqueda de Símbolos","Velocidad de Procesamiento","BqSimbolosActivity",null,false));
                Navegacion.irA(ExamenActivity.this, BqSimbolosActivity.class);

            }
        });

        buttonVocabulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Vocabulario","Comprensión verbal","VocabularioActivity",null,false));
                Navegacion.irA(ExamenActivity.this, VocabularioActivity.class);

            }
        });

        buttonComprension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Comprensión","Comprensión verbal","ComprensionActivity",null,false));
                Navegacion.irA(ExamenActivity.this, ComprensionActivity.class);

            }
        });

        buttonAdivinanzas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Adivinanzas","Comprensión verbal","AdivinanzasActivity",null,false));
                Navegacion.irA(ExamenActivity.this, AdivinanzasActivity.class);

            }
        });

        buttonSemejanzas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Semejanzas","Comprensión verbal","SemejanzasActivity",null,false));
                Navegacion.irA(ExamenActivity.this, SemejanzasActivity.class);

            }
        });
        buttonClaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Claves","Velocidad de Procesamiento","ClavesActivity",null,false));
                Navegacion.irA(ExamenActivity.this, ClavesActivity.class);

            }
        });
        buttonAritmetica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Aritmetica","Memoria de Trabajo","AritmeticaActivity",null,false));
                Navegacion.irA(ExamenActivity.this, AritmeticaActivity.class);

            }
        });
        buttonDigitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Retención de Digitos","Memoria de Trabajo","DigitosActivity",null,false));
                Navegacion.irA(ExamenActivity.this, DigitosActivity.class);

            }
        });
        buttonLetrasYNumeros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Letras y Números","Memoria de Trabajo","LetrasYNumerosActivity",null,false));
                Navegacion.irA(ExamenActivity.this, LetrasYNumerosActivity.class);

            }
        });
        buttonAnimales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Animales","Velocidad de Procesamiento","AnimalesActivity",null,false));
                Navegacion.irA(ExamenActivity.this, AnimalesActivity.class);

            }
        });
        buttonCubos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Construcción con Cubos","Razonamiento Perceptivo","CubosActivity",null,false));
                Navegacion.irA(ExamenActivity.this, CubosActivity.class);

            }
        });

        buttonConceptos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego("Coceptos","Razonamiento Perceptivo","ConceptosActivity",null,false));
                Navegacion.irA(ExamenActivity.this, ConceptosActivity.class);

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Navegacion.irA(ExamenActivity.this, MainActivity.class);
    }


}