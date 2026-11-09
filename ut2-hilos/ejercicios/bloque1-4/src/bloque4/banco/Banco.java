/*
 * UT2 · Relación de ejercicios · Ejercicio 4.4 – El banco
 *
 * 10 cuentas con 1.000 € cada una.
 */
package bloque4.banco;

public class Banco {

    private final Cuenta[] cuentas;

    public Banco(int numCuentas, int saldoInicial) {
        cuentas = new Cuenta[numCuentas];
        for (int i = 0; i < numCuentas; i++) {
            cuentas[i] = new Cuenta(i, saldoInicial);
        }
    }

    /** Mueve dinero de una cuenta a otra, solo si la de origen tiene saldo suficiente. */
    public void transferir(int origen, int destino, int cantidad) {
        // TODO
    }

    /** Dinero total del banco. */
    public int total() {
        // TODO
        return 0;
    }
}
