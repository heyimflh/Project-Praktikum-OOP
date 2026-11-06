// Inheritance: Mobil mewarisi class Kendaraan
public class Mobil extends Kendaraan {

    // Encapsulation: Atribut khusus Mobil bersifat private
    private int jumlahPintu;

    // Konstanta biaya asuransi tetap
    private static final double ASURANSI = 50000;

    // Constructor: Memanggil super constructor
    public Mobil(String idKendaraan, String merek, double hargaPerHari, int jumlahPintu) {
        super(idKendaraan, merek, hargaPerHari);
        setJumlahPintu(jumlahPintu);
    }

    // Encapsulation: Getter dan Setter dengan validasi
    public int getJumlahPintu() {
        return jumlahPintu;
    }

    public void setJumlahPintu(int jumlahPintu) {
        if (jumlahPintu <= 0) {
            System.out.println("Gagal   : Jumlah pintu harus lebih dari 0. Perubahan ditolak.");
        } else {
            this.jumlahPintu = jumlahPintu;
        }
    }

    // Overriding: Implementasi abstract method getJenis()
    @Override
    public String getJenis() {
        return "Mobil";
    }

    // Overriding: Implementasi abstract method hitungBiayaSewa() sesuai aturan bisnis Mobil
    // Mengakses harga sewa via getHargaPerHari() untuk menjaga enkapsulasi
    @Override
    public double hitungBiayaSewa(int hari) {
        return (getHargaPerHari() * hari) + ASURANSI;
    }

    // Overriding: Menampilkan info lengkap Mobil
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.printf("%-10s: %d%n", "Jml Pintu", jumlahPintu);
        System.out.println("----------------------------------------------");
    }
}

