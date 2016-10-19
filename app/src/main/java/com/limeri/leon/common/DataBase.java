package com.limeri.leon.common;

import android.os.StrictMode;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limeri.leon.Models.Paciente;
import com.limeri.leon.Models.Profesional;
import com.limeri.leon.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class DataBase {

    public static final String URL_DB = "https://leondb-13e75.firebaseio.com/";

    public static String getEntidad(String entidad) {
        Boolean jsonJuegosLocal = false;

        if (jsonJuegosLocal) {
            return getEntidadJuego(entidad);
        } else {
            return getEntidadDB(entidad);
        }
    }

    public static String getEntidadDB (String entidad) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String tokenString = getStringToken(user);
            if(isUniquelyLogin(tokenString)) {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                StringBuilder stringBuffer = new StringBuilder("");
                BufferedReader bufferedReader = null;
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet();

                    URI uri = new URI(URL_DB + entidad + ".json?" + tokenString);
                    httpGet.setURI(uri);

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
                setUserLogin(tokenString);
                return stringBuffer.toString();
            } else return null;
        } else {
            return null;
        }
    }

    private static String getStringToken(FirebaseUser user) {
        String token = user.getToken(false).getResult().getToken();
        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair("auth", token));
        return URLEncodedUtils.format(params, "utf-8");
    }

    private static boolean isUniquelyLogin(String token) {
        Profesional profesional = Profesional.getProfesionalActual();
        if (profesional != null) {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            StringBuilder stringBuffer = new StringBuilder("");
            BufferedReader bufferedReader = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet();

                URI uri = new URI(URL_DB + "profesionales/" + profesional.getMatricula() +"/login.json?" + token);
                httpGet.setURI(uri);

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
            String stringLogin = stringBuffer.toString();
            if (!stringLogin.equals("")) {
                Gson gson = new Gson();
                Login login = gson.fromJson(stringLogin, Login.class);
                Profesional.getProfesionalActual().setLogin(login);
                return Login.isLoginValido(login);
            } else {
                return false;
            }
        }
        else return true;
    }

    private static void setUserLogin(String token) {
        Profesional profesional = Profesional.getProfesionalActual();
        if(profesional != null) {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            try {
                Login login = profesional.getLogin();
                if (login == null) {
                    login = new Login();
                    profesional.setLogin(login);
                } else {
                    login.setDispositivo(Application.getDeviceId());
                    login.setTimeStamp(new Date());
                }
                Gson gson = new Gson();
                Type listType = new TypeToken<Login>() {}.getType();
                String stringLogin = gson.toJson(login, listType);

                HttpClient httpClient = new DefaultHttpClient();
                HttpPut httpPut = new HttpPut();
                URI uri = new URI(URL_DB + "profesionales/" + profesional.getMatricula() + "/login.json?" + token);
                httpPut.setURI(uri);
                StringEntity entity = new StringEntity(stringLogin, ContentType.APPLICATION_JSON);

                httpPut.setEntity(entity);
                httpClient.execute(httpPut);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setProfesionalLogin(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String tokenString = getStringToken(user);
            setUserLogin(tokenString);
        }
    }

    public static String getEntidadJuego(String entidad) {
        String jsonString;
        JSONObject jsonRootObject;

        jsonString = JSONLoader.loadJSON(Application.getApplicationContext().getResources().openRawResource(R.raw.leondb));
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

    public static String getpuntuacionCompuesta(String puntuacionCompuesta) {
        return getEntidadDB("puntuacionescompuestas/" + puntuacionCompuesta);
    }

    public static void savePacientes() {
        Profesional profesional = Profesional.getProfesionalActual();
        String matricula = profesional.getMatricula();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Paciente>>() {}.getType();
        String jsonPaciente = gson.toJson(Paciente.getCuentas(),listType);

        saveEntidad("profesionales/" + matricula + "/pacientes", jsonPaciente);
    }

    private static void saveEntidad(String entidad, String datos) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String tokenString = getStringToken(user);
            if(isUniquelyLogin(tokenString)) {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPut httpPut = new HttpPut();
                    URI uri = new URI(URL_DB + entidad + ".json?" + tokenString);
                    httpPut.setURI(uri);
                    StringEntity entity = new StringEntity(datos, ContentType.APPLICATION_JSON);

                    httpPut.setEntity(entity);
                    httpClient.execute(httpPut);
                    setUserLogin(tokenString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveProfesional(Profesional profesional) {
        Gson gson = new Gson();
        Type listType = new TypeToken<Profesional>() {}.getType();
        String jsonProfesional = gson.toJson(profesional,listType);

        saveEntidad("profesionales/" + profesional.getMatricula(), jsonProfesional);
    }
}
