package com.pedrox.whatsclone.helper;

import android.app.Activity;
import android.content.SharedPreferences;

public class MyPreferences {

    public static String PREFERENCIASLOCAIS="PREFERENCIASWHATSCLONE";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Activity activity;

    public MyPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.editor=sharedPreferences.edit();
    }

    public MyPreferences(Activity activity) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences(PREFERENCIASLOCAIS,0);
        this.editor=sharedPreferences.edit();
    }

    public boolean salvarPreferencia(String key, String value){
        try{
        editor.putString(key,value);
        editor.commit();
        return true;}
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String recuperarPreferencia(String key){
        try{
            String value=sharedPreferences.getString(key,null);
        return value;}
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
