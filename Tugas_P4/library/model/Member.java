package library.model;

import java.util.ArrayList;

public class Member {
    private String id;
    private String nama;
    private ArrayList<Book> daftarPinjaman;
    private int totalPinjamanSepanjangWaktu;

    public Member(String id, String nama) {
        this.id = id;
        this.nama = nama;
        this.daftarPinjaman = new ArrayList<>();
        this.totalPinjamanSepanjangWaktu = 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public ArrayList<Book> getDaftarPinjaman() { return daftarPinjaman; }

    public int getTotalPinjamanSepanjangWaktu() { return totalPinjamanSepanjangWaktu; }
    public void tambahTotalPinjamanSepanjangWaktu() { this.totalPinjamanSepanjangWaktu++; }

    public boolean sudahMencapaiBatasPinjam() {
        return this.daftarPinjaman.size() >= 3;
    }

    public void tambahPinjaman(Book b) {
        this.daftarPinjaman.add(b);
    }

    public void hapusPinjaman(Book b) {
        this.daftarPinjaman.remove(b);
    }

    public int getJumlahPinjamanAktif() {
        return this.daftarPinjaman.size();
    }

    @Override
    public String toString() {
        return String.format("Anggota [%s] %-20s | Pinjaman Aktif: %d/3 | Total Historis: %d", 
                id, nama, getJumlahPinjamanAktif(), totalPinjamanSepanjangWaktu);
    }
}
