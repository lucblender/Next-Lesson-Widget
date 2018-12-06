/*
Copyright © 2018, Lucas Bonvin

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the “Software”), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

The Software is provided “as is”, without warranty of any kind, express or implied, including but
not limited to the warranties of merchantability, fitness for a particular purpose and
noninfringement. In no event shall the authors or copyright holders be liable for any claim,
damages or other liability, whether in an action of contract, tort or otherwise, arising from,
out of or in connection with the software or the use or other dealings in the Software.

Except as contained in this notice, the name of Lucas Bonvin shall not be used in
advertising or otherwise to promote the sale, use or other dealings in this Software without
prior written authorization from Lucas Bonvin.
 */

package com.lucblender.lucasbonvin.widgettest.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CustomPreferencesManager {

    private static CustomPreferencesManager instance;

    private CustomPreferencesManager()
    {

    }

    public static CustomPreferencesManager getInstance() {
        if(instance==null)
            instance = new CustomPreferencesManager();
        return instance;
    }

    public void addCustomPreference(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = preferences.edit();
        Set<String> favoritePath = preferences.getStringSet("favoritePath",null);

        if(favoritePath == null) {
            editor.putStringSet("favoritePath", new LinkedHashSet<>() );
            editor.apply();
        }


        String widgetColorMap = preferences.getString("widgetColorMap",null);

        if(widgetColorMap == null) {
            editor.putString("widgetColorMap", "");
            editor.apply();
        }

        String widgetPathMap = preferences.getString("widgetPathMap",null);

        if(widgetPathMap == null) {
            editor.putString("widgetPathMap", "");
            editor.apply();
        }

        String widgetNextTextMap = preferences.getString("widgetNextTextMap",null);

        if(widgetNextTextMap == null) {
            editor.putString("widgetNextTextMap", "");
            editor.apply();
        }

        String widgetTextColortMap = preferences.getString("widgetTextColortMap",null);

        if(widgetNextTextMap == null) {
            editor.putString("widgetTextColortMap", "");
            editor.apply();
        }
    }

    public void addWidgetColor(Context context, int id, int color)
    {
        Map<String, String> myMap = loadMap(context, "widgetColorMap");
        myMap.put(String.valueOf(id), String.valueOf(color));
        saveMap(context, myMap, "widgetColorMap");
    }

    public void removeWidgetColor(Context context, int id)
    {
        Map<String, String> myMap = loadMap(context, "widgetColorMap");
        myMap.remove(String.valueOf(id));
        saveMap(context, myMap, "widgetColorMap");
    }

    public void addWidgetPathMap(Context context, int id, String path)
    {
        Map<String, String> myMap = loadMap(context, "widgetPathMap");
        myMap.put(String.valueOf(id), path);
        saveMap(context, myMap, "widgetPathMap");
    }

    public void removeWidgetPathMap(Context context, int id)
    {
        Map<String, String> myMap = loadMap(context, "widgetPathMap");
        myMap.remove(String.valueOf(id));
        saveMap(context, myMap, "widgetPathMap");
    }

    public void addWidgetNextText(Context context, int id, String text)
    {
        Map<String, String> myMap = loadMap(context, "widgetNextTextMap");
        myMap.put(String.valueOf(id), text);
        saveMap(context, myMap, "widgetNextTextMap");
    }

    public void removeWidgetnextText(Context context, int id)
    {
        Map<String, String> myMap = loadMap(context, "widgetNextTextMap");
        myMap.remove(String.valueOf(id));
        saveMap(context, myMap, "widgetNextTextMap");
    }

    public void addWidgetTextColor(Context context, int id, Boolean isBlack)
    {
        Map<String, String> myMap = loadMap(context, "widgetTextColortMap");
        myMap.put(String.valueOf(id), String.valueOf(isBlack));
        saveMap(context, myMap, "widgetTextColortMap");
    }

    public void removeWidgetTextColor(Context context, int id)
    {
        Map<String, String> myMap = loadMap(context, "widgetTextColortMap");
        myMap.remove(String.valueOf(id));
        saveMap(context, myMap, "widgetTextColortMap");
    }

    private void saveMap(Context context, Map<String,String> inputMap, String preferenceKey){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (preferences != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(preferenceKey).commit();
            editor.putString(preferenceKey, jsonString);
            editor.commit();
        }
    }

    public Map<String,String> loadMap(Context context, String preferenceKey){
        Map<String,String> outputMap = new HashMap<String,String>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        try{
            if (preferences != null){
                String jsonString = preferences.getString(preferenceKey, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
}
