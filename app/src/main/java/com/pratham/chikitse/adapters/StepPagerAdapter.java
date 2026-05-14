package com.pratham.chikitse.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pratham.chikitse.models.Step;
import com.pratham.chikitse.ui.steps.StepFragment;

import java.util.List;

public class StepPagerAdapter extends FragmentStateAdapter {

    private final List<Step> steps;

    public StepPagerAdapter(@NonNull FragmentActivity fa, List<Step> steps) {
        super(fa);
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepFragment createFragment(int position) {
        return StepFragment.newInstance(steps.get(position));
    }

    @Override
    public int getItemCount() { return steps.size(); }
}
