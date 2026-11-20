/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · Examen RA1 (09/11/2026) · Solución de repaso
 *  Enunciado: ../../../../examen-ut1.md
 * ============================================================
 */

package PSP.c2;

/*
 * C2.4 · Reflexión: ¿qué cambiaría con tres hilos en lugar de tres procesos?
 *
 * Todo iría en un solo proceso (una JVM): cada grupo lo calcularía un Thread y se esperaría con
 * join() en vez de waitFor(). Los hilos comparten la memoria, así que no harían falta redirecciones
 * ni tuberías ni un formato de texto: cada hilo dejaría su cantidad y su media en un objeto común.
 * VENTAJA: crear un hilo es mucho más ligero que arrancar una JVM por grupo, y compartir datos es
 * directo. INCONVENIENTES: al compartir memoria aparecen las condiciones de carrera (habría que
 * sincronizar el acceso al resultado común con synchronized), y se pierde el aislamiento: un error
 * grave o un System.exit en un hilo tumba todo el programa, y ya no habría códigos de salida para
 * saber qué grupo ha fallado.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;

/**
 * Ejercicio C2.2 · Lanza un {@link HijoEstadistica} por grupo EN PARALELO, muestra el resultado
 * de cada uno y calcula la media global ponderada (no la media de las medias).
 */
public class Padre {

    /** Grupos a procesar: cada uno tiene su fichero datos/&lt;grupo&gt;.txt. */
    private static final String[] GRUPOS = {"grupoA", "grupoB", "grupoC"};

    /** Fichero común de errores: los hijos AÑADEN sus avisos sin borrar lo que hubiera. */
    private static final File ERRORES = new File("errores.log");

    /**
     * Punto de entrada: lanza los tres hijos, recoge sus resultados y muestra la media global.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        try {
            // 1. Lanzar TODOS primero (paralelo). Si hiciera start() y waitFor() en el mismo
            //    bucle, cada hijo esperaría al anterior: sería secuencial.
            Process[] hijos = new Process[GRUPOS.length];
            for (int i = 0; i < GRUPOS.length; i++) {
                hijos[i] = lanzarHijo(GRUPOS[i]);
                System.out.println("Lanzado " + GRUPOS[i] + " con PID " + hijos[i].pid());
            }

            // 2. Recoger después el resultado de cada uno
            int cantidadTotal = 0;           // notas válidas de los grupos que terminaron bien
            double sumaTotal = 0;            // suma de esas notas (cantidad × media de cada grupo)
            for (int i = 0; i < GRUPOS.length; i++) {
                double[] resultado = recogerResultado(GRUPOS[i], hijos[i]);
                if (resultado != null) {
                    cantidadTotal += (int) resultado[0];
                    sumaTotal += resultado[0] * resultado[1];       // CANTIDAD × MEDIA
                }
            }

            // 3. Media global: suma de todas las notas / número de notas
            if (cantidadTotal > 0) {
                System.out.printf("MEDIA GLOBAL: %.2f (%d notas válidas)%n", sumaTotal / cantidadTotal, cantidadTotal);
            } else {
                System.out.println("MEDIA GLOBAL: ningún grupo ha terminado bien");
            }
            System.out.println("Los avisos de los hijos se han añadido a " + ERRORES.getName());
        } catch (IOException e) {
            System.err.println("Error al lanzar un hijo o leer su salida: " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println("El padre fue interrumpido mientras esperaba a los hijos");
            System.exit(1);
        }
    }

    /**
     * Lanza un HijoEstadistica para un grupo con las redirecciones que pide el enunciado.
     *
     * @param grupo nombre del grupo (por ejemplo "grupoA")
     * @return el {@link Process} del hijo, ya arrancado
     * @throws IOException si no se puede crear el proceso
     */
    static Process lanzarHijo(String grupo) throws IOException {
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder(java, "-cp", classpath, "PSP.c2.HijoEstadistica", grupo);

        // stdin del hijo < datos/grupo.txt : lee el fichero «como si lo escribieran por teclado».
        // El padre NO abre ni lee los ficheros de notas.
        pb.redirectInput(new File("datos", grupo + ".txt"));
        // stderr del hijo >> errores.log : appendTo AÑADE (con new File cada hijo borraría lo anterior).
        pb.redirectError(Redirect.appendTo(ERRORES));
        // stdout se queda en la tubería (PIPE): es lo que leerá el padre.
        return pb.start();
    }

    /**
     * Lee las cuatro líneas de resultado de un hijo, espera a que termine y las muestra.
     * Si el hijo termina con un código distinto de 0, lo indica y el grupo no cuenta.
     *
     * @param grupo nombre del grupo, para los mensajes
     * @param hijo  proceso del hijo
     * @return {cantidad, media} del grupo, o {@code null} si el hijo terminó mal
     * @throws IOException          si falla la lectura de la salida del hijo
     * @throws InterruptedException si el padre es interrumpido mientras espera
     */
    static double[] recogerResultado(String grupo, Process hijo) throws IOException, InterruptedException {
        int cantidad = 0;
        double media = 0;
        String maxima = "", minima = "";

        // REGLA DE ORO: primero leer TODA la salida del hijo, después waitFor().
        // getInputStream() = lo que sale del hijo (su stdout) y entra al padre.
        try (BufferedReader br = new BufferedReader(new InputStreamReader(hijo.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.trim().split(" ");     // "MEDIA 7.25" → ["MEDIA", "7.25"]
                if (partes.length != 2) continue;              // línea inesperada: se ignora
                // if/else y no switch con flechas: el proyecto del profe pide JDK 11 o superior
                if (partes[0].equals("CANTIDAD")) {
                    cantidad = Integer.parseInt(partes[1]);
                } else if (partes[0].equals("MEDIA")) {
                    media = Double.parseDouble(partes[1]);
                } else if (partes[0].equals("MAXIMA")) {
                    maxima = partes[1];
                } else if (partes[0].equals("MINIMA")) {
                    minima = partes[1];
                }
            }
        }
        int codigo = hijo.waitFor();

        if (codigo != 0) {
            System.out.println(grupo + ": el hijo terminó con código " + codigo + " → no se tiene en cuenta");
            return null;
        }
        System.out.println(grupo + ": CANTIDAD " + cantidad + " · MEDIA " + media
                + " · MAXIMA " + maxima + " · MINIMA " + minima);
        return new double[]{cantidad, media};
    }
}
