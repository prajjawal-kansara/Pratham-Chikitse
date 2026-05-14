package com.pratham.chikitse.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.pratham.chikitse.R;
import com.pratham.chikitse.models.Hospital;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {

    private final List<Hospital> hospitals;

    public HospitalAdapter(List<Hospital> hospitals) {
        this.hospitals = hospitals;
    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hospital, parent, false);
        return new HospitalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
        holder.bind(hospitals.get(position), position + 1);
    }

    @Override
    public int getItemCount() { return hospitals.size(); }

    static class HospitalViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvRank;
        private final TextView tvName;
        private final TextView tvAddress;
        private final TextView tvDistance;
        private final TextView tvTime;
        private final TextView tvOpen;
        private final TextView tvPhone;
        private final MaterialButton btnCall;

        HospitalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tv_rank);
            tvName = itemView.findViewById(R.id.tv_hospital_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvOpen = itemView.findViewById(R.id.tv_open_status);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            btnCall = itemView.findViewById(R.id.btn_call);
        }

        void bind(Hospital hospital, int rank) {
            Context ctx = itemView.getContext();
            tvRank.setText(String.valueOf(rank));
            tvName.setText(hospital.getName());
            tvAddress.setText(hospital.getAddress());
            tvPhone.setText(hospital.getPhone());

            if (hospital.getDistanceKm() > 0) {
                tvDistance.setText(String.format("%.1f km", hospital.getDistanceKm()));
                tvTime.setText(hospital.getEstimatedMinutes() + " min");
            } else {
                tvDistance.setText("Nearby");
                tvTime.setText("~10 min");
            }

            tvOpen.setText(hospital.isOpen24x7() ? "Open 24/7" : "Check hours");
            tvOpen.setTextColor(ctx.getColor(
                    hospital.isOpen24x7() ? R.color.colorSuccess : R.color.colorWarning));

            btnCall.setOnClickListener(v -> {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL,
                            Uri.parse("tel:" + hospital.getPhone()));
                    ctx.startActivity(callIntent);
                } catch (Exception e) {
                    Toast.makeText(ctx, "Cannot make call", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
