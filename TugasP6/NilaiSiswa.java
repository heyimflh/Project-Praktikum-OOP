import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Program Pengelolaan Nilai Siswa
 * Pendekatan: Procedural / Fungsional sederhana (method static)
 * Berdasarkan spesifikasi dokumen PRD.md
 */
public class NilaiSiswa {

    // Konstanta nama file output dan ambang batas kelulusan
    static final String NAMA_FILE = "laporan_nilai.txt";
    static final double NILAI_LULUS = 70.0;

    /**
     * Method utama (titik masuk program).
     * Menginisialisasi Scanner, memanggil tulisLaporan() lalu bacaLaporan(),
     * dan menutup Scanner di akhir program.
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Langkah 1: Input data dari konsol dan tulis ke file
        tulisLaporan(NAMA_FILE, input);

        // Langkah 2: Baca kembali file dan tampilkan ke konsol
        bacaLaporan(NAMA_FILE);

        // Menutup resource Scanner
        input.close();
    }

    /**
     * Membaca data siswa dari keyboard, menentukan status kelulusan,
     * lalu menulis tabel nilai siswa dan ringkasan statistik ke file teks.
     * Menggunakan try-with-resources pada BufferedWriter.
     */
    static void tulisLaporan(String namaFile, Scanner input) {
        // Validasi input jumlah siswa (> 0)
        int jumlahSiswa = bacaInteger(input, "Masukkan jumlah siswa: ");
        System.out.println();

        // Penulisan file dibungkus try-with-resources (otomatis close)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(namaFile))) {
            // Tulis header tabel ke file
            writer.write("===== LAPORAN NILAI SISWA =====");
            writer.newLine();
            writer.write(String.format("%-2s | %-17s | %-6s | %s", "No", "Nama", "Nilai", "Status"));
            writer.newLine();

            double totalNilai = 0.0;
            int jumlahLulus = 0;
            int jumlahTidakLulus = 0;

            // Looping untuk membaca data tiap siswa
            for (int i = 1; i <= jumlahSiswa; i++) {
                System.out.println("--- Siswa ke-" + i + " ---");

                // Input nama siswa (tidak boleh kosong)
                String nama;
                while (true) {
                    System.out.print("Nama  : ");
                    nama = input.nextLine().trim();
                    if (!nama.isEmpty()) {
                        break;
                    }
                    System.out.println("Nama tidak boleh kosong! Silakan coba lagi.");
                }

                // Input nilai siswa (rentang 0 sampai 100)
                double nilai = bacaNilai(input, "Nilai : ");
                System.out.println();

                // Tentukan status kelulusan langsung di dalam loop
                String status = tentukanStatus(nilai);

                // Akumulasi total nilai dan hitung status
                totalNilai += nilai;
                if (nilai >= NILAI_LULUS) {
                    jumlahLulus++;
                } else {
                    jumlahTidakLulus++;
                }

                // Tulis satu baris data siswa ke file teks dengan format rapi
                writer.write(String.format(Locale.US, "%-2d | %-17s | %-6.1f | %s", i, nama, nilai, status));
                writer.newLine();
            }

            // Tulis ringkasan statistik di akhir file
            writer.newLine();
            writer.write("===== RINGKASAN =====");
            writer.newLine();
            writer.write("Jumlah Siswa        : " + jumlahSiswa);
            writer.newLine();

            double rataRata = totalNilai / jumlahSiswa;
            writer.write(String.format(Locale.US, "Rata-rata Nilai     : %.2f", rataRata));
            writer.newLine();

            writer.write("Jumlah Lulus        : " + jumlahLulus);
            writer.newLine();
            writer.write("Jumlah Tidak Lulus  : " + jumlahTidakLulus);
            writer.newLine();

            System.out.println("Data berhasil ditulis ke " + namaFile);
            System.out.println();

        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menulis file: " + e.getMessage());
        }
    }

    /**
     * Membaca isi file teks baris demi baris menggunakan BufferedReader
     * hingga null, lalu menampilkannya ke konsol.
     * Menggunakan try-with-resources pada BufferedReader.
     */
    static void bacaLaporan(String namaFile) {
        System.out.println("===== ISI FILE " + namaFile + " =====");

        // Pembacaan file dibungkus try-with-resources (otomatis close)
        try (BufferedReader reader = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            while ((baris = reader.readLine()) != null) {
                System.out.println(baris);
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        }
    }

    /**
     * Menentukan status kelulusan berdasarkan nilai siswa.
     * Mengembalikan "Lulus" jika nilai >= NILAI_LULUS (70.0), selain itu "Tidak Lulus".
     */
    static String tentukanStatus(double nilai) {
        return nilai >= NILAI_LULUS ? "Lulus" : "Tidak Lulus";
    }

    /**
     * Membaca input bilangan bulat (integer) yang valid dan bernilai > 0.
     * Melakukan pengulangan (re-prompt) jika input bukan angka atau <= 0.
     */
    static int bacaInteger(Scanner input, String pesan) {
        while (true) {
            System.out.print(pesan);
            String baris = input.nextLine().trim();
            try {
                int hasil = Integer.parseInt(baris);
                if (hasil > 0) {
                    return hasil;
                }
                System.out.println("Jumlah siswa harus lebih dari 0! Silakan coba lagi.");
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka bulat! Silakan coba lagi.");
            }
        }
    }

    /**
     * Membaca input nilai double dalam rentang 0 sampai 100.
     * Menangani penggantian tanda koma (',') menjadi titik ('.') agar ramah locale Indonesia.
     * Melakukan pengulangan jika input bukan angka atau di luar rentang.
     */
    static double bacaNilai(Scanner input, String pesan) {
        while (true) {
            System.out.print(pesan);
            String baris = input.nextLine().trim().replace(',', '.');
            try {
                double hasil = Double.parseDouble(baris);
                if (hasil >= 0.0 && hasil <= 100.0) {
                    return hasil;
                }
                System.out.println("Nilai harus berada dalam rentang 0 sampai 100! Silakan coba lagi.");
            } catch (NumberFormatException e) {
                System.out.println("Input nilai tidak valid! Silakan coba lagi.");
            }
        }
    }
}
