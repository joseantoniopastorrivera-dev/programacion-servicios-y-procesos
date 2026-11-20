/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · Examen RA1 (09/11/2026) · Solución de repaso
 *  Enunciado: ../../../../examen-ut1.md
 * ============================================================
 */

package PSP.c1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Ejercicio C1 · Ejecutor de tareas.
 *
 * Lanza la clase {@link Tarea} como proceso hijo con su salida guardada en tarea.log,
 * la espera como máximo el tiempo indicado (si no ha terminado, la destruye), informa
 * del código de salida y muestra el contenido de tarea.log con cada línea precedida de [TAREA].
 *
 * Argumentos: {@code <segundos> <codigo> <esperaMaxima>}
 * Ejemplo: {@code 8 0 3} → la tarea tarda 8 s, terminaría con código 0 y solo se esperan 3 s.
 */
public class EjecutorTareas {

    /** Fichero donde se guarda la salida de la tarea (se sobrescribe en cada ejecución). */
    private static final File LOG = new File("tarea.log");

    /**
     * Punto de entrada: comprueba los argumentos, lanza la tarea, la espera y muestra el resultado.
     *
     * @param args segundos que tarda la tarea, código con el que termina y espera máxima en segundos
     */
    public static void main(String[] args) {
        // --- Comprobar los argumentos (manejo de errores: mensajes claros por stderr) ---
        if (args.length != 3) {
            System.err.println("Uso: EjecutorTareas <segundos> <codigo> <esperaMaxima>");
            System.exit(1);
        }
        long esperaMaxima;
        try {
            Integer.parseInt(args[0]);                  // solo compruebo que son números:
            Integer.parseInt(args[1]);                  // a Tarea se le pasan tal cual, como texto
            esperaMaxima = Long.parseLong(args[2]);     // este sí lo uso yo (waitFor pide un long)
        } catch (NumberFormatException e) {
            System.err.println("Los tres argumentos deben ser números enteros: " + e.getMessage());
            System.exit(1);
            return;                                     // (el compilador no sabe que exit no vuelve)
        }

        try {
            Process tarea = lanzarTarea(args[0], args[1]);                  // 1. lanzar
            System.out.println("Tarea lanzada con PID " + tarea.pid());

            int codigo = esperarConLimite(tarea, esperaMaxima);             // 2. esperar (o destruir)
            mostrarResultado(codigo);                                       // 3. código de salida
            mostrarLog();                                                   // 4. contenido de tarea.log
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al lanzar la tarea o leer el log: " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println("El ejecutor fue interrumpido mientras esperaba a la tarea");
            System.exit(1);
        }
    }

    /**
     * Lanza la clase Tarea como proceso hijo, con su salida estándar y de error guardadas en tarea.log.
     *
     * @param segundos segundos que tardará la tarea (tal cual se reciben por args)
     * @param codigo   código de salida con el que terminará la tarea
     * @return el {@link Process} de la tarea, ya arrancada
     * @throws IOException si no se puede crear el proceso
     */
    static Process lanzarTarea(String segundos, String codigo) throws IOException {
        // "Java lanza Java" con el MISMO java y el MISMO classpath que este programa.
        // En el examen también valía la forma corta: new ProcessBuilder("java", "-cp", "bin", "PSP.c1.Tarea", ...)
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");

        ProcessBuilder pb = new ProcessBuilder(java, "-cp", classpath, "PSP.c1.Tarea", segundos, codigo);
        pb.redirectOutput(LOG);          // stdout del hijo > tarea.log  (new File = SOBRESCRIBE)
        pb.redirectErrorStream(true);    // stderr del hijo al mismo sitio: ninguna tubería queda sin leer
        return pb.start();               // crea el proceso y vuelve enseguida, sin esperar
    }

    /**
     * Espera a la tarea como máximo {@code segundos}. Si no ha terminado, avisa, la destruye
     * y espera a que muera de verdad.
     *
     * @param tarea    proceso de la tarea
     * @param segundos tiempo máximo de espera
     * @return el código de salida de la tarea (el que le da el sistema si se ha destruido: 143 en Linux, 1 en Windows)
     * @throws InterruptedException si este hilo es interrumpido mientras espera
     */
    static int esperarConLimite(Process tarea, long segundos) throws InterruptedException {
        // waitFor(tiempo, unidad) devuelve un BOOLEAN: true si terminó dentro del plazo.
        boolean termino = tarea.waitFor(segundos, TimeUnit.SECONDS);
        if (!termino) {
            System.out.println("La tarea no ha terminado en " + segundos + " s: se destruye");
            tarea.destroy();                             // pide que termine (SIGTERM en Linux → código 143)
            if (!tarea.waitFor(2, TimeUnit.SECONDS)) {   // destroy() NO espera: le doy 2 s de margen…
                tarea.destroyForcibly();                 // …y si sigue viva, la mato (SIGKILL)
            }
        }
        return tarea.waitFor();                          // waitFor() sin argumentos devuelve el CÓDIGO
    }

    /**
     * Muestra si la tarea ha terminado bien o mal según su código de salida.
     *
     * @param codigo código de salida de la tarea (0 = correcta)
     */
    static void mostrarResultado(int codigo) {
        if (codigo == 0) {
            System.out.println("Tarea correcta");
        } else {
            System.out.println("La tarea ha fallado (código " + codigo + ")");
        }
    }

    /**
     * Muestra el contenido de tarea.log, cada línea precedida de [TAREA].
     * La salida del hijo fue al fichero, así que se lee el FICHERO (getInputStream() no traería nada).
     *
     * @throws IOException si no se puede leer el fichero
     */
    static void mostrarLog() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(LOG))) {   // se cierra solo
            String linea;
            while ((linea = br.readLine()) != null) {                         // null = fin del fichero
                System.out.println("[TAREA] " + linea);
            }
        }
    }
}
