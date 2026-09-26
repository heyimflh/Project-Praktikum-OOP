# Laporan Praktikum Pemrograman Berorientasi Objek
# Sistem Manajemen Perpustakaan Mini (Mini Library Management System)

---

### Informasi Mahasiswa
| Atribut | Keterangan |
| :--- | :--- |
| **Nama Lengkap** | Muhammad Fakhri Abdullah |
| **NIM** | L0325030 |
| **Kelas** | B Informatika PSDKU |
| **Mata Kuliah** | Praktikum Pemrograman Berorientasi Objek |
| **Bahasa Pemrograman** | Java (JDK 11+) |
| **Paradigma** | Object-Oriented Programming (OOP) |

---

## Daftar Isi
1. [Latar Belakang & Deskripsi Sistem](#1-latar-belakang--deskripsi-sistem)
2. [Tujuan Pembelajaran & Ruang Lingkup](#2-tujuan-pembelajaran--ruang-lingkup)
3. [Arsitektur Proyek & Struktur Package](#3-arsitektur-proyek--struktur-package)
4. [Pemetaan Konsep OOP & Fitur Bahasa Java](#4-pemetaan-konsep-oop--fitur-bahasa-java)
5. [Bedah Kode Sumber & Penjelasan Sintaks Detail](#5-bedah-kode-sumber--penjelasan-sintaks-detail)
   - [5.1 Package `library.model`](#51-package-librarymodel)
   - [5.2 Package `library.exception`](#52-package-libraryexception)
   - [5.3 Package `library.service`](#53-package-libraryservice)
   - [5.4 Package `library.main`](#54-package-librarymain)
6. [Alur Bisnis & Mekanisme Validasi (Flow Logic)](#6-alur-bisnis--mekanisme-validasi-flow-logic)
7. [Panduan Kompilasi & Eksekusi Program](#7-panduan-kompilasi--eksekusi-program)
8. [Analisis Hasil Pengujian & Output Sistem](#8-analisis-hasil-pengujian--output-sistem)
9. [Kesimpulan](#9-kesimpulan)

---

## 1. Latar Belakang & Deskripsi Sistem

Pada sistem perpustakaan konvensional, pengelolaan data buku dan pencatatan sirkulasi peminjaman sering kali mengalami kendala dalam hal validasi integritas data, pembatasan kuota peminjaman anggota, serta keterlambatan dalam menyusun statistik buku populer.

**Mini Library Management System** dirancang sebagai aplikasi *Console-based* (CLI) interaktif berbasis Java yang mengimplementasikan prinsip-prinsip Pemrograman Berorientasi Objek (OOP) secara komprehensif. Aplikasi ini **bukan sekadar sistem CRUD (Create, Read, Update, Delete) sederhana**, melainkan mengintegrasikan validasi integritas internal (*assertions*), mekanisme proteksi transaksi bisnis berbasis *custom checked exceptions*, manipulasi string tingkat lanjut untuk kebutuhan penelusuran data (*case-insensitive search*), dan mesin analitik berbasis koleksi (*Java Collections Framework*) untuk menyajikan laporan ringkasan berkala secara *real-time*.

---

## 2. Tujuan Pembelajaran & Ruang Lingkup

### 2.1 Tujuan Pembelajaran
1. **Penerapan Struktur OOP Fundamental:** Mengonstruksi *Class*, menginstansiasi *Object*, merancang *Constructor*, memanfaatkan *Encapsulation* melalui *Access Modifiers*, dan memisahkan modul ke dalam *Package*.
2. **Pemanfaatan Tipe Data Primitif vs Referensi:** Membedakan alokasi nilai tipe primitif (`int`, `boolean`) dengan tipe referensi (`String`, `ArrayList`, `HashMap`, objek buatan).
3. **Mekanisme Penanganan Galat (Robust Exception Handling):** Membangun *Custom Checked Exceptions* turunan dari class `java.lang.Exception` serta memitigasi anomali input menggunakan blok `try-catch`.
4. **Validasi Kondisi Internal dengan Assertion:** Menguji prakondisi logika pemrograman menggunakan kata kunci `assert` guna menjamin keabsahan data sebelum manipulasi status objek dijalankan.
5. **Manipulasi String & Karakter:** Menerapkan fungsi-fungsi bawaan class `String` dan `Character` untuk standardisasi format, pembersihan spasi, pencarian fleksibel, dan validasi karakter pertama.
6. **Agregasi Data & Collections Framework:** Memanfaatkan `ArrayList` untuk daftar dinamis dan `HashMap` untuk perhitungan frekuensi dan pengelompokan (*grouping/aggregation*).

### 2.2 Batasan Sistem (Scope & Assumptions)
- Penyimpanan data bersifat **In-Memory** (data tersimpan dalam memori RAM selama siklus hidup program berjalan).
- Setiap buku dianggap memiliki kuantitas tunggal (1 judul = 1 eksemplar fisik).
- Kuota peminjaman buku dibatasi maksimal **3 buku aktif** per anggota.
- ID Anggota diinput secara manual dan harus memenuhi konvensi diawali dengan karakter huruf kapital.

---

## 3. Arsitektur Proyek & Struktur Package

Proyek ini dibangun dengan mematuhi prinsip *Separation of Concerns* (pemisahan tanggung jawab kode) dengan membagi kode ke dalam 4 *package*:

```text
Tugas_P4/
├── library/
│   ├── exception/
│   │   ├── BookNotFoundException.java        # Galat ketika buku tidak ditemukan
│   │   ├── BookAlreadyBorrowedException.java # Galat ketika buku sedang dipinjam
│   │   └── BorrowLimitExceededException.java # Galat ketika kuota peminjaman penuh (>=3)
│   ├── model/
│   │   ├── Book.java                         # Entitas data buku
│   │   └── Member.java                       # Entitas data anggota perpustakaan
│   ├── service/
│   │   └── LibraryService.java               # Logika bisnis, manajemen data, dan analitik
│   └── main/
│       └── MainApp.java                      # Antarmuka CLI, parsing input, dan routing menu
├── PRD.md                                    # Dokumen spesifikasi kebutuhan produk
└── README.md                                 # Dokumentasi teknis & laporan praktikum
```

---

## 4. Pemetaan Konsep OOP & Fitur Bahasa Java

Tabel berikut menunjukkan keselarasan antara materi perkuliahan dengan implementasi nyata pada kode sumber:

| Konsep / Fitur Java | Letak Implementasi | Deskripsi Singkat Implementasi |
| :--- | :--- | :--- |
| **Class & Object** | `Book`, `Member`, `LibraryService` | Representasi entitas perpustakaan beserta instansiasinya. |
| **Encapsulation** | Semua atribut di package `model` | Atribut diatur `private`, akses mutasi dikontrol via *getter/setter*. |
| **Constructor Overloading/Init** | `Book(...)`, `Member(...)` | Menginisialisasi *state* awal objek seperti ketersediaan dan koleksi list. |
| **Package Separation** | 4 sub-package pada `library.*` | Memisahkan domain model, exception, service bisnis, dan UI konsol. |
| **Primitive Types** | `int tahunTerbit`, `boolean statusKetersediaan`, `int jumlahDipinjam` | Menyimpan nilai skalar efisien langsung di *stack memory*. |
| **Reference Types** | `String`, `ArrayList<Book>`, `HashMap<String, Member>` | Menyimpan referensi alamat objek dinamis pada *heap memory*. |
| **Control Structures** | `switch-case` di `MainApp`, `for-each`, `if-else` | Mengontrol jalannya alur menu, validasi status, dan iterasi pencarian. |
| **Custom Checked Exception** | Package `library.exception` | Memaksa *caller* menangani skenario batas logika bisnis. |
| **Try-Catch-Finally** | `MainApp.java` | Menangkap kesalahan input dan checked exception agar program tidak *crash*. |
| **Java Assertion (`assert`)** | `LibraryService.java` (`pinjamBuku`) | Memastikan variabel tidak null dan ID tidak kosong sebelum dieksekusi. |
| **Manipulasi Karakter** | `Character.isUpperCase(id.charAt(0))` | Memvalidasi sintaks format ID anggota pada assertion. |
| **Manipulasi String** | `toLowerCase()`, `contains()`, `equalsIgnoreCase()`, `trim()`, `substring()` | Digunakan pada mesin pencarian, pembersihan whitespace, dan formatting UI. |
| **Collections Framework** | `ArrayList` & `HashMap` | Mengelola koleksi buku, riwayat pinjaman, dan penghitungan buku per kategori. |

---

## 5. Bedah Kode Sumber & Penjelasan Sintaks Detail

### 5.1 Package `library.model`

#### A. `Book.java`
Class ini merepresentasikan entitas buku perpustakaan.

```java
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
        this.statusKetersediaan = true; // Inisialisasi default buku tersedia
        this.jumlahDipinjam = 0;        // Inisialisasi frekuensi awal
    }
    // ... getter, setter, dan metode operasional ...
```
- **Sintaks `this`:** Digunakan untuk membedakan antara parameter konstruktor dengan atribut internal *instance* class.
- **Pencarian Case-Insensitive (`cocokDenganKataKunci`):**
  ```java
  public boolean cocokDenganKataKunci(String keyword) {
      return this.judul.toLowerCase().contains(keyword.toLowerCase());
  }
  ```
  Sintaks ini mengubah judul buku dan kata kunci pencarian ke dalam huruf kecil (*lowercase*) sebelum melakukan pengecekan *substring* dengan metode `.contains()`. Hal ini menjamin fleksibilitas input user.
- **Formatting Representasi Teks (`toString`):**
  Menggunakan `String.format()` dengan pemotongan teks melalui `.substring()` untuk merapikan tampilan kolom tabel konsol agar tidak bergeser jika judul buku terlalu panjang.

#### B. `Member.java`
Class ini merepresentasikan data anggota peminjam.

```java
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
```
- **Komposisi Koleksi:** `daftarPinjaman` bertipe `ArrayList<Book>` menyimpan daftar buku yang sedang dipegang secara aktif oleh anggota.
- **Logika Batas Kuota:**
  ```java
  public boolean sudahMencapaiBatasPinjam() {
      return this.daftarPinjaman.size() >= 3;
  }
  ```
  Evaluasi boolean langsung terhadap ukuran list pinjaman aktif untuk memvalidasi aturan bisnis perpustakaan.

---

### 5.2 Package `library.exception`

Tiga class pengecualian dibuat dengan menurunkan class `java.lang.Exception`:
1. **`BookNotFoundException.java`**: Dilempar saat pencarian buku gagal atau buku yang akan dikembalikan tidak ditemukan di daftar pinjaman anggota.
2. **`BookAlreadyBorrowedException.java`**: Dilempar ketika anggota mencoba meminjam buku yang atribut `statusKetersediaan`-nya bernilai `false`.
3. **`BorrowLimitExceededException.java`**: Dilempar ketika method `sudahMencapaiBatasPinjam()` bernilai `true`.

```java
package library.exception;

public class BorrowLimitExceededException extends Exception {
    public BorrowLimitExceededException(String message) {
        super(message); // Meneruskan pesan error ke superclass Exception
    }
}
```
*Catatan:* Karena mewarisi `Exception` (bukan `RuntimeException`), ketiganya bertindak sebagai *Checked Exception* yang mewajibkan penanganan eksplisit (*compile-time enforcement*) via klausa `throws` dan blok `try-catch`.

---

### 5.3 Package `library.service`

Class `LibraryService.java` merupakan pengendali proses operasional perpustakaan.

```java
package library.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// ... import model & exception ...

public class LibraryService {
    private ArrayList<Book> daftarBuku;
    private HashMap<String, Member> daftarAnggota;
    private int totalTransaksiPinjam;
```

#### A. Pemanfaatan Struktur Data HashMap
Anggota perpustakaan disimpan dalam `HashMap<String, Member>` di mana ID Anggota menjadi kunci (*key*). Keuntungan:
- Akses pencarian anggota memiliki kompleksitas waktu rata-rata $O(1)$ melalui pemanggilan `daftarAnggota.get(idAnggota)`.

#### B. Logika Validasi Peminjaman & Penerapan Assertion
```java
public Book pinjamBuku(String idAnggota, String judulBuku) 
        throws BookNotFoundException, BookAlreadyBorrowedException, BorrowLimitExceededException {
    
    Member member = daftarAnggota.get(idAnggota);
    if (member == null) {
        member = new Member(idAnggota, "Anggota " + idAnggota);
        tambahAnggota(member);
    }

    // 1. Validasi Internal Menggunakan Assertion
    assert member != null : "Anggota tidak valid (null)";
    assert member.getId() != null && !member.getId().trim().isEmpty() : "ID anggota tidak boleh kosong";
    assert Character.isUpperCase(member.getId().charAt(0)) : "ID anggota harus diawali huruf kapital";

    // 2. Pencarian Buku
    Book targetBook = null;
    for (Book b : daftarBuku) {
        if (b.cocokDenganKataKunci(judulBuku)) {
            targetBook = b;
            break;
        }
    }

    if (targetBook == null) {
        throw new BookNotFoundException("Buku dengan kata kunci '" + judulBuku + "' tidak ditemukan di perpustakaan.");
    }

    // 3. Pengecekan Ketersediaan
    if (!targetBook.isStatusKetersediaan()) {
        throw new BookAlreadyBorrowedException("Buku '" + targetBook.getJudul() + "' sedang dipinjam oleh anggota lain.");
    }

    // 4. Pengecekan Kuota Peminjaman
    if (member.sudahMencapaiBatasPinjam()) {
        throw new BorrowLimitExceededException("Anggota '" + member.getNama() + "' sudah mencapai batas maksimal peminjaman (3 buku).");
    }

    // Eksekusi Peminjaman
    targetBook.setStatusKetersediaan(false);
    targetBook.tambahHitunganPinjam();
    member.tambahPinjaman(targetBook);
    member.tambahTotalPinjamanSepanjangWaktu();
    this.totalTransaksiPinjam++;
    
    return targetBook;
}
```
- **Return Type `Book`:** Mengembalikan referensi objek buku yang berhasil dipinjam agar antarmuka UI dapat menampilkan informasi detail buku tersebut kepada pengguna.

#### C. Agregasi Statistik & Analisis Laporan
```java
public Map<String, Integer> hitungJumlahBukuPerKategori() {
    Map<String, Integer> rekap = new HashMap<>();
    for (Book b : daftarBuku) {
        String kat = b.getKategori();
        rekap.put(kat, rekap.getOrDefault(kat, 0) + 1);
    }
    return rekap;
}
```
- **Sintaks `rekap.getOrDefault(kat, 0) + 1`:** Pola ringkas untuk menghitung frekuensi distribusi buku. Jika kategori belum ada di Map, fungsi mengembalikan nilai dasar `0` lalu menambahkan `1`.

---

### 5.4 Package `library.main`

Class `MainApp.java` menyajikan antarmuka pengguna berbasis terminal yang interaktif dan *resilient* (tidak mudah *crash*).

#### A. Pencegahan Input Mismatch Crash
```java
int pilihan = 0;
try {
    pilihan = scanner.nextInt();
    scanner.nextLine(); // Membersihkan sisa baris (\n) dari buffer scanner
} catch (InputMismatchException e) {
    System.out.println("\n[ERROR] Masukkan angka yang valid!");
    scanner.nextLine(); // Membersihkan token string yang keliru
    continue;           // Mengembalikan loop ke menu utama
}
```
Teknik ini mencegah program terhenti (*runtime abnormal termination*) saat pengguna memasukkan karakter alfabet padahal sistem meminta angka pilihan menu.

#### B. Penanganan Multi-Catch Exception
```java
try {
    Book ygDipinjam = service.pinjamBuku(idPinjam, judulPinjam);
    System.out.println("\n[SUKSES PINJAM] Buku yang dipinjam: \"" + ygDipinjam.getJudul() + "\" oleh " + ygDipinjam.getPenulis());
} catch (BookNotFoundException | BookAlreadyBorrowedException | BorrowLimitExceededException e) {
    System.out.println("\n[GAGAL PINJAM] " + e.getMessage());
} catch (AssertionError e) {
    System.out.println("\n[ERROR VALIDASI] " + e.getMessage());
}
```
Sintaks *multi-catch* memadatkan penanganan beberapa checked exception yang memiliki perlakuan serupa, sekaligus memisahkan penanganan `AssertionError` saat mode debug aktif.

---

## 6. Alur Bisnis & Mekanisme Validasi (Flow Logic)

### Diagram Alur Transaksi Peminjaman Buku:

```text
[User Input: ID Anggota & Judul Buku]
                   │
                   ▼
       Cek Anggota di HashMap?
       ├── Tidak Ada ──> Buat Objek Member Baru & Simpan
       └── Ada       ──> Ambil Objek Member
                   │
                   ▼
     [Evaluasi Assertion (-ea)]
     1. member != null ?
     2. ID tidak kosong ?
     3. ID diawali Huruf Kapital ?
       ├── Tidak Terpenuhi ──> Lempar AssertionError (Validasi Gagal)
       └── Terpenuhi
                   │
                   ▼
       Cari Buku Berdasarkan Judul
       ├── Tidak Ditemukan ──> Throw BookNotFoundException
       └── Ditemukan
                   │
                   ▼
     Cek statusKetersediaan == true ?
       ├── False ─────────────> Throw BookAlreadyBorrowedException
       └── True
                   │
                   ▼
     Cek sudahMencapaiBatasPinjam() (< 3)?
       ├── False (Sudah 3) ───> Throw BorrowLimitExceededException
       └── True (Masih < 3)
                   │
                   ▼
     [Eksekusi Mutasi Status]
     - statusKetersediaan = false
     - jumlahDipinjam++
     - Tambahkan buku ke daftarPinjaman anggota
     - totalPinjamanSepanjangWaktu++
     - totalTransaksiPinjam++
                   │
                   ▼
     [Kembalikan Objek Buku & Cetak Sukses]
```

---

## 7. Panduan Kompilasi & Eksekusi Program

### 7.1 Persyaratan Sistem
- Java Development Kit (JDK) versi 11 ke atas terinstal di sistem.
- Terminal / PowerShell / Command Prompt.

### 7.2 Langkah Kompilasi
Buka terminal dan arahkan *Current Working Directory* ke folder `Tugas_P4`:

```powershell
cd "e:\Folder Tugas-Q\SEMESTER 3\Praktikum OOP\Project-Praktikum-OOP\Tugas_P4"
javac library\exception\*.java library\model\*.java library\service\*.java library\main\*.java
```

### 7.3 Langkah Eksekusi (Mengaktifkan Assertion)
Jalankan program utama dengan menambahkan flag **`-ea` (*enableassertions*)** agar instruksi validasi internal diproses oleh JVM:

```powershell
java -ea library.main.MainApp
```

---

## 8. Analisis Hasil Pengujian & Output Sistem

Pengujian dilakukan untuk membuktikan ketahanan program dalam menangani berbagai variasi skenario kasus (baik skenario ideal/positif maupun skenario batas/negatif):

### Kasus Uji 1: Penambahan Koleksi Buku Baru (Menu 1)
- **Input:**
  - Judul: `Ketika Cinta Bertasbih`
  - Penulis: `Habiburrahman El-Shirazy`
  - Tahun: `2007`
  - Kategori: `Romansa`
- **Output Terminal:**
  ```text
  [SUKSES] Buku "Ketika Cinta Bertasbih" berhasil ditambahkan ke koleksi!
  ```
- **Analisis:** Objek `Book` baru berhasil diinstansiasi dengan `statusKetersediaan = true` dan nilai `jumlahDipinjam = 0`, lalu dimasukkan ke dalam `daftarBuku`.

---

### Kasus Uji 2: Tampilan Tabel Data Buku (Menu 2)
- **Tampilan Konsol:**
  ```text
  [=] --- DAFTAR SELURUH BUKU ---
  +---------------------------------------------------------------------------------------------------------+
  | STATUS     | JUDUL                          | PENULIS              | TAHUN| KATEGORI     | PINJAM
  +---------------------------------------------------------------------------------------------------------+
  | [TERSEDIA] | Laskar Pelangi                 | Andrea Hirata        | 2005 | Fiksi        |  0 x
  | [TERSEDIA] | Bumi Manusia                   | Pramoedya Ananta ... | 1980 | Fiksi        |  0 x
  | [TERSEDIA] | Cosmos                         | Carl Sagan           | 1980 | Sains        |  0 x
  | [TERSEDIA] | A Brief History of Time        | Stephen Hawking      | 1988 | Sains        |  0 x
  | [TERSEDIA] | Sapiens                        | Yuval Noah Harari    | 2011 | Sejarah      |  0 x
  +---------------------------------------------------------------------------------------------------------+
  ```
- **Analisis:** Pemanfaatan format teks terstruktur menghasilkan penyajian data layaknya tabel tabular. Nama penulis yang panjang secara otomatis dipangkas secara rapi (`Pramoedya Ananta ...`) sehingga lebar kolom tetap konsisten.

---

### Kasus Uji 3: Pencarian Judul Fleksibel / Case-Insensitive (Menu 3)
- **Input:** Kata kunci `cOsMoS` atau `hIsToRy`
- **Output Terminal:**
  ```text
  --- HASIL PENCARIAN (JUDUL) ---
  +---------------------------------------------------------------------------------------------------------+
  | [TERSEDIA] | Cosmos                         | Carl Sagan           | 1980 | Sains        |  0 x
  +---------------------------------------------------------------------------------------------------------+
  ```
- **Analisis:** Meskipun pengguna menginputkan kombinasi huruf kapital dan kecil acak, buku tetap ditemukan berkat operasi `.toLowerCase()` pada kedua sisi perbandingan.

---

### Kasus Uji 4: Peminjaman Berhasil dengan Informasi Jelas (Menu 4)
- **Input:**
  - ID Anggota: `A001`
  - Judul Buku: `Cosmos`
- **Output Terminal:**
  ```text
  [SUKSES PINJAM] Buku yang dipinjam: "Cosmos" oleh Carl Sagan
    -> Jangan lupa kembalikan jika sudah selesai dibaca!
  ```
- **Analisis:** Program tidak hanya memunculkan status sukses kosong, melainkan menampilkan judul dan penulis buku yang berhasil dipinjam secara transparan.

---

### Kasus Uji 5: Proteksi Assertion pada Format ID Anggota (Menu 4)
- **Input:**
  - ID Anggota: `a001` *(huruf kecil di awal)*
  - Judul Buku: `Sapiens`
- **Output Terminal:**
  ```text
  [ERROR VALIDASI] ID anggota harus diawali huruf kapital
  ```
- **Analisis:** Sesuai aturan PRD, assertion mengevaluasi `Character.isUpperCase(id.charAt(0))`. Karena bernilai `false`, JVM melempar `AssertionError` yang ditangkap dengan aman oleh blok try-catch di `MainApp`, tanpa menyebabkan aplikasi tertutup paksa.

---

### Kasus Uji 6: Buku Sedang Dipinjam / Double Borrowing (Menu 4)
- **Input:** Anggota `A002` mencoba meminjam `Cosmos` yang statusnya masih dipinjam oleh `A001`.
- **Output Terminal:**
  ```text
  [GAGAL PINJAM] Buku 'Cosmos' sedang dipinjam oleh anggota lain.
  ```
- **Analisis:** Sistem berhasil mendeteksi `statusKetersediaan == false` dan melempar `BookAlreadyBorrowedException`.

---

### Kasus Uji 7: Pembatasan Kuota Maksimal Peminjaman (Menu 4)
- **Skenario:** Anggota `A001` yang telah meminjam 3 buku mencoba meminjam buku ke-4.
- **Output Terminal:**
  ```text
  [GAGAL PINJAM] Anggota 'Budi Santoso' sudah mencapai batas maksimal peminjaman (3 buku).
  ```
- **Analisis:** Method `sudahMencapaiBatasPinjam()` menghasilkan evaluasi `true`, memicu pelemparan `BorrowLimitExceededException`.

---

### Kasus Uji 8: Pengembalian Buku Berhasil (Menu 5)
- **Input:**
  - ID Anggota: `A001`
  - Judul Buku: `Cosmos`
- **Output Terminal:**
  ```text
  [SUKSES KEMBALI] Buku "Cosmos" berhasil dikembalikan.
    -> Terima kasih telah mengembalikan buku tepat waktu!
  ```
- **Analisis:** Buku dihapus dari `daftarPinjaman` milik anggota dan nilai atribut `statusKetersediaan` buku diubah kembali menjadi `true`.

---

### Kasus Uji 9: Laporan Agregasi Otomatis (Menu 6)
- **Tampilan Konsol:**
  ```text
  +=================================================+
  |             LAPORAN PERPUSTAKAAN                |
  +=================================================+
  | Total Buku                : 5                   |
  | Total Anggota             : 2                   |
  | Total Transaksi Peminjaman: 4                   |
  +-------------------------------------------------+
  | [ Peringkat Rekor ]                             |
  | Anggota Paling Aktif : Budi Santoso (3x)        |
  | Buku Sering Dipinjam : Cosmos (2x)              |
  | Kategori Terpopuler  : Sains (2 buku)           |
  +-------------------------------------------------+
  | [ Statistik Kategori ]                          |
  |  - Fiksi                : 2                    |
  |  - Sains                : 2                    |
  |  - Sejarah              : 1                    |
  +=================================================+
  ```
- **Analisis:** Algoritma pemindaian linear berhasil mengidentifikasi peminjam terbanyak sepanjang waktu dan buku yang paling sering disirkulasikan, serta melakukan agregasi kategori menggunakan struktur data Map.

---

### Kasus Uji 10: Ketahanan Terhadap Input Non-Numerik pada Menu
- **Input Pilihan Menu:** `xyz` (Karakter String acak)
- **Output Terminal:**
  ```text
  [ERROR] Masukkan angka yang valid!

  === MENU PERPUSTAKAAN ===
  1. [+] Tambah Buku Baru
  ...
  Pilih menu (1-7):
  ```
- **Analisis:** Pengecualian `InputMismatchException` berhasil diredam. Buffer scanner dibersihkan dan loop perulangan kembali menyajikan menu tanpa interupsi kegagalan program.

---

## 9. Kesimpulan

Proyek **Mini Library Management System** ini telah berhasil mengimplementasikan seluruh target capaian pembelajaran Praktikum Pemrograman Berorientasi Objek. Melalui rancang bangun aplikasi ini, pemahaman terhadap:
1. Pemisahan peran komponen (*Separation of Concerns*) berbasis package;
2. Pengendalian integritas data melalui *Encapsulation*, *Assertion*, dan *Custom Checked Exceptions*;
3. Pengolahan koleksi dinamis dengan *Java Collections Framework* (`ArrayList` dan `HashMap`); serta
4. Desain antarmuka terminal interaktif yang berorientasi pada kenyamanan pengguna (*User Experience*)

telah teruji dan terintegrasi secara harmonis dalam sebuah perangkat lunak yang kokoh, terstruktur, dan terstandarisasi.
