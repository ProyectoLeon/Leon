package com.limeri.leon.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.limeri.leon.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public static void flush(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public static String getAuthToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString("access_token", null);
    }

    public static boolean getFirstTime(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return Boolean.valueOf(pref.getString("first_time", null));
    }

    public static void saveAuthToken(Context context, String accessToken) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString("access_token", accessToken);
        editor.commit();
    }

    public static void saveFirstTimr(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString("first_time", "true");
        editor.commit();
    }


    public static String getUserID(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_ID, "");
    }

    public static void saveUserID(Context context, String username) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(Constants.JSON_USER_ID, username);
        editor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_USERNAME, "");
    }

    public static void saveUserName(Context context, String username) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(Constants.JSON_USER_USERNAME, username);
        editor.commit();
    }

    public static String getUserFirstName(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_FIRST_NAME, "");
    }



    public static String getUserLastName(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_LAST_NAME, "");
    }

    public static String getUserEmail(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_EMAIL, "");
    }

    public static void saveUserEmail(Context context, String field) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(Constants.JSON_USER_EMAIL, field);
        editor.commit();
    }

    public static void setOcultarCaducados(Context context, Boolean field) {
        SharedPreferences pref = context.getSharedPreferences(User.getUserName(context), Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putBoolean(Constants.CADUCADOS, field);
        editor.commit();
    }

    public static boolean getOcultarCaducados(Context context) {
        SharedPreferences pref = context.getSharedPreferences(User.getUserName(context), Context.MODE_PRIVATE);
        return pref.getBoolean(Constants.CADUCADOS, true);
    }

    public static String getUserPhone(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_PHONE, "");
    }

    public static String getUserCellphone(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_CELLPHONE, "");
    }

    public static String getUserAddrStreet(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_ADDR_STREET, "");
    }

    public static String getUserAddrNumber(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_ADDR_NUMBER, "");
    }

    public static String getUserAddrExtra(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_ADDR_EXTRA, "");
    }

    public static String getUserAddrPostalCode(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_ADDR_POSTAL_CODE, "");
    }

    public static String getUserGeoCountry(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_GEO_COUNTRY, "");
    }

    public static String getUserGeoState(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_GEO_STATE, "");
    }

    public static String getUserGeoCity(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_GEO_CITY, "");
    }

    public static String getUserAddrCountry(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_ADDR_COUNTRY, "");
    }

    public static String getUserAddrState(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_ADDR_STATE, "");
    }

    public static String getUserAddrCity(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_ADDR_CITY, "");
    }

    public static String getUserPublicPhone(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_PUBLIC_PHONE, "");
    }

    public static String getUserPublicEmail(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_PUBLIC_EMAIL, "");
    }

    public static String getUserPublicGeo(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_PUBLIC_GEO, "");
    }

    public static String getUserAvatar(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString(Constants.JSON_USER_AVATAR, "");
    }

    public static void saveUserAvatar(Context context, String avatar) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(Constants.JSON_USER_AVATAR, avatar);
        editor.commit();
    }

    public static JSONObject getUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        try {
            Log.d("XXX", "Levantando User: " + pref.getString("user", null));
            JSONObject user = new JSONObject(pref.getString("user", null));
            return user;
        } catch (Exception e) {
            //ErrorsManager.log(e);
            return null;
        }
    }

    public static void saveUser(Context context, String userJson) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        Log.d("XXX", "Guardando User: " + userJson);
        editor.putString("user", userJson);

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_ID);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_ID, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_USERNAME);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_USERNAME, field);
            editor.commit();
        } catch (JSONException e) {

        }
        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_FIRST_NAME);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_FIRST_NAME, field);
            editor.commit();
        } catch (JSONException e) {

        }
        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_LAST_NAME);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_LAST_NAME, field);
            editor.commit();
        } catch (JSONException e) {

        }
        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_EMAIL);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_EMAIL, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_PHONE);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_PHONE, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_CELLPHONE);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_CELLPHONE, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_ADDR_STREET);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_ADDR_STREET, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_ADDR_NUMBER);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_ADDR_NUMBER, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_ADDR_EXTRA);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_ADDR_EXTRA, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_ADDR_POSTAL_CODE);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_ADDR_POSTAL_CODE, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_GEO_COUNTRY);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_GEO_COUNTRY, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_GEO_STATE);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_GEO_STATE, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_GEO_CITY);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_GEO_CITY, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_ADDR_COUNTRY);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_ADDR_COUNTRY, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_ADDR_STATE);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_ADDR_STATE, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_ADDR_CITY);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_ADDR_CITY, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_PUBLIC_PHONE);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_PUBLIC_PHONE, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_PUBLIC_EMAIL);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_PUBLIC_EMAIL, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String field = user.getString(Constants.JSON_USER_PUBLIC_GEO);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_PUBLIC_GEO, field);
            editor.commit();
        } catch (JSONException e) {

        }

        try {
            JSONObject user = new JSONObject(userJson);
            String avatar = user.getString(Constants.JSON_USER_AVATAR);
            editor = pref.edit();
            editor.putString(Constants.JSON_USER_AVATAR, avatar);
            editor.commit();
        } catch (JSONException e) {

        }
        editor.commit();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        return pref.getString("access_token", null) != null;
    }


}
