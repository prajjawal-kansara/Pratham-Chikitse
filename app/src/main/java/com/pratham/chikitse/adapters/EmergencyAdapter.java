package com.pratham.chikitse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pratham.chikitse.R;
import com.pratham.chikitse.models.Emergency;

import java.util.ArrayList;
import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.EmergencyViewHolder> {

    public interface OnEmergencyClickListener {
        void onEmergencyClick(Emergency emergency);
    }

    private List<Emergency> emergencies;
    private final OnEmergencyClickListener listener;

    public EmergencyAdapter(List<Emergency> emergencies, OnEmergencyClickListener listener) {
        this.emergencies = new ArrayList<>(emergencies);
        this.listener = listener;
    }

    public void updateList(List<Emergency> newList) {
        this.emergencies = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmergencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_tile, parent, false);
        return new EmergencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyViewHolder holder, int position) {
        holder.bind(emergencies.get(position), listener);
    }

    @Override
    public int getItemCount() { return emergencies.size(); }

    static class EmergencyViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView tvIcon;
        private final TextView tvName;
        private final TextView tvNameKannada;
        private final View severityStrip;

        EmergencyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_emergency);
            tvIcon = itemView.findViewById(R.id.tv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNameKannada = itemView.findViewById(R.id.tv_name_kn);
            severityStrip = itemView.findViewById(R.id.view_severity_strip);
        }

        void bind(Emergency emergency, OnEmergencyClickListener listener) {
            Context ctx = itemView.getContext();
            tvIcon.setText(emergency.getIcon());
            tvName.setText(emergency.getName());
            tvNameKannada.setText(emergency.getNameKannada());

            // Color the severity strip
            int stripColor;
            switch (emergency.getSeverity()) {
                case "critical":
                    stripColor = ctx.getColor(R.color.colorCritical);
                    break;
                case "warning":
                    stripColor = ctx.getColor(R.color.colorWarning);
                    break;
                default:
                    stripColor = ctx.getColor(R.color.colorNormal);
                    break;
            }
            severityStrip.setBackgroundColor(stripColor);

            cardView.setOnClickListener(v -> listener.onEmergencyClick(emergency));

            // Ripple animation on click
            itemView.setOnClickListener(v -> listener.onEmergencyClick(emergency));
        }
    }
}
