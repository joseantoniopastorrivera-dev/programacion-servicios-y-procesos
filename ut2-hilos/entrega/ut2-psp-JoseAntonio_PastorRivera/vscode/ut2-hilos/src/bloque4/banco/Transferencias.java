/*
 * UT2 · Relación de ejercicios · Ejercicio 4.4 – El banco
 *
 * Cuatro cajeros trabajan a la vez.
 *
 * SOLUCIÓN. Los tres modos del banco, 3 veces cada uno, comprobando el dinero total y midiendo el tiempo.
 */
package bloque4.banco;

public class Transferencias {

    static final int CUENTAS = 10;
    static final int SALDO_INICIAL = 1000;
    static final int CAJEROS = 4;

    public static void main(String[] args) throws InterruptedException {
        for (Banco.Modo modo : Banco.Modo.values()) {
            for (int prueba = 1; prueba <= 3; prueba++) {
                simular(modo);
            }
            System.out.println();
        }
    }

    private static void simular(Banco.Modo modo) throws InterruptedException {
        Banco banco = new Banco(CUENTAS, SALDO_INICIAL, modo);
        int inicial = banco.total();                        // 10 × 1.000 = 10.000 €

        Cajero[] cajeros = new Cajero[CAJEROS];
        long inicio = System.nanoTime();
        for (int i = 0; i < CAJEROS; i++) {
            cajeros[i] = new Cajero("Cajero " + (i + 1), banco);
            cajeros[i].start();
        }
        for (Cajero c : cajeros) {
            c.join();
        }
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        int fin = banco.total();
        System.out.printf("%-14s  inicial %,6d €  final %,6d €  → %-24s (%d ms)%n", modo, inicial, fin,
                fin == inicial ? "cuadra" : "¡NO CUADRA! (" + (fin - inicial) + " €)", ms);
    }
}
