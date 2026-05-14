package com.pratham.chikitse.models;

public class Hospital {
    private String name;
    private String nameKannada;
    private String address;
    private String addressKannada;
    private String phone;
    private double latitude;
    private double longitude;
    private boolean isOpen24x7;
    private String type;  // "government", "private", "trauma"

    public Hospital() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNameKannada() { return nameKannada; }
    public void setNameKannada(String nameKannada) { this.nameKannada = nameKannada; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAddressKannada() { return addressKannada; }
    public void setAddressKannada(String addressKannada) { this.addressKannada = addressKannada; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public boolean isOpen24x7() { return isOpen24x7; }
    public void setOpen24x7(boolean open24x7) { isOpen24x7 = open24x7; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    // Calculated at runtime — not stored in JSON
    private double distanceKm;
    private int estimatedMinutes;

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public int getEstimatedMinutes() { return estimatedMinutes; }
    public void setEstimatedMinutes(int estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }
}
