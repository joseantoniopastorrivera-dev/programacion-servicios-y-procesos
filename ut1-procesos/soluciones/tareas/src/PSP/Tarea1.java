package PSP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Tarea 1 (PDF de teoría). Recibe por args una lista de programas, los lanza y muestra nombre,
 * PID y momento de inicio. Después, por cada uno: espera 3 s, lo elimina y muestra su código de salida.
 *
 *   Linux   : java -cp bin PSP.Tarea1 gnome-calculator gedit xeyes
 *   Windows : java -cp bin PSP.Tarea1 notepad.exe mspaint.exe
 * Sin argumentos lanza dos procesos de prueba que no abren ventanas (sleep / ping).
 */
public class Tarea1 {

    public static void main(String[] args) throws InterruptedException {
        ArrayList<String[]> programas = new ArrayList<String[]>();
        if (args.length == 0) {
            programas.add(SO.dormir(60));
            programas.add(SO.comando(new String[]{"ping", "-c", "60", "127.0.0.1"},
                    new String[]{"ping", "-n", "60", "127.0.0.1"}));
        } else {
            for (String a : args) {
                programas.add(new String[]{a});              // cada argumento es un programa
            }
        }

        ArrayList<Process> lanzados = new ArrayList<Process>();
        for (String[] programa : programas) {
            try {
                Process p = new ProcessBuilder(programa).start();
                lanzados.add(p);
                // info() da datos del SO sobre el proceso. Todo son Optional: puede no haber dato → orElse.
                ProcessHandle.Info info = p.info();
                System.out.println("Aplicación: " + info.command().orElse(programa[0]));
                System.out.println("  PID:     " + p.pid());
                System.out.println("  Inicio:  " + info.startInstant().map(Object::toString).orElse("desconocido"));
            } catch (IOException e) {
                System.out.println("No se pudo lanzar " + programa[0] + ": " + e.getMessage());
            }
        }

        // Segunda parte: por cada proceso, esperar 3 s, eliminarlo y mostrar el código de salida.
        for (Process p : lanzados) {
            p.waitFor(3, TimeUnit.SECONDS);                     // espera hasta 3 s (antes si termina solo)
            p.destroy();
            int codigo = p.waitFor();
            System.out.println("PID " + p.pid() + " eliminado. Código de salida: " + codigo);
        }
    }
}
