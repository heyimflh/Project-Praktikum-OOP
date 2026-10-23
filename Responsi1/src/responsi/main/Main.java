package responsi.main;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import responsi.exception.BatasPinjamException;
import responsi.exception.BukuTidakTersediaException;
import responsi.model.Anggota;
import responsi.model.Buku;
import responsi.service.Perpustakaan;
import responsi.util.FileHandler;
import responsi.util.Validator;

// kelas utama buat antarmuka console aplikasi PustakaUNS
public class Main {

    private static final String FILE_KATALOG = "katalog.txt";
    private static final String FILE_ANGGOTA = "data.obj";

    private final Scanner scanner;
    private final Perpustakaan perpustakaan;
    private final DecimalFormat formatRupiah;

    public Main() {
        this.scanner = new Scanner(System.in);
        this.perpustakaan = new Perpustakaan();

        // format tampilan nominal denda rupiah
        DecimalFormatSymbols simbol = new DecimalFormatSymbols(Locale.forLanguageTag("id-ID"));
        simbol.setGroupingSeparator('.');
        this.formatRupiah = new DecimalFormat("#,###", simbol);
    }

    public static void main(String[] args) {
        Main app = new Main();

        // cek argumen baris perintah buat mode demo
        if (args != null && args.length > 0) {
            String opsi = args[0].trim().toLowerCase();
            if (opsi.equals("demo")) {
                // jalanin demo konsep referensi
                app.perpustakaan.demoReference();
                return;
            } else if (opsi.equals("demo-assert")) {
                // demo memicu AssertionError pas opsi -ea aktif
                System.out.println("=== DEMO KEGAGALAN ASSERTION (SKENARIO 10) ===");
                System.out.println("Membuat buku dengan stok = 0 lalu memaksakan pengurangan stok...");
                Buku bDemo = new Buku("TEST01", "Buku Uji Assert", "Tester", "Pengujian", 2024, 0);
                bDemo.kurangiStok(1);
                System.out.println("Jika baris ini tercetak, berarti opsi -ea TIDAK diaktifkan.");
                return;
            }
        }

        app.jalankan();
    }

    // loop menu utama pakai do-while biar minimal tampil sekali, lalu switch-case buat milih menu
    public void jalankan() {
        int pilihan = -1;

        do {
            tampilkanMenu();
            System.out.print("Pilih menu: ");
            String inputMenu = scanner.nextLine().trim();

            try {
                pilihan = Integer.parseInt(inputMenu);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Pilihan menu harus berupa angka (0-9)!\n");
                pilihan = -1;
                continue;
            }

            switch (pilihan) {
                case 1:
                    menuTambahBukuUI();
                    break;
                case 2:
                    menuTampilkanKatalogUI();
                    break;
                case 3:
                    menuCariBukuUI();
                    break;
                case 4:
                    menuDaftarkanAnggotaUI();
                    break;
                case 5:
                    menuPinjamBukuUI();
                    break;
                case 6:
                    menuKembalikanBukuUI();
                    break;
                case 7:
                    menuRekapMingguanUI();
                    break;
                case 8:
                    menuSimpanDataUI();
                    break;
                case 9:
                    menuMuatDataUI();
                    break;
                case 0:
                    System.out.println("\nTerima kasih. Sampai jumpa!");
                    break;
                default:
                    System.out.println("[ERROR] Menu tidak dikenali. Silakan pilih 0-9.\n");
                    break;
            }

            if (pilihan != 0) {
                System.out.println();
            }
        } while (pilihan != 0);

        scanner.close();
    }

    private void tampilkanMenu() {
        System.out.println("=== PUSTAKAUNS ===");
        System.out.println("1. Tambah Buku");
        System.out.println("2. Tampilkan Katalog");
        System.out.println("3. Cari Buku (judul)");
        System.out.println("4. Daftarkan Anggota");
        System.out.println("5. Pinjam Buku");
        System.out.println("6. Kembalikan Buku");
        System.out.println("7. Rekap Peminjaman Mingguan");
        System.out.println("8. Simpan Data");
        System.out.println("9. Muat Data");
        System.out.println("0. Keluar");
    }

