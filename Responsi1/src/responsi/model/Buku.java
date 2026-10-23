package responsi.model;

import java.io.Serializable;
import responsi.util.Validator;

// model buat nyimpen data buku di perpustakaan
public class Buku implements Serializable {

    private static final long serialVersionUID = 1L;

    // class variable (static): nilainya dibagi barengan ke semua objek Buku
    public static int totalBuku = 0;

    // instance variable: atribut masing-masing objek buku
    private String kodeBuku;
    private String judul;
    private String pengarang;
    private String kategori;
    private int tahunTerbit;
    private int stok;

    // parameter variable: kodeBuku, judul, pengarang, kategori, tahunTerbit, stok
    public Buku(String kodeBuku, String judul, String pengarang, String kategori, int tahunTerbit, int stok) {
        assert judul != null : "Judul tidak boleh null";
        assert kodeBuku != null : "Kode buku tidak boleh null";
        assert stok >= 0 : "Stok awal tidak boleh negatif";

        // pakai this buat bedain variabel instance sama parameter
        this.kodeBuku = kodeBuku;
        this.judul = judul;
        this.pengarang = pengarang;
        this.kategori = kategori;
        this.tahunTerbit = tahunTerbit;
        this.stok = stok;

        totalBuku++;
    }

    public String getKodeBuku() {
        return kodeBuku;
    }

    public void setKodeBuku(String kodeBuku) {
        this.kodeBuku = kodeBuku;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        assert judul != null : "Judul tidak boleh null";
        this.judul = judul;
    }

    public String getPengarang() {
        return pengarang;
    }

    public void setPengarang(String pengarang) {
        this.pengarang = pengarang;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getTahunTerbit() {
        return tahunTerbit;
    }

    public void setTahunTerbit(int tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        assert stok >= 0 : "Stok tidak boleh bernilai negatif";
        this.stok = stok;
    }

    // nambah stok buku pas dibalikin
    public void tambahStok(int jumlah) {
        if (jumlah > 0) {
            this.stok += jumlah;
        }
    }

    // ngurangin stok buku pas dipinjam, ada assert biar stok gak minus
    public void kurangiStok(int jumlah) {
        assert (this.stok - jumlah) >= 0 : "Stok tidak boleh negatif";
        this.stok -= jumlah;
    }

    // nampilin detail info buku ke layar
    public void tampilkanInfo() {
        // local variable: status ketersediaan sama hitungan vokal
        String statusKetersediaan = (this.stok > 0) ? "Tersedia (" + this.stok + ")" : "Habis (0)";
        int jumlahVokalJudul = Validator.hitungVokal(this.judul);

        System.out.println("Kode Buku    : " + this.kodeBuku);
        System.out.println("Judul        : " + this.judul + " (" + jumlahVokalJudul + " vokal)");
        System.out.println("Pengarang    : " + this.pengarang);
        System.out.println("Kategori     : " + this.kategori);
        System.out.println("Tahun Terbit : " + this.tahunTerbit);
        System.out.println("Status Stok  : " + statusKetersediaan);
    }

    public static void resetTotalBuku() {
        totalBuku = 0;
    }

    @Override
    public String toString() {
        return kodeBuku + " - " + judul + " (" + pengarang + ", " + tahunTerbit + ") [Stok: " + stok + "]";
    }
}
