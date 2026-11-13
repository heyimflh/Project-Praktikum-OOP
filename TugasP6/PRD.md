# PRD — Program Pengelolaan Nilai Siswa (Java Procedural + File I/O)

| Item | Keterangan |
|---|---|
| Nama Produk | Program Pengelolaan Nilai Siswa |
| Bahasa | Java (JDK 8 atau lebih baru) |
| Paradigma | Procedural / Fungsional sederhana (tanpa kelas model, hanya method `static`) |
| Jenis Aplikasi | Console Application (CLI) |
| File Keluaran | `laporan_nilai.txt` |
| File Sumber | `NilaiSiswa.java` |

---

## 1. Latar Belakang

Pencatatan nilai siswa secara manual rawan salah dan sulit diarsipkan. Program ini mendemonstrasikan alur dasar pengolahan data di Java: **input dari keyboard → proses → simpan ke file teks → baca kembali dari file → tampilkan ke layar**. Program juga melatih penggunaan *Java I/O* (`FileWriter`, `BufferedWriter`, `FileReader`, `BufferedReader`) serta *exception handling* dan *resource management*.

## 2. Tujuan

1. Membaca data siswa (nama dan nilai) dari konsol menggunakan `Scanner`.
2. Menentukan status kelulusan setiap siswa secara otomatis di dalam loop.
3. Menulis seluruh data siswa beserta ringkasan statistik ke file `laporan_nilai.txt`.
4. Membaca kembali isi file baris demi baris dan menampilkannya ke konsol sebagai bukti file berhasil dibuat.
5. Menangani error I/O dengan benar dan menutup semua resource (stream) secara aman.

## 3. Ruang Lingkup

**Termasuk (In Scope)**
- Input jumlah siswa, nama, dan nilai via keyboard.
- Penentuan status Lulus / Tidak Lulus.
- Penulisan file teks dengan `FileWriter` + `BufferedWriter`.
- Pembacaan file teks dengan `FileReader` + `BufferedReader`.
- Perhitungan rata-rata kelas, jumlah lulus, dan jumlah tidak lulus.
- Exception handling `IOException` dan `try-with-resources`.

**Tidak Termasuk (Out of Scope)**
- GUI, database, atau koneksi jaringan.
- Pendekatan OOP (kelas model, inheritance, interface).
- Fitur edit / hapus / cari data.
- Penambahan data ke file lama (mode append); file selalu ditimpa baru.

## 4. Pengguna

| Pengguna | Kebutuhan |
|---|---|
| Guru / operator | Memasukkan nilai siswa dan mendapatkan laporan otomatis |
| Mahasiswa (konteks tugas) | Memahami penerapan Java I/O dan exception handling |

## 5. Kebutuhan Fungsional

| ID | Kebutuhan | Prioritas |
|---|---|---|
| FR-01 | Program meminta input **jumlah siswa** (integer) menggunakan `Scanner`. | Wajib |
| FR-02 | Program melakukan **looping** sebanyak jumlah siswa untuk membaca **nama** (`String`) dan **nilai** (`double`) tiap siswa. | Wajib |
| FR-03 | Status kelulusan ditentukan **langsung di dalam loop**: nilai **≥ 70 → "Lulus"**, nilai **< 70 → "Tidak Lulus"**. | Wajib |
| FR-04 | Data tiap siswa ditulis ke `laporan_nilai.txt` menggunakan `FileWriter` dan `BufferedWriter`. | Wajib |
| FR-05 | Di akhir file ditulis ringkasan: **rata-rata nilai kelas**, **jumlah siswa lulus**, dan **jumlah siswa tidak lulus**. | Wajib |
| FR-06 | Setelah penulisan selesai, file dibuka kembali dengan `FileReader` dan `BufferedReader`. | Wajib |
| FR-07 | Isi file dibaca per baris dengan `readLine()` **sampai `null`**, lalu seluruhnya ditampilkan ke konsol. | Wajib |
| FR-08 | Seluruh proses baca/tulis file dibungkus `try-catch` untuk menangani `IOException`. | Wajib |
| FR-09 | Semua stream (`BufferedWriter`, `BufferedReader`) ditutup dengan benar, disarankan memakai `try-with-resources`. | Wajib |
| FR-10 | Validasi input: jumlah siswa harus > 0; nilai harus di antara 0–100; input non-angka diminta ulang. | Opsional (disarankan) |

## 6. Kebutuhan Non-Fungsional

