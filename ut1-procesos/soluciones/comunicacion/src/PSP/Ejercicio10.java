package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Ejercicio 10. Hijo calculadora con un protocolo. Igual en Linux y en Windows.
 * Protocolo (las reglas que padre e hijo acuerdan):
 *   1. Una petición por línea: «número operador número» separados por espacios.
 *   2. Una respuesta por línea: el resultado, o la palabra ERROR.
 *   3. La palabra «fin» termina la conversación.
 */
public class Ejercicio10 {

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] operaciones = {"3 + 4", "10 / 4", "6 * 7", "8 / 0", "hola + 2", "5 ^ 2", "20 - 25"};
        Process hijo = new ProcessBuilder("java", "-cp", "bin", "PSP.HijoCalculadora").start();
        PrintWriter haciaHijo = new PrintWriter(hijo.getOutputStream());
        BufferedReader desdeHijo = new BufferedReader(new InputStreamReader(hijo.getInputStream()));
        int errores = 0;
        for (String op : operaciones) {
            haciaHijo.println(op);
            haciaHijo.flush();
            String respuesta = desdeHijo.readLine();        // una petición → una respuesta (regla 2)
            if (respuesta.equals("ERROR")) {
                errores++;
                System.out.println(op + "  ->  operación no válida");
            } else {
                System.out.println(op + "  =  " + respuesta);
            }
        }
        haciaHijo.println("fin");                          // regla 3
        haciaHijo.close();
        System.out.println("\nOperaciones no válidas: " + errores);
        System.out.println("El hijo terminó con código " + hijo.waitFor());
    }
}
