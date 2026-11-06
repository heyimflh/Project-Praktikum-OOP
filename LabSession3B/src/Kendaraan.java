import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

// Abstract Class: Superclass yang merepresentasikan entitas umum Kendaraan
// Mengimplementasikan interface Rentable
public abstract class Kendaraan implements Rentable {

    // Encapsulation: Semua atribut bersifat private
    private String idKendaraan;
    private String merek;
    private double hargaPerHari;
    private boolean tersedia;
    private String namaPenyewa;
    private int lamaSewa;

    // Constructor: Inisialisasi atribut superclass dengan validasi setter
    public Kendaraan(String idKendaraan, String merek, double hargaPerHari) {
        this.idKendaraan = idKendaraan;
        this.tersedia = true;
        this.namaPenyewa = "-";
        this.lamaSewa = 0;
        setMerek(merek);
        setHargaPerHari(hargaPerHari);
    }

    // Encapsulation: Getter untuk semua atribut
    public String getIdKendaraan() {
        return idKendaraan;
    }

    public String getMerek() {
        return merek;
    }

    public double getHargaPerHari() {
        return hargaPerHari;
    }

    public String getNamaPenyewa() {
        return namaPenyewa;
    }

    public int getLamaSewa() {
        return lamaSewa;
    }

    // Encapsulation: Setter hanya untuk merek dan hargaPerHari dengan validasi
    public void setMerek(String merek) {
        if (merek == null || merek.trim().isEmpty()) {
            System.out.println("Gagal   : Merek tidak boleh kosong. Perubahan ditolak.");
        } else {
            this.merek = merek;
        }
    }

    public void setHargaPerHari(double hargaPerHari) {
        if (hargaPerHari <= 0) {
            System.out.println("Gagal   : Harga harus lebih dari 0. Perubahan ditolak.");
        } else {
            this.hargaPerHari = hargaPerHari;
        }
    }

    // Abstract Method: Wajib diimplementasikan oleh setiap subclass konkret
    public abstract double hitungBiayaSewa(int hari);
    public abstract String getJenis();

    // Overloading: Menghitung biaya sewa dengan potongan diskon
    public double hitungBiayaSewa(int hari, double diskonPersen) {
        if (diskonPersen < 0 || diskonPersen > 100) {
            diskonPersen = 0;
        }
        double biayaDasar = hitungBiayaSewa(hari);
        return biayaDasar * (1.0 - (diskonPersen / 100.0));
    }

    // Overriding: Implementasi method dari interface Rentable
    @Override
    public boolean sewa(String namaPenyewa, int lamaHari) {
        if (!tersedia) {
            System.out.println("Gagal   : " + merek + " sedang disewa, tidak dapat disewa lagi.");
            return false;
        }
        if (lamaHari < 1) {
            System.out.println("Gagal   : Lama sewa minimal 1 hari.");
            return false;
        }
        this.tersedia = false;
        this.namaPenyewa = namaPenyewa;
        this.lamaSewa = lamaHari;
        System.out.println("Berhasil: " + merek + " disewa oleh " + namaPenyewa + " selama " + lamaHari + " hari.");
        return true;
    }

    // Overloading: Method sewa dengan default 1 hari
    public boolean sewa(String namaPenyewa) {
        return sewa(namaPenyewa, 1);
    }

    // Overriding: Implementasi method dari interface Rentable
    @Override
    public void kembalikan() {
        this.tersedia = true;
        this.namaPenyewa = "-";
        this.lamaSewa = 0;
        System.out.println(merek + " telah dikembalikan.");
    }

    // Overriding: Implementasi method dari interface Rentable
    @Override
    public boolean isTersedia() {
        return tersedia;
    }

    // Menampilkan informasi umum kendaraan
    public void tampilkanInfo() {
        System.out.printf("%-10s: %s%n", "ID", idKendaraan);
        System.out.printf("%-10s: %s%n", "Jenis", getJenis());
        System.out.printf("%-10s: %s%n", "Merek", merek);
        System.out.printf("%-10s: %s%n", "Harga/Hari", formatRupiah(hargaPerHari));
        System.out.printf("%-10s: %s%n", "Status", (tersedia ? "Tersedia" : "Disewa oleh " + namaPenyewa));
    }

    // Helper method statis untuk memformat nominal ke mata uang Rupiah
    public static String formatRupiah(double nominal) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,##0", symbols);
        return "Rp" + df.format((long) nominal);
    }
}

