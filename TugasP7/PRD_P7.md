# PRD — Program Java Pengelolaan Nilai Mahasiswa

**Tugas:** Pengganti Pertemuan 7
**Bahasa:** Java (JDK 11 atau lebih baru)
**Paradigma:** Object-Oriented Programming (OOP)
**Output permanen:** `laporan_nilai.txt`

---

## 1. Ringkasan Produk

Program konsol (console application) berbasis Java untuk mengelola data nilai mahasiswa. Program:

1. Membaca input dinamis dari pengguna lewat `Scanner`.
2. Memvalidasi data dengan **Assertion** dan **Custom Exception**.
3. Menyimpan banyak objek `Mahasiswa` dalam `ArrayList<Mahasiswa>`.
4. Merapikan teks nama/NIM dengan manipulasi **String** dan **Character**.
5. Menghitung grade, status kelulusan, dan statistik kelas (rata-rata, jumlah lulus, jumlah tidak lulus).
6. Menyimpan rekapitulasi ke file `laporan_nilai.txt` memakai **File I/O** (`BufferedWriter` + `FileWriter`).

PRD ini disusun agar setiap poin pada soal PDF punya padanan yang jelas (lihat bagian **Traceability Matrix**).

---

## 2. Tujuan dan Non-Tujuan

### 2.1 Tujuan
- Menghasilkan program Java yang **utuh, terstruktur rapi, dan bisa dijalankan**.
- Memenuhi **seluruh indikator** dari Pertemuan 2, 3, 4, 5, dan 6.
- Kode bersih: penamaan jelas, ada komentar yang menjelaskan materi yang dipakai.

### 2.2 Non-Tujuan
- Tidak memakai database, GUI, atau framework.
- Tidak memakai array statis biasa untuk menyimpan mahasiswa (wajib `ArrayList`).
- Tidak ada fitur login atau multi-user.

---

## 3. Struktur Package dan File

```
PengelolaanNilai/
└── src/
    ├── model/
    │   ├── Mahasiswa.java
    │   └── NilaiInvalidException.java
    └── app/
        └── Main.java
```

Setelah program dijalankan, file `laporan_nilai.txt` terbentuk di **working directory** (folder tempat program dijalankan).

| Package | Isi | Peran |
|---|---|---|
| `model` | `Mahasiswa`, `NilaiInvalidException` | Kelas data dan exception |
| `app` | `Main` | Kelas utama pengeksekusi (berisi `main()`) |

---

## 4. Spesifikasi Kelas

### 4.1 `model.NilaiInvalidException`

| Item | Spesifikasi |
|---|---|
| Package | `model` |
| Turunan dari | `Exception` (checked exception) |
| Constructor | `NilaiInvalidException(String pesan)` memanggil `super(pesan)` |
| Dilempar (throw) saat | nilai `< 0` **atau** nilai `> 100` |
| Contoh pesan | `"Nilai tidak valid: 105.0. Nilai harus antara 0 sampai 100."` |

### 4.2 `model.Mahasiswa`

**Atribut (private):**

| Atribut | Tipe | Jenis | Keterangan |
|---|---|---|---|
| `nama` | `String` | Reference type | Nama mahasiswa (huruf kapital semua) |
| `nim` | `String` | Reference type | NIM, hanya angka, tidak boleh kosong |
| `nilai` | `double` | Primitive type | Rentang 0 sampai 100 |

**Konstanta:**

```java
public static final double FINAL_PASSING_GRADE = 70.0;
```

Memakai kata kunci `final` sesuai ketentuan (standar kelulusan).

**Constructor:**

```java
public Mahasiswa(String nama, String nim, double nilai) throws NilaiInvalidException
```

Urutan proses di dalam constructor (urutan ini penting):

1. `nama = nama.trim().toUpperCase();` → merapikan nama.
2. `nim = nim.trim();` → merapikan NIM.
3. `assert !nim.isEmpty() : "NIM tidak boleh kosong!";` → **Assertion** (persis seperti soal).
4. Validasi NIM hanya angka: loop tiap karakter dengan `Character.isDigit()`. Jika ada karakter non-angka, lempar `IllegalArgumentException("NIM hanya boleh berisi angka!")`.
5. Validasi nilai: `if (nilai < 0 || nilai > 100) throw new NilaiInvalidException(...)`.
6. Isi atribut `this.nama`, `this.nim`, `this.nilai`.

