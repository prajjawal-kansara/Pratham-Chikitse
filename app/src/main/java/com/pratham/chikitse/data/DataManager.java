package com.pratham.chikitse.data;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pratham.chikitse.R;
import com.pratham.chikitse.models.Emergency;
import com.pratham.chikitse.models.Hospital;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static DataManager instance;
    private List<Emergency> emergencies;
    private List<Hospital> hospitals;
    private final Gson gson = new Gson();

    private DataManager() {}

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    /** Call once at app start on a background thread */
    public void preloadData(Context context) {
        if (emergencies == null) {
            emergencies = loadEmergencies(context);
        }
        if (hospitals == null) {
            hospitals = loadHospitals(context);
        }
    }

    public List<Emergency> getEmergencies() {
        return emergencies != null ? emergencies : new ArrayList<>();
    }

    public List<Hospital> getHospitals() {
        return hospitals != null ? hospitals : new ArrayList<>();
    }

    public Emergency getEmergencyById(String id) {
        if (emergencies == null) return null;
        for (Emergency e : emergencies) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    private List<Emergency> loadEmergencies(Context context) {
        String json = readRawResource(context, R.raw.emergencies);
        if (json == null) return new ArrayList<>();
        JsonObject root = gson.fromJson(json, JsonObject.class);
        Type listType = new TypeToken<List<Emergency>>() {}.getType();
        return gson.fromJson(root.get("emergencies"), listType);
    }

    private List<Hospital> loadHospitals(Context context) {
        String json = readRawResource(context, R.raw.hospitals);
        if (json == null) return new ArrayList<>();
        JsonObject root = gson.fromJson(json, JsonObject.class);
        Type listType = new TypeToken<List<Hospital>>() {}.getType();
        return gson.fromJson(root.get("hospitals"), listType);
    }

    private String readRawResource(Context context, int resId) {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = context.getResources().openRawResource(resId);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }
}