    // menu 1: tambah buku baru ke katalog
    private void menuTambahBukuUI() {
        System.out.println("\n--- Tambah Buku Baru ---");

        String kode = "";
        while (kode.isEmpty()) {
            System.out.print("Kode Buku       : ");
            kode = scanner.nextLine().trim();
            if (kode.isEmpty()) {
                System.out.println("[PERINGATAN] Kode buku tidak boleh kosong.");
            } else if (kode.contains(";")) {
                System.out.println("[PERINGATAN] Kode buku tidak boleh mengandung karakter ';'.");
                kode = "";
            } else if (perpustakaan.cariBukuByKode(kode) != null) {
                System.out.println("[GAGAL] Kode buku '" + kode + "' sudah terdaftar dalam katalog!");
                return;
            }
        }

        String judul = "";
        while (judul.isEmpty()) {
            System.out.print("Judul Buku      : ");
            judul = scanner.nextLine();
            if (judul.trim().isEmpty()) {
                System.out.println("[PERINGATAN] Judul buku tidak boleh kosong.");
                judul = "";
            } else if (judul.contains(";")) {
                System.out.println("[PERINGATAN] Judul tidak boleh mengandung karakter ';'.");
                judul = "";
            }
        }

        String pengarang = "";
        while (pengarang.isEmpty()) {
            System.out.print("Pengarang       : ");
            pengarang = scanner.nextLine().trim();
            if (pengarang.isEmpty()) {
                System.out.println("[PERINGATAN] Nama pengarang tidak boleh kosong.");
            } else if (pengarang.contains(";")) {
                System.out.println("[PERINGATAN] Nama pengarang tidak boleh mengandung ';'.");
                pengarang = "";
            }
        }

        String kategori = "";
        while (kategori.isEmpty()) {
            System.out.print("Kategori        : ");
            kategori = scanner.nextLine().trim();
            if (kategori.isEmpty()) {
                System.out.println("[PERINGATAN] Kategori tidak boleh kosong.");
            } else if (kategori.contains(";")) {
                System.out.println("[PERINGATAN] Kategori tidak boleh mengandung ';'.");
                kategori = "";
            }
        }

        // input angka pakai while dan try-catch biar gak crash kalau diisi huruf
        int tahun = 0;
        while (tahun <= 0) {
            System.out.print("Tahun Terbit    : ");
            String inputTahun = scanner.nextLine().trim();
            try {
                tahun = Integer.parseInt(inputTahun);
                if (tahun <= 0) {
                    System.out.println("[PERINGATAN] Tahun terbit harus berupa angka positif.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Masukan tahun harus berupa angka bulat! Silakan ulangi.");
            }
        }

        int stok = -1;
        while (stok < 0) {
            System.out.print("Stok Awal       : ");
            String inputStok = scanner.nextLine().trim();
            try {
                stok = Integer.parseInt(inputStok);
                if (stok < 0) {
                    System.out.println("[PERINGATAN] Stok buku tidak boleh bernilai negatif.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Masukan stok harus berupa angka bulat! Silakan ulangi.");
            }
        }

        boolean sukses = perpustakaan.tambahBuku(kode, judul, pengarang, kategori, tahun, stok);
        if (sukses) {
            String judulNormal = Validator.normalisasiJudul(judul);
            System.out.println("[OK] Buku \"" + judulNormal + "\" berhasil ditambahkan.");
        } else {
            System.out.println("[GAGAL] Buku gagal ditambahkan.");
        }
    }

    // menu 2: nampilin daftar katalog buku
    private void menuTampilkanKatalogUI() {
        System.out.println("\n--- Katalog Buku PustakaUNS ---");
        List<Buku> katalog = perpustakaan.getKatalog();

        if (katalog.isEmpty()) {
            System.out.println("Katalog masih kosong.");
            return;
        }

        // tabel pakai tab (\t) biar rapi
        System.out.println("Kode\tTahun\tStok\tKategori\tJudul & Pengarang");
        System.out.println("----------------------------------------------------------------------");

        for (Buku b : katalog) {
            System.out.println(b.getKodeBuku() + "\t"
                    + b.getTahunTerbit() + "\t"
                    + b.getStok() + "\t"
                    + b.getKategori() + "\t"
                    + b.getJudul() + " (" + b.getPengarang() + ")");
        }
        System.out.println("----------------------------------------------------------------------");

        System.out.println("Kategori tersedia: " + perpustakaan.getKategoriUnik());
        System.out.println("Total judul buku : " + katalog.size() + " judul.");
    }

    // menu 3: cari buku berdasarkan judul
    private void menuCariBukuUI() {
        System.out.println("\n--- Cari Buku (Berdasarkan Judul) ---");
        System.out.print("Masukkan kata kunci judul: ");
        String kataKunci = scanner.nextLine().trim();

        if (kataKunci.isEmpty()) {
            System.out.println("[PERINGATAN] Kata kunci pencarian tidak boleh kosong.");
            return;
        }

        List<Buku> hasil = perpustakaan.cariBuku(kataKunci);
        if (hasil.isEmpty()) {
            System.out.println("Tidak ada buku yang cocok dengan kata kunci \"" + kataKunci + "\".");
            return;
        }

        System.out.println("Ditemukan " + hasil.size() + " buku:");
        for (int i = 0; i < hasil.size(); i++) {
            Buku b = hasil.get(i);
            System.out.println((i + 1) + ". [" + b.getKodeBuku() + "] " + b.getJudul()
                    + " | Pengarang: " + b.getPengarang()
                    + " | Stok: " + b.getStok());
        }
    }

    // menu 4: pendaftaran anggota baru, ada loop while biar maksa input ulang kalau ID salah
    private void menuDaftarkanAnggotaUI() {
        System.out.println("\n--- Pendaftaran Anggota Baru ---");

        String idAnggota = "";
        while (true) {
            System.out.print("Masukkan ID Anggota (contoh: AGT001): ");
            idAnggota = scanner.nextLine().trim();

            if (!Validator.validasiIdAnggota(idAnggota)) {
                System.out.println("[GAGAL] Format ID tidak valid! Harus diawali 'AGT' diikuti 3 digit angka (contoh: AGT001).");
                System.out.println("Silakan masukkan ulang ID yang benar.");
                continue;
            }

            if (perpustakaan.cariAnggota(idAnggota) != null) {
                System.out.println("[GAGAL] ID Anggota '" + idAnggota + "' sudah terdaftar! Gunakan ID lain.");
                continue;
            }

            break;
        }

        String nama = "";
        while (nama.isEmpty()) {
            System.out.print("Masukkan Nama Lengkap: ");
            nama = scanner.nextLine().trim();
            if (nama.isEmpty()) {
                System.out.println("[PERINGATAN] Nama anggota tidak boleh kosong.");
            } else if (nama.contains(";")) {
                System.out.println("[PERINGATAN] Nama tidak boleh mengandung karakter ';'.");
                nama = "";
            }
        }

        Anggota anggotaBaru = new Anggota(idAnggota, nama);
        boolean sukses = perpustakaan.daftarkanAnggota(anggotaBaru);

        if (sukses) {
            System.out.println("[OK] Anggota " + nama + " (" + idAnggota + ") berhasil didaftarkan.");
        } else {
            System.out.println("[GAGAL] Pendaftaran anggota tidak dapat diproses.");
        }
    }

    // menu 5: transaksi pinjam buku, pakai try-catch-finally dan nested try-catch buat validasi input
    private void menuPinjamBukuUI() {
        System.out.println("\n--- Peminjaman Buku ---");

        Anggota anggota = null;
        Buku buku = null;

        try {
            String idAnggota = "";
            String kodeBuku = "";

            // nested try-catch buat validasi inputan ID dan kode buku
            try {
                System.out.print("ID Anggota : ");
                idAnggota = scanner.nextLine().trim();
                System.out.print("Kode Buku  : ");
                kodeBuku = scanner.nextLine().trim();

                if (idAnggota.isEmpty() || kodeBuku.isEmpty()) {
                    throw new IllegalArgumentException("ID Anggota dan Kode Buku tidak boleh kosong.");
                }
            } catch (IllegalArgumentException iae) {
                System.out.println("[GAGAL] Masukan tidak valid: " + iae.getMessage());
                return;
            }

            anggota = perpustakaan.cariAnggota(idAnggota);
            if (anggota == null) {
                System.out.println("[GAGAL] Anggota dengan ID '" + idAnggota + "' tidak ditemukan.");
                return;
            }

            buku = perpustakaan.cariBukuByKode(kodeBuku);
            if (buku == null) {
                System.out.println("[GAGAL] Buku dengan kode '" + kodeBuku + "' tidak ditemukan.");
                return;
            }

            perpustakaan.pinjamBuku(anggota, buku);

            System.out.println("[OK] " + anggota.getNama() + " berhasil meminjam \"" + buku.getJudul()
                    + "\". Sisa stok: " + buku.getStok());
            System.out.println("Informasi batas waktu pinjam: " + Perpustakaan.LAMA_PINJAM + " hari.");

            // kode verifikasi peminjaman dari pembalikan kode buku
            String kodeVerifikasi = Validator.balikString(buku.getKodeBuku());
            System.out.println("Kode verifikasi: " + kodeVerifikasi);

        } catch (BukuTidakTersediaException e) {
            // kalau stok habis, masukin ke antrian
            if (buku != null && anggota != null) {
                int posisi = perpustakaan.masukAntrian(buku, anggota);
                System.out.println("[GAGAL] Buku \"" + buku.getJudul() + "\" tidak tersedia. Anda masuk antrian (posisi " + posisi + ").");
            } else {
                System.out.println("[GAGAL] " + e.getMessage());
            }
        } catch (BatasPinjamException e) {
            System.out.println("[GAGAL] " + e.getMessage());
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("[ERROR] Format masukan salah: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[ERROR] Terjadi kendala saat memproses peminjaman: " + e.getMessage());
        } finally {
            // blok finally selalu jalan di akhir apapun hasilnya
            System.out.println("Transaksi selesai.");
        }
    }

    // menu 6: transaksi pengembalian buku dan hitung denda
    private void menuKembalikanBukuUI() {
        System.out.println("\n--- Pengembalian Buku ---");

        try {
            System.out.print("ID Anggota : ");
            String idAnggota = scanner.nextLine().trim();
            System.out.print("Kode Buku  : ");
            String kodeBuku = scanner.nextLine().trim();

            Anggota anggota = perpustakaan.cariAnggota(idAnggota);
            if (anggota == null) {
                System.out.println("[GAGAL] Anggota dengan ID '" + idAnggota + "' tidak ditemukan.");
                return;
            }

            Buku buku = perpustakaan.cariBukuByKode(kodeBuku);
            if (buku == null) {
                System.out.println("[GAGAL] Buku dengan kode '" + kodeBuku + "' tidak ditemukan.");
                return;
            }

            boolean sedangDipinjam = false;
            for (Buku b : anggota.getDaftarPinjam()) {
                if (b.getKodeBuku().equalsIgnoreCase(buku.getKodeBuku())) {
                    sedangDipinjam = true;
                    break;
                }
            }

            if (!sedangDipinjam) {
                System.out.println("[GAGAL] Anggota " + anggota.getNama() + " tidak tercatat sedang meminjam buku \""
                        + buku.getJudul() + "\".");
                return;
            }

            int terlambat = 0;
            boolean inputAngkaValid = false;
            while (!inputAngkaValid) {
                System.out.print("Terlambat (hari) : ");
                String inputHari = scanner.nextLine().trim();
                try {
                    terlambat = Integer.parseInt(inputHari);
                    inputAngkaValid = true;
                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] Masukan hari harus berupa angka bulat! Silakan ulangi.");
                }
            }

            int denda = perpustakaan.kembalikanBuku(anggota, buku, terlambat);
            String nominalDenda = formatRupiah.format(denda);

            System.out.println("[OK] \"" + buku.getJudul() + "\" dikembalikan. Denda: Rp" + nominalDenda);

            if (terlambat > Perpustakaan.BATAS_TELAT_2) {
                System.out.println("[PERINGATAN] Anggota diblokir sementara");
            }

            // cek ada antrian peminjam atau gak
            Anggota anggotaAntre = perpustakaan.ambilAntrianTerdepan(buku.getKodeBuku());
            if (anggotaAntre != null) {
                System.out.println("[NOTIFIKASI] Buku \"" + buku.getJudul() + "\" kini tersedia untuk "
                        + anggotaAntre.getNama() + " (" + anggotaAntre.getIdAnggota() + ").");
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Terjadi kendala saat proses pengembalian: " + e.getMessage());
        } finally {
            System.out.println("Transaksi selesai.");
        }
    }

    // menu 7: rekap transaksi mingguan
    private void menuRekapMingguanUI() {
        System.out.println("\n=== REKAP PEMINJAMAN MINGGUAN ===");
        int[] pinjamPerHari = perpustakaan.getPinjamPerHari();

        System.out.println("Idx\tHari\tJumlah");
        System.out.println("----------------------");

        int totalTransaksi = 0;
        int jumlahTerbanyak = -1;
        String hariTersibuk = "Belum ada transaksi";

        for (int i = 0; i < Perpustakaan.JUMLAH_HARI; i++) {
            int jumlah = pinjamPerHari[i];
            String namaHari = Perpustakaan.NAMA_HARI[i];

            System.out.println(i + "\t" + namaHari + "\t" + jumlah);
            totalTransaksi += jumlah;

            if (jumlah > jumlahTerbanyak && jumlah > 0) {
                jumlahTerbanyak = jumlah;
                hariTersibuk = namaHari + " (" + jumlah + " transaksi)";
            }
        }

        System.out.println("----------------------");
        System.out.println("Total transaksi : " + totalTransaksi);
        System.out.println("Hari tersibuk   : " + hariTersibuk);
    }

    // menu 8: simpan data katalog dan anggota ke file
    private void menuSimpanDataUI() {
        System.out.println("\n--- Menyimpan Data Perpustakaan ---");

        boolean suksesKatalog = FileHandler.simpanKatalog(FILE_KATALOG, perpustakaan.getKatalog());
        if (suksesKatalog) {
            System.out.println("[OK] File katalog berhasil disimpan ke '" + FILE_KATALOG + "'.");
        } else {
            System.out.println("[GAGAL] Gagal menyimpan file katalog.");
        }

        boolean suksesAnggota = FileHandler.simpanAnggota(FILE_ANGGOTA, perpustakaan.getDaftarAnggota());
        if (suksesAnggota) {
            System.out.println("[OK] File data anggota berhasil disimpan ke '" + FILE_ANGGOTA + "'.");
        } else {
            System.out.println("[GAGAL] Gagal menyimpan file data anggota.");
        }
    }

    // menu 9: muat data katalog dan anggota dari file
    private void menuMuatDataUI() {
        System.out.println("\n--- Memuat Data Perpustakaan ---");

        List<Buku> bukuDimuat = FileHandler.muatKatalog(FILE_KATALOG);
        if (!bukuDimuat.isEmpty()) {
            perpustakaan.setKatalog(new java.util.ArrayList<>(bukuDimuat));
            System.out.println("[OK] Berhasil memuat " + bukuDimuat.size() + " buku dari '" + FILE_KATALOG + "'.");
        } else {
            System.out.println("[INFO] Tidak ada buku yang dimuat dari '" + FILE_KATALOG + "'.");
        }

        List<Anggota> anggotaDimuat = FileHandler.muatAnggota(FILE_ANGGOTA);
        if (!anggotaDimuat.isEmpty()) {
            perpustakaan.setDaftarAnggota(anggotaDimuat);
            perpustakaan.sinkronkanDaftarPinjam();
            System.out.println("[OK] Berhasil memuat " + anggotaDimuat.size() + " anggota dari '" + FILE_ANGGOTA + "'.");
        } else {
            System.out.println("[INFO] Tidak ada data anggota yang dimuat dari '" + FILE_ANGGOTA + "'.");
        }
    }
}

/*
 * Catatan perbedaan Assertion vs Exception di program ini:
 * 1. Assertion (kata kunci assert) dipakai buat ngecek bug logika internal programmer (invarian), misalnya buat
 *    mastiin stok buku gak boleh minus setelah transaksi atau buku yang dipinjam anggota gak ngelewatin batas MAX_PINJAM.
 *    Assert cuma aktif kalau program dijalankan pakai opsi '-ea' pas tahap development/testing, dan bukan buat validasi input user.
 * 2. Exception (seperti BukuTidakTersediaException atau BatasPinjamException) dipakai buat nanganin kondisi error runtime
 *    yang wajar terjadi saat program dipakai sehari-hari, misalnya pas user mau minjam buku yang stoknya habis atau
 *    pinjamannya udah penuh. Exception harus ditangkap pakai try-catch biar aplikasi gak crash dan tetap jalan lancar.
 */
