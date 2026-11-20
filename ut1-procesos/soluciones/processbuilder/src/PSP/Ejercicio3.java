/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

import java.io.IOException;

/**
 * Ejercicio 3. Lanzador por argumentos: lanza el comando que se recibe en args.
 *
 *   Linux   : java -cp bin PSP.Ejercicio3 gnome-calculator
 *             java -cp bin PSP.Ejercicio3 mkdir carpetaNueva
 *   Windows : java -cp bin PSP.Ejercicio3 notepad.exe
 *             java -cp bin PSP.Ejercicio3 cmd /c mkdir carpetaNueva   (mkdir es interno de cmd)
 */
public class Ejercicio3 {

    /**
     * Lanza el comando (con sus argumentos) que se recibe por la línea de órdenes.
     *
     * @param args el comando y sus argumentos
     */
    public static void main(String[] args) {
        if (args.length == 0) {                                   // sin argumentos: explicar el uso y salir
            System.err.println("Uso: java -cp bin PSP.Ejercicio3 <comando> [argumentos...]");
            System.err.println("Ejemplo: java -cp bin PSP.Ejercicio3 " + (SO.WINDOWS ? "notepad.exe" : "gnome-calculator"));
            System.exit(1);                                        // código distinto de 0: el uso era incorrecto
        }
        System.out.println("Comando a lanzar: " + String.join(" ", args));
        try {
            // args ya es un String[]: ProcessBuilder lo acepta tal cual (cada elemento es un argumento).
            Process p = new ProcessBuilder(args).start();
            System.out.println("Lanzado con PID " + p.pid());
        } catch (IOException e) {
            System.out.println("No se pudo lanzar: " + e.getMessage());
        }
    }
}
