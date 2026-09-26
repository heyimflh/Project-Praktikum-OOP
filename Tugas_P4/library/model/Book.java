package library.model;

public class Book {
    private String judul;
    private String penulis;
    private int tahunTerbit;
    private String kategori;
    private boolean statusKetersediaan;
    private int jumlahDipinjam;

    public Book(String judul, String penulis, int tahunTerbit, String kategori) {
        this.judul = judul;
        this.penulis = penulis;
        this.tahunTerbit = tahunTerbit;
        this.kategori = kategori;
        this.statusKetersediaan = true; // default true
        this.jumlahDipinjam = 0; // default 0
    }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getPenulis() { return penulis; }
    public void setPenulis(String penulis) { this.penulis = penulis; }

    public int getTahunTerbit() { return tahunTerbit; }
    public void setTahunTerbit(int tahunTerbit) { this.tahunTerbit = tahunTerbit; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public boolean isStatusKetersediaan() { return statusKetersediaan; }
    public void setStatusKetersediaan(boolean statusKetersediaan) { this.statusKetersediaan = statusKetersediaan; }

    public int getJumlahDipinjam() { return jumlahDipinjam; }
    public void tambahHitunganPinjam() { this.jumlahDipinjam++; }

    public boolean cocokDenganKataKunci(String keyword) {
        // Manipulasi String 1: toLowerCase() dan contains()
        return this.judul.toLowerCase().contains(keyword.toLowerCase());
    }

    @Override
    public String toString() {
        String status = statusKetersediaan ? "[TERSEDIA]" : "[DIPINJAM]";
        // Batasi panjang teks agar tabel rapi di console
        String jdl = (judul.length() > 30) ? judul.substring(0, 27) + "..." : judul;
        String pnl = (penulis.length() > 20) ? penulis.substring(0, 17) + "..." : penulis;
        String kat = (kategori.length() > 12) ? kategori.substring(0, 9) + "..." : kategori;
        
        return String.format("%-10s | %-30s | %-20s | %-4d | %-12s | %2d x", 
                status, jdl, pnl, tahunTerbit, kat, jumlahDipinjam);
    }
}
