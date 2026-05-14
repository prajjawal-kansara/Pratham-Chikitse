package com.pratham.chikitse.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.pratham.chikitse.R;
import com.pratham.chikitse.adapters.EmergencyAdapter;
import com.pratham.chikitse.data.DataManager;
import com.pratham.chikitse.data.TTSManager;
import com.pratham.chikitse.models.Emergency;
import com.pratham.chikitse.ui.hospital.HospitalActivity;
import com.pratham.chikitse.ui.steps.StepDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EmergencyAdapter.OnEmergencyClickListener {

    private static final int REQUEST_CALL_PERMISSION = 101;
    private static final String AMBULANCE_NUMBER = "108";

    private RecyclerView rvEmergencies;
    private EmergencyAdapter adapter;
    private List<Emergency> allEmergencies;
    private EditText etSearch;
    private View btnHospitals;
    private ExtendedFloatingActionButton fabSOS;
    private TextView tvOfflineBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupSearch();
        setupSOSButton();
        setupHospitalButton();
        initTTS();
    }

    private void initViews() {
        rvEmergencies = findViewById(R.id.rv_emergencies);
        etSearch = findViewById(R.id.et_search);
        btnHospitals = findViewById(R.id.btn_hospitals);
        fabSOS = findViewById(R.id.fab_sos);
        tvOfflineBadge = findViewById(R.id.tv_offline_badge);
    }

    private void setupRecyclerView() {
        allEmergencies = DataManager.getInstance().getEmergencies();
        adapter = new EmergencyAdapter(allEmergencies, this);
        rvEmergencies.setLayoutManager(new GridLayoutManager(this, 2));
        rvEmergencies.setAdapter(adapter);
        // Prevent nested scroll conflicts
        rvEmergencies.setNestedScrollingEnabled(false);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEmergencies(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void filterEmergencies(String query) {
        if (query.isEmpty()) {
            adapter.updateList(allEmergencies);
            return;
        }
        String lowerQuery = query.toLowerCase();
        List<Emergency> filtered = new ArrayList<>();
        for (Emergency e : allEmergencies) {
            if (e.getName().toLowerCase().contains(lowerQuery)
                    || e.getNameKannada().contains(query)) {
                filtered.add(e);
            }
        }
        adapter.updateList(filtered);
    }

    private void setupSOSButton() {
        fabSOS.setOnClickListener(v -> callAmbulance());
        fabSOS.setOnLongClickListener(v -> {
            Toast.makeText(this, "Calling 108 Ambulance...", Toast.LENGTH_SHORT).show();
            callAmbulance();
            return true;
        });
    }

    private void callAmbulance() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL,
                    Uri.parse("tel:" + AMBULANCE_NUMBER));
            startActivity(callIntent);
        }
    }

    private void setupHospitalButton() {
        btnHospitals.setOnClickListener(v -> {
            Intent intent = new Intent(this, HospitalActivity.class);
            startActivity(intent);
        });
    }

    private void initTTS() {
        TTSManager.getInstance().init(this, success -> {
            if (success) {
                // TTS ready — welcome message
                TTSManager.getInstance().speak("ಪ್ರಥಮ ಚಿಕಿತ್ಸೆ. ತುರ್ತು ಪ್ರಕಾರ ಆಯ್ಕೆ ಮಾಡಿ.");
            }
        });
    }

    @Override
    public void onEmergencyClick(Emergency emergency) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putExtra(StepDetailActivity.EXTRA_EMERGENCY_ID, emergency.getId());
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            callAmbulance();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TTSManager.getInstance().stop();
    }
}
