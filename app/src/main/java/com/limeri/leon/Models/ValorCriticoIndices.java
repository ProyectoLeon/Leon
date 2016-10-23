package com.limeri.leon.Models;

import com.limeri.leon.common.DataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LocalUser on 23/10/2016.
 */
public class ValorCriticoIndices {
    private String valorcritico;

    public String getValorcritico() {
        return valorcritico;
    }

    public void setValorcritico(String valorcritico) {
        this.valorcritico = valorcritico;
    }

    public static ValorCriticoIndices getValorCritico(String nombre) {

        String string;
        String strValorCritico = DataBase.getValorCritico();

        ValorCriticoIndices valorCriticoIndices = null;

        if (!strValorCritico.equals("")){
            try{
                JSONObject jsonPuntCompuesta = new JSONObject(strValorCritico);
                valorCriticoIndices = new ValorCriticoIndices();

                String valor = jsonPuntCompuesta.get(nombre).toString();
                valorCriticoIndices.setValorcritico(valor);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return valorCriticoIndices;
    }



}