**Method:**

| Method | Return | Deskripsi |
|---|---|---|
| `getNama()` | `String` | Getter nama |
| `getNim()` | `String` | Getter NIM |
| `getNilai()` | `double` | Getter nilai |
| `getGrade()` | `char` | Memetakan nilai ke grade huruf (lihat 5.2) |
| `isLulus()` | `boolean` | `true` jika `nilai >= FINAL_PASSING_GRADE` |
| `getStatus()` | `String` | `"LULUS"` atau `"TIDAK LULUS"` |
| `toString()` | `String` | Satu baris ringkas data mahasiswa |

> **Catatan Assertion:** `assert` hanya aktif jika program dijalankan dengan flag `-ea`. Karena itu, di `Main` input NIM kosong tetap dicegah lebih dulu lewat pengecekan ulang input (lihat 4.3), sehingga program tidak bergantung pada flag tersebut. Assertion tetap ada di constructor sebagai pengaman sesuai ketentuan soal.

### 4.3 `app.Main`

Kelas utama berisi `public static void main(String[] args)` dan method bantu `static`.

| Method | Deskripsi |
|---|---|
| `main(String[] args)` | Alur utama program |
| `inputMahasiswa(Scanner, ArrayList<Mahasiswa>)` | Membaca nama, NIM, nilai untuk satu mahasiswa, membuat objek, lalu `add()` ke list |
| `hitungRataRata(ArrayList<Mahasiswa>)` | Mengembalikan rata-rata nilai (loop for-each) |
| `hitungJumlahLulus(ArrayList<Mahasiswa>)` | Mengembalikan jumlah mahasiswa lulus |
| `tampilkanRekap(ArrayList<Mahasiswa>)` | Mencetak tabel rekap ke layar |
| `simpanLaporan(ArrayList<Mahasiswa>, String namaFile)` | Menulis laporan ke file teks |

---

## 5. Aturan Bisnis

### 5.1 Validasi Input

| Data | Aturan | Mekanisme | Perilaku saat gagal |
|---|---|---|---|
| Nama | `trim()` lalu `toUpperCase()`; tidak boleh kosong | Pengecekan di `Main` | Minta input ulang |
| NIM | `trim()`; tidak boleh kosong | `assert` di constructor + cek ulang di `Main` | Minta input ulang |
| NIM | Hanya angka | `Character.isDigit()` | Tampilkan pesan, minta input ulang |
| Nilai | Harus angka (double) | `try-catch` `NumberFormatException` | Tampilkan pesan, minta input ulang |
| Nilai | `0 <= nilai <= 100` | `throw NilaiInvalidException` | `catch` di `Main`, tampilkan pesan, minta input ulang |

### 5.2 Pemetaan Nilai ke Grade (If-Else atau Switch-Case)

| Rentang Nilai | Grade | Status |
|---|---|---|
| 85 – 100 | A | LULUS |
| 70 – 84,99 | B | LULUS |
| 55 – 69,99 | C | TIDAK LULUS |
| 0 – 54,99 | D | TIDAK LULUS |

Status kelulusan ditentukan oleh `FINAL_PASSING_GRADE = 70.0`: lulus jika `nilai >= 70.0`.

Implementasi pemetaan memakai struktur **if-else if** (atau `switch` pada `(int) nilai / 5`). Pilih salah satu dan jelaskan lewat komentar.

### 5.3 Statistik Kelas

- **Rata-rata** = total nilai semua mahasiswa / `list.size()`.
- **Jumlah lulus** = banyak mahasiswa dengan `isLulus() == true`.
- **Jumlah tidak lulus** = `list.size()` dikurangi jumlah lulus.
- Jika list kosong, rata-rata dianggap `0.0` (hindari pembagian dengan nol) dan program tidak membuat laporan, atau membuat laporan dengan keterangan "Tidak ada data".

---

## 6. Alur Program

