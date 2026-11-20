/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque3.jardin;

/**
 * UT2 · Relación de ejercicios · Ejercicio 3.2 – El jardín ornamental
 *
 * Dos puertas, 1.000.000 de visitantes por cada una.
 *
 * SOLUCIÓN. Hace tres experimentos, cada uno 5 veces (con hilos, una sola ejecución no demuestra nada):
 *   1) contador compartido como objeto, 1.000.000 por puerta
 *   2) contador compartido como atributo static (pregunta 3)
 *   3) solo 1.000 visitantes por puerta (pregunta 4)
 */
public class Jardin {

    static final int REPETICIONES = 5;

    /**
     * Simula el jardín varias veces (contador objeto, contador static y pocos visitantes) para ver la carrera.
     *
     * @param args no se usan
     * @throws InterruptedException si el main es interrumpido mientras espera a las puertas
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("== 1) Contador compartido (objeto), 1.000.000 por puerta");
        for (int r = 0; r < REPETICIONES; r++) {
            simularDia(1_000_000);
        }

        System.out.println("== 2) Contador compartido (atributo static), 1.000.000 por puerta");
        for (int r = 0; r < REPETICIONES; r++) {
            PuertaContadorEstatico.visitantes = 0;                  // se reinicia el contador común
            PuertaContadorEstatico norte = new PuertaContadorEstatico("Norte", 1_000_000);
            PuertaContadorEstatico sur = new PuertaContadorEstatico("Sur", 1_000_000);
            norte.start();
            sur.start();
            norte.join();
            sur.join();
            mostrar(PuertaContadorEstatico.visitantes, 2_000_000);
        }

        System.out.println("== 3) Solo 1.000 visitantes por puerta");
        for (int r = 0; r < REPETICIONES; r++) {
            simularDia(1_000);
        }
    }

    /** Un día con dos puertas que comparten el MISMO objeto contador. */
    private static void simularDia(int porPuerta) throws InterruptedException {
        ContadorVisitantes contador = new ContadorVisitantes();        // un único contador…
        Puerta norte = new Puerta("Norte", contador, porPuerta);       // … que reciben las dos puertas
        Puerta sur = new Puerta("Sur", contador, porPuerta);
        norte.start();
        sur.start();
        norte.join();                                                  // esperar a que cierren las dos
        sur.join();
        mostrar(contador.total(), 2 * porPuerta);
    }

    /**
     * Muestra el resultado de una vuelta y cuántos visitantes se han perdido.
     *
     * @param obtenido valor final del contador
     * @param esperado valor que debería tener
     */
    private static void mostrar(int obtenido, int esperado) {
        System.out.printf("   contador = %,9d  (esperado %,9d)  %s%n", obtenido, esperado,
                obtenido == esperado ? "OK" : "¡se han perdido " + String.format("%,d", esperado - obtenido) + "!");
    }
}
