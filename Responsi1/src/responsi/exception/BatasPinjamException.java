package responsi.exception;

// exception kalau anggota udah minjam buku maksimal 3
public class BatasPinjamException extends Exception {

    private static final long serialVersionUID = 1L;

    public BatasPinjamException(String message) {
        super(message);
    }
}
