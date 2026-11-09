/*
 * UT2 · Relación de ejercicios · Ejercicio 4.4 – El banco
 *
 * Una cuenta del banco.
 */
package bloque4.banco;

public class Cuenta {

    final int id;
    int saldo;

    public Cuenta(int id, int saldoInicial) {
        this.id = id;
        this.saldo = saldoInicial;
    }
}
