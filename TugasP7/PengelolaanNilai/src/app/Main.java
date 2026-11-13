package app;

import model.Mahasiswa;
import model.NilaiInvalidException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

// Class utama buat alur jalannya aplikasi dan antarmuka konsol
public class Main {

    public static void main(String[] args) {
        // [Materi: Collection Framework - ArrayList]
        // Pakai ArrayList<Mahasiswa> buat nampung data secara dinamis (bukan array statis [])
        ArrayList<Mahasiswa> daftar = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        System.out.println("=== PROGRAM PENGELOLAAN NILAI MAHASISWA ===");

        // Validasi input jumlah mahasiswa: harus berupa angka bulat dan lebih dari 0
        int jumlahMahasiswa = 0;
        while (true) {
            System.out.print("Jumlah mahasiswa yang akan diinput: ");
            String strJumlah = input.nextLine().trim();
            try {
                jumlahMahasiswa = Integer.parseInt(strJumlah);
                if (jumlahMahasiswa <= 0) {
                    System.out.println("Jumlah mahasiswa harus lebih dari 0! Silakan ulangi.");
                    continue;
                }
                break; // input valid, lanjut ke pengisian data mahasiswa
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Harap masukkan bilangan bulat positif.");
            }
        }

        // Loop buat manggil method inputMahasiswa sebanyak jumlah yang diminta user
        for (int i = 0; i < jumlahMahasiswa; i++) {
            inputMahasiswa(input, daftar);
        }

        // Tampilkan rekapitulasi data dan statistik kelas ke layar konsol
        tampilkanRekap(daftar);

        // [Materi: File I/O]
        // Simpan hasil rekapitulasi ke file teks laporan_nilai.txt
        simpanLaporan(daftar, "laporan_nilai.txt");

        // Tutup resource Scanner setelah selesai dipakai
        input.close();
    }

    // Method buat nerima input data 1 mahasiswa dari user dan nambahin ke ArrayList
    public static void inputMahasiswa(Scanner in, ArrayList<Mahasiswa> daftar) {
        // Tampilkan nomor urut mahasiswa yang lagi diinput (pakai size() + 1)
        System.out.printf("%n--- Mahasiswa ke-%d ---%n", daftar.size() + 1);

        // 1. Input Nama (gak boleh kosong)
        String nama;
        while (true) {
            System.out.print("Nama  : ");
            nama = in.nextLine().trim();
            if (nama.isEmpty()) {
                System.out.println("Nama tidak boleh kosong! Silakan ulangi.");
                continue;
            }
            break;
        }

        // 2. Input NIM
        // Dicegah kosong dulu di sini (Ketentuan 13) biar gak bergantung ke flag -ea
        // Dicegah huruf pakai Character.isDigit() biar kalau typo langsung minta ulang field NIM
        String nim;
        while (true) {
            System.out.print("NIM   : ");
            nim = in.nextLine().trim();
            if (nim.isEmpty()) {
                System.out.println("NIM tidak boleh kosong! Silakan ulangi.");
                continue;
            }

            // Validasi karakter NIM hanya angka
            boolean hanyaAngka = true;
            for (char c : nim.toCharArray()) {
                if (!Character.isDigit(c)) {
                    hanyaAngka = false;
                    break;
                }
            }

            if (!hanyaAngka) {
                System.out.println("NIM hanya boleh berisi angka! Silakan ulangi.");
                continue;
            }
            break;
        }

        // 3. Input Nilai
        // Tangkap NumberFormatException kalau bukan angka, dan NilaiInvalidException kalau di luar 0-100
        double nilai = 0.0;
        while (true) {
            System.out.print("Nilai : ");
            String strNilai = in.nextLine().trim();
            try {
                nilai = Double.parseDouble(strNilai);

                // Cek rentang nilai, lempar custom exception kita kalau gak valid
                if (nilai < 0 || nilai > 100) {
                    throw new NilaiInvalidException(
                        "Nilai tidak valid: " + nilai + ". Nilai harus antara 0 sampai 100.");
                }
                break; // nilai lolos validasi
            } catch (NumberFormatException e) {
                System.out.println("Nilai harus berupa angka! Silakan ulangi.");
            } catch (NilaiInvalidException e) {
                System.out.println(e.getMessage());
            }
        }

        // Buat objek Mahasiswa dan tambahkan ke ArrayList pakai method add()
        try {
            Mahasiswa mhs = new Mahasiswa(nama, nim, nilai);
            daftar.add(mhs);
            System.out.println("Data berhasil ditambahkan.");
        } catch (NilaiInvalidException | IllegalArgumentException e) {
            // Pengaman tambahan kalau ada exception tak terduga dari constructor
            System.out.println("Gagal menambahkan data: " + e.getMessage());
        }
    }

