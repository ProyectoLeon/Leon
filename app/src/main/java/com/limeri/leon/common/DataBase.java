package com.limeri.leon.common;

import android.os.AsyncTask;
import android.os.StrictMode;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limeri.leon.Models.Paciente;
import com.limeri.leon.Models.Profesional;

import org.json.JSONArray;
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
import cz.msebera.android.httpclient.client.methods.HttpDelete;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class DataBase {

    private static final String URL_DB = "https://leondb-13e75.firebaseio.com/";
    private static String jsonDB;

    public static void loadDB() {
//        new AsyncTask<Integer, Void, Void>(){
//            @Override
//            protected Void doInBackground(Integer... params) {
                try {
                    JSONObject jsonObject = new JSONObject(getEntidadDB(""));
                    DataBase.setJsonDB(jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                return null;
//            }
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    public static boolean isLoaded() {
        return jsonDB != null;
    }

    public static String getJuego(String juego) {
        return getArray(juego);
    }

    private static String getEntidadDB (String entidad) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            String tokenString = getStringToken(user);
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
            return stringBuffer.toString();
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

    private static String getArray(String entidad) {
        try {
            JSONObject jsonObject = new JSONObject(jsonDB);
            JSONArray jsonArray = null;
            String[] subEntidades = entidad.split("/");
            for(int i=0; i < subEntidades.length; i++) {
                if(!subEntidades[i].equals("")) {
                    if(i == subEntidades.length - 1) {
                        jsonArray = jsonObject.getJSONArray(subEntidades[i]);
                    } else {
                        jsonObject = jsonObject.getJSONObject(subEntidades[i]);
                    }
                }
            }
            return jsonArray.toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getEntidad(String entidad) {
        try {
            JSONObject jsonObject = new JSONObject(jsonDB);
            for(String subEntidad : entidad.split("/")) {
                if(!subEntidad.equals("")) {
                    jsonObject = jsonObject.getJSONObject(subEntidad);
                }
            }
            return jsonObject.toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPacientes(String matricula) {
        return getArray("profesionales/" + matricula + "/pacientes");
    }

    public static String getProfesional(String matricula) {
        return getEntidad("profesionales/" + matricula);
    }

    public static String getPuntuacionCompuesta(String puntuacionCompuesta) {
        return getEntidad("puntuacionescompuestas/" + puntuacionCompuesta);
    }

    public static String getValorCritico() {
        return getEntidad("valorcritico");
    }

    public static void savePacientes() {
        Profesional profesional = Profesional.getProfesionalActual();
        String matricula = profesional.getMatricula();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Paciente>>() {}.getType();
        String jsonPaciente = gson.toJson(Paciente.getCuentas(),listType);

        saveEntidad("profesionales/" + matricula + "/pacientes", jsonPaciente);
    }

    private static void saveEntidad(final String entidad, final String datos) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String tokenString = getStringToken(user);
            if(isUniquelyLogin(tokenString)) {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                try {
                    new AsyncTask<Integer, Void, Void>(){
                        @Override
                        protected Void doInBackground(Integer... params) {
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
                            return null;
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
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

    public static String getProtocolo() {
        return getArray("protocolo");
    }

    public static void setJsonDB(String jsonDB) {
        DataBase.jsonDB = jsonDB;
    }

    public static void deleteProfesionalLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String tokenString = getStringToken(user);
            deleteUserLogin(tokenString);
        }
    }

    private static void deleteUserLogin(String token) {
        Profesional profesional = Profesional.getProfesionalActual();
        if(profesional != null) {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpDelete httpDelete = new HttpDelete();
                URI uri = new URI(URL_DB + "profesionales/" + profesional.getMatricula() + "/login.json?" + token);
                httpDelete.setURI(uri);
                httpClient.execute(httpDelete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
