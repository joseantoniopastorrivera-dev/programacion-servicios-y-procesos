/*
 * UT2 · Relación de ejercicios · Ejercicio 4.4 – El banco
 *
 * Una cuenta del banco.
 *
 * SOLUCIÓN. Se deja igual. En la versión POR_CUENTA (ampliación), el propio objeto Cuenta se usa como
 * cerrojo: synchronized (cuenta) { … }. Cualquier objeto de Java tiene un cerrojo intrínseco.
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
