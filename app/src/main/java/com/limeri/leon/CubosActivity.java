package com.limeri.leon;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.neuroph.contrib.imgrec.ImageRecognitionPlugin;
import org.neuroph.contrib.imgrec.image.Image;
import org.neuroph.contrib.imgrec.image.ImageFactory;
import org.neuroph.core.NeuralNetwork;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CubosActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PRIMER_NIVEL = 0;
    private ImageRecognitionPlugin imageRecognition;
    private File foto;
    private Uri uriImage;
    private String pathFoto;
    private int nivel;
    private int ultimoNivel;
    private List<String> listCubos;
    private ImageView imgCubo;
    private String respuesta;
    private Runnable redNeuronal = new Runnable() {
        public void run() {
            //Abro el archivo de la red neuronal
            InputStream is = getResources().openRawResource(R.raw.red_cubos);
            //Cargo la red a partir del archivo
            NeuralNetwork nnet = NeuralNetwork.load(is);
            //Obtengo el plugin de reconocimiento
            imageRecognition = (ImageRecognitionPlugin) nnet.getPlugin(ImageRecognitionPlugin.class);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cubos);

        //Busco los ImageView
        imgCubo = (ImageView) findViewById(R.id.cubo);

        // Agrego el menu y pongo el puntaje en 0
        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        //Seteo el metodo en el botón Reconocer
        View reconocer = findViewById(R.id.reconocer);
        if (reconocer != null) {
            reconocer.setOnClickListener(clickReconocer());
        }

        //Cargo la red neuronal para el reconocimiento de la imagen
        cargarRedNeuronal();

        inicializarVariables();
    }

    private void cargarRedNeuronal() {
        new Thread(null, redNeuronal, "dataLoader", 32000).start();
    }

    private View.OnClickListener clickReconocer() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                abrirCamara();
            }
        };
    }

    private void inicializarVariables() {
        cargarCubo();
    }

    private void cargarCubo() {

        if (nivel == PRIMER_NIVEL) {
            try {
                String jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.cubos));
                JSONObject jsonRootObject = new JSONObject(jsonString);
                JSONArray jsonFigurasArray = jsonRootObject.getJSONArray("cubos");
                listCubos = new ArrayList<>();
                if (jsonFigurasArray != null) {
                    int len = jsonFigurasArray.length();
                    for (int i = 0; i < len; i++) {
                        String cubo = jsonFigurasArray.get(i).toString();
                        listCubos.add(cubo);
                    }
                    ultimoNivel = len - 1;
                }
            } catch (JSONException e) {
                guardar();
            }
        }

        //Cargo el cubo al imageView
        respuesta = listCubos.get(nivel);
        imgCubo.setImageResource(getResources().getIdentifier(respuesta, "drawable", this.getPackageName()));
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }

    private void abrirCamara() {
        try {
            //Creo un File para la imagen a capturar
            foto = createImageFile();
            uriImage = Uri.fromFile(foto);
            //Creo el Intent de la cámara
            Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (camaraIntent.resolveActivity(getPackageManager()) != null) {
                //Indico que la imagen la guarde en el File creado
                camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
                startActivityForResult(camaraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file path
        pathFoto = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                //Obtengo la imagen a partir del path guardado
                Image image = ImageFactory.getImage(pathFoto);
                //Muestro el nombre de la imagen reconocida
                String imagen = "cubo" + reconocerImagen(image);
                //Elimino el File donde se guardó la imagen
                foto.delete();

                if(imagen.equals(respuesta)) {
                    Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT);
                    AdministradorJuegos.getInstance().sumarPuntos(1);
                    cargarSiguienteNivel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cargarSiguienteNivel() {
        inicializarVariables();
    }

    private String reconocerImagen(Image image) {
        //Reconozco la imagen
        HashMap<String, Double> output = imageRecognition.recognizeImage(image);
        //Devuelvo el nombre de la imagen reconocida
        return getAnswer(output);
    }

    private String getAnswer(HashMap<String, Double> output) {
        double highest = 0;
        String answer = "";
        for (Map.Entry<String, Double> entry : output.entrySet()) {
            if (entry.getValue() > highest) {
                highest = entry.getValue();
                answer = entry.getKey();
            }
        }
        return answer;
    }
}
