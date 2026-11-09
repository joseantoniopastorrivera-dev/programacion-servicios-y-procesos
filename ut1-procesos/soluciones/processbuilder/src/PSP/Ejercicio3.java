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

    public static void main(String[] args) {
        if (args.length == 0) {                                   // sin argumentos: explicar el uso y salir
            System.out.println("Uso: java -cp bin PSP.Ejercicio3 <comando> [argumentos...]");
            System.out.println("Ejemplo: java -cp bin PSP.Ejercicio3 " + (SO.WINDOWS ? "notepad.exe" : "gnome-calculator"));
            return;
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
