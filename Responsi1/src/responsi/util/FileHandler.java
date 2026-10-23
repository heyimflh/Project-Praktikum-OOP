package responsi.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import responsi.model.Anggota;
import responsi.model.Buku;

// class helper buat operasi file (file teks katalog dan file objek serialisasi)
public class FileHandler {

    private FileHandler() {}

    // simpan isi katalog ke file teks katalog.txt
    public static boolean simpanKatalog(String path, List<Buku> katalog) {
        if (katalog == null) {
            System.out.println("[PERINGATAN] Data katalog kosong.");
            return false;
        }

        // pakai try-with-resources biar stream-nya otomatis nutup sendiri
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Buku b : katalog) {
                // formatnya: kode;judul;pengarang;kategori;tahun;stok
                String baris = String.format("%s;%s;%s;%s;%d;%d",
                        b.getKodeBuku(),
                        b.getJudul(),
                        b.getPengarang(),
                        b.getKategori(),
                        b.getTahunTerbit(),
                        b.getStok());
                bw.write(baris);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menulis ke file katalog '" + path + "': " + e.getMessage());
            return false;
        }
    }

    // muat data buku dari file teks katalog.txt
    public static List<Buku> muatKatalog(String path) {
        List<Buku> daftarBuku = new ArrayList<>();
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("[INFO] File katalog '" + path + "' belum ditemukan.");
            return daftarBuku;
        }

        // baca baris demi baris pakai BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String baris;
            int nomorBaris = 0;

            while ((baris = br.readLine()) != null) {
                nomorBaris++;
                String barisBersih = baris.trim();
                if (barisBersih.isEmpty()) {
                    continue;
                }

                // pecah kolom berdasarkan tanda titik koma ';'
                String[] bagian = barisBersih.split(";");

                if (bagian.length != 6) {
                    System.out.println("[PERINGATAN] Baris " + nomorBaris + " dilewati (format kolom tidak valid): " + baris);
                    continue;
                }

                try {
                    String kode = bagian[0].trim();
                    String judul = bagian[1].trim();
                    String pengarang = bagian[2].trim();
                    String kategori = bagian[3].trim();
                    int tahun = Integer.parseInt(bagian[4].trim());
                    int stok = Integer.parseInt(bagian[5].trim());

                    if (stok < 0) {
                        System.out.println("[PERINGATAN] Baris " + nomorBaris + " dilewati (stok negatif).");
                        continue;
                    }

                    Buku b = new Buku(kode, judul, pengarang, kategori, tahun, stok);
                    daftarBuku.add(b);
                } catch (NumberFormatException nfe) {
                    System.out.println("[PERINGATAN] Baris " + nomorBaris + " dilewati (tahun/stok bukan angka valid).");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR] File katalog '" + path + "' tidak ditemukan: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[ERROR] Terjadi kesalahan saat membaca file katalog: " + e.getMessage());
        }

        return daftarBuku;
    }

    // simpan data anggota ke file objek biner pakai serialization
    public static boolean simpanAnggota(String path, List<Anggota> anggota) {
        if (anggota == null) {
            System.out.println("[PERINGATAN] Data anggota kosong.");
            return false;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(new ArrayList<>(anggota));
            return true;
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menyimpan data anggota ke '" + path + "': " + e.getMessage());
            return false;
        }
    }

    // muat data anggota dari file objek serialisasi
    @SuppressWarnings("unchecked")
    public static List<Anggota> muatAnggota(String path) {
        List<Anggota> hasil = new ArrayList<>();
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("[INFO] File data anggota '" + path + "' belum ditemukan.");
            return hasil;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                hasil = (List<Anggota>) obj;
            }
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR] File anggota '" + path + "' tidak ditemukan: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR] Kelas objek anggota tidak cocok/tidak ditemukan: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[ERROR] Terjadi kesalahan saat membaca file anggota: " + e.getMessage());
        }

        return hasil;
    }
}
