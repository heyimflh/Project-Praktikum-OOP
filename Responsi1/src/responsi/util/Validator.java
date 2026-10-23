package responsi.util;

// class helper buat validasi dan olah string/karakter
public class Validator {

    private Validator() {}

    // validasi format ID anggota: harus diawali "AGT" dan 3 digit angka di belakangnya
    public static boolean validasiIdAnggota(String id) {
        if (id == null || id.length() != 6) {
            return false;
        }

        if (!id.startsWith("AGT")) {
            return false;
        }

        // 3 karakter terakhir harus digit angka
        for (int i = 3; i < id.length(); i++) {
            char c = id.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    // normalisasi judul biar rapi jadi title case (huruf depan tiap kata kapital)
    public static String normalisasiJudul(String judul) {
        if (judul == null) {
            return "";
        }

        String bersih = judul.trim();
        if (bersih.isEmpty()) {
            return "";
        }

        String[] kata = bersih.split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < kata.length; i++) {
            String k = kata[i];
            if (!k.isEmpty()) {
                sb.append(Character.toUpperCase(k.charAt(0)));
                if (k.length() > 1) {
                    sb.append(k.substring(1).toLowerCase());
                }
                if (i < kata.length - 1) {
                    sb.append(" ");
                }
            }
        }

        return sb.toString();
    }

    // cari judul buku tanpa bedain huruf besar kecil
    public static boolean cariJudul(String judul, String kataKunci) {
        if (judul == null || kataKunci == null) {
            return false;
        }
        return judul.toLowerCase().contains(kataKunci.toLowerCase().trim());
    }

    // ngitung berapa banyak huruf vokal di dalam teks
    public static int hitungVokal(String teks) {
        if (teks == null) {
            return 0;
        }

        int jumlahVokal = 0;
        for (int i = 0; i < teks.length(); i++) {
            char c = teks.charAt(i);
            if (Character.isLetter(c)) {
                char kecil = Character.toLowerCase(c);
                if (kecil == 'a' || kecil == 'i' || kecil == 'u' || kecil == 'e' || kecil == 'o') {
                    jumlahVokal++;
                }
            }
        }
        return jumlahVokal;
    }

    // ngitung jumlah huruf sama jumlah angka di dalam teks
    public static int[] hitungHurufDanAngka(String teks) {
        int[] hasil = new int[2]; // [0] = huruf, [1] = angka
        if (teks == null) {
            return hasil;
        }

        for (int i = 0; i < teks.length(); i++) {
            char c = teks.charAt(i);
            if (Character.isLetter(c)) {
                hasil[0]++;
            } else if (Character.isDigit(c)) {
                hasil[1]++;
            }
        }

        return hasil;
    }

    // balik urutan string buat bikin kode verifikasi
    public static String balikString(String teks) {
        if (teks == null) {
            return "";
        }
        return new StringBuilder(teks).reverse().toString();
    }

    // catatan: perbandingan String di Java harus pakai equals() / equalsIgnoreCase() karena ngecek isi nilainya,
    // bukan pakai '==' yang cuma ngebandingin alamat memori (referensi) objeknya.
}
