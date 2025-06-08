package Uas_Pbo;

/**
 * Kelas untuk representasi kendaraan Mobil
 * Extends dari kelas abstrak Kendaraan
 */
public class Mobil extends Kendaraan {
    // Konstanta tarif untuk mobil
    private static final long TARIF_PER_JAM = 5000;
    private static final long TARIF_PER_MENIT = TARIF_PER_JAM / 60;
    private static final long TARIF_MAX_PER_HARI = 30000;
    private static final long TARIF_INAP = 40000;
    private static final long TARIF_MINIMUM = 5000;
    
    /**
     * Constructor untuk Mobil
     * @param plat nomor plat mobil
     * @param jamMasuk waktu mobil masuk
     */
    public Mobil(String plat, String jamMasuk) {
        super(plat, jamMasuk);
    }
    
    /**
     * Implementasi method hitungTarif untuk mobil
     * @param durasiMenit durasi parkir dalam menit
     * @param jumlahHari jumlah hari parkir
     * @return tarif parkir yang harus dibayar
     */
    @Override
    public long hitungTarif(long durasiMenit, long jumlahHari) {
        long tarif;
        
        if (jumlahHari >= 1) {
            // Jika parkir lebih dari 1 hari, gunakan tarif inap
            tarif = TARIF_INAP * (jumlahHari + 1);
        } else {
            // Hitung tarif berdasarkan durasi menit
            tarif = durasiMenit * TARIF_PER_MENIT;
            
            // Batasi tarif maksimal per hari
            if (tarif > TARIF_MAX_PER_HARI) {
                tarif = TARIF_MAX_PER_HARI;
            }
            
            // Tarif minimum
            if (tarif < TARIF_MINIMUM) {
                tarif = TARIF_MINIMUM;
            }
        }
        
        return tarif;
    }
    
    /**
     * Mendapatkan jenis kendaraan
     * @return string "Mobil"
     */
    public String getJenis() {
        return "Mobil";
    }
    
    /**
     * Mendapatkan informasi tarif mobil
     * @return string informasi tarif
     */
    public static String getInfoTarif() {
        return String.format(
            "Tarif Mobil:\n" +
            "- Per jam: Rp %,d\n" +
            "- Maksimal per hari: Rp %,d\n" +
            "- Tarif inap: Rp %,d\n" +
            "- Minimum: Rp %,d",
            TARIF_PER_JAM, TARIF_MAX_PER_HARI, TARIF_INAP, TARIF_MINIMUM
        );
    }
}