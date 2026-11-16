/*
 * ============================================================
 *  Solución de repaso (no es la entrega de ningún alumno).
 *  El enunciado oficial no venía en el zip: está reconstruido en
 *  ../../../../enunciado-reconstruido.md
 * ============================================================
 */

package PSP.c1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Ejercicio C1: Ejecutor de tareas.
 *
 * Lanza la clase Tarea como proceso hijo, guarda su salida en tarea.log,
 * la espera como mucho <esperaMaxima> segundos y, si no ha terminado, la destruye.
 *
 * Argumentos: <segundos> <codigo> <esperaMaxima>
 */
public class EjecutorTareas {

    public static void main(String[] args) throws IOException, InterruptedException {

        // --- 1. Comprobar los argumentos -------------------------------------
        // Sin los 3 argumentos no se puede seguir. El mensaje va por System.err
        // (canal de errores) y se sale con un código distinto de 0 (= fallo).
        if (args.length != 3) {
            System.err.println("Uso: EjecutorTareas <segundos> <codigo> <esperaMaxima>");
            System.exit(1);
        }
        String segundos = args[0];                        // se le pasa tal cual al hijo: no hace falta convertirlo
        String codigo = args[1];                          // ídem
        long esperaMaxima = Long.parseLong(args[2]);      // este sí lo uso yo como número (waitFor pide un long)

        // --- 2. Preparar el proceso hijo (Java lanza Java) -------------------
        // Forma segura: la misma JVM y el mismo classpath que el padre.
        // En el examen (proyecto que compila en bin) valía también la forma corta:
        //     new ProcessBuilder("java", "-cp", "bin", "PSP.c1.Tarea", segundos, codigo);
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");

        // Un String por argumento: ejecutable, opción, valor, clase con paquete y los args de su main.
        ProcessBuilder pb = new ProcessBuilder(java, "-cp", classpath, "PSP.c1.Tarea", segundos, codigo);

        // Redirección: la salida del hijo va DIRECTA al fichero (equivale a "> tarea.log").
        // Con new File(...) se sobrescribe en cada ejecución. Se configura ANTES de start().
        File log = new File("tarea.log");
        pb.redirectOutput(log);
        // stderr se mezcla con stdout: los errores del hijo también acaban en tarea.log
        // y no queda ninguna tubería sin leer que pueda llenarse.
        pb.redirectErrorStream(true);

        // --- 3. Lanzar y esperar con límite de tiempo ------------------------
        Process p = pb.start();                           // crea el proceso y NO espera
        System.out.println("Tarea lanzada con PID " + p.pid() + " (espera máxima: " + esperaMaxima + " s)");

        // waitFor con tiempo devuelve un boolean: true si el hijo terminó dentro del plazo.
        // (El waitFor() sin argumentos devuelve el código de salida; no confundirlos.)
        boolean terminoATiempo = p.waitFor(esperaMaxima, TimeUnit.SECONDS);

        // --- 4. Los tres finales posibles ------------------------------------
        if (terminoATiempo) {
            int salida = p.exitValue();                   // seguro: ya sé que ha terminado
            if (salida == 0) {
                System.out.println("RESULTADO: la tarea terminó bien (código 0)");
            } else {
                System.out.println("RESULTADO: la tarea terminó con error (código " + salida + ")");
            }
        } else {
            p.destroy();                                  // pide que termine (SIGTERM en Linux)
            // destroy() NO espera: le doy 2 s y, si sigue vivo, lo obligo (SIGKILL).
            if (!p.waitFor(2, TimeUnit.SECONDS)) {
                p.destroyForcibly();
            }
            int salida = p.waitFor();                     // ahora sí espero a que muera del todo
            System.out.println("RESULTADO: la tarea superó los " + esperaMaxima
                    + " s y se ha destruido (código " + salida + ")");
        }

        // --- 5. Mostrar lo que el hijo dejó escrito en el log -----------------
        // Como la salida fue al fichero, p.getInputStream() no trae nada: hay que leer el FICHERO.
        // try-with-resources cierra el lector aunque salte una excepción.
        System.out.println("--- Contenido de " + log.getName() + " ---");
        try (BufferedReader br = new BufferedReader(new FileReader(log))) {
            String linea;
            while ((linea = br.readLine()) != null) {     // null = fin de fichero
                System.out.println(linea);
            }
        }
    }
}
