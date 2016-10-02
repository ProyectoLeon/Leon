package com.limeri.leon.common;

import android.os.StrictMode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limeri.leon.Models.Paciente;
import com.limeri.leon.Models.Profesional;

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
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class DataBase {

    public static final String URL_DB = "https://leondb-13e75.firebaseio.com/";

    public static String getEntidad(String entidad) {
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

    public static String getPacientes(String matricula) {
        return getEntidad("profesionales/" + matricula + "/pacientes");
    }

    public static String getProfesional(String matricula) {
        return getEntidad("profesionales/" + matricula);
    }

    public static void savePacientes() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            Profesional profesional = Profesional.getProfesional();
            String matricula = profesional.getmMatricula();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Paciente>>() {}.getType();
            String jsonPaciente = gson.toJson(Paciente.getCuentas(),listType);

            saveEntidad(URL_DB + "profesionales/" + matricula + "/pacientes.json", jsonPaciente);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
