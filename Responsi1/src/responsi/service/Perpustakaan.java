package responsi.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import responsi.exception.BatasPinjamException;
import responsi.exception.BukuTidakTersediaException;
import responsi.model.Anggota;
import responsi.model.Buku;
import responsi.util.Validator;

// service utama pengelola data dan transaksi perpustakaan
public class Perpustakaan {

    // konstanta sistem biar gak ada angka ajaib (magic numbers)
    public static final int MAX_PINJAM = 3;
    public static final int DENDA_PER_HARI = 1000;
    public static final int LAMA_PINJAM = 7;

    public static final char STATUS_AKTIF = 'A';
    public static final char STATUS_NONAKTIF = 'N';

    public static final int BATAS_TELAT_1 = 7;
    public static final int BATAS_TELAT_2 = 14;
    public static final int PENGALI_TARIF_2 = 2;
    public static final int PENGALI_TARIF_3 = 3;

    public static final int JUMLAH_HARI = 7;
    public static final String[] NAMA_HARI = {
        "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"
    };

    // pakai ArrayList buat katalog karena ukurannya dinamis, gampang add/remove buku tanpa ribet ngatur resize array manual
    private ArrayList<Buku> katalog;
    private List<Anggota> daftarAnggota;
    private Set<String> kategoriUnik; // pakai TreeSet biar otomatis urut abjad dan gak duplikat
    private Map<String, Queue<Anggota>> antrian; // antrian per buku pakai LinkedList
    private int[] pinjamPerHari; // array 7 hari buat nyatat transaksi harian

    public Perpustakaan() {
        this.katalog = new ArrayList<>();
        this.daftarAnggota = new ArrayList<>();
        this.kategoriUnik = new TreeSet<>();
        this.antrian = new HashMap<>();
        this.pinjamPerHari = new int[JUMLAH_HARI];
    }

    // nambah buku baru ke katalog
    public boolean tambahBuku(String kode, String judul, String pengarang, String kategori, int tahun, int stok) {
        assert judul != null : "Judul tidak boleh null";
        assert kode != null : "Kode buku tidak boleh null";
        assert stok >= 0 : "Stok buku tidak boleh negatif";

        // cek kode buku duplikat
        if (cariBukuByKode(kode) != null) {
            return false;
        }

        String judulNormal = Validator.normalisasiJudul(judul);
        String kategoriNormal = Validator.normalisasiJudul(kategori);

        Buku bukuBaru = new Buku(kode.trim().toUpperCase(), judulNormal, pengarang.trim(), kategoriNormal, tahun, stok);
        katalog.add(bukuBaru);
        kategoriUnik.add(kategoriNormal);

        return true;
    }

    // cari buku berdasarkan judul
    public List<Buku> cariBuku(String kataKunci) {
        List<Buku> hasil = new ArrayList<>();
        // telusuri katalog pakai for-each
        for (Buku b : katalog) {
            if (Validator.cariJudul(b.getJudul(), kataKunci)) {
                hasil.add(b);
            }
        }
        return hasil;
    }

    // cari buku berdasarkan kode
    public Buku cariBukuByKode(String kode) {
        if (kode == null) return null;
        for (Buku b : katalog) {
            // perbandingan string pakai equalsIgnoreCase karena ngebandingin isi karakternya, bukan alamat memorinya (==)
            if (b.getKodeBuku().equalsIgnoreCase(kode.trim())) {
                return b;
            }
        }
        return null;
    }

    // daftar anggota baru
    public boolean daftarkanAnggota(Anggota a) {
        if (a == null) return false;
        if (cariAnggota(a.getIdAnggota()) != null) {
            return false;
        }
        daftarAnggota.add(a);
        return true;
    }

    // cari anggota lewat ID
    public Anggota cariAnggota(String id) {
        if (id == null) return null;
        for (Anggota a : daftarAnggota) {
            if (a.getIdAnggota().equalsIgnoreCase(id.trim())) {
                return a;
            }
        }
        return null;
    }

    // proses peminjaman buku
    public void pinjamBuku(Anggota a, Buku b) throws BatasPinjamException, BukuTidakTersediaException {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Anggota dan buku tidak boleh null");
        }

        // cek apakah anggota lagi diblokir
        if (a.getStatus() == STATUS_NONAKTIF) {
            throw new BatasPinjamException("Anggota " + a.getNama() + " sedang diblokir sementara karena keterlambatan.");
        }

        // cek batas maksimal pinjam dulu
        if (a.getJumlahPinjam() >= MAX_PINJAM) {
            throw new BatasPinjamException("Anggota " + a.getNama() + " sudah meminjam " + a.getJumlahPinjam()
                    + " buku (maksimal " + MAX_PINJAM + ").");
        }

        // cek ketersediaan stok
        if (b.getStok() <= 0) {
            throw new BukuTidakTersediaException("Buku \"" + b.getJudul() + "\" tidak tersedia (stok 0).");
        }

        // eksekusi pinjam
        b.kurangiStok(1);
        a.tambahPinjam(b);

        // catat peminjaman ke array rekap berdasarkan hari sekarang (0 = Senin ... 6 = Minggu)
        int indeksHariIni = LocalDate.now().getDayOfWeek().getValue() - 1;
        if (indeksHariIni >= 0 && indeksHariIni < JUMLAH_HARI) {
            pinjamPerHari[indeksHariIni]++;
        }

