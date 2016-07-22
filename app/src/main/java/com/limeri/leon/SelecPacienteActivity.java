package com.limeri.leon;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class SelecPacienteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selec_paciente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }}



 /*   private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.text_input_adobe_home, (ViewGroup) getView(), false);
        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        final EditText input2 = (EditText) viewInflated.findViewById(R.id.inputPassword);
        final Spinner spin1 = (Spinner) viewInflated.findViewById(R.id.inputProvider);


        final ListView lView = (ListView) viewInflated.findViewById(R.id.lvCuentas);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cuenta_item);


        List<Cuenta> mCuentas = Cuenta.getCuentas();

        for (int i = 0; i < mCuentas.size(); i++) {

            adapter.add(mCuentas.get(i).getUsuario().toString());

        }

        if (adapter.getCount() != 0) {
            lView.setAdapter(adapter);
        }

        builder.setView(viewInflated);


        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

        // Set up the buttons

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mostrarDialogo = true;
                nomostrar = true;
                if (clickedBook != null) {
                    clickedBook.setDescargado(false);
                    clickedBook.setDescargando(true);
                    clickedBook.setLeido(false);
                    clickedBook.setEstado("");
                    gvLibros.invalidateViews();
                }

                mUser = input.getText().toString();
                mContr = input2.getText().toString();
                mProv = spin1.getSelectedItem().toString();

                Cuenta cuenta = new Cuenta();

                cuenta.setContraseña(mContr);
                cuenta.setUsuario(mUser);
                cuenta.setProvider(mProv);

                try {

                    if (clickedBook != null) {
                        if (mProv.contains("BajaLibros")) {
                            GVReaderService.fulfill(getContext(), GVReaderService.Provider.BajaLibros, cuenta.getUsuario(), cuenta.getContraseña(), mDirectorio, clickedBook.getIdBook());

                        } else {

                            GVReaderService.fulfill(getContext(), GVReaderService.Provider.AdobeID, cuenta.getUsuario(), cuenta.getContraseña(), mDirectorio, clickedBook.getIdBook());

                        }
                    }

                } catch (Exception ex) {
                    Log.d("Fullfill", "Fullfill error" + ex.toString());
                }

                // callAdapter();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                cancelar = true;
                nomostrar = true;
                mostrarDialogo = true;
                if (clickedBook != null) {
                    clickedBook.setDescargado(false);
                    clickedBook.setLeido(false);
                    clickedBook.setDescargando(false);
                    clickedBook.setEstado("");
                    gvLibros.invalidateViews();
                }
            }
        });


        dialog = builder.create();


        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarDialogo = true;

                String selection = ((TextView) view.findViewById(R.id.tvLinea)).getText().toString();

                Cuenta cuenta = Cuenta.getCuentaByName(selection);

                if (cuenta != null) {
                    try {
                        if (cuenta.getmProvider().contains("BajaLibros")) {
                            GVReaderService.fulfill(getContext(), GVReaderService.Provider.BajaLibros, cuenta.getUsuario(), cuenta.getContraseña(), mDirectorio, clickedBook.getIdBook());

                        } else {

                            GVReaderService.fulfill(getContext(), GVReaderService.Provider.AdobeID, cuenta.getUsuario(), cuenta.getContraseña(), mDirectorio, clickedBook.getIdBook());

                        }

                    } catch (Exception ex) {
                        Log.d("Fullfill", "Fullfill error" + ex.toString());
                    }

                    dialog.dismiss();
                }

            }
        });


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
*/