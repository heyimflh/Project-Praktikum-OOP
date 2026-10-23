# Sistem Perpustakaan Mini "PustakaUNS"

Proyek Aplikasi Console Manajemen Perpustakaan Program Studi berbasis Java (JDK Standar) untuk Responsi 1 Praktikum Pemrograman Berorientasi Objek.

---

## 1. Struktur Package & File Proyek

```
Responsi1/
├── src/
│   └── responsi/
│       ├── model/
│       │   ├── Buku.java                   (Entitas Buku, Serializable, static totalBuku, assertion invarian)
│       │   └── Anggota.java                (Entitas Anggota, Serializable, List<Buku> daftarPinjam)
│       ├── exception/
│       │   ├── BukuTidakTersediaException.java (Checked Exception stok habis)
│       │   └── BatasPinjamException.java   (Checked Exception batas pinjam tercapai)
│       ├── service/
│       │   └── Perpustakaan.java           (Logika bisnis, konstanta terpusat, array & collections)
│       ├── util/
│       │   ├── FileHandler.java            (I/O file teks katalog.txt & serialisasi data.obj)
│       │   └── Validator.java              (Validasi ID anggota, normalisasi judul, string reverse)
│       └── main/
│           └── Main.java                   (Menu CLI interaktif, loop do-while & switch, try-catch-finally)
├── out/                                    (Bytecode hasil kompilasi .class)
├── katalog.txt                             (Penyimpanan teks katalog buku format CSV titik koma)
├── data.obj                                (Penyimpanan biner serialisasi objek anggota)
├── screenshots/                            (Panduan & bukti tangkapan layar pengujian)
├── PRD.md                                  (Product Requirements Document v1.0)
└── README.md                               (Dokumentasi lengkap proyek)
```

---

## 2. Pemetaan Konsep Praktikum PBO (Bagian A - G)

| Bagian | Konsep Praktikum | Implementasi pada Kode | Lokasi File |
|---|---|---|---|
| **A** | Class, Object, Constructor, `this`, 4 Jenis Variabel | - Constructor berparameter dengan `this` di semua model.<br>- Komentar eksplisit 4 jenis variabel: `instance`, `class (static)`, `local`, `parameter`.<br>- Variabel statis `totalBuku` bertambah setiap objek dibuat.<br>- Method `tampilkanInfo()` memuat variabel lokal. | `responsi/model/Buku.java`<br>`responsi/model/Anggota.java` |
| **B** | Konstanta & Reference Types | - Seluruh konstanta (`MAX_PINJAM`, `DENDA_PER_HARI`, `LAMA_PINJAM`, `STATUS_AKTIF`, dll.) terpusat di `Perpustakaan` tanpa angka ajaib.<br>- Method `demoReference()` mendemonstrasikan mutasi objek bersama di heap (`b1 == b2`), default `null` dan `0`, serta pencegahan NPE. | `responsi/service/Perpustakaan.java` |
| **C** | Kondisional & Looping | - Loop utama memakai `do-while` dan `switch-case`.<br>- Perhitungan denda memakai struktur berjenjang `if - else if - else`.<br>- Penelusuran data dengan `for-each` dan penomoran urut dengan `for biasa`.<br>- Input ulang validasi ID menggunakan loop `while`. | `responsi/main/Main.java`<br>`responsi/service/Perpustakaan.java` |
| **D** | String & Character | - Validasi ID (`length()`, `startsWith("AGT")`, `Character.isDigit()`).<br>- Normalisasi judul ke Title Case menggunakan `StringBuilder` dan `trim()`.<br>- Pencarian judul case-insensitive (`toLowerCase()` + `contains()`).<br>- Hitung vokal (`hitungVokal`), huruf & angka (`hitungHurufDanAngka`).<br>- Balik string untuk kode verifikasi (`balikString`).<br>- Perbandingan string konsisten memakai `equals()` / `equalsIgnoreCase()`.<br>- Escape sequence tab (`\t`) pada tabel output. | `responsi/util/Validator.java`<br>`responsi/main/Main.java` |
| **E** | Exception & Assertion | - Dua custom checked exception: `BukuTidakTersediaException` & `BatasPinjamException`.<br>- `pinjamBuku` mendeklarasikan `throws`.<br>- Penanganan dengan `try-catch-finally`, `multiple catch`, dan `nested try-catch`.<br>- Blok `finally` selalu mencetak "Transaksi selesai."<br>- Assertion pada 3 tempat (parameter, invarian stok `>= 0`, invarian jumlah pinjam `<= MAX_PINJAM`).<br>- Penjelasan perbedaan assertion vs exception di akhir `Main.java`. | `responsi/exception/`<br>`responsi/service/Perpustakaan.java`<br>`responsi/main/Main.java` |
| **F** | Array & Collection | - Array `int[] pinjamPerHari = new int[7]` untuk transaksi harian.<br>- List (`ArrayList`) untuk katalog buku & daftar pinjam anggota (lengkap dengan add, remove, search).<br>- Set (`TreeSet`) untuk kategori unik tanpa duplikat.<br>- Queue (`LinkedList`) untuk antrian peminjam buku habis stok beserta operasi `poll()`. | `responsi/service/Perpustakaan.java` |
| **G** | Input / Output (I/O) | - Masukan console via `Scanner(System.in)`, keluaran via `System.out`.<br>- Simpan/muat file teks `katalog.txt` via `BufferedWriter`/`BufferedReader` (`split(";")`).<br>- Simpan/muat serialisasi objek `data.obj` via `ObjectOutputStream`/`ObjectInputStream`.<br>- Seluruh operasi file menggunakan `try-with-resources` dan penanganan error ramah tanpa crash. | `responsi/util/FileHandler.java` |

