package model;

// Class model buat nyimpen data mahasiswa sekaligus logika bisnisnya
public class Mahasiswa {

    // [Materi: Keyword final & Konstanta]
    // Standar kelulusan dibikin public static final biar jadi konstanta tetap
    // Nilainya gak bisa diubah-ubah lagi setelah dideklarasikan
    public static final double FINAL_PASSING_GRADE = 70.0;

    // Atribut di-private biar aman (enkapsulasi), cuma bisa diakses lewat getter
    // nama & nim itu reference type (String), sedangkan nilai itu tipe primitif (double)
    private String nama;
    private String nim;
    private double nilai;

    // Constructor buat inisialisasi data mahasiswa dan langsung memvalidasinya
    public Mahasiswa(String nama, String nim, double nilai) throws NilaiInvalidException {
        // [Materi: Manipulasi String]
        // Pakai trim() biar spasi nyasar di awal/akhir hilang, lalu toUpperCase() biar seragam huruf kapital
        nama = (nama == null) ? "" : nama.trim().toUpperCase();
        nim = (nim == null) ? "" : nim.trim();

        // [Materi: Assertion]
        // Pengaman internal buat mastiin NIM gak kosong pas development / testing (-ea)
        assert !nim.isEmpty() : "NIM tidak boleh kosong!";

        // [Materi: Class Karakter / Character]
        // Validasi NIM wajib angka semua, kita cek karakter per karakter pakai Character.isDigit()
        for (char c : nim.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("NIM hanya boleh berisi angka!");
            }
        }

        // [Materi: Custom Exception]
        // Nilai di luar 0-100 kita tolak, lempar exception bikinan kita sendiri
        if (nilai < 0 || nilai > 100) {
            throw new NilaiInvalidException(
                "Nilai tidak valid: " + nilai + ". Nilai harus antara 0 sampai 100.");
        }

        // Kalau semua validasi lolos, baru simpan ke atribut objek
        this.nama = nama;
        this.nim = nim;
        this.nilai = nilai;
    }

    // Getter nama (mengembalikan tipe reference String)
    public String getNama() {
        return nama;
    }

    // Getter NIM (mengembalikan tipe reference String)
    public String getNim() {
        return nim;
    }

    // Getter nilai (mengembalikan tipe primitif double)
    public double getNilai() {
        return nilai;
    }

    // [Materi: Percabangan if-else]
    // Pemetaan nilai angka ke grade huruf sesuai kriteria di PRD
    public char getGrade() {
        if (nilai >= 85.0) {
            return 'A';
        } else if (nilai >= 70.0) {
            return 'B';
        } else if (nilai >= 55.0) {
            return 'C';
        } else {
            return 'D';
        }
    }

    // Cek kelulusan pakai konstanta FINAL_PASSING_GRADE (70.0)
    // Tipe kembalian boolean ini tipe primitif
    public boolean isLulus() {
        return nilai >= FINAL_PASSING_GRADE;
    }

    // Mengembalikan status kelulusan dalam bentuk teks buat tabel rekap
    public String getStatus() {
        return isLulus() ? "LULUS" : "TIDAK LULUS";
    }

    // Representasi ringkas objek Mahasiswa dalam satu baris string
    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "%-10s %-20s %6.2f  %c  %s",
                nim, nama, nilai, getGrade(), getStatus());
    }
}

