/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque2.taxis;

/**
 * UT2 · Relación de ejercicios · Ejercicio 2.4 – Taxis
 *
 * Los 3 taxis trabajan a la vez.
 */
public class Flota {

    /**
     * Pone a trabajar los tres taxis a la vez, espera a todos y muestra los km de cada uno y el total.
     *
     * @param args no se usan
     * @throws InterruptedException si el main es interrumpido mientras espera a los taxis
     */
    public static void main(String[] args) throws InterruptedException {
        Taxi[] taxis = {new Taxi("1234-ABC"), new Taxi("5678-DEF"), new Taxi("9012-GHI")};
        Thread[] hilos = new Thread[taxis.length];

        for (int i = 0; i < taxis.length; i++) {
            hilos[i] = new Thread(taxis[i], taxis[i].getMatricula());  // el Taxi (Runnable) se le da a un Thread
            hilos[i].start();
        }
        for (Thread h : hilos) {
            h.join();                               // "cuando han terminado todos"
        }

        // El total se calcula AL FINAL, en el main, sumando los km de cada taxi. Cada taxi solo ha escrito en
        // su propio contador: no ha habido nada compartido mientras trabajaban → no hay condición de carrera.
        int total = 0;
        for (Taxi t : taxis) {
            System.out.printf("Taxi %s: %3d km%n", t.getMatricula(), t.getKmRecorridos());
            total += t.getKmRecorridos();
        }
        System.out.printf("Total de la flota: %d km%n", total);
    }
}
