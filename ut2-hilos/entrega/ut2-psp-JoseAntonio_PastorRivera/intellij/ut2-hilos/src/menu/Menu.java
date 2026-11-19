/*
 * UT2 · Relación de ejercicios · Menú para elegir el ejercicio que se ejecuta
 *
 * Alumno: José Antonio Pastor Rivera · PSP · 2º DAM
 *
 * Cada ejercicio tiene su propio main. Este menú no los llama directamente: lanza cada uno en un
 * PROCESO HIJO con ProcessBuilder (lo visto en la UT1). Así:
 *   - si un ejercicio deja hilos vivos (el vigilante del 3.3, que es daemon, o uno que se cuelgue),
 *     esos hilos mueren con su proceso y no se quedan dentro del menú;
 *   - cada ejercicio arranca con una JVM limpia: los contadores static (3.2) empiezan siempre a 0
 *     y el JIT no viene «calentado» de un ejercicio anterior, así que los tiempos son comparables;
 *   - el código de los ejercicios no se toca: es el mismo que se ejecuta suelto.
 */
package menu;

import java.io.File;
import java.util.Scanner;

public class Menu {

    /** Una opción del menú: el número que se ve, la descripción y la clase con el main. */
    private record Opcion(String numero, String descripcion, String clase) { }

    /** Las 18 clases con main de la relación, en el orden del enunciado. */
    private static final Opcion[] OPCIONES = {
        new Opcion("1.1a", "Descargas · secuencial (un solo hilo)",            "bloque1.descargas.DescargasSecuencial"),
        new Opcion("1.1b", "Descargas · con hilos (uno por fichero)",           "bloque1.descargas.DescargasConHilos"),
        new Opcion("1.2a", "Tarea pesada · sin hilos (se «cuelga»)",           "bloque1.tarea_pesada.SinHilos"),
        new Opcion("1.2b", "Tarea pesada · con hilos (A: Runnable)",           "bloque1.tarea_pesada.ConHilos"),
        new Opcion("1.2c", "Tarea pesada · con hilos (B: extends Thread)",     "bloque1.tarea_pesada.ConHilosB"),
        new Opcion("2.1",  "Estados de un hilo",                                "bloque2.estados.ObservarEstados"),
        new Opcion("2.2a", "El examen · un alumno",                             "bloque2.examen.Examen"),
        new Opcion("2.2b", "El examen · cinco alumnos (ampliación)",            "bloque2.examen.ExamenVariosAlumnos"),
        new Opcion("2.3",  "Autoguardado (hilo daemon)",                        "bloque2.autoguardado.Editor"),
        new Opcion("2.4",  "Taxis (extends Vehiculo implements Runnable)",      "bloque2.taxis.Flota"),
        new Opcion("3.1",  "Informe de ventas (Bernstein + join)",              "bloque3.informe_ventas.Informe"),
        new Opcion("3.2",  "Jardín ornamental (condición de carrera)",          "bloque3.jardin.Jardin"),
        new Opcion("3.3",  "El vigilante (visibilidad y volatile)",             "bloque3.vigilante.Vigilancia"),
        new Opcion("3.4",  "Venta de entradas (overbooking)",                   "bloque3.entradas.Cine"),
        new Opcion("4.1",  "Arregla el jardín (3 formas + tiempos)",            "bloque4.jardin.JardinCorregido"),
        new Opcion("4.2",  "Taquillas sin overbooking",                         "bloque4.entradas.CineCorregido"),
        new Opcion("4.3",  "Máquina de turnos (sin repetidos)",                 "bloque4.turnos.Carniceria"),
        new Opcion("4.4",  "El banco (cerrojos e interbloqueo)",                "bloque4.banco.Transferencias"),
    };

