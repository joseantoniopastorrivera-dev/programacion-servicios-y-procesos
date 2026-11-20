/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Ejercicio 15. Adivinar el número de HijoAdivina por búsqueda binaria: el padre envía un intento
 * y el hijo contesta «mayor», «menor» o «acertado».
 */
public class Ejercicio15 {
    /**
     * Juega contra HijoAdivina probando siempre el punto medio del intervalo y cuenta los intentos.
     *
     * @param args opcional: el número secreto que se le pasa al hijo (para probar con uno fijo)
     * @throws IOException          si no se puede lanzar el hijo o comunicarse con él
     * @throws InterruptedException si el padre es interrumpido mientras espera
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder pb;
        if (args.length > 0) {
            pb = new ProcessBuilder("java", "-cp", "bin", "PSP.HijoAdivina", args[0]);
        } else {
            pb = new ProcessBuilder("java", "-cp", "bin", "PSP.HijoAdivina");
        }
        Process hijo = pb.start();
        PrintWriter haciaHijo = new PrintWriter(hijo.getOutputStream());
        BufferedReader desdeHijo = new BufferedReader(new InputStreamReader(hijo.getInputStream()));

        // Búsqueda binaria: probamos siempre el punto medio del intervalo
        int minimo = 1;
        int maximo = 100;
        int intentos = 0;
        String respuesta = "";

        while (!respuesta.equals("acertado")) {
            int intento = (minimo + maximo) / 2;
            intentos++;

            haciaHijo.println(intento);
            haciaHijo.flush();
            respuesta = desdeHijo.readLine();
            System.out.println("Intento " + intentos + ": " + intento + " -> " + respuesta);

            if (respuesta.equals("mayor")) {
                minimo = intento + 1;
            } else if (respuesta.equals("menor")) {
                maximo = intento - 1;
            }
        }

        haciaHijo.close();
        hijo.waitFor();
        System.out.println("¡Adivinado en " + intentos + " intentos!");
    }
}
