package com.pratham.chikitse.ui.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pratham.chikitse.R;
import com.pratham.chikitse.models.Step;

public class StepFragment extends Fragment {

    private static final String ARG_STEP_NUMBER = "step_number";
    private static final String ARG_TITLE = "title";
    private static final String ARG_TITLE_KN = "title_kn";
    private static final String ARG_INSTRUCTION = "instruction";
    private static final String ARG_INSTRUCTION_KN = "instruction_kn";
    private static final String ARG_DO = "do_text";
    private static final String ARG_DO_KN = "do_text_kn";
    private static final String ARG_DONT = "dont_text";
    private static final String ARG_DONT_KN = "dont_text_kn";

    public static StepFragment newInstance(Step step) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STEP_NUMBER, step.getStepNumber());
        args.putString(ARG_TITLE, step.getTitle());
        args.putString(ARG_TITLE_KN, step.getTitleKannada());
        args.putString(ARG_INSTRUCTION, step.getInstruction());
        args.putString(ARG_INSTRUCTION_KN, step.getInstructionKannada());
        args.putString(ARG_DO, step.getDoText());
        args.putString(ARG_DO_KN, step.getDoTextKannada());
        args.putString(ARG_DONT, step.getDontText());
        args.putString(ARG_DONT_KN, step.getDontTextKannada());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null) return;

        TextView tvStepNum = view.findViewById(R.id.tv_step_num);
        TextView tvTitle = view.findViewById(R.id.tv_step_title);
        TextView tvTitleKn = view.findViewById(R.id.tv_step_title_kn);
        TextView tvInstruction = view.findViewById(R.id.tv_instruction);
        TextView tvInstructionKn = view.findViewById(R.id.tv_instruction_kn);
        TextView tvDo = view.findViewById(R.id.tv_do);
        TextView tvDoKn = view.findViewById(R.id.tv_do_kn);
        TextView tvDont = view.findViewById(R.id.tv_dont);
        TextView tvDontKn = view.findViewById(R.id.tv_dont_kn);

        tvStepNum.setText(String.valueOf(args.getInt(ARG_STEP_NUMBER)));
        tvTitle.setText(args.getString(ARG_TITLE));
        tvTitleKn.setText(args.getString(ARG_TITLE_KN));
        tvInstruction.setText(args.getString(ARG_INSTRUCTION));
        tvInstructionKn.setText(args.getString(ARG_INSTRUCTION_KN));
        tvDo.setText("✓  " + args.getString(ARG_DO));
        tvDoKn.setText(args.getString(ARG_DO_KN));
        tvDont.setText("✗  " + args.getString(ARG_DONT));
        tvDontKn.setText(args.getString(ARG_DONT_KN));
    }
}
