package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Ejercicio 1. Eco: leer la salida del hijo y mostrarla con el prefijo [HIJO].
 *   Linux   : echo Hola desde el hijo
 *   Windows : cmd /c echo Hola desde el hijo   (echo es una orden interna de cmd, no un .exe)
 */
public class Ejercicio01 {

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] comando = SO.comando(
                new String[]{"echo", "Hola desde el hijo"},
                // Palabra a palabra: si se pasa "Hola desde el hijo" junto, Java le pone comillas
                // (por los espacios) y el echo de cmd las imprime.
                new String[]{"cmd", "/c", "echo", "Hola", "desde", "el", "hijo"});
        Process p = new ProcessBuilder(comando).start();
        // getInputStream() = lo que el hijo escribe en SU salida estándar (para el padre es una ENTRADA).
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String linea;
        while ((linea = br.readLine()) != null) {   // readLine devuelve null cuando el hijo cierra su salida
            System.out.println("[HIJO] " + linea);
        }
        br.close();
        System.out.println("Código de salida: " + p.waitFor());   // regla de oro: primero leer, después waitFor
    }
}
