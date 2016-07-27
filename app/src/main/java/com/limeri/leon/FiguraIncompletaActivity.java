package com.limeri.leon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class FiguraIncompletaActivity extends AppCompatActivity {


    ImageView Figura;
    static final double PORC_PUNTO_CORRECTO_Y = 0.51;
    static final double PORC_PUNTO_CORRECTO_X = 0.55;
    static final double PORC_COTA_Y = 0.072;
    static final double PORC_COTA_X = 0.08;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figura_incompleta);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent mainIntent = new Intent(FiguraIncompletaActivity.this, ExamenActivity.class);
        FiguraIncompletaActivity.this.startActivity(mainIntent);
        FiguraIncompletaActivity.this.finish();
    }
}
