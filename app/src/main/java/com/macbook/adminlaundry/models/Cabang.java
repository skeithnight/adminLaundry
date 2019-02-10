package com.macbook.adminlaundry.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cabang {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("alamat")
    @Expose
    private String alamat;

    public Cabang(String id, String nama, Double latitude, Double longitude, String alamat) {
        this.id = id;
        this.nama = nama;
        this.latitude = latitude;
        this.longitude = longitude;
        this.alamat = alamat;
    }

    public Cabang(String nama, Double latitude, Double longitude, String alamat) {
        this.nama = nama;
        this.latitude = latitude;
        this.longitude = longitude;
        this.alamat = alamat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

}