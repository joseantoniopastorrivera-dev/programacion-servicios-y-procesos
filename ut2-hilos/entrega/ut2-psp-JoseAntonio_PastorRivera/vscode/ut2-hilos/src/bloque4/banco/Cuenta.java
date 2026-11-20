/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque4.banco;

/**
 * UT2 · Relación de ejercicios · Ejercicio 4.4 – El banco
 *
 * Una cuenta del banco.
 *
 * SOLUCIÓN. Se deja igual. En la versión POR_CUENTA (ampliación), el propio objeto Cuenta se usa como
 * cerrojo: synchronized (cuenta) { … }. Cualquier objeto de Java tiene un cerrojo intrínseco.
 */
public class Cuenta {

    final int id;
    int saldo;

    /**
     * Crea una cuenta.
     *
     * @param id           identificador (sirve para ordenar los cerrojos y evitar el interbloqueo)
     * @param saldoInicial saldo inicial en euros
     */
    public Cuenta(int id, int saldoInicial) {
        this.id = id;
        this.saldo = saldoInicial;
    }
}
