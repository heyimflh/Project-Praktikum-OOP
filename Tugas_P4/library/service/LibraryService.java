package library.service;

import library.model.Book;
import library.model.Member;
import library.exception.BookNotFoundException;
import library.exception.BookAlreadyBorrowedException;
import library.exception.BorrowLimitExceededException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryService {
    private ArrayList<Book> daftarBuku;
    private HashMap<String, Member> daftarAnggota;
    private int totalTransaksiPinjam;

    public LibraryService() {
        this.daftarBuku = new ArrayList<>();
        this.daftarAnggota = new HashMap<>();
        this.totalTransaksiPinjam = 0;
    }

    public void tambahBuku(Book b) {
        this.daftarBuku.add(b);
    }

    public void tambahAnggota(Member m) {
        this.daftarAnggota.put(m.getId(), m);
    }

    public List<Book> tampilkanSemuaBuku() {
        return this.daftarBuku;
    }

    public List<Book> cariBukuByJudul(String keyword) {
        List<Book> hasil = new ArrayList<>();
        for (Book b : daftarBuku) {
            if (b.cocokDenganKataKunci(keyword)) {
                hasil.add(b);
            }
        }
        return hasil;
    }

    public List<Book> cariBukuByKategori(String kategori) {
        List<Book> hasil = new ArrayList<>();
        for (Book b : daftarBuku) {
            // Manipulasi string 2: equalsIgnoreCase() dan trim()
            if (b.getKategori().trim().equalsIgnoreCase(kategori.trim())) {
                hasil.add(b);
            }
        }
        return hasil;
    }

    public Map<String, Integer> hitungJumlahBukuPerKategori() {
        Map<String, Integer> rekap = new HashMap<>();
        for (Book b : daftarBuku) {
            String kat = b.getKategori();
            rekap.put(kat, rekap.getOrDefault(kat, 0) + 1);
        }
        return rekap;
    }

    // Mengembalikan objek Book yang berhasil dipinjam agar bisa ditampilkan
    public Book pinjamBuku(String idAnggota, String judulBuku) 
            throws BookNotFoundException, BookAlreadyBorrowedException, BorrowLimitExceededException {
        
        Member member = daftarAnggota.get(idAnggota);
        if (member == null) {
            member = new Member(idAnggota, "Anggota " + idAnggota);
            tambahAnggota(member);
        }

        // Validasi menggunakan assertion
        assert member != null : "Anggota tidak valid (null)";
        assert member.getId() != null && !member.getId().trim().isEmpty() : "ID anggota tidak boleh kosong";
        // Manipulasi Character
        assert Character.isUpperCase(member.getId().charAt(0)) : "ID anggota harus diawali huruf kapital";

        Book targetBook = null;
        for (Book b : daftarBuku) {
            if (b.cocokDenganKataKunci(judulBuku)) {
                targetBook = b;
                break;
            }
        }

        if (targetBook == null) {
            throw new BookNotFoundException("Buku dengan kata kunci '" + judulBuku + "' tidak ditemukan di perpustakaan.");
        }

        if (!targetBook.isStatusKetersediaan()) {
            throw new BookAlreadyBorrowedException("Buku '" + targetBook.getJudul() + "' sedang dipinjam oleh anggota lain.");
        }

        if (member.sudahMencapaiBatasPinjam()) {
            throw new BorrowLimitExceededException("Anggota '" + member.getNama() + "' sudah mencapai batas maksimal peminjaman (3 buku).");
        }

        // Jika lolos validasi, proses peminjaman
        targetBook.setStatusKetersediaan(false);
        targetBook.tambahHitunganPinjam();
        member.tambahPinjaman(targetBook);
        member.tambahTotalPinjamanSepanjangWaktu();
        this.totalTransaksiPinjam++;
        
        return targetBook;
    }

    // Mengembalikan objek Book yang berhasil dikembalikan agar bisa ditampilkan
    public Book kembalikanBuku(String idAnggota, String judulBuku) throws BookNotFoundException {
        Member member = daftarAnggota.get(idAnggota);
        if (member == null) {
            throw new BookNotFoundException("Anggota dengan ID '" + idAnggota + "' tidak ditemukan atau belum pernah terdaftar.");
        }

        Book targetBook = null;
        for (Book b : member.getDaftarPinjaman()) {
            if (b.cocokDenganKataKunci(judulBuku)) {
                targetBook = b;
                break;
            }
        }

        if (targetBook == null) {
            throw new BookNotFoundException("Buku '" + judulBuku + "' tidak sedang dipinjam oleh '" + member.getNama() + "'.");
        }

        targetBook.setStatusKetersediaan(true);
        member.hapusPinjaman(targetBook);
        
        return targetBook;
    }

    public Book bukuPalingSeringDipinjam() {
        if (daftarBuku.isEmpty()) return null;
        Book palingSering = daftarBuku.get(0);
        for (Book b : daftarBuku) {
            if (b.getJumlahDipinjam() > palingSering.getJumlahDipinjam()) {
                palingSering = b;
            }
        }
        return palingSering;
    }

    public Member anggotaPalingAktif() {
        if (daftarAnggota.isEmpty()) return null;
        Member palingAktif = null;
        for (Member m : daftarAnggota.values()) {
            if (palingAktif == null || m.getTotalPinjamanSepanjangWaktu() > palingAktif.getTotalPinjamanSepanjangWaktu()) {
                palingAktif = m;
            }
        }
        return palingAktif;
    }

    public String kategoriPalingPopuler() {
        Map<String, Integer> rekap = hitungJumlahBukuPerKategori();
        String populer = null;
        int max = -1;
        for (Map.Entry<String, Integer> entry : rekap.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                populer = entry.getKey();
            }
        }
        return populer;
    }

    public void cetakLaporan() {
        System.out.println("+=================================================+");
        System.out.println("|             LAPORAN PERPUSTAKAAN                |");
        System.out.println("+=================================================+");
        System.out.println(String.format("| %-25s : %-19d |", "Total Buku", daftarBuku.size()));
        System.out.println(String.format("| %-25s : %-19d |", "Total Anggota", daftarAnggota.size()));
        System.out.println(String.format("| %-25s : %-19d |", "Total Transaksi Peminjaman", totalTransaksiPinjam));
        System.out.println("+-------------------------------------------------+");
        System.out.println("| [ Peringkat Rekor ]                             |");
        
        Member pAktif = anggotaPalingAktif();
        if (pAktif != null && pAktif.getTotalPinjamanSepanjangWaktu() > 0) {
            System.out.println(String.format("| Anggota Paling Aktif : %-24s |", pAktif.getNama() + " (" + pAktif.getTotalPinjamanSepanjangWaktu() + "x)"));
        } else {
            System.out.println("| Anggota Paling Aktif : -                        |");
        }

        Book bPopuler = bukuPalingSeringDipinjam();
        if (bPopuler != null && bPopuler.getJumlahDipinjam() > 0) {
            // Potong teks jika kepanjangan agar tidak merusak tabel
            String jd = bPopuler.getJudul();
            if(jd.length() > 15) jd = jd.substring(0, 12) + "...";
            System.out.println(String.format("| Buku Sering Dipinjam : %-24s |", jd + " (" + bPopuler.getJumlahDipinjam() + "x)"));
        } else {
            System.out.println("| Buku Sering Dipinjam : -                        |");
        }

        String katPop = kategoriPalingPopuler();
        Map<String, Integer> rekap = hitungJumlahBukuPerKategori();
        if (katPop != null) {
            System.out.println(String.format("| Kategori Terpopuler  : %-24s |", katPop + " (" + rekap.get(katPop) + " buku)"));
        } else {
            System.out.println("| Kategori Terpopuler  : -                        |");
        }
        
        System.out.println("+-------------------------------------------------+");
        System.out.println("| [ Statistik Kategori ]                          |");
        for (Map.Entry<String, Integer> entry : rekap.entrySet()) {
            System.out.println(String.format("|  - %-20s : %-20d |", entry.getKey(), entry.getValue()));
        }
        System.out.println("+=================================================+");
    }
}
