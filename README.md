# 🚀 Project Praktikum Pemrograman Berorientasi Objek (Java)

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![OOP](https://img.shields.io/badge/Paradigm-OOP-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Maintained-Active-brightgreen?style=for-the-badge)
![License](https://img.shields.io/badge/License-Academic%20Use-lightgrey?style=for-the-badge)

<p align="center">
  <b>Koleksi Repositori Tugas & Studi Kasus Praktikum Pemrograman Berorientasi Objek (PBO)</b><br>
  <i>Program Studi Informatika PSDKU — Semester 3</i>
</p>

</div>

---

## 👋 Sambutan & Pengantar

> **Halo guys! 👋 Selamat datang di repositori ini!**  
> Repositori ini aku buat khusus untuk menyimpan, mengelola, dan mendokumentasikan seluruh program tugas Praktikum Pemrograman Berorientasi Objek (OOP) berbasis Java selama masa perkuliahan.  
> 
> Harapannya, repositori ini bukan cuma jadi arsip pribadi, tapi juga bisa bermanfaat buat teman-teman yang lagi belajar konsep-konsep OOP seperti *Class*, *Encapsulation*, *Inheritance*, *Polymorphism*, *Checked Exception*, *Assertion*, hingga manipulasi struktur data dan koleksi dinamis (*Java Collections Framework*).  
> 
> *Feel free to explore, clone, or star this repo if you find it helpful! Happy coding!* ☕💻

---

## 👨‍💻 Profil Mahasiswa

<div align="center">

| 🏷️ Profil | 📝 Informasi Lengkap |
| :--- | :--- |
| **Nama Lengkap** | **Muhammad Fakhri Abdullah** |
| **NIM** | **L0325030** |
| **Kelas** | **B Informatika PSDKU** |
| **Institusi** | **Universitas Sebelas Maret (UNS)** |
| **Fokus Studi** | Pemrograman Berorientasi Objek (Java SE 11+) |

</div>

---

## 🧭 Pusat Navigasi Program (Project Navigation Hub)

Gunakan tabel navigasi di bawah ini sebagai narahubung utama untuk melompat langsung ke folder tugas, memeriksa dokumentasi detail, maupun memahami konsep yang dipelajari pada masing-masing pertemuan:

| No | Modul / Tugas | Topik & Studi Kasus | Konsep OOP Utama | Tautan Langsung | Status |
| :---: | :--- | :--- | :--- | :---: | :---: |
| **01** | **Tugas 02** | **Sistem Pendataan Nilai Siswa**<br>*(Student Grade Management System)* | • Class & Object Instantiation<br>• Array of Objects<br>• Basic Control Flow & Scanner | [📂 Buka Folder](./Tugas-02/)<br>[📄 Main.java](./Tugas-02/Main.java) | `SELESAI` ✅ |
| **02** | **Tugas P4** | **Sistem Manajemen Perpustakaan Mini**<br>*(Mini Library Management System)* | • Multi-Package Architecture<br>• Custom Checked Exceptions<br>• Java Assertions (`-ea`)<br>• String & Character Manipulation<br>• `ArrayList` & `HashMap` Collection<br>• Analytic Aggregation Engine | [📂 Buka Folder](./Tugas_P4/)<br>[📖 Baca README Detail](./Tugas_P4/README.md)<br>[📋 PRD Spesifikasi](./Tugas_P4/PRD.md) | `SELESAI` ✅ |
| **03** | **Tugas Berikutnya** | *Akan diperbarui seiring perkuliahan...* | *Polymorphism, Abstract Class, Interface, GUI, dll.* | *Segera Hadir* ⏳ | `PROGRESS` 🚧 |

---

## 🔍 Ringkasan & Sorotan Modul

<details open>
<summary><b>📚 1. Tugas_P4 — Mini Library Management System (Unggulan)</b></summary>
<br>

Aplikasi sistem perpustakaan mini berbasis CLI interaktif yang dirancang dengan standar kualitas *production-grade*:
- **Pemisahan Modul (Package):** Terbagi rapi ke dalam `library.model`, `library.service`, `library.exception`, dan `library.main`.
- **Integritas Bisnis Berlapis:** Memeriksa kuota pinjam anggota (maksimal 3 buku), mencegah peminjaman ganda (*double borrowing*), dan memvalidasi sintaks format ID anggota via *Assertion*.
- **Mesin Laporan Otomatis:** Agregasi frekuensi kategori buku paling populer dan identifikasi anggota paling aktif secara *real-time*.
- **User Experience (UX) Terminal:** Menggunakan format tabel ASCII dinamis dengan sistem pemotongan string otomatis (`.substring()`) agar tata letak konsol selalu simetris dan rapi.

👉 **[Baca Dokumentasi Lengkap & Analisis Kode Tugas P4 di Sini](./Tugas_P4/README.md)**

</details>

<br>

<details>
<summary><b>🎓 2. Tugas-02 — Sistem Pendataan Nilai Siswa</b></summary>
<br>

Aplikasi pencatatan performa akademik siswa yang mendemonstrasikan dasar penginstansian array bertipe objek:
- Menerapkan class `Student` sebagai model data nilai dan status kelulusan.
- Memproses rata-rata nilai kelas, rekapitulasi jumlah siswa lulus/tidak lulus, serta pencarian nilai tertinggi & terendah.

👉 **[Buka Direktori Tugas-02](./Tugas-02/)**

</details>

---

## 🛠️ Prasyarat & Lingkungan Pengembangan

| Komponen | Spesifikasi / Rekomendasi |
| :--- | :--- |
| **JDK (Java Development Kit)** | Oracle JDK / OpenJDK 11 atau versi lebih baru (JDK 17 / 21 LTS direkomendasikan) |
| **Runtime Flag** | Parameter VM `-ea` (*Enable Assertions*) wajib dinyalakan pada modul tertentu |
| **IDE / Editor** | VS Code / IntelliJ IDEA / Eclipse |
| **Terminal / CLI** | PowerShell, Bash, CMD, atau Zsh |

---

## ⚡ Panduan Menjalankan Proyek Secara Cepat

### 1. Clone Repositori
```bash
git clone https://github.com/heyimflh/Project-Praktikum-OOP.git
cd Project-Praktikum-OOP
```

### 2. Menjalankan Modul Tertentu
Setiap modul dapat dikompilasi dan dijalankan secara mandiri. Contoh untuk **Tugas P4 (Sistem Perpustakaan Mini)**:

```powershell
# 1. Pindah ke direktori tugas
cd Tugas_P4

# 2. Kompilasi seluruh package
javac library/exception/*.java library/model/*.java library/service/*.java library/main/*.java

# 3. Jalankan aplikasi dengan mengaktifkan Assertion (-ea)
java -ea library.main.MainApp
```

---

## 💡 Konsep OOP yang Diimplementasikan di Repo Ini

```text
📦 Project-Praktikum-OOP
 ├── 🧩 Encapsulation       -> Penerapan access modifier (private) dan getter/setter
 ├── 🏛️ Inheritance         -> Penurunan hierarki class Exception untuk galat bisnis
 ├── 🛡️ Error Handling      -> Blok try-catch-finally tangguh & anti-crash
 ├── 🔍 Robust Assertion    -> Pengujian prakondisi program internal dengan keyword assert
 ├── 🔤 String & Char API   -> Case-insensitive search, formatting tabular, dan validasi karakter
 └── 📊 Java Collections    -> Pemanfaatan ArrayList dinamis & HashMap O(1) Key-Value
```

---

<div align="center">

Dibuat dengan dedikasi dan semangat belajar oleh **Muhammad Fakhri Abdullah**  
*Praktikum Pemrograman Berorientasi Objek — Teknik Informatika PSDKU*

⭐ **Jangan lupa tinggalkan Star jika repositori ini membantu proses belajarmu!** ⭐

</div>
