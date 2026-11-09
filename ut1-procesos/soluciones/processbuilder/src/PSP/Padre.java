package PSP;

import java.io.IOException;

/**
 * Ejercicio 8 (padre): Java lanza Java. Igual en Linux y en Windows.
 * Ejecutar desde la raíz del proyecto (donde está bin/):  java -cp bin PSP.Padre
 *
 * Reflexión: con System.exit(300) y System.exit(-1)
 *   Linux   : 300 → 44   y  -1 → 255  (el código es 1 byte: 0-255, se guarda «módulo 256»)
 *   Windows : 300 → 300  y  -1 → -1   (en Windows el código de salida es un entero de 32 bits)
 */
public class Padre {

    public static void main(String[] args) throws IOException, InterruptedException {
        int[] valores = {0, 5, 42, 300, -1};
        for (int v : valores) {
            String[] comando = {"java", "-cp", "bin", "PSP.Hijo", String.valueOf(v)};
            Process hijo = new ProcessBuilder(comando).start();
            int recibido = hijo.waitFor();
            System.out.println("El hijo hizo System.exit(" + v + ") y el padre recibe: " + recibido);
        }
    }
}
