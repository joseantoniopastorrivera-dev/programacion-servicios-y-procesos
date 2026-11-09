package PSP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Ejercicio 12. Mini gestor de procesos (con la ampliación: opción 4, matar cualquier PID).
 *
 * Comandos de prueba:
 *   Linux   : sleep 100        ·   ping -c 50 127.0.0.1
 *   Windows : ping -n 100 127.0.0.1   ·   notepad.exe
 */
public class GestorProcesos {

    private static final ArrayList<Process> procesos = new ArrayList<Process>();
    private static final ArrayList<String> comandos = new ArrayList<String>();   // mismo índice que procesos
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        int opcion = 0;
        while (opcion != 5) {
            System.out.println("\n1. Lanzar proceso\n2. Listar procesos lanzados\n3. Terminar un proceso por PID"
                    + "\n4. Terminar CUALQUIER proceso del sistema por PID (ampliación)\n5. Salir");
            System.out.print("Opción: ");
            opcion = leerEntero();
            switch (opcion) {
                case 1: lanzar(); break;
                case 2: listar(); break;
                case 3: terminarPropio(); break;
                case 4: terminarCualquiera(); break;
                case 5: salir(); break;
                default: System.out.println("Opción no válida");
            }
        }
    }

    private static void lanzar() {
        System.out.print("Comando (" + (SO.WINDOWS ? "p. ej. ping -n 100 127.0.0.1" : "p. ej. sleep 100") + "): ");
        if (!sc.hasNextLine()) {
            return;
        }
        String linea = sc.nextLine().trim();
        if (linea.isEmpty()) {
            return;
        }
        try {
            // split("\\s+") parte por uno o más espacios: "sleep  100" → ["sleep", "100"]
            Process p = new ProcessBuilder(linea.split("\\s+")).start();
            procesos.add(p);
            comandos.add(linea);
            System.out.println("Lanzado con PID " + p.pid());
        } catch (IOException e) {
            System.out.println("No se pudo lanzar: " + e.getMessage());
        }
    }

    private static void listar() {
        if (procesos.isEmpty()) {
            System.out.println("No hay procesos lanzados.");
        }
        for (int i = 0; i < procesos.size(); i++) {
            Process p = procesos.get(i);
            String estado = p.isAlive() ? "vivo" : "terminado, código " + p.exitValue();  // exitValue solo si terminó
            System.out.println("PID " + p.pid() + " · " + comandos.get(i) + " · " + estado);
        }
    }

    private static void terminarPropio() throws InterruptedException {
        System.out.print("PID a terminar: ");
        long pid = leerEntero();
        for (Process p : procesos) {
            if (p.pid() == pid) {
                terminar(p);
                return;
            }
        }
        System.out.println("Aviso: el PID " + pid + " no es de ningún proceso lanzado por el gestor.");
    }

    /** destroy() y, si en 2 s no ha terminado, destroyForcibly(). */
    private static void terminar(Process p) throws InterruptedException {
        if (!p.isAlive()) {
            System.out.println("El proceso " + p.pid() + " ya había terminado.");
            return;
        }
        p.destroy();
        if (!p.waitFor(2, TimeUnit.SECONDS)) {
            System.out.println("No terminó con destroy(): uso destroyForcibly()");
            p.destroyForcibly();
            p.waitFor();
        }
        System.out.println("Proceso " + p.pid() + " terminado con código " + p.exitValue());
    }

    /** Ampliación: ProcessHandle.of(pid) da acceso a cualquier proceso del sistema, no solo a los nuestros. */
    private static void terminarCualquiera() {
        System.out.print("PID de cualquier proceso: ");
        long pid = leerEntero();
        Optional<ProcessHandle> h = ProcessHandle.of(pid);       // vacío si ese PID no existe
        if (h.isEmpty()) {
            System.out.println("No existe ningún proceso con PID " + pid);
            return;
        }
        System.out.println("Proceso: " + h.get().info().commandLine().orElse(h.get().info().command().orElse("?")));
        boolean pedido = h.get().destroy();                       // false si el SO no lo permite (p. ej., de otro usuario)
        System.out.println(pedido ? "Petición de terminar enviada." : "No se ha podido terminar (¿sin permisos?).");
    }

    private static void salir() throws InterruptedException {
        for (Process p : procesos) {
            if (p.isAlive()) {
                terminar(p);
            }
        }
        System.out.println("Hasta luego.");
    }

    private static int leerEntero() {
        if (!sc.hasNextLine()) {                 // fin de la entrada (Ctrl+D / Ctrl+Z): equivale a «Salir»
            return 5;
        }
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
