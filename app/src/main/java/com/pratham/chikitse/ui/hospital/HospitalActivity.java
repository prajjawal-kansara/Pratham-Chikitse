package com.pratham.chikitse.ui.hospital;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.pratham.chikitse.R;
import com.pratham.chikitse.adapters.HospitalAdapter;
import com.pratham.chikitse.data.DataManager;
import com.pratham.chikitse.models.Hospital;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HospitalActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 102;
    private static final double DEFAULT_LAT = 12.9716;
    private static final double DEFAULT_LNG = 77.5946;

    private RecyclerView rvHospitals;
    private TextView tvLocationStatus;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        rvHospitals = findViewById(R.id.rv_hospitals);
        tvLocationStatus = findViewById(R.id.tv_location_status);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
        MaterialButton btnAmbulance = findViewById(R.id.btn_ambulance);
        btnAmbulance.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:108"));
            startActivity(i);
        });

        rvHospitals.setLayoutManager(new LinearLayoutManager(this));
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationAndLoad();
    }

    private void requestLocationAndLoad() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            loadHospitals(DEFAULT_LAT, DEFAULT_LNG);
        } else {
            getLastLocation();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;
        fusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, location -> {
                if (location != null) {
                    tvLocationStatus.setText("📍 Using your current location");
                    loadHospitals(location.getLatitude(), location.getLongitude());
                } else {
                    tvLocationStatus.setText("📍 Using Bengaluru default location");
                    loadHospitals(DEFAULT_LAT, DEFAULT_LNG);
                }
            })
            .addOnFailureListener(e -> {
                tvLocationStatus.setText("📍 Using Bengaluru default location");
                loadHospitals(DEFAULT_LAT, DEFAULT_LNG);
            });
    }

    private void loadHospitals(double userLat, double userLng) {
        List<Hospital> all = DataManager.getInstance().getHospitals();
        for (Hospital h : all) {
            double d = haversine(userLat, userLng, h.getLatitude(), h.getLongitude());
            h.setDistanceKm(d);
            h.setEstimatedMinutes((int) Math.ceil((d / 20.0) * 60));
        }
        List<Hospital> sorted = new ArrayList<>(all);
        Collections.sort(sorted, Comparator.comparingDouble(Hospital::getDistanceKm));
        rvHospitals.setAdapter(new HospitalAdapter(sorted.subList(0, Math.min(5, sorted.size()))));
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2)*Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2)*Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }

    @Override
    public void onRequestPermissionsResult(int rc, String[] perms, int[] results) {
        super.onRequestPermissionsResult(rc, perms, results);
        if (rc == REQUEST_LOCATION && results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED)
            getLastLocation();
    }
}
