import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class EscritorDiario {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String rutaFichero = "diario.txt";

        System.out.println("--- DIARIO ---");
        System.out.println("Escribe tus frases (escribe 'salir' para terminar):");

        // Bloque de escritura en modo append
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero, true))) {
            String frase;
            
            while (true) {
                System.out.print("> ");
                frase = scanner.nextLine().trim();

                if (frase.equalsIgnoreCase("salir")) {
                    break;
                }

                if (!frase.isEmpty()) {
                    bw.write(frase);
                    bw.newLine(); // Añade un salto de línea compatible con el sistema operativo
                }
            }
        } catch (IOException e) {
            System.out.println("Error al escribir en el fichero: " + e.getMessage());
        }

        System.out.println("\n--- COMPROBACIÓN: LEYENDO diario.txt ---");
        
        // Bloque de comprobación (Lectura)
        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }

        scanner.close();
    }
}