```
MULAI
  │
  ├─ Buat ArrayList<Mahasiswa> daftar
  ├─ Buat Scanner
  ├─ Tanya: "Berapa mahasiswa yang akan diinput?" (validasi angka > 0)
  │
  ├─ ULANGI sebanyak jumlah mahasiswa:
  │     ├─ Input nama  → trim, cek tidak kosong
  │     ├─ Input NIM   → trim, cek tidak kosong
  │     ├─ Input nilai → parse double
  │     ├─ Buat objek Mahasiswa (constructor memvalidasi)
  │     │     ├─ sukses  → daftar.add(mhs)
  │     │     └─ gagal   → catch exception, tampilkan pesan, ulangi input mahasiswa ini
  │
  ├─ Hitung rata-rata, jumlah lulus, jumlah tidak lulus (for-each)
  ├─ Tampilkan rekap di layar
  ├─ Simpan rekap ke laporan_nilai.txt (BufferedWriter + FileWriter)
  ├─ Tampilkan pesan sukses / gagal simpan
  └─ Tutup Scanner
SELESAI
```

---

## 7. Spesifikasi File I/O

| Item | Spesifikasi |
|---|---|
| Nama file | `laporan_nilai.txt` |
| Lokasi | Working directory program |
| Kelas | `BufferedWriter` dibungkus `FileWriter` |
| Mekanisme | **try-with-resources** (utama), atau try-catch-finally |
| Exception | Tangkap `IOException`, tampilkan pesan error yang jelas |
| Mode | Overwrite (file ditulis ulang setiap program dijalankan) |

Contoh penulisan:

```java
try (BufferedWriter writer = new BufferedWriter(new FileWriter("laporan_nilai.txt"))) {
    writer.write("LAPORAN NILAI MAHASISWA");
    writer.newLine();
    // ... tulis data tiap mahasiswa dengan for-each, lalu statistik
} catch (IOException e) {
    System.out.println("Gagal menyimpan laporan: " + e.getMessage());
}
```

### 7.1 Format Isi `laporan_nilai.txt`

```
==============================================
          LAPORAN NILAI MAHASISWA
==============================================
No  NIM          Nama                 Nilai  Grade  Status
----------------------------------------------------------
1   L0325030     BUDI SANTOSO         88.50  A      LULUS
2   L0325031     SITI AMINAH          72.00  B      LULUS
3   L0325032     ANDI PRATAMA         60.00  C      TIDAK LULUS
----------------------------------------------------------
STATISTIK KELAS
Jumlah Mahasiswa   : 3
Rata-rata Nilai    : 73.50
Jumlah Lulus       : 2
Jumlah Tidak Lulus : 1
Standar Kelulusan  : 70.0
==============================================
```

Isi laporan wajib memuat: **daftar seluruh mahasiswa** (NIM, nama, nilai, grade, status) dan **statistik kelas** (rata-rata, jumlah lulus, jumlah tidak lulus).

---

## 8. Contoh Interaksi Konsol

```
=== PROGRAM PENGELOLAAN NILAI MAHASISWA ===
Jumlah mahasiswa yang akan diinput: 3

--- Mahasiswa ke-1 ---
Nama  : budi santoso
NIM   : L0325030
NIM hanya boleh berisi angka! Silakan ulangi.
NIM   : 0325030
Nilai : 120
Nilai tidak valid: 120.0. Nilai harus antara 0 sampai 100.
Nilai : 88.5
Data berhasil ditambahkan.

...

=== REKAP NILAI ===
(tabel seperti pada 7.1)

Laporan berhasil disimpan ke laporan_nilai.txt
```

---

## 9. Traceability Matrix (Soal PDF → Implementasi)

| No | Ketentuan di PDF | Implementasi |
|---|---|---|
| 1a | Package `model` dan `app` | Bagian 3 |
| 1b | Kelas `Mahasiswa` (nama String, nim String, nilai double) | 4.2 |
| 1c | Constructor inisialisasi atribut | 4.2 |
| 1d | `final` untuk `FINAL_PASSING_GRADE = 70.0` | 4.2 |
| 2a | If-Else / Switch-Case untuk grade A-D dan status lulus | 5.2, `getGrade()`, `isLulus()` |
| 2b | Looping (while / for-each) untuk proses daftar dan rata-rata | `hitungRataRata()` |
| 2c | Primitive (`double`, `boolean`) vs reference (`Mahasiswa`, `String`) | 4.2 (komentar kode menjelaskan perbedaan) |
| 3a | Custom Exception `NilaiInvalidException` (nilai < 0 atau > 100) | 4.1, constructor `Mahasiswa` |
| 3b | `assert !nim.isEmpty() : "NIM tidak boleh kosong!";` | 4.2 langkah 3 |
| 3c | `toUpperCase()`, `trim()`, `Character.isDigit()` | 4.2 langkah 1, 2, 4 |
| 4a | `ArrayList<Mahasiswa>` (bukan array statis) | `Main` |
| 4b | `add()`, `size()`, for-each | `Main`, 5.3 |
| 5a | `Scanner` untuk input dinamis banyak mahasiswa | 4.3, bagian 6 |
| 5b | `BufferedWriter` + `FileWriter`, tulis ke `laporan_nilai.txt` dengan statistik | Bagian 7 |
| 5c | Try-Catch-Finally / Try-with-Resources untuk kemungkinan error file | Bagian 7 |

