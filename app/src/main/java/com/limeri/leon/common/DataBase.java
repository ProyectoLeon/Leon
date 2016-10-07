package com.limeri.leon.common;

import android.content.Context;
import android.os.StrictMode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limeri.leon.Models.Paciente;
import com.limeri.leon.Models.Profesional;
import com.limeri.leon.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

public class DataBase {

    public static final String URL_DB = "https://leondb-13e75.firebaseio.com/";
    private static Context applicationContext = null;

    public static void setContext(Context context) {
        applicationContext = context;
    }

    public static String getEntidad(String entidad) {
        Boolean jsonJuegosLocal = true;

        if (jsonJuegosLocal) {
            return getEntidadJuego(entidad);
        } else {
            return getEntidadDB(entidad);
        }
    }

    public static String getEntidadDB (String entidad) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        StringBuffer stringBuffer = new StringBuffer("");
        BufferedReader bufferedReader = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet();

            URI uri = new URI(URL_DB + entidad + ".json");
            httpGet.setURI(uri);
//            httpGet.addHeader(BasicScheme.authenticate(
//                    new UsernamePasswordCredentials("nicolas.benega@gmail.com", "Mon321654"),
//                    HTTP.UTF_8, false));

            HttpResponse httpResponse = httpClient.execute(httpGet);
            InputStream inputStream = httpResponse.getEntity().getContent();
            bufferedReader = new BufferedReader(new InputStreamReader(
                    inputStream));

            String readLine = bufferedReader.readLine();
            while (readLine != null && !readLine.equals("null")) {
                stringBuffer.append(readLine);
                stringBuffer.append("\n");
                readLine = bufferedReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuffer.toString();
    }

    public static String getEntidadJuego(String entidad) {
        String jsonString;
        JSONObject jsonRootObject;

        jsonString = JSONLoader.loadJSON(applicationContext.getResources().openRawResource(R.raw.leondb));
        try {
            jsonRootObject = new JSONObject(jsonString);
            //Get the instance of JSONArray that contains JSONObjects
            return jsonRootObject.getJSONArray(entidad).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPacientes(String matricula) {
        return getEntidadDB("profesionales/" + matricula + "/pacientes");
    }

    public static String getProfesional(String matricula) {
        return getEntidadDB("profesionales/" + matricula);
    }

    public static void savePacientes() {
        Profesional profesional = Profesional.getProfesionalActual();
        String matricula = profesional.getMatricula();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Paciente>>() {}.getType();
        String jsonPaciente = gson.toJson(Paciente.getCuentas(),listType);

        saveEntidad(URL_DB + "profesionales/" + matricula + "/pacientes.json", jsonPaciente);
    }

    private static void saveEntidad(String url, String datos) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPut httpPut= new HttpPut();
            URI uri = new URI(url);
            httpPut.setURI(uri);
            StringEntity entity = new StringEntity(datos, ContentType.APPLICATION_JSON);

            httpPut.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPut);
            HttpEntity entity1 = response.getEntity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveProfesional(Profesional profesional) {
        Gson gson = new Gson();
        Type listType = new TypeToken<Profesional>() {}.getType();
        String jsonProfesional = gson.toJson(profesional,listType);

        saveEntidad(URL_DB + "profesionales/" + profesional.getMatricula() + ".json", jsonProfesional);
    }
}
