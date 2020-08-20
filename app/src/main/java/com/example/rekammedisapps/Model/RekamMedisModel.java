package com.example.rekammedisapps.Model;

public class RekamMedisModel {
    private String idPasien;
    private String idPerawat;
    private String tanggalsekarang;
    private String namaPasien;
    private String namaPerawat;
    private String alamatPasien;
    private String keluhanPasien;
    private String imageURLPerawat;

    public RekamMedisModel(){
    }

    public RekamMedisModel(String idPasien, String idPerawat, String tanggalsekarang, String namaPasien, String namaPerawat, String alamatPasien, String keluhanPasien){
        this.idPasien = idPasien;
        this.idPerawat = idPerawat;
        this.tanggalsekarang = tanggalsekarang;
        this.namaPasien = namaPasien;
        this.namaPerawat = namaPerawat;
        this.alamatPasien = alamatPasien;
        this.keluhanPasien = keluhanPasien;
    }

    public String getIdPasien() {
        return idPasien;
    }

    public void setIdPasien(String idPasien) {
        this.idPasien = idPasien;
    }

    public String getKeluhanPasien() {
        return keluhanPasien;
    }

    public void setKeluhanPasien(String keluhanPasien) {
        this.keluhanPasien = keluhanPasien;
    }

    public String getIdPerawat() {
        return idPerawat;
    }

    public void setIdPerawat(String idPerawat) {
        this.idPerawat = idPerawat;
    }

    public String getTanggalsekarang() {
        return tanggalsekarang;
    }

    public void setTanggalsekarang(String tanggalsekarang) {
        this.tanggalsekarang = tanggalsekarang;
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public String getNamaPerawat() {
        return namaPerawat;
    }

    public void setNamaPerawat(String namaPerawat) {
        this.namaPerawat = namaPerawat;
    }

    public String getAlamatPasien() {
        return alamatPasien;
    }

    public void setAlamatPasien(String alamatPasien) {
        this.alamatPasien = alamatPasien;
    }

    public String getImageURLPerawat() {
        return imageURLPerawat;
    }

    public void setImageURLPerawat(String imageURLPerawat) {
        this.imageURLPerawat = imageURLPerawat;
    }
}
