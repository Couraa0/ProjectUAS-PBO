package Uas_Pbo;

/**
 * Kelas abstrak untuk representasi kendaraan
 */
public abstract class Kendaraan {
    protected String plat;
    protected String jamMasuk;
    
    /**
     * Constructor untuk Kendaraan
     * @param plat nomor plat kendaraan
     * @param jamMasuk waktu kendaraan masuk
     */
    public Kendaraan(String plat, String jamMasuk) {
        this.plat = plat;
        this.jamMasuk = jamMasuk;
    }
    
    /**
     * Method abstrak untuk menghitung tarif parkir
     * @param durasiMenit durasi parkir dalam menit
     * @param jumlahHari jumlah hari parkir
     * @return tarif parkir yang harus dibayar
     */
    public abstract long hitungTarif(long durasiMenit, long jumlahHari);
    
    // Getter methods
    public String getPlat() {
        return plat;
    }
    
    public String getJamMasuk() {
        return jamMasuk;
    }
    
    // Setter methods
    public void setPlat(String plat) {
        this.plat = plat;
    }
    
    public void setJamMasuk(String jamMasuk) {
        this.jamMasuk = jamMasuk;
    }
}