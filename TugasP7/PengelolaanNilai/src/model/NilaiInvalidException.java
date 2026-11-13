package model;

// [Materi: Custom Exception]
// Ini custom exception turunan dari Exception (checked exception).
// Sengaja dibikin khusus buat nangkep input nilai yang di luar rentang wajar (0 - 100).
public class NilaiInvalidException extends Exception {

    // Constructor buat nerusin pesan error ke class induk (Exception)
    public NilaiInvalidException(String pesan) {
        super(pesan);
    }
}

