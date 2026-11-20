/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · Simulacro de examen UT2 (hilos) · Solución
 * ============================================================
 */

package PSP.c2;

import java.util.List;

/**
 * Ejercicio C2.2 · Carrera con tiempo límite.
 *
 * Lanza un hilo por corredor, espera a todos con una HORA LÍMITE COMÚN, retira (interrumpe) a los
 * que no han llegado y muestra la clasificación y el número de retirados.
 *
 * Argumentos: {@code <corredores (3-5)> <tiempoMaximo (segundos, > 0)>}
 */
public class Carrera {

    /**
     * Punto de entrada: valida los argumentos y organiza la carrera.
     *
     * @param args número de corredores y tiempo máximo en segundos
     */
    public static void main(String[] args) {
        int[] valores = leerArgumentos(args);
        int corredores = valores[0];
        int tiempoMaximo = valores[1];

        System.out.println("Salida de " + corredores + " corredores (tiempo máximo: " + tiempoMaximo + " s)");
        Meta meta = new Meta();
        Thread[] hilos = lanzar(corredores, meta);
        try {
            esperarConLimite(hilos, tiempoMaximo * 1000L);
            retirarRezagados(hilos);
        } catch (InterruptedException e) {
            System.err.println("El juez fue interrumpido mientras esperaba");
            System.exit(1);
        }
        mostrarClasificacion(meta, corredores);
    }

    /**
     * Comprueba y convierte los argumentos. Si son incorrectos, avisa por stderr y termina con código 1.
     *
     * @param args argumentos del programa
     * @return {corredores, tiempoMaximo}
     */
    static int[] leerArgumentos(String[] args) {
        if (args.length != 2) {
            System.err.println("Uso: Carrera <corredores (3-5)> <tiempoMaximo (s)>");
            System.exit(1);
        }
        try {
            int corredores = Integer.parseInt(args[0]);
            int tiempo = Integer.parseInt(args[1]);
            if (corredores < 3 || corredores > 5 || tiempo <= 0) {
                System.err.println("Los corredores deben ser entre 3 y 5, y el tiempo mayor que 0");
                System.exit(1);
            }
            return new int[]{corredores, tiempo};
        } catch (NumberFormatException e) {
            System.err.println("Los argumentos deben ser números enteros: " + e.getMessage());
            System.exit(1);
            return null;                     // no se llega: el compilador no sabe que exit no vuelve
        }
    }

    /**
     * Crea y arranca un hilo por corredor, todos con la misma meta.
     *
     * @param n    número de corredores
     * @param meta meta compartida
     * @return los hilos arrancados
     */
    static Thread[] lanzar(int n, Meta meta) {
        Thread[] hilos = new Thread[n];
        for (int i = 0; i < n; i++) {
            String nombre = "Corredor " + (i + 1);
            hilos[i] = new Thread(new Corredor(nombre, meta), nombre);
            hilos[i].start();
        }
        return hilos;
    }

    /**
     * Espera a todos los hilos, pero como mucho hasta una hora límite COMÚN.
     * Si se hiciera join(ms) a cada uno, las esperas se sumarían (hasta n × ms).
     *
     * @param hilos hilos a esperar
     * @param ms    tiempo máximo total, en milisegundos
     * @throws InterruptedException si el hilo principal es interrumpido
     */
    static void esperarConLimite(Thread[] hilos, long ms) throws InterruptedException {
        long limite = System.currentTimeMillis() + ms;
        for (Thread h : hilos) {
            long queda = limite - System.currentTimeMillis();
            if (queda > 0) {                 // ¡join(0) significaría esperar SIN límite!
                h.join(queda);
            }
        }
    }

    /**
     * Interrumpe a los corredores que siguen en carrera y espera a que se retiren.
     *
     * @param hilos hilos de los corredores
     * @throws InterruptedException si el hilo principal es interrumpido
     */
    static void retirarRezagados(Thread[] hilos) throws InterruptedException {
        for (Thread h : hilos) {
            if (h.isAlive()) {
                h.interrupt();               // su sleep() lanza InterruptedException y se retira
            }
        }
        for (Thread h : hilos) {
            h.join();                        // espera a que todos terminen de verdad
        }
    }

    /**
     * Muestra la clasificación final y cuántos se han retirado.
     *
     * @param meta       meta con la clasificación
     * @param corredores número de corredores que salieron
     */
    static void mostrarClasificacion(Meta meta, int corredores) {
        List<String> clasificacion = meta.getClasificacion();
        System.out.println("CLASIFICACIÓN");
        for (int i = 0; i < clasificacion.size(); i++) {
            System.out.println((i + 1) + ". " + clasificacion.get(i));
        }
        System.out.println("Retirados: " + (corredores - clasificacion.size()));
    }
}
