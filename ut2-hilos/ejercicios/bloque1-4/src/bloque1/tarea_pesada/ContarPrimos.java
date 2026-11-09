/*
 * UT2 · Relación de ejercicios · Ejercicio 1.2 – Tarea pesada sin bloquear
 *
 * El cálculo largo: cuántos números primos hay hasta un límite.
 */
package bloque1.tarea_pesada;

public class ContarPrimos {

    private final int limite;

    public ContarPrimos(int limite) {
        this.limite = limite;
    }

    /** Ya está hecho: no forma parte del ejercicio. */
    static boolean esPrimo(int n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;
        for (int d = 3; (long) d * d <= n; d += 2) {
            if (n % d == 0) return false;
        }
        return true;
    }

    /** Cuenta los primos entre 2 y el límite. */
    public int contar() {
        // TODO
        return 0;
    }
}
