// Inheritance: Motor mewarisi class Kendaraan
public class Motor extends Kendaraan {

    // Encapsulation: Atribut khusus Motor bersifat private
    private int kapasitasCC;

    // Konstanta biaya sewa helm per hari
    private static final double BIAYA_HELM_PER_HARI = 10000;

    // Constructor: Memanggil super constructor
    public Motor(String idKendaraan, String merek, double hargaPerHari, int kapasitasCC) {
        super(idKendaraan, merek, hargaPerHari);
        setKapasitasCC(kapasitasCC);
    }

    // Encapsulation: Getter dan Setter dengan validasi
    public int getKapasitasCC() {
        return kapasitasCC;
    }

    public void setKapasitasCC(int kapasitasCC) {
        if (kapasitasCC <= 0) {
            System.out.println("Gagal   : Kapasitas CC harus lebih dari 0. Perubahan ditolak.");
        } else {
            this.kapasitasCC = kapasitasCC;
        }
    }

    // Overriding: Implementasi abstract method getJenis()
    @Override
    public String getJenis() {
        return "Motor";
    }

    // Overriding: Implementasi abstract method hitungBiayaSewa() sesuai aturan bisnis Motor
    // Mengakses harga sewa via getHargaPerHari() untuk menjaga enkapsulasi
    @Override
    public double hitungBiayaSewa(int hari) {
        return (getHargaPerHari() * hari) + (BIAYA_HELM_PER_HARI * hari);
    }

    // Overriding: Menampilkan info lengkap Motor
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.printf("%-10s: %d cc%n", "Kapasitas", kapasitasCC);
        System.out.println("----------------------------------------------");
    }
}