> Catatan: teks PDF terpotong pada poin terakhir ("menangani kemungkinan ..."). Kalimat tersebut diasumsikan berlanjut ke `IOException` saat operasi file, dan PRD ini menanganinya lewat `try-with-resources` + `catch (IOException e)`.

---

## 10. Kebutuhan Non-Fungsional

- **Keterbacaan:** penamaan mengikuti konvensi Java (kelas `PascalCase`, method/variabel `camelCase`, konstanta `UPPER_SNAKE_CASE`).
- **Komentar:** setiap blok materi (assert, exception, Character, Collection, File I/O) diberi komentar singkat.
- **Ketahanan:** input salah tidak membuat program crash; pengguna diminta mengulang.
- **Enkapsulasi:** atribut `private`, akses lewat getter.
- **Portabilitas:** hanya memakai library standar Java (`java.util`, `java.io`).

---

## 11. Cara Kompilasi dan Menjalankan

Dari folder `PengelolaanNilai/`:

```bash
# Kompilasi
javac -d out src/model/*.java src/app/*.java

# Jalankan dengan assertion aktif (-ea)
java -ea -cp out app.Main
```

Gunakan `-ea` agar `assert` benar-benar aktif saat demo atau pengujian.

---

## 12. Skenario Pengujian

| ID | Skenario | Input | Hasil yang diharapkan |
|---|---|---|---|
| T1 | Data valid | `budi santoso`, `0325030`, `88.5` | Tersimpan; nama jadi `BUDI SANTOSO`; grade A; LULUS |
| T2 | Nilai di atas 100 | `105` | `NilaiInvalidException` ditangkap, minta input ulang |
| T3 | Nilai negatif | `-5` | `NilaiInvalidException` ditangkap, minta input ulang |
| T4 | Batas bawah | `0` | Diterima; grade D; TIDAK LULUS |
| T5 | Batas atas | `100` | Diterima; grade A; LULUS |
| T6 | Batas kelulusan | `70` | Diterima; grade B; LULUS |
| T7 | Tepat di bawah batas | `69.99` | Grade C; TIDAK LULUS |
| T8 | NIM kosong | (Enter) | Ditolak, minta input ulang; jika lolos ke constructor dengan `-ea`, `AssertionError` "NIM tidak boleh kosong!" |
| T9 | NIM mengandung huruf | `L0325030` | Ditolak lewat `Character.isDigit()` |
| T10 | NIM dengan spasi | `  0325030  ` | Dirapikan oleh `trim()` |
| T11 | Nilai bukan angka | `abc` | `NumberFormatException` ditangkap, minta input ulang |
| T12 | Rata-rata | 88.5, 72, 60 | Rata-rata `73.50` |
| T13 | File laporan | Selesai input | `laporan_nilai.txt` terbentuk, berisi daftar dan statistik |
| T14 | Gagal tulis file | Folder tanpa izin tulis | Pesan error dari `IOException`, program tidak crash |

---

## 13. Kriteria Penerimaan (Definition of Done)