    public static void main(String[] args) throws Exception {
        Scanner teclado = new Scanner(System.in);                // lee lo que escribe el usuario

        while (true) {                                           // el menú se repite hasta elegir 0
            mostrarMenu();
            System.out.print("Elige una opción (número, T = todos, 0 = salir): ");
            if (!teclado.hasNextLine()) return;                  // fin de la entrada (Ctrl+Z / Ctrl+D): salir
            String eleccion = teclado.nextLine().trim();

            if (eleccion.equals("0")) {
                System.out.println("¡Hasta luego!");
                return;                                          // termina el main → termina el programa
            }
            if (eleccion.equalsIgnoreCase("T")) {                // ejecutar la relación entera, en orden
                for (Opcion o : OPCIONES) ejecutar(o);
                continue;
            }

            Opcion elegida = buscar(eleccion);
            if (elegida == null) {
                System.out.println("  Opción no válida: escribe el número de la lista (1-18) o el código (p. ej. 3.2).");
                continue;
            }
            ejecutar(elegida);
            System.out.print("\nPulsa Intro para volver al menú...");
            if (teclado.hasNextLine()) teclado.nextLine();       // pausa para leer la salida con calma
        }
    }

    /** Pinta el menú: posición en la lista, código del ejercicio y descripción. */
    private static void mostrarMenu() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  PSP · UT2 · Hilos · José Antonio Pastor Rivera          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        String bloqueAnterior = "";
        for (int i = 0; i < OPCIONES.length; i++) {
            String bloque = OPCIONES[i].numero().substring(0, 1);    // "1", "2", "3" o "4"
            if (!bloque.equals(bloqueAnterior)) {                    // cabecera al cambiar de bloque
                System.out.println("  ── Bloque " + bloque + " " + "─".repeat(46));
                bloqueAnterior = bloque;
            }
            System.out.printf("  %2d) %-5s %s%n", i + 1, OPCIONES[i].numero(), OPCIONES[i].descripcion());
        }
        System.out.println("   T) Ejecutar todos      0) Salir");
    }

    /** Acepta la posición (1-18) o el código del ejercicio ("3.2", "1.1b"...). Devuelve null si no existe. */
    private static Opcion buscar(String texto) {
        try {
            int n = Integer.parseInt(texto);                     // ¿es un número de la lista?
            if (n >= 1 && n <= OPCIONES.length) return OPCIONES[n - 1];
        } catch (NumberFormatException e) {
            // no es un número entero: se prueba como código ("3.2", "2.2a"...)
        }
        for (Opcion o : OPCIONES) {
            // "2.2" sin letra elige la primera variante (2.2a); con letra, la exacta
            if (o.numero().equalsIgnoreCase(texto) || o.numero().equalsIgnoreCase(texto + "a")) return o;
        }
        return null;
    }

    /**
     * Lanza el ejercicio en un proceso hijo y espera a que termine.
     *
     * Se usa el MISMO java y el MISMO classpath que el menú:
     *   - java.home → el JDK con el que se ha arrancado el menú (no el «java» del PATH, que en algunos
     *     equipos es un Java 8 que no puede abrir clases compiladas con un JDK moderno);
     *   - java.class.path → la carpeta con las clases compiladas (out/ en VS Code e IntelliJ).
     */
    private static void ejecutar(Opcion o) throws Exception {
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String codificacion = System.getProperty("stdout.encoding", "UTF-8");   // para que salgan bien las tildes

        System.out.println();
        System.out.println("▶ " + o.numero() + " · " + o.descripcion() + "   [" + o.clase() + "]");
        System.out.println("─".repeat(60));

        ProcessBuilder pb = new ProcessBuilder(java, "-Dstdout.encoding=" + codificacion,
                "-cp", classpath, o.clase());
        pb.inheritIO();                       // el hijo escribe directamente en ESTA consola (como en la UT1)
        long inicio = System.nanoTime();
        Process hijo = pb.start();            // crea el proceso: una JVM nueva con el main del ejercicio
        int codigo = hijo.waitFor();          // el menú (padre) espera a que el hijo termine
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        System.out.println("─".repeat(60));
        System.out.printf("■ Fin de %s (código de salida %d, %,d ms)%n", o.numero(), codigo, ms);
    }
}
