import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LectorFichero {
    public static void main(String[] args) {
        String rutaFichero = "alumnos.txt";

        // Declaración del recurso entre paréntesis. Se cerrará automáticamente.
        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
            String linea;
            int contador = 1;

            // readLine() devuelve null cuando alcanza el final del fichero
            while ((linea = br.readLine()) != null) {
                System.out.println(contador + ". " + linea);
                contador++;
            }
            
        } catch (FileNotFoundException e) {
            // Control específico por si no has creado el fichero alumnos.txt previamente
            System.out.println("Error: El fichero '" + rutaFichero + "' no existe.");
        } catch (IOException e) {
            // Captura cualquier otro error de entrada/salida durante la lectura
            System.out.println("Error de lectura: " + e.getMessage());
        }
    }
}