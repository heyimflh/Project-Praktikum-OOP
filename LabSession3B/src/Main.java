import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Satu objek Scanner untuk seluruh program
        Scanner sc = new Scanner(System.in);
        boolean berjalan = true;

        while (berjalan) {
            System.out.println("==============================================");
            System.out.println("        RENTAGO - SISTEM RENTAL KENDARAAN");
            System.out.println("==============================================");
            System.out.println("1. Jalankan demo otomatis");
            System.out.println("2. Mode interaktif");
            System.out.println("0. Keluar");

            int pilihan = bacaInt(sc, "Pilih menu: ");
            switch (pilihan) {
                case 1:
                    // Data baru dibuat setiap kali demo dijalankan agar kondisi bersih
                    jalankanDemoOtomatis(buatDaftarKendaraan());
                    break;
                case 2:
                    // Data baru dibuat untuk sesi interaktif baru
                    jalankanModeInteraktif(buatDaftarKendaraan(), sc);
                    break;
                case 0:
                    System.out.println("Terima kasih, sampai jumpa!");
                    berjalan = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.\n");
            }
        }

        sc.close();
    }

    // Helper membuat 4 objek awal kendaraan (Inheritance & Constructor Chaining)
    public static ArrayList<Kendaraan> buatDaftarKendaraan() {
        ArrayList<Kendaraan> daftar = new ArrayList<>();
        daftar.add(new Mobil("MBL-001", "Toyota Avanza", 350000, 5));
        daftar.add(new Mobil("MBL-002", "Honda Brio", 300000, 5));
        daftar.add(new Motor("MTR-001", "Yamaha NMAX", 120000, 155));
        daftar.add(new Bus("BUS-001", "Hino Medium Bus", 1500000, 30));
        return daftar;
    }

    // Menjalankan demo otomatis 8 tahap (output identik dengan PRD Bagian 12)
    public static void jalankanDemoOtomatis(ArrayList<Kendaraan> daftarKendaraan) {
        System.out.println("==============================================");
        System.out.println("        RENTAGO - SISTEM RENTAL KENDARAAN");
        System.out.println("==============================================");
        System.out.println();

        Kendaraan mobil1 = daftarKendaraan.get(0);
        Kendaraan mobil2 = daftarKendaraan.get(1);
        Kendaraan motor1 = daftarKendaraan.get(2);
        Kendaraan bus1 = daftarKendaraan.get(3);

        // [1] DAFTAR KENDARAAN
        System.out.println("[1] DAFTAR KENDARAAN");
        System.out.println("----------------------------------------------");
        for (Kendaraan k : daftarKendaraan) {
            // Polymorphism & Overriding: tampilkanInfo() memanggil implementasi tiap subclass
            k.tampilkanInfo();
        }
        System.out.println();

        // [2] BIAYA SEWA 3 HARI (Runtime Polymorphism)
        System.out.println("[2] BIAYA SEWA 3 HARI (Runtime Polymorphism)");
        for (Kendaraan k : daftarKendaraan) {
            // Runtime Polymorphism: hitungBiayaSewa dieksekusi sesuai jenis objek asli saat runtime
            System.out.printf("%-5s - %-17s : %s%n", k.getJenis(), k.getMerek(), formatRupiah(k.hitungBiayaSewa(3)));
        }
        System.out.println();

        // [3] BIAYA DENGAN DISKON (Overloading)
        System.out.println("[3] BIAYA DENGAN DISKON (Overloading)");
        // Overloading: memanggil method hitungBiayaSewa(int hari, double diskonPersen)
        System.out.println(mobil1.getMerek() + " 3 hari, diskon 10% : " + formatRupiah(mobil1.hitungBiayaSewa(3, 10)));
        System.out.println();

        // [4] PROSES PENYEWAAN
        System.out.println("[4] PROSES PENYEWAAN");
        // Penyewaan normal
        mobil1.sewa("Fakhri", 3);
        // Penyewaan gagal karena kendaraan sedang disewa (Business Rule)
        mobil1.sewa("Budi");
        // Overloading: sewa(namaPenyewa) dengan default durasi 1 hari
        motor1.sewa("Sari");
        System.out.println();

        // [5] VALIDASI ENCAPSULATION
        System.out.println("[5] VALIDASI ENCAPSULATION");
        // Validasi setter harga: nilai <= 0 ditolak
        mobil2.setHargaPerHari(-5000);
        System.out.println();

        // [6] PENGEMBALIAN
        System.out.println("[6] PENGEMBALIAN");
        // Interface & Encapsulation: proses pengembalian kendaraan
        mobil1.kembalikan();
        System.out.println("Status " + mobil1.getMerek() + " tersedia? " + mobil1.isTersedia());
        System.out.println();

        // [7] AKSES VIA INTERFACE
        System.out.println("[7] AKSES VIA INTERFACE");
        // Polymorphism: Mengakses objek Bus melalui referensi interface Rentable
        Rentable r = bus1;
        System.out.println("Rentable r = bus1 -> tersedia? " + r.isTersedia());
        System.out.println();

        // [8] RINGKASAN STATUS AKHIR
        System.out.println("[8] RINGKASAN STATUS AKHIR");
        for (Kendaraan k : daftarKendaraan) {
            String status = k.isTersedia() ? "Tersedia" : "Disewa oleh " + k.getNamaPenyewa() + " (" + k.getLamaSewa() + " hari)";
            System.out.printf("%s | %-15s | %s%n", k.getIdKendaraan(), k.getMerek(), status);
        }
        System.out.println("==============================================");
        System.out.println();
    }

    // Loop Mode Interaktif
    public static void jalankanModeInteraktif(ArrayList<Kendaraan> daftar, Scanner sc) {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n--- MODE INTERAKTIF ---");
            System.out.println("1. Lihat daftar kendaraan");
            System.out.println("2. Hitung biaya sewa");
            System.out.println("3. Sewa kendaraan");
            System.out.println("4. Kembalikan kendaraan");
            System.out.println("5. Ubah harga sewa per hari");
            System.out.println("0. Kembali ke menu utama");

            int pilihan = bacaInt(sc, "Pilih menu: ");
            switch (pilihan) {
                case 1:
                    menuLihatDaftar(daftar);
                    break;
                case 2:
                    menuHitungBiaya(daftar, sc);
                    break;
                case 3:
                    menuSewaKendaraan(daftar, sc);
                    break;
                case 4:
                    menuKembalikanKendaraan(daftar, sc);
                    break;
                case 5:
                    menuUbahHarga(daftar, sc);
                    break;
                case 0:
                    System.out.println("Kembali ke menu utama...\n");
                    kembali = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // Menu Interaktif 1: Lihat daftar kendaraan
    private static void menuLihatDaftar(ArrayList<Kendaraan> daftar) {
        System.out.println("\n--- DAFTAR KENDARAAN ---");
        System.out.println("----------------------------------------------");
        // Runtime polymorphism: memanggil tampilkanInfo() sesuai subclass masing-masing
        for (Kendaraan k : daftar) {
            k.tampilkanInfo();
        }
    }

    // Menu Interaktif 2: Hitung biaya sewa (menunjukkan overloading hitungBiayaSewa)
    private static void menuHitungBiaya(ArrayList<Kendaraan> daftar, Scanner sc) {
        System.out.println("\n--- HITUNG BIAYA SEWA ---");
        for (Kendaraan k : daftar) {
            System.out.println(k.getIdKendaraan() + " | " + k.getJenis() + " | " + k.getMerek());
        }

        String id = bacaString(sc, "Masukkan ID kendaraan: ");
        Kendaraan k = cariKendaraan(daftar, id);
        if (k == null) {
            System.out.println("Kendaraan dengan ID tersebut tidak ditemukan.");
            return;
        }

        int hari = bacaInt(sc, "Masukkan lama sewa (hari): ");
        double diskon = bacaDouble(sc, "Masukkan diskon persen (boleh 0): ");

        double total;
        if (diskon > 0) {
            // Overloading: memanggil hitungBiayaSewa(int hari, double diskonPersen)
            total = k.hitungBiayaSewa(hari, diskon);
            String diskonStr = (diskon == (long) diskon) ? String.format("%d", (long) diskon) : String.valueOf(diskon);
            System.out.println("Total biaya sewa " + k.getMerek() + " (" + hari + " hari, diskon " + diskonStr + "%): " + formatRupiah(total));
        } else {
            // Memanggil hitungBiayaSewa(int hari) tanpa diskon
            total = k.hitungBiayaSewa(hari);
            System.out.println("Total biaya sewa " + k.getMerek() + " (" + hari + " hari): " + formatRupiah(total));
        }
    }

    // Menu Interaktif 3: Sewa kendaraan (menunjukkan overloading sewa)
    private static void menuSewaKendaraan(ArrayList<Kendaraan> daftar, Scanner sc) {
        System.out.println("\n--- SEWA KENDARAAN ---");
        for (Kendaraan k : daftar) {
            String status = k.isTersedia() ? "Tersedia" : "Disewa";
            System.out.println(k.getIdKendaraan() + " | " + k.getJenis() + " - " + k.getMerek() + " | Status: " + status);
        }

        String id = bacaString(sc, "Masukkan ID kendaraan: ");
        Kendaraan k = cariKendaraan(daftar, id);
        if (k == null) {
            System.out.println("Kendaraan dengan ID tersebut tidak ditemukan.");
            return;
        }

        String nama = bacaString(sc, "Masukkan nama penyewa: ");
        if (nama.isEmpty()) {
            System.out.println("Nama penyewa tidak boleh kosong.");
            return;
        }

        int lamaHari = 1;
        boolean pakaiDefault = false;
        while (true) {
            System.out.print("Masukkan lama sewa (hari) [Tekan Enter untuk 1 hari]: ");
            String inputHari = sc.nextLine().trim();
            if (inputHari.isEmpty()) {
                pakaiDefault = true;
                break;
            }
            try {
                lamaHari = Integer.parseInt(inputHari);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka.");
            }
        }

        if (pakaiDefault) {
            // Overloading: sewa(String namaPenyewa) dengan default 1 hari
            k.sewa(nama);
        } else {
            // Memanggil sewa(String namaPenyewa, int lamaHari)
            k.sewa(nama, lamaHari);
        }
    }

    // Menu Interaktif 4: Kembalikan kendaraan
    private static void menuKembalikanKendaraan(ArrayList<Kendaraan> daftar, Scanner sc) {
        System.out.println("\n--- PENGEMBALIAN KENDARAAN ---");
        String id = bacaString(sc, "Masukkan ID kendaraan: ");
        Kendaraan k = cariKendaraan(daftar, id);
        if (k == null) {
            System.out.println("Kendaraan dengan ID tersebut tidak ditemukan.");
            return;
        }

        if (!k.isTersedia()) {
            // Memanggil method kembalikan() dari interface Rentable
            k.kembalikan();
        } else {
            System.out.println("Kendaraan " + k.getMerek() + " tidak sedang disewa.");
        }
    }

    // Menu Interaktif 5: Ubah harga sewa per hari (menunjukkan Encapsulation via setter)
    private static void menuUbahHarga(ArrayList<Kendaraan> daftar, Scanner sc) {
        System.out.println("\n--- UBAH HARGA SEWA ---");
        String id = bacaString(sc, "Masukkan ID kendaraan: ");
        Kendaraan k = cariKendaraan(daftar, id);
        if (k == null) {
            System.out.println("Kendaraan dengan ID tersebut tidak ditemukan.");
            return;
        }

        System.out.println("Harga saat ini: " + formatRupiah(k.getHargaPerHari()));
        double hargaBaru = bacaDouble(sc, "Masukkan harga sewa per hari yang baru: ");

        // Encapsulation: Validasi harga diserahkan sepenuhnya ke setter class Kendaraan
        k.setHargaPerHari(hargaBaru);
        System.out.println("Harga sekarang: " + formatRupiah(k.getHargaPerHari()));
    }

    // Helper input integer aman
    public static int bacaInt(Scanner sc, String pesan) {
        while (true) {
            System.out.print(pesan);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka.");
            }
        }
    }

    // Helper input double aman
    public static double bacaDouble(Scanner sc, String pesan) {
        while (true) {
            System.out.print(pesan);
            String input = sc.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka.");
            }
        }
    }

    // Helper input string aman
    public static String bacaString(Scanner sc, String pesan) {
        System.out.print(pesan);
        return sc.nextLine().trim();
    }

    // Helper pencarian kendaraan berdasarkan ID (case-insensitive)
    public static Kendaraan cariKendaraan(ArrayList<Kendaraan> daftar, String id) {
        for (Kendaraan k : daftar) {
            if (k.getIdKendaraan().equalsIgnoreCase(id)) {
                return k;
            }
        }
        return null;
    }

    // Helper format mata uang Rupiah (memanfaatkan helper yang sudah ada di Kendaraan)
    public static String formatRupiah(double nilai) {
        return Kendaraan.formatRupiah(nilai);
    }
}
