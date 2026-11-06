// Interface: Kontrak perilaku untuk objek yang dapat disewa
public interface Rentable {
    boolean sewa(String namaPenyewa, int lamaHari);
    void kembalikan();
    boolean isTersedia();
}

