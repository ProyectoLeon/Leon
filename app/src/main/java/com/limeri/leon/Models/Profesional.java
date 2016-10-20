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

    /*public static Profesional getSavedProfesional(Activity activity, String id) {

        Gson gson = new Gson();

        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        if (s != null) {

            //   for (int i = 0; i < myStrings.size(); i++) {
            Iterator iter = s.iterator();

            while (iter.hasNext()) {

                Profesional profesionalGuardado = (gson.fromJson(iter.next().toString(), Profesional.class));

                if (profesionalGuardado.getMatricula() != null && profesionalGuardado.getMatricula().equals(id)) {

                   return profesionalGuardado;


                } else {

                }

            }


        }


        return null;
    }
*/
    /*
        public static void borrarCuentas(Activity activity) {

            Gson gson = new Gson();
            // String cuentas = gson.toJson(cuenta);

            SharedPreferences prefs = activity.getSharedPreferences("Usuer", Context.MODE_PRIVATE);

            Set<String> s = new HashSet<String>(prefs.getStringSet("User", new HashSet<String>()));

            s.clear();

            SharedPreferences.Editor edit = prefs.edit();


            edit.putStringSet("User", s);
            edit.apply();
            edit.commit();
        }



        public static void loadCuentas(Activity activity) {
            SharedPreferences prefs = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
            Set<String> myStrings = prefs.getStringSet("User", null);

            Gson gson = new Gson();

            if (myStrings != null) {

                //   for (int i = 0; i < myStrings.size(); i++) {
                Iterator iter = myStrings.iterator();

                while (iter.hasNext()) {

                    Paciente.agregarPaciente(gson.fromJson(iter.next().toString(), Paciente.class));

                }


            }


        }

*/

    private static Profesional mSelectedProfesional;

    public static Profesional getProfesionalActual() {
        return mSelectedProfesional;
    }

    public static void setProfesionalActual(Profesional profesional) {
        mSelectedProfesional = profesional;
    }

    public static void saveProfesional(Profesional profesional) {

/*
        Gson gson = new Gson();
        String cuentas = gson.toJson(profesional);

        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);

        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        s.add(cuentas);

        SharedPreferences.Editor edit = prefs.edit();


        edit.putStringSet("Profesional", s);
        edit.apply();
        edit.commit();
*/

        DataBase.saveProfesional(profesional);
    }

/*
    public static void borrarCuentaBase(Activity activity, Profesional profesional) {


        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        Gson gson = new Gson();

        if (s != null) {

            //   for (int i = 0; i < myStrings.size(); i++) {
            Iterator iter = s.iterator();

            while (iter.hasNext()) {

                Profesional profesionalGuardado = (gson.fromJson(iter.next().toString(), Profesional.class));

                if (profesionalGuardado.getMatricula() != null && profesionalGuardado.getMatricula().equals(profesional.getMatricula())) {

                    iter.remove();
                    break;

                } else {

                }

            }


        }

        SharedPreferences.Editor edit = prefs.edit();

        edit.clear();
        edit.apply();
        edit.commit();
        edit.putStringSet("Profesional", s);
        edit.apply();
        edit.commit();


    }

    public static boolean existeMatricula(Activity activity, String matricula) {
*/
/*
        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        Gson gson = new Gson();

        boolean existe = false;
        for (String prof : s) {
            Profesional profesional = gson.fromJson(prof, Profesional.class);
            if (profesional.getMatricula().equals(matricula)) {
                existe = true;
                break;
            }
        }
*//*

        String strProfesional = DataBase.getProfesionalActual(matricula);
        return !strProfesional.equals("");
    }

    public static boolean isMatriculaRegistrada(Activity activity, String matricula) {
        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        Gson gson = new Gson();

        boolean registrada = false;
        for (String prof : s) {
            Profesional profesional = gson.fromJson(prof, Profesional.class);
            if (profesional.getMatricula().equals(matricula) && profesional.isRegistrado()) {
                registrada = true;
                break;
            }
        }
        return registrada;
    }

    public static String getProductoMatricula(Activity activity, String matricula) {
        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        Gson gson = new Gson();

        String producto = "";
        for (String prof : s) {
            Profesional profesional = gson.fromJson(prof, Profesional.class);
            if (profesional.getMatricula().equals(matricula)) {
                producto = profesional.getProducto();
                break;
            }
        }
        return producto;
    }

    public static List<Profesional> getProfesionales(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        Gson gson = new Gson();

        List<Profesional> profs = new ArrayList<>();
        for (String prof : s) {
            Profesional profesional = gson.fromJson(prof, Profesional.class);
            profs.add(profesional);
        }
        return profs;
    }
*/

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

    /**
        public static void borrarCuenta(Paciente paciente) {

            if (mPacientes != null) {
                for (int i = 0; i < mPacientes.size(); i++) {

                    if (paciente.getDni().equals(mPacientes.get(i).getDni())) {
                        mPacientes.remove(i);

                    }
                }


            }
        }

        public static void borrarSelectedPaciente() {

            mSelectedPaciente = null;

        }
    }
 **/
}
