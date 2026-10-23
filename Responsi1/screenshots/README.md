# Folder Screenshot Hasil Pengujian PustakaUNS

Folder ini disediakan untuk menyimpan tangkapan layar (screenshot) sebagai bukti hasil pengujian program sesuai dengan ketentuan PRD (Bagian 13 dan Bagian 14).

## Daftar Screenshot yang Diperlukan:
1. `01_menu_utama_tambah_buku.png`: Menu utama & tambah buku dengan normalisasi judul (`" pemrograman berorientasi objek "` -> `"Pemrograman Berorientasi Objek"`).
2. `02_daftarkan_anggota_validasi.png`: Pendaftaran anggota dengan ID `agt12` ditolak oleh `Validator`, lalu input `AGT001` diterima.
3. `03_pinjam_sukses_kode_verifikasi.png`: Peminjaman sukses mencetak sisa stok, batas pinjam 7 hari, kode verifikasi dibalik (`100KB`), dan "Transaksi selesai."
4. `04_batas_pinjam_exception.png`: Anggota meminjam buku ke-4 melempar `BatasPinjamException` dan ditangani dengan ramah.
5. `05_stok_habis_antrian.png`: Meminjam buku stok 0 (`BK009`) melempar `BukuTidakTersediaException` dan masuk antrian posisi 1.
6. `06_pengembalian_denda_10_hari.png`: Pengembalian buku terlambat 10 hari menghasilkan denda Rp20.000.
7. `07_pengembalian_denda_16_hari_blokir.png`: Pengembalian terlambat 16 hari menghasilkan denda Rp48.000 dan peringatan `[PERINGATAN] Anggota diblokir sementara`.
8. `08_error_handling_input_huruf.png`: Memasukkan karakter huruf pada input angka (menu/tahun/stok/terlambat) ditangani ramah tanpa crash.
9. `09_simpan_dan_muat_data.png`: Simpan data (Menu 8), keluar, buka kembali, lalu muat data (Menu 9) dan tampilkan katalog (Menu 2).
10. `10_muat_file_tidak_ada.png`: Menjalankan Menu 9 saat file belum ada (menampilkan info ramah tanpa crash).
11. `11_rekap_mingguan.png`: Tampilan rekap mingguan dengan tabel tabulasi, total transaksi, dan hari tersibuk.
12. `12_demo_reference.png`: Menjalankan `java -ea -cp out responsi.main.Main demo` (membuktikan b1==b2, mutasi heap, dan null check).
13. `13_assertion_berhasil_dan_gagal.png`: 
    - Berhasil: alur normal dijalankan dengan `-ea` tanpa AssertionError.
    - Gagal: `java -ea -cp out responsi.main.Main demo-assert` memicu `AssertionError: Stok tidak boleh negatif`.
