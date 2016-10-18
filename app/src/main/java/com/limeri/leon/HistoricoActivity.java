package com.limeri.leon;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.HistoricoAdapter;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import java.util.ArrayList;
import java.util.List;

public class HistoricoActivity extends AppCompatActivity {

    Paciente paciente;
    HistoricoAdapter historicoAdapter;
    List<Evaluacion> evaluaciones;
    ListView listView;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_historico);

            listView = (ListView) findViewById(R.id.listview);

            AdministradorJuegos.setContext(getApplicationContext());
            paciente = Paciente.getSelectedPaciente();

            ActionBar AB = getSupportActionBar();
            if (AB != null) {
                AB.setTitle("Histórico " + paciente.getNombreCompleto());
            }

            // creamos nuestra coleccion de datos
            evaluaciones = new ArrayList<Evaluacion>();
            evaluaciones = paciente.getEvaluaciones();

            // creamos el listado
            historicoAdapter = new HistoricoAdapter(this, evaluaciones);

            // establecemos el adaptador en la lista
            listView.setAdapter(historicoAdapter);

        }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(this, ResumenActivity.class);
    }
}