        // assert invarian setelah transaksi
        assert b.getStok() >= 0 : "Stok tidak boleh negatif setelah peminjaman";
        assert a.getJumlahPinjam() <= MAX_PINJAM : "Jumlah buku yang dipinjam anggota melebihi MAX_PINJAM";
    }

    // proses pengembalian buku dan hitung denda
    public int kembalikanBuku(Anggota a, Buku b, int hariTerlambat) {
        if (a == null || b == null) {
            return 0;
        }

        a.hapusPinjam(b);
        b.tambahStok(1);

        int denda = hitungDenda(hariTerlambat);

        // kalau telat lebih dari 14 hari, anggota kena blokir sementara
        if (hariTerlambat > BATAS_TELAT_2) {
            a.setStatus(STATUS_NONAKTIF);
        }

        return denda;
    }

    // hitung denda bertingkat pakai if - else if - else
    public int hitungDenda(int hariTerlambat) {
        if (hariTerlambat <= 0) {
            return 0;
        } else if (hariTerlambat <= BATAS_TELAT_1) {
            return hariTerlambat * DENDA_PER_HARI;
        } else if (hariTerlambat <= BATAS_TELAT_2) {
            return hariTerlambat * (DENDA_PER_HARI * PENGALI_TARIF_2);
        } else {
            return hariTerlambat * (DENDA_PER_HARI * PENGALI_TARIF_3);
        }
    }

    // masukin anggota ke antrian buku yang stoknya habis
    public int masukAntrian(Buku b, Anggota a) {
        String kode = b.getKodeBuku().toUpperCase();
        Queue<Anggota> queue = antrian.computeIfAbsent(kode, k -> new LinkedList<>());
        if (!queue.contains(a)) {
            queue.offer(a);
        }
        return queue.size();
    }

    // ambil anggota paling depan pas ada buku yang dibalikin
    public Anggota ambilAntrianTerdepan(String kodeBuku) {
        if (kodeBuku == null) return null;
        Queue<Anggota> queue = antrian.get(kodeBuku.trim().toUpperCase());
        if (queue != null && !queue.isEmpty()) {
            return queue.poll();
        }
        return null;
    }

    // demo konsep reference types dan tipe primitif
    public void demoReference() {
        System.out.println("\n=== DEMO REFERENCE TYPES & PRIMITIVE (FR-B5) ===");

        // buat b1, lalu b2 mengacu ke objek yang sama kayak b1
        Buku b1 = new Buku("DEMO01", "Dasar Pemrograman Java", "Tim Dosen", "Edukasi", 2023, 5);
        Buku b2 = b1;

        System.out.println("1. Inisialisasi awal:");
        System.out.println("   Stok b1 awal: " + b1.getStok());
        System.out.println("   Stok b2 awal: " + b2.getStok());

        // stok diubah lewat b2, otomatis stok b1 juga berubah karena referensi objeknya sama di heap
        b2.tambahStok(3);
        System.out.println("\n2. Setelah b2.tambahStok(3):");
        System.out.println("   Stok b2: " + b2.getStok());
        System.out.println("   Stok b1: " + b1.getStok() + " (Stok b1 ikut bertambah menjadi " + b1.getStok()
                + " karena b1 dan b2 menunjuk objek yang sama di heap memory!)");

        // b1 == b2 hasilnya true karena operator '==' ngecek kesamaan alamat referensi memori
        boolean isSameRef = (b1 == b2);
        System.out.println("\n3. Perbandingan referensi (b1 == b2): " + isSameRef);

        // nilai default tipe referensi itu null, sedangkan tipe primitif int itu 0
        Buku bNull = cariBukuByKode("DEMO_NULL");
        int stokDefault = 0;

        System.out.println("\n4. Nilai Default & Pencegahan NullPointerException:");
        System.out.println("   Nilai default reference Buku : " + bNull);
        System.out.println("   Nilai default primitive int  : " + stokDefault);

        // dicek null dulu biar aman dari NullPointerException
        if (bNull != null) {
            System.out.println("   Judul buku: " + bNull.getJudul());
        } else {
            System.out.println("   [AMAN] bNull bernilai null; pengecekan 'if (bNull != null)' mencegah NullPointerException.");
        }
        System.out.println("=== AKHIR DEMO REFERENCE ===\n");
    }

    public void bangunUlangKategori() {
        kategoriUnik.clear();
        for (Buku b : katalog) {
            if (b.getKategori() != null && !b.getKategori().isEmpty()) {
                kategoriUnik.add(b.getKategori());
            }
        }
    }

    // sinkronisasi objek buku di daftar pinjam anggota sama yang ada di katalog setelah muat file
    public void sinkronkanDaftarPinjam() {
        for (Anggota a : daftarAnggota) {
            List<Buku> bukuTersinkron = new ArrayList<>();
            for (Buku bPinjam : a.getDaftarPinjam()) {
                Buku diKatalog = cariBukuByKode(bPinjam.getKodeBuku());
                if (diKatalog != null) {
                    bukuTersinkron.add(diKatalog);
                } else {
                    bukuTersinkron.add(bPinjam);
                }
            }
            a.setDaftarPinjam(bukuTersinkron);
        }
    }

    public ArrayList<Buku> getKatalog() {
        return katalog;
    }

    public void setKatalog(ArrayList<Buku> katalog) {
        this.katalog = katalog;
        bangunUlangKategori();
    }

    public List<Anggota> getDaftarAnggota() {
        return daftarAnggota;
    }

    public void setDaftarAnggota(List<Anggota> daftarAnggota) {
        this.daftarAnggota = daftarAnggota;
    }

    public Set<String> getKategoriUnik() {
        return kategoriUnik;
    }

    public int[] getPinjamPerHari() {
        return pinjamPerHari;
    }
}
