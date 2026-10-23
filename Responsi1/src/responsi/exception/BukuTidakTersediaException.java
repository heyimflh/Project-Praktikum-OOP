package responsi.exception;

// exception kalau stok buku lagi habis (stok = 0)
public class BukuTidakTersediaException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public BukuTidakTersediaException(String message) {
        super(message);
    }
}
