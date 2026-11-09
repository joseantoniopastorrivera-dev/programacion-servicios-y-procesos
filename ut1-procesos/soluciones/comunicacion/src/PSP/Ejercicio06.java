package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Ejercicio 6. Revisor de comandos: [OK], [FALLO n] o [NO EXISTE].
 * Reflexión: «comandoInventado» no tiene código de salida porque NUNCA llega a ser un proceso:
 * start() lanza IOException. «ls carpetaQueNoExiste» sí es un proceso que se ejecuta y falla (código 2).
 */
public class Ejercicio06 {

    public static void main(String[] args) throws InterruptedException {
        String[][] linux = {{"date"}, {"ls", "carpetaQueNoExiste"}, {"comandoInventado"},
                {"cat", "ficheroQueNoExiste.txt"}, {"whoami"}};
        String[][] windows = {{"cmd", "/c", "date", "/t"}, {"cmd", "/c", "dir", "carpetaQueNoExiste"},
                {"comandoInventado"}, {"cmd", "/c", "type", "ficheroQueNoExiste.txt"}, {"whoami"}};
        String[][] comandos = SO.WINDOWS ? windows : linux;

        int correctos = 0;
        for (String[] comando : comandos) {
            ProcessBuilder pb = new ProcessBuilder(comando);
            pb.redirectErrorStream(true);                        // errores y salida por el MISMO flujo
            String nombre = String.join(" ", comando);
            try {
                Process p = pb.start();
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String primera = br.readLine();
                while (br.readLine() != null) {
                    // consumimos el resto: si no, el hijo podría bloquearse con el búfer lleno
                }
                br.close();
                int codigo = p.waitFor();
                if (codigo == 0) {
                    System.out.println("[OK]        " + nombre);
                    correctos++;
                } else {
                    System.out.println("[FALLO " + codigo + "]   " + nombre + " -> " + primera);
                }
            } catch (IOException e) {                            // el ejecutable no existe
                System.out.println("[NO EXISTE] " + nombre);
            }
        }
        System.out.println("\nCorrectos: " + correctos + " de " + comandos.length);
    }
}
