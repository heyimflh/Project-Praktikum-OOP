import java.util.Scanner;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US); // Memastikan input/output desimal dengan titik (dot)

        System.out.println("========================================");
        System.out.println("      SISTEM PENDATAAN NILAI SISWA      ");
        System.out.println("========================================\n");

        System.out.print("> Masukkan jumlah siswa: ");
        int jumlahSiswa = scanner.nextInt();
        scanner.nextLine(); // Membersihkan newline

        Student[] students = new Student[jumlahSiswa];

        for (int i = 0; i < jumlahSiswa; i++) {
            System.out.println("\n----------------------------------------");
            System.out.println("[+] Data siswa ke-" + (i + 1) + ":");
            System.out.print("  - Masukkan nama  : ");
            String name = scanner.nextLine();

            System.out.print("  - Masukkan nilai : ");
            double score = scanner.nextDouble();
            scanner.nextLine(); // Membersihkan newline

            students[i] = new Student(name, score);
        }

        System.out.println("\n========================================");
        System.out.println("            HASIL KELULUSAN             ");
        System.out.println("========================================");

        double totalNilai = 0;
        int jumlahLulus = 0;
        int jumlahTidakLulus = 0;

        for (int i = 0; i < jumlahSiswa; i++) {
            students[i].checkPassed();

            String status = students[i].passed ? "Lulus" : "Tidak Lulus";
            System.out.printf("%-18s | Nilai: %-5.1f | %s\n", 
                              (i + 1) + ". " + students[i].name, 
                              students[i].score, 
                              status);

            totalNilai += students[i].score;
            if (students[i].passed) {
                jumlahLulus++;
            } else {
                jumlahTidakLulus++;
            }
        }

        double rataRata = totalNilai / jumlahSiswa;

        System.out.println("----------------------------------------\n");
        System.out.println("========================================");
        System.out.println("            STATISTIK KELAS             ");
        System.out.println("========================================");
        System.out.printf(Locale.US, "> Rata-rata nilai kelas    : %.2f\n", rataRata);
        System.out.println("> Jumlah siswa lulus       : " + jumlahLulus);
        System.out.println("> Jumlah siswa tidak lulus : " + jumlahTidakLulus);
        System.out.println("========================================\n");

        scanner.close();
    }
}
