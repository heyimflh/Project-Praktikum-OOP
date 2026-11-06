// Inheritance: Bus mewarisi class Kendaraan
public class Bus extends Kendaraan {

    // Encapsulation: Atribut khusus Bus bersifat private
    private int kapasitasPenumpang;

    // Konstanta biaya sopir per hari
    private static final double BIAYA_SOPIR_PER_HARI = 300000;

    // Constructor: Memanggil super constructor
    public Bus(String idKendaraan, String merek, double hargaPerHari, int kapasitasPenumpang) {
        super(idKendaraan, merek, hargaPerHari);
        setKapasitasPenumpang(kapasitasPenumpang);
    }

    // Encapsulation: Getter dan Setter dengan validasi
    public int getKapasitasPenumpang() {
        return kapasitasPenumpang;
    }

    public void setKapasitasPenumpang(int kapasitasPenumpang) {
        if (kapasitasPenumpang <= 0) {
            System.out.println("Gagal   : Kapasitas penumpang harus lebih dari 0. Perubahan ditolak.");
        } else {
            this.kapasitasPenumpang = kapasitasPenumpang;
        }
    }

    // Overriding: Implementasi abstract method getJenis()
    @Override
    public String getJenis() {
        return "Bus";
    }

    // Overriding: Implementasi abstract method hitungBiayaSewa() sesuai aturan bisnis Bus
    // Mengakses harga sewa via getHargaPerHari() untuk menjaga enkapsulasi
    @Override
    public double hitungBiayaSewa(int hari) {
        return (getHargaPerHari() * hari) + (BIAYA_SOPIR_PER_HARI * hari);
    }

    // Overriding: Menampilkan info lengkap Bus
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.printf("%-10s: %d penumpang%n", "Kapasitas", kapasitasPenumpang);
        System.out.println("----------------------------------------------");
    }
}