| ID | Kebutuhan |
|---|---|
| NFR-01 | Kode ditulis procedural: logika dipecah ke beberapa method `static` yang jelas fungsinya. |
| NFR-02 | Kode rapi, diberi komentar, penamaan variabel dan method deskriptif (camelCase). |
| NFR-03 | Program tidak *crash* saat terjadi `IOException`; tampilkan pesan error yang informatif. |
| NFR-04 | Tidak ada resource leak (semua stream tertutup). |
| NFR-05 | Dapat dijalankan di semua OS yang memiliki JDK tanpa library eksternal. |
| NFR-06 | Menggunakan `System.lineSeparator()` / `newLine()` agar baris baru konsisten antar OS. |

## 7. Alur Program

```
Mulai
  │
  ▼
Input jumlah siswa (n)
  │
  ▼
Buka BufferedWriter (laporan_nilai.txt)
  │
  ▼
Loop i = 1..n
  ├─ Input nama
  ├─ Input nilai
  ├─ Tentukan status (>=70 Lulus / <70 Tidak Lulus)
  ├─ Tambah total nilai, hitung lulus / tidak lulus
  └─ Tulis baris data ke file
  │
  ▼
Tulis ringkasan (rata-rata, jumlah lulus, jumlah tidak lulus)
  │
  ▼
Tutup writer (otomatis via try-with-resources)
  │
  ▼
Buka BufferedReader (laporan_nilai.txt)
  │
  ▼
while ((baris = readLine()) != null) → cetak ke konsol
  │
  ▼
Tutup reader → Selesai
```

## 8. Rancangan Struktur Program

Satu file: `NilaiSiswa.java`, satu kelas publik berisi method `static`.

| Method | Fungsi |
|---|---|
| `public static void main(String[] args)` | Titik masuk; memanggil `tulisLaporan()` lalu `bacaLaporan()`. |
| `static void tulisLaporan(String namaFile, Scanner input)` | Membaca input siswa, menentukan status, menulis data dan ringkasan ke file. Memakai `try-with-resources` pada `BufferedWriter`. |
| `static void bacaLaporan(String namaFile)` | Membaca file dengan `BufferedReader` dan menampilkan tiap baris ke konsol. |
| `static String tentukanStatus(double nilai)` | Mengembalikan `"Lulus"` jika nilai ≥ 70, selain itu `"Tidak Lulus"`. |
| `static int bacaInteger(Scanner input, String pesan)` *(opsional)* | Membaca integer valid dengan pengulangan jika input salah. |
| `static double bacaNilai(Scanner input, String pesan)` *(opsional)* | Membaca nilai `double` valid dalam rentang 0–100. |

**Konstanta**
```java
static final String NAMA_FILE = "laporan_nilai.txt";
static final double NILAI_LULUS = 70.0;
```

**Import yang dibutuhkan**
```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
```

## 9. Spesifikasi Data & Format

### 9.1 Input

| Data | Tipe | Aturan |
|---|---|---|
| Jumlah siswa | `int` | > 0 |
| Nama siswa | `String` | Tidak kosong (gunakan `nextLine()`; hati-hati sisa *newline* setelah `nextInt()`) |
| Nilai siswa | `double` | 0 – 100 |

### 9.2 Aturan Status

| Kondisi | Status |
|---|---|
| nilai ≥ 70 | Lulus |
| nilai < 70 | Tidak Lulus |

### 9.3 Format `laporan_nilai.txt`

```
===== LAPORAN NILAI SISWA =====
No | Nama              | Nilai  | Status
1  | Budi Santoso      | 85.0   | Lulus
2  | Siti Aminah       | 65.5   | Tidak Lulus
3  | Andi Pratama      | 70.0   | Lulus

===== RINGKASAN =====
Jumlah Siswa        : 3
Rata-rata Nilai     : 73.50
Jumlah Lulus        : 2
Jumlah Tidak Lulus  : 1
```

> Rata-rata ditampilkan 2 angka desimal (`String.format("%.2f", rataRata)`). Rata-rata = total nilai ÷ jumlah siswa.

## 10. Penanganan Error & Resource

| Situasi | Penanganan |
|---|---|
| Gagal membuat / menulis file | `catch (IOException e)` → tampilkan `"Terjadi kesalahan saat menulis file: " + e.getMessage()` |
| File tidak ditemukan / gagal dibaca | `catch (IOException e)` → tampilkan `"Terjadi kesalahan saat membaca file: " + e.getMessage()` |
| Input bukan angka | Tangkap `NumberFormatException` / `InputMismatchException`, minta input ulang |
| Nilai di luar 0–100 | Tampilkan pesan, minta input ulang |
| Penutupan stream | Gunakan `try-with-resources`, sehingga `close()` dipanggil otomatis (atau `finally` bila memakai `try-catch` biasa) |

