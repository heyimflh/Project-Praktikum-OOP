package library.main;

import library.model.Book;
import library.model.Member;
import library.service.LibraryService;
import library.exception.BookNotFoundException;
import library.exception.BookAlreadyBorrowedException;
import library.exception.BorrowLimitExceededException;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    
    private static void cetakPemisah() {
        System.out.println("+---------------------------------------------------------------------------------------------------------+");
    }

    public static void main(String[] args) {
        LibraryService service = new LibraryService();

        // Data awal
        service.tambahBuku(new Book("Laskar Pelangi", "Andrea Hirata", 2005, "Fiksi"));
        service.tambahBuku(new Book("Bumi Manusia", "Pramoedya Ananta Toer", 1980, "Fiksi"));
        service.tambahBuku(new Book("Cosmos", "Carl Sagan", 1980, "Sains"));
        service.tambahBuku(new Book("A Brief History of Time", "Stephen Hawking", 1988, "Sains"));
        service.tambahBuku(new Book("Sapiens", "Yuval Noah Harari", 2011, "Sejarah"));

        service.tambahAnggota(new Member("A001", "Budi Santoso"));
        service.tambahAnggota(new Member("A002", "Siti Aminah"));

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println("\n+=================================================+");
        System.out.println("|         MINI LIBRARY MANAGEMENT SYSTEM          |");
        System.out.println("+=================================================+");

        while (isRunning) {
            System.out.println("+=================================+");
            System.out.println("|           MENU UTAMA            |");
            System.out.println("+=================================+");
            System.out.println("| 1. [+] Tambah Buku Baru         |");
            System.out.println("| 2. [=] Daftar Seluruh Buku      |");
            System.out.println("| 3. [?] Cari Buku                |");
            System.out.println("| 4. [>] Pinjam Buku              |");
            System.out.println("| 5. [<] Kembalikan Buku          |");
            System.out.println("| 6. [#] Laporan Perpustakaan     |");
            System.out.println("| 7. [x] Keluar Aplikasi          |");
            System.out.println("+---------------------------------+");
            System.out.print("Pilih menu (1-7): ");

            int pilihan = 0;
            try {
                pilihan = scanner.nextInt();
                scanner.nextLine(); // clear buffer
            } catch (InputMismatchException e) {
                System.out.println("\n[ERROR] Masukkan angka yang valid!");
                scanner.nextLine();
                continue;
            }

            System.out.println(); // spasi agar rapi
            
            switch (pilihan) {
                case 1:
                    System.out.println("[+] --- TAMBAH BUKU BARU ---");
                    System.out.print(" -> Judul Buku    : ");
                    String judul = scanner.nextLine();
                    System.out.print(" -> Penulis       : ");
                    String penulis = scanner.nextLine();
                    
                    int tahun = 0;
                    try {
                        System.out.print(" -> Tahun Terbit  : ");
                        tahun = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("\n[ERROR] Tahun harus berupa angka.");
                        scanner.nextLine();
                        break;
                    }
                    
                    System.out.print(" -> Kategori      : ");
                    String kategori = scanner.nextLine();

                    Book bukuBaru = new Book(judul, penulis, tahun, kategori);
                    service.tambahBuku(bukuBaru);
                    System.out.println("\n[SUKSES] Buku \"" + judul + "\" berhasil ditambahkan ke koleksi!");
                    break;

                case 2:
                    System.out.println("[=] --- DAFTAR SELURUH BUKU ---");
                    List<Book> daftar = service.tampilkanSemuaBuku();
                    if (daftar.isEmpty()) {
                        System.out.println("  > Belum ada buku dalam koleksi.");
                    } else {
                        cetakPemisah();
                        System.out.println(String.format("| %-10s | %-30s | %-20s | %-4s | %-12s | %s", "STATUS", "JUDUL", "PENULIS", "TAHUN", "KATEGORI", "PINJAM"));
                        cetakPemisah();
                        for (Book b : daftar) {
                            System.out.println("| " + b);
                        }
                        cetakPemisah();
                    }
                    break;

                case 3:
                    System.out.println("[?] --- CARI BUKU ---");
                    System.out.println("  1. Berdasarkan Judul");
                    System.out.println("  2. Berdasarkan Kategori");
                    System.out.print(" -> Pilih metode pencarian (1/2): ");
                    String metode = scanner.nextLine();

                    if (metode.equals("1")) {
                        System.out.print(" -> Masukkan kata kunci judul: ");
                        String keyword = scanner.nextLine();
                        List<Book> hasilJudul = service.cariBukuByJudul(keyword);
                        
                        System.out.println("\n--- HASIL PENCARIAN (JUDUL) ---");
                        if (hasilJudul.isEmpty()) {
                            System.out.println("  > Buku tidak ditemukan.");
                        } else {
                            cetakPemisah();
                            for (Book b : hasilJudul) System.out.println("| " + b);
                            cetakPemisah();
                        }
                    } else if (metode.equals("2")) {
                        System.out.print(" -> Masukkan kategori: ");
                        String katSearch = scanner.nextLine();
                        List<Book> hasilKategori = service.cariBukuByKategori(katSearch);
                        
                        System.out.println("\n--- HASIL PENCARIAN (KATEGORI) ---");
                        if (hasilKategori.isEmpty()) {
                            System.out.println("  > Buku tidak ditemukan.");
                        } else {
                            cetakPemisah();
                            for (Book b : hasilKategori) System.out.println("| " + b);
                            cetakPemisah();
                        }
                    } else {
                        System.out.println("\n[ERROR] Pilihan metode tidak valid.");
                    }
                    break;

                case 4:
                    System.out.println("[>] --- PINJAM BUKU ---");
                    System.out.print(" -> ID Anggota (Mulai Huruf Kapital, cth A001): ");
                    String idPinjam = scanner.nextLine();
                    System.out.print(" -> Kata Kunci / Judul Buku                   : ");
                    String judulPinjam = scanner.nextLine();

                    try {
                        Book ygDipinjam = service.pinjamBuku(idPinjam, judulPinjam);
                        System.out.println("\n[SUKSES PINJAM] Buku yang dipinjam: \"" + ygDipinjam.getJudul() + "\" oleh " + ygDipinjam.getPenulis());
                        System.out.println("  -> Jangan lupa kembalikan jika sudah selesai dibaca!");
                    } catch (BookNotFoundException | BookAlreadyBorrowedException | BorrowLimitExceededException e) {
                        System.out.println("\n[GAGAL PINJAM] " + e.getMessage());
                    } catch (AssertionError e) {
                        System.out.println("\n[ERROR VALIDASI] " + e.getMessage());
                    }
                    break;

                case 5:
                    System.out.println("[<] --- KEMBALIKAN BUKU ---");
                    System.out.print(" -> ID Anggota                                : ");
                    String idKembali = scanner.nextLine();
                    System.out.print(" -> Kata Kunci / Judul Buku                   : ");
                    String judulKembali = scanner.nextLine();

                    try {
                        Book ygDikembalikan = service.kembalikanBuku(idKembali, judulKembali);
                        System.out.println("\n[SUKSES KEMBALI] Buku \"" + ygDikembalikan.getJudul() + "\" berhasil dikembalikan.");
                        System.out.println("  -> Terima kasih telah mengembalikan buku tepat waktu!");
                    } catch (BookNotFoundException e) {
                        System.out.println("\n[GAGAL KEMBALI] " + e.getMessage());
                    } catch (AssertionError e) {
                        System.out.println("\n[ERROR VALIDASI] " + e.getMessage());
                    }
                    break;

                case 6:
                    service.cetakLaporan();
                    break;

                case 7:
                    System.out.println("  Terima kasih telah menggunakan Mini Library Management System!");
                    System.out.println("  Sampai jumpa!");
                    isRunning = false;
                    break;

                default:
                    System.out.println("[ERROR] Pilihan menu tidak tersedia. Harap pilih 1-7.");
                    break;
            }
            System.out.println(); // spasi bawah menu
        }
        scanner.close();
    }
}
