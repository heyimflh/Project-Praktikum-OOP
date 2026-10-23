package responsi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// model data anggota perpustakaan
public class Anggota implements Serializable {

    private static final long serialVersionUID = 1L;

    // instance variable
    private String idAnggota;
    private String nama;
    private ArrayList<Buku> daftarPinjam; // list buku yang lagi dipinjam
    private char status; // 'A' = aktif, 'N' = nonaktif / diblokir

    // parameter variable: idAnggota, nama
    public Anggota(String idAnggota, String nama) {
        this.idAnggota = idAnggota;
        this.nama = nama;
        this.daftarPinjam = new ArrayList<>();
        this.status = 'A';
    }

    public Anggota(String idAnggota, String nama, char status) {
        this.idAnggota = idAnggota;
        this.nama = nama;
        this.daftarPinjam = new ArrayList<>();
        this.status = status;
    }

    public String getIdAnggota() {
        return idAnggota;
    }

    public void setIdAnggota(String idAnggota) {
        this.idAnggota = idAnggota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public List<Buku> getDaftarPinjam() {
        return daftarPinjam;
    }

    public void setDaftarPinjam(List<Buku> daftarPinjam) {
        if (daftarPinjam instanceof ArrayList) {
            this.daftarPinjam = (ArrayList<Buku>) daftarPinjam;
        } else if (daftarPinjam != null) {
            this.daftarPinjam = new ArrayList<>(daftarPinjam);
        } else {
            this.daftarPinjam = new ArrayList<>();
        }
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public int getJumlahPinjam() {
        return this.daftarPinjam.size();
    }

    // masukin buku ke daftar pinjam anggota
    public void tambahPinjam(Buku b) {
        if (b != null) {
            this.daftarPinjam.add(b);
        }
    }

    // keluarin buku dari daftar pinjam pas dibalikin
    public boolean hapusPinjam(Buku b) {
        if (b == null) {
            return false;
        }
        for (int i = 0; i < daftarPinjam.size(); i++) {
            if (daftarPinjam.get(i).getKodeBuku().equalsIgnoreCase(b.getKodeBuku())) {
                daftarPinjam.remove(i);
                return true;
            }
        }
        return false;
    }

    // nampilin info anggota dan buku-buku yang lagi dipinjam
    public void tampilkanInfo() {
        // local variable: teks status anggota
        String teksStatus = (this.status == 'A') ? "Aktif" : "Nonaktif (Diblokir Sementara)";

        System.out.println("ID Anggota    : " + this.idAnggota);
        System.out.println("Nama          : " + this.nama);
        System.out.println("Status        : " + teksStatus);
        System.out.println("Jumlah Pinjam : " + this.daftarPinjam.size() + " buku");

        if (this.daftarPinjam.isEmpty()) {
            System.out.println("  (Tidak sedang meminjam buku)");
        } else {
            System.out.println("  Daftar Buku yang Dipinjam:");
            // pakai for biasa buat nampilin nomor urut 1, 2, dst
            for (int i = 0; i < this.daftarPinjam.size(); i++) {
                Buku b = this.daftarPinjam.get(i);
                System.out.println("    " + (i + 1) + ". [" + b.getKodeBuku() + "] " + b.getJudul());
            }
        }
    }

    @Override
    public String toString() {
        return idAnggota + " - " + nama + " (Pinjam: " + daftarPinjam.size() + ", Status: " + status + ")";
    }
}