- [ ] Ada package `model` dan `app`.
- [ ] Kelas `Mahasiswa` punya `nama`, `nim`, `nilai`, constructor, dan konstanta `final FINAL_PASSING_GRADE = 70.0`.
- [ ] Grade A-D dan status lulus ditentukan dengan if-else atau switch.
- [ ] Rata-rata dihitung dengan loop (for-each atau while).
- [ ] `NilaiInvalidException` dilempar untuk nilai < 0 atau > 100.
- [ ] `assert !nim.isEmpty() : "NIM tidak boleh kosong!";` ada di constructor/method.
- [ ] `trim()`, `toUpperCase()`, dan `Character.isDigit()` dipakai.
- [ ] Data disimpan di `ArrayList<Mahasiswa>` dengan `add()`, `size()`, dan for-each.
- [ ] Input memakai `Scanner` dan bisa memasukkan beberapa mahasiswa.
- [ ] `laporan_nilai.txt` ditulis memakai `BufferedWriter` + `FileWriter`.
- [ ] Laporan berisi daftar mahasiswa, rata-rata, jumlah lulus, dan jumlah tidak lulus.
- [ ] Error file ditangani dengan try-with-resources atau try-catch-finally.
- [ ] Program bisa dikompilasi dan dijalankan tanpa error.

---

## 14. Kerangka Kode (Skeleton)

### `model/NilaiInvalidException.java`
```java
package model;

public class NilaiInvalidException extends Exception {
    public NilaiInvalidException(String pesan) {
        super(pesan);
    }
}
```

### `model/Mahasiswa.java`
```java
package model;

public class Mahasiswa {
    public static final double FINAL_PASSING_GRADE = 70.0;

    private String nama;   // reference type
    private String nim;    // reference type
    private double nilai;  // primitive type

    public Mahasiswa(String nama, String nim, double nilai) throws NilaiInvalidException {
        String namaRapi = nama.trim().toUpperCase();
        String nimRapi = nim.trim();

        assert !nimRapi.isEmpty() : "NIM tidak boleh kosong!";

        for (char c : nimRapi.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("NIM hanya boleh berisi angka!");
            }
        }

        if (nilai < 0 || nilai > 100) {
            throw new NilaiInvalidException(
                "Nilai tidak valid: " + nilai + ". Nilai harus antara 0 sampai 100.");
        }

        this.nama = namaRapi;
        this.nim = nimRapi;
        this.nilai = nilai;
    }

    public String getNama() { return nama; }
    public String getNim()  { return nim; }
    public double getNilai() { return nilai; }

    public char getGrade() {
        if (nilai >= 85) return 'A';
        else if (nilai >= 70) return 'B';
        else if (nilai >= 55) return 'C';
        else return 'D';
    }

    public boolean isLulus() {
        return nilai >= FINAL_PASSING_GRADE;
    }

    public String getStatus() {
        return isLulus() ? "LULUS" : "TIDAK LULUS";
    }

    @Override
    public String toString() {
        return String.format("%-10s %-20s %6.2f  %c  %s",
                nim, nama, nilai, getGrade(), getStatus());
    }
}
```

### `app/Main.java` (kerangka)
```java
package app;

import model.Mahasiswa;
import model.NilaiInvalidException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Mahasiswa> daftar = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        // 1. Baca jumlah mahasiswa (validasi angka > 0)
        // 2. Loop: inputMahasiswa(input, daftar) sebanyak jumlah tersebut
        // 3. Hitung statistik, tampilkanRekap(daftar)
        // 4. simpanLaporan(daftar, "laporan_nilai.txt")

        input.close();
    }

    // static void inputMahasiswa(Scanner in, ArrayList<Mahasiswa> daftar) { ... }
    // static double hitungRataRata(ArrayList<Mahasiswa> daftar) { ... }
    // static int hitungJumlahLulus(ArrayList<Mahasiswa> daftar) { ... }
    // static void tampilkanRekap(ArrayList<Mahasiswa> daftar) { ... }
    // static void simpanLaporan(ArrayList<Mahasiswa> daftar, String namaFile) { ... }
}
```

---

## 15. Rencana Pengerjaan (Urutan Disarankan)

1. Buat struktur folder dan package `model` serta `app`.
2. Tulis `NilaiInvalidException`.
3. Tulis `Mahasiswa` (atribut, konstanta, constructor dengan validasi, grade, status).
4. Tulis `Main`: input `Scanner` dengan loop dan `try-catch`.
5. Tambahkan perhitungan statistik dengan for-each.
6. Tambahkan penulisan `laporan_nilai.txt` dengan try-with-resources.
7. Jalankan semua skenario di bagian 12 dengan flag `-ea`.
8. Cek ulang checklist di bagian 13 sebelum dikumpulkan.
