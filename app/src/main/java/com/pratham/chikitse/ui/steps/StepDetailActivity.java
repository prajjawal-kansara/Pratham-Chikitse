package com.pratham.chikitse.ui.steps;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.pratham.chikitse.R;
import com.pratham.chikitse.adapters.StepPagerAdapter;
import com.pratham.chikitse.data.DataManager;
import com.pratham.chikitse.data.TTSManager;
import com.pratham.chikitse.models.Emergency;
import com.pratham.chikitse.models.Step;

import java.util.List;

public class StepDetailActivity extends AppCompatActivity {

    public static final String EXTRA_EMERGENCY_ID = "emergency_id";

    private ViewPager2 viewPager;
    private StepPagerAdapter pagerAdapter;
    private TextView tvTitle;
    private TextView tvStepCounter;
    private TextView tvProgressText;
    private ProgressBar progressDots;
    private ImageButton btnAudio;
    private MaterialButton btnPrev;
    private MaterialButton btnNext;
    private View btnBack;

    private Emergency emergency;
    private List<Step> steps;
    private boolean audioEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        String emergencyId = getIntent().getStringExtra(EXTRA_EMERGENCY_ID);
        emergency = DataManager.getInstance().getEmergencyById(emergencyId);

        if (emergency == null) {
            finish();
            return;
        }

        steps = emergency.getSteps();
        initViews();
        setupViewPager();
        setupNavigation();
        setupAudioButton();
        updateUI(0);
    }

    private void initViews() {
        viewPager = findViewById(R.id.view_pager);
        tvTitle = findViewById(R.id.tv_emergency_title);
        tvStepCounter = findViewById(R.id.tv_step_counter);
        tvProgressText = findViewById(R.id.tv_progress_text);
        progressDots = findViewById(R.id.progress_bar_steps);
        btnAudio = findViewById(R.id.btn_audio);
        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        btnBack = findViewById(R.id.btn_back);

        tvTitle.setText(emergency.getIcon() + "  " + emergency.getName() + " — " + emergency.getNameKannada());
        progressDots.setMax(steps.size());
    }

    private void setupViewPager() {
        pagerAdapter = new StepPagerAdapter(this, steps);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateUI(position);
                if (audioEnabled) {
                    speakCurrentStep(position);
                }
            }
        });
    }

    private void setupNavigation() {
        btnBack.setOnClickListener(v -> onBackPressed());

        btnPrev.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current > 0) viewPager.setCurrentItem(current - 1, true);
        });

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < steps.size() - 1) {
                viewPager.setCurrentItem(current + 1, true);
            } else {
                // Last step — go back
                Toast.makeText(this, "All steps completed! ✓ ಎಲ್ಲ ಹಂತಗಳು ಪೂರ್ಣ!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void setupAudioButton() {
        btnAudio.setOnClickListener(v -> {
            audioEnabled = !audioEnabled;

            if (audioEnabled) {
                btnAudio.setImageResource(R.drawable.ic_volume_on);
                btnAudio.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                Toast.makeText(this, "Audio ON — ಆಡಿಯೋ ಆನ್", Toast.LENGTH_SHORT).show();
                speakCurrentStep(viewPager.getCurrentItem());
            } else {
                btnAudio.setImageResource(R.drawable.ic_volume_off);
                btnAudio.setBackgroundTintList(getColorStateList(R.color.colorSurface));
                TTSManager.getInstance().stop();
                Toast.makeText(this, "Audio OFF — ಆಡಿಯೋ ಆಫ್", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(int position) {
        progressDots.setProgress(position + 1);
        tvStepCounter.setText("Step " + (position + 1) + " of " + steps.size());
        tvProgressText.setText("ಹಂತ " + (position + 1) + " / " + steps.size());

        boolean isFirst = position == 0;
        boolean isLast = position == steps.size() - 1;

        btnPrev.setEnabled(!isFirst);
        btnPrev.setAlpha(isFirst ? 0.4f : 1.0f);
        btnNext.setText(isLast ? "✓ Done" : "Next →");
    }

    private void speakCurrentStep(int position) {
        if (position >= 0 && position < steps.size()) {
            Step step = steps.get(position);
            TTSManager.getInstance().speakStep(
                    step.getTitleKannada() + ". " + step.getInstructionKannada(),
                    step.getTitle() + ". " + step.getInstruction()
            );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TTSManager.getInstance().stop();
    }
}
