package com.mehmet.ingbank_interview_android_project.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehmet.ingbank_interview_android_project.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

//SharedPrefenrece was used to star the repository and keep it in the local area.
//For this reason, a class has been defined for SharedPrefenrece.
public class PrefrenceHelper
{
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

    public static void PrefrenceInit(Context context)
    {
        preferences = context.getSharedPreferences("shared", MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void SaveArrayList(ArrayList<String> list, String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.Current);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public static ArrayList<String> GetArrayList(String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.Current);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
}