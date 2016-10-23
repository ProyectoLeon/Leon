package com.limeri.leon.Models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limeri.leon.common.DataBase;
import com.limeri.leon.common.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Profesional {

    private String nombre;
    private String correo;
    private String contrasena;
    private String matricula;
    private String producto;
    private boolean registrado;
    private List<Paciente> pacientes;
    private Login login;
    private boolean local;

    public static void loadCuentas() {
        Gson gson = new Gson();
        String jsonPacientes = DataBase.getPacientes(mSelectedProfesional.matricula);
        if (!jsonPacientes.equals("")) {
            Type listType = new TypeToken<ArrayList<Paciente>>() {}.getType();
            mSelectedProfesional.pacientes = gson.fromJson(jsonPacientes, listType);
        } else {
            mSelectedProfesional.pacientes = new ArrayList<>();
        }
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public boolean isRegistrado() {
        return registrado;
    }

    public void setRegistrado(boolean registrado) {
        this.registrado = registrado;
    }

    private static Profesional mSelectedProfesional;

    public static Profesional getProfesionalActual() {
        return mSelectedProfesional;
    }

    public static void setProfesionalActual(Profesional profesional) {
        mSelectedProfesional = profesional;
    }

    public static void saveProfesional(Profesional profesional) {
        DataBase.saveProfesional(profesional);
    }

    public static Profesional getProfesional(String matricula) {
        String strProfesional = DataBase.getProfesional(matricula);
        Profesional profesional = null;
        if (!strProfesional.equals("")) {
            try {
                JSONObject jsonProfesional = new JSONObject(strProfesional);
                profesional = new Profesional();
                profesional.setMatricula(jsonProfesional.getString("matricula"));
                profesional.setContrasena(jsonProfesional.getString("contrasena"));
                profesional.setRegistrado(jsonProfesional.getBoolean("registrado"));
                profesional.setProducto(jsonProfesional.getString("producto"));
                profesional.setNombre(jsonProfesional.getString("nombre"));
                profesional.setCorreo(jsonProfesional.getString("correo"));
                profesional.setLocal(jsonProfesional.getBoolean("local"));
                if (!jsonProfesional.isNull("login")){
                    Gson gson = new Gson();
                    profesional.setLogin(gson.fromJson(jsonProfesional.getJSONObject("login").toString(),Login.class));
                } else {
                    profesional.setLogin(new Login());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return profesional;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public static void cerrarSesionProfesionalActual() {
        DataBase.deleteProfesionalLogin();
        mSelectedProfesional = null;
    }
}