---

## 3. Cara Kompilasi dan Menjalankan

Buka terminal di direktori `Responsi1`:

### Kompilasi Semua File
```powershell
javac -Xlint:all -d out (Get-ChildItem -Recurse -Filter *.java src).FullName
```

### Menjalankan Program Normal (Assertion Aktif)
```powershell
java -ea -cp out responsi.main.Main
```

### Menjalankan Demo Reference Types (FR-B5)
```powershell
java -ea -cp out responsi.main.Main demo
```

### Menjalankan Demo Kegagalan Assertion (Skenario 10)
```powershell
java -ea -cp out responsi.main.Main demo-assert
```
*(Akan memicu `AssertionError: Stok tidak boleh negatif` membuktikan assertion bekerja saat `-ea` diaktifkan)*

---

## 4. Status Pengujian 10 Skenario Wajib

| No | Skenario Pengujian | Hasil Pengujian | Status |
|:---:|---|---|:---:|
| **1** | Mendaftarkan ID `agt12` ditolak dan diminta mengulang | Ditolak oleh `Validator.validasiIdAnggota()`. Loop while meminta ulang sampai ID `AGT001` dimasukkan dan berhasil didaftarkan. | **LULUS** |
| **2** | Menambahkan buku dengan judul `"  pemrograman berorientasi objek  "` | Judul dinormalisasi otomatis menjadi `Pemrograman Berorientasi Objek` saat ditampilkan di katalog. | **LULUS** |
| **3** | Anggota meminjam buku ke-4 | Peminjaman 1-3 berhasil; peminjaman ke-4 melempar `BatasPinjamException` dengan pesan batas tercapai dan mencetak "Transaksi selesai." | **LULUS** |
| **4** | Meminjam buku dengan stok 0 (`BK009`) | Melempar `BukuTidakTersediaException`, anggota dimasukkan ke antrian antrian (posisi 1), dan mencetak "Transaksi selesai." | **LULUS** |
| **5** | Pengembalian buku terlambat 10 hari | Denda dihitung dengan tarif tingkat 2 (10 x 2.000) = **Rp20.000**. | **LULUS** |
| **6** | Pengembalian buku terlambat 16 hari | Denda dihitung dengan tarif tingkat 3 (16 x 3.000) = **Rp48.000** dan mencetak `[PERINGATAN] Anggota diblokir sementara`. | **LULUS** |
| **7** | Masukan huruf pada input angka (menu/tahun/stok/hari terlambat) | `NumberFormatException` / `InputMismatchException` tertangkap, menampilkan pesan ramah dan meminta input ulang tanpa crash. | **LULUS** |
| **8** | Simpan data (Menu 8) -> Tutup -> Buka lagi -> Muat data (Menu 9) | Seluruh katalog dan data anggota pulih secara identik. | **LULUS** |
| **9** | Muat data saat file `katalog.txt` / `data.obj` tidak ada | Menampilkan informasi ramah bahwa file belum ditemukan tanpa melempar exception crash. | **LULUS** |
| **10** | Menjalankan dengan flag `-ea` dan memaksakan stok negatif | Melempar `java.lang.AssertionError: Stok tidak boleh negatif` jika dijalankan dengan `java -ea -cp out responsi.main.Main demo-assert`. | **LULUS** |