    // [Materi: Perulangan for-each & Koleksi]
    // Menghitung nilai rata-rata kelas pakai loop for-each
    public static double hitungRataRata(ArrayList<Mahasiswa> daftar) {
        // Antisipasi list kosong biar gak pembagian dengan nol (zero division)
        if (daftar == null || daftar.isEmpty()) {
            return 0.0;
        }

        double totalNilai = 0.0;
        // Looping for-each untuk menjelajahi setiap objek Mahasiswa di dalam ArrayList
        for (Mahasiswa m : daftar) {
            totalNilai += m.getNilai();
        }

        return totalNilai / daftar.size();
    }

    // Menghitung berapa banyak mahasiswa yang dinyatakan lulus
    public static int hitungJumlahLulus(ArrayList<Mahasiswa> daftar) {
        if (daftar == null || daftar.isEmpty()) {
            return 0;
        }

        int jumlahLulus = 0;
        for (Mahasiswa m : daftar) {
            if (m.isLulus()) {
                jumlahLulus++;
            }
        }
        return jumlahLulus;
    }

    // Menampilkan tabel rekapitulasi nilai dan statistik ke layar konsol
    public static void tampilkanRekap(ArrayList<Mahasiswa> daftar) {
        System.out.println("\n=== REKAP NILAI ===");
        System.out.println("==============================================");
        System.out.println("          LAPORAN NILAI MAHASISWA");
        System.out.println("==============================================");
        System.out.println("No  NIM          Nama                 Nilai  Grade  Status");
        System.out.println("----------------------------------------------------------");

        // Format tabel dibuat rapi dan konsisten pakai Locale.US agar pemisah desimal berupa titik (.)
        int no = 1;
        for (Mahasiswa m : daftar) {
            System.out.printf(Locale.US, "%-4d%-13s%-21s%5.2f  %-7c%s%n",
                    no++, m.getNim(), m.getNama(), m.getNilai(), m.getGrade(), m.getStatus());
        }

        System.out.println("----------------------------------------------------------");
        System.out.println("STATISTIK KELAS");
        System.out.printf("Jumlah Mahasiswa   : %d%n", daftar.size());
        System.out.printf(Locale.US, "Rata-rata Nilai    : %.2f%n", hitungRataRata(daftar));
        int jumlahLulus = hitungJumlahLulus(daftar);
        int jumlahTidakLulus = daftar.size() - jumlahLulus;
        System.out.printf("Jumlah Lulus       : %d%n", jumlahLulus);
        System.out.printf("Jumlah Tidak Lulus : %d%n", jumlahTidakLulus);
        System.out.printf(Locale.US, "Standar Kelulusan  : %.1f%n", Mahasiswa.FINAL_PASSING_GRADE);
        System.out.println("==============================================");
    }

    // [Materi: File I/O & try-with-resources]
    // Menulis seluruh isi laporan rekapitulasi ke file teks (laporan_nilai.txt)
    public static void simpanLaporan(ArrayList<Mahasiswa> daftar, String namaFile) {
        // try-with-resources otomatis nutup writer dan file-nya begitu blok selesai (gak butuh finally manual)
        // BufferedWriter dibungkus FileWriter sesuai ketentuan soal
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(namaFile))) {
            writer.write("==============================================");
            writer.newLine();
            writer.write("          LAPORAN NILAI MAHASISWA");
            writer.newLine();
            writer.write("==============================================");
            writer.newLine();
            writer.write("No  NIM          Nama                 Nilai  Grade  Status");
            writer.newLine();
            writer.write("----------------------------------------------------------");
            writer.newLine();

            // Tulis baris data tiap mahasiswa
            int no = 1;
            for (Mahasiswa m : daftar) {
                String baris = String.format(Locale.US, "%-4d%-13s%-21s%5.2f  %-7c%s",
                        no++, m.getNim(), m.getNama(), m.getNilai(), m.getGrade(), m.getStatus());
                writer.write(baris);
                writer.newLine();
            }

            writer.write("----------------------------------------------------------");
            writer.newLine();
            writer.write("STATISTIK KELAS");
            writer.newLine();
            writer.write(String.format("Jumlah Mahasiswa   : %d", daftar.size()));
            writer.newLine();
            writer.write(String.format(Locale.US, "Rata-rata Nilai    : %.2f", hitungRataRata(daftar)));
            writer.newLine();
            int jumlahLulus = hitungJumlahLulus(daftar);
            int jumlahTidakLulus = daftar.size() - jumlahLulus;
            writer.write(String.format("Jumlah Lulus       : %d", jumlahLulus));
            writer.newLine();
            writer.write(String.format("Jumlah Tidak Lulus : %d", jumlahTidakLulus));
            writer.newLine();
            writer.write(String.format(Locale.US, "Standar Kelulusan  : %.1f", Mahasiswa.FINAL_PASSING_GRADE));
            writer.newLine();
            writer.write("==============================================");
            writer.newLine();

            System.out.println("\nLaporan berhasil disimpan ke " + namaFile);
        } catch (IOException e) {
            // Tangani error I/O (misalnya file terkunci atau izin akses ditolak) agar program gak crash
            System.err.println("Gagal menyimpan laporan ke file: " + e.getMessage());
        }
    }
}