Contoh pola yang diwajibkan:
```java
try (BufferedWriter writer = new BufferedWriter(new FileWriter(NAMA_FILE))) {
    // proses tulis
} catch (IOException e) {
    System.out.println("Terjadi kesalahan saat menulis file: " + e.getMessage());
}

try (BufferedReader reader = new BufferedReader(new FileReader(NAMA_FILE))) {
    String baris;
    while ((baris = reader.readLine()) != null) {
        System.out.println(baris);
    }
} catch (IOException e) {
    System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
}
```

## 11. Contoh Interaksi Konsol

```
Masukkan jumlah siswa: 3

--- Siswa ke-1 ---
Nama  : Budi Santoso
Nilai : 85

--- Siswa ke-2 ---
Nama  : Siti Aminah
Nilai : 65.5

--- Siswa ke-3 ---
Nama  : Andi Pratama
Nilai : 70

Data berhasil ditulis ke laporan_nilai.txt

===== ISI FILE laporan_nilai.txt =====
===== LAPORAN NILAI SISWA =====
No | Nama              | Nilai  | Status
1  | Budi Santoso      | 85.0   | Lulus
2  | Siti Aminah       | 65.5   | Tidak Lulus
3  | Andi Pratama      | 70.0   | Lulus

===== RINGKASAN =====
Jumlah Siswa        : 3
Rata-rata Nilai     : 73.50
Jumlah Lulus        : 2
Jumlah Tidak Lulus  : 1
```

## 12. Kriteria Penerimaan (Acceptance Criteria)

- [ ] Program meminta jumlah siswa, lalu looping membaca nama (String) dan nilai (double).
- [ ] Status "Lulus" / "Tidak Lulus" ditentukan di dalam loop dengan batas 70 (nilai 70 = Lulus).
- [ ] File `laporan_nilai.txt` terbuat dan berisi data semua siswa.
- [ ] Ringkasan (rata-rata, jumlah lulus, jumlah tidak lulus) tertulis di akhir file.
- [ ] Penulisan memakai `FileWriter` + `BufferedWriter`.
- [ ] Pembacaan memakai `FileReader` + `BufferedReader` dengan `readLine()` hingga `null`.
- [ ] Isi file tampil lengkap di konsol setelah proses tulis selesai.
- [ ] Ada `try-catch` untuk `IOException`; `try-with-resources` atau penutupan manual lewat `finally`.
- [ ] Tidak ada resource leak; program berjalan tanpa error pada input valid.
- [ ] Struktur kode procedural (method `static`), tanpa kelas model.

## 13. Skenario Pengujian

| No | Skenario | Input | Hasil yang Diharapkan |
|---|---|---|---|
| T1 | Semua lulus | 2 siswa: 80, 90 | Rata-rata 85.00, lulus 2, tidak lulus 0 |
| T2 | Semua tidak lulus | 2 siswa: 50, 60 | Rata-rata 55.00, lulus 0, tidak lulus 2 |
| T3 | Batas nilai | 1 siswa: 70 | Status Lulus |
| T4 | Di bawah batas | 1 siswa: 69.99 | Status Tidak Lulus |
| T5 | Campuran | 3 siswa: 85, 65.5, 70 | Rata-rata 73.50, lulus 2, tidak lulus 1 |
| T6 | Satu siswa | 1 siswa: 100 | Rata-rata 100.00, lulus 1 |
| T7 | Input nilai tidak valid | "abc" atau 150 | Diminta input ulang (jika FR-10 diterapkan) |
| T8 | Folder tidak bisa ditulis | Jalankan di lokasi read-only | Pesan error `IOException`, program tidak crash |
| T9 | File ditimpa | Jalankan dua kali | File berisi data eksekusi terakhir saja |

## 14. Cara Build & Jalankan

```bash
javac NilaiSiswa.java
java NilaiSiswa
```

## 15. Deliverable

1. `NilaiSiswa.java` — kode sumber program.
2. `laporan_nilai.txt` — file hasil eksekusi program.
3. Screenshot konsol yang menampilkan input dan isi file hasil pembacaan.
4. `PRD.md` — dokumen ini.

## 16. Catatan Teknis

- Setelah `nextInt()`, panggil `input.nextLine()` sekali untuk membuang sisa *newline* sebelum membaca nama dengan `nextLine()`.
- Untuk input nilai desimal, `Scanner` mengikuti *locale* sistem; bila `nextDouble()` gagal dengan titik (misalnya locale Indonesia memakai koma), gunakan `Double.parseDouble(input.nextLine())` atau set `Locale.US` pada `Scanner`.
- `FileWriter(NAMA_FILE)` menimpa file lama; gunakan `new FileWriter(NAMA_FILE, true)` hanya bila ingin append (di luar lingkup).
- Gunakan `String.format` dengan `%-18s` dan `%-6.1f` agar kolom tabel rapi.
