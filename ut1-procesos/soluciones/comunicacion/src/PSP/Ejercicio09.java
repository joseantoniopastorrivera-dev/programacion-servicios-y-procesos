package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Ejercicio 9. Hijo inversor: conversación con un hijo Java. Igual en Linux y en Windows.
 * Reflexión: aquí se usa flush() y no close() porque la conversación CONTINÚA:
 *   flush() = envía ya lo escrito y deja el canal abierto;
 *   close() = envía y cierra: el hijo ve fin de entrada y termina.
 */
public class Ejercicio09 {

    public static void main(String[] args) throws IOException, InterruptedException {
        Process hijo = new ProcessBuilder("java", "-cp", "bin", "PSP.HijoInverso").start();
        PrintWriter haciaHijo = new PrintWriter(hijo.getOutputStream());
        BufferedReader desdeHijo = new BufferedReader(new InputStreamReader(hijo.getInputStream()));
        Scanner sc = new Scanner(System.in);

        System.out.println("Escribe frases (\"salir\" para terminar):");
        while (sc.hasNextLine()) {
            String frase = sc.nextLine();
            if (frase.equals("salir")) {
                break;
            }
            haciaHijo.println(frase);
            haciaHijo.flush();                               // sin flush la frase se queda en el búfer y el hijo no la ve
            String invertida = desdeHijo.readLine();
            System.out.println("Hijo: " + invertida);
            String limpia = frase.replace(" ", "");
            if (limpia.equalsIgnoreCase(invertida.replace(" ", ""))) {
                System.out.println("  ¡Es un palíndromo!");
            }
        }
        haciaHijo.close();                                   // ahora sí: fin de la conversación
        System.out.println("El hijo terminó con código " + hijo.waitFor());
        sc.close();
    }
}
