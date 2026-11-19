/*
 * UT2 · Relación de ejercicios · Ejercicio 4.4 – El banco
 *
 * 10 cuentas con 1.000 € cada una.
 *
 * SOLUCIÓN. Tres modos de transferir:
 *   SIN_PROTECCION → el enunciado pide verlo fallar primero.
 *   UN_CERROJO     → primera corrección: un único cerrojo (el banco) para TODAS las transferencias.
 *   POR_CUENTA     → ampliación: un cerrojo por cuenta; transferencias entre cuentas distintas van a la vez.
 */
package bloque4.banco;

public class Banco {

    public enum Modo { SIN_PROTECCION, UN_CERROJO, POR_CUENTA }

    private final Cuenta[] cuentas;
    private final Modo modo;

    public Banco(int numCuentas, int saldoInicial, Modo modo) {
        cuentas = new Cuenta[numCuentas];
        for (int i = 0; i < numCuentas; i++) {
            cuentas[i] = new Cuenta(i, saldoInicial);
        }
        this.modo = modo;
    }

    public int getNumCuentas() {
        return cuentas.length;
    }

    /** Mueve dinero de una cuenta a otra, solo si la de origen tiene saldo suficiente. */
    public void transferir(int origen, int destino, int cantidad) {
        Cuenta de = cuentas[origen];
        Cuenta a = cuentas[destino];
        switch (modo) {
            case SIN_PROTECCION:
                mover(de, a, cantidad);                 // dos cajeros pueden tocar la misma cuenta a la vez
                break;

            case UN_CERROJO:
                synchronized (this) {                   // el cerrojo del BANCO: una transferencia a la vez,
                    mover(de, a, cantidad);             // aunque sean de cuentas que no tienen nada que ver
                }
                break;

            case POR_CUENTA:
                // Hay que coger los cerrojos de las DOS cuentas. Riesgo: INTERBLOQUEO (deadlock).
                //   Cajero 1: transferir(3 → 7) coge la 3 y espera la 7.
                //   Cajero 2: transferir(7 → 3) coge la 7 y espera la 3.  → se esperan para siempre.
                // Solución: coger SIEMPRE los cerrojos en el mismo orden (el de menor id primero).
                // Así nunca se forma un ciclo de esperas (se rompe la condición de "espera circular").
                Cuenta primera = de.id < a.id ? de : a;
                Cuenta segunda = de.id < a.id ? a : de;
                synchronized (primera) {
                    synchronized (segunda) {
                        mover(de, a, cantidad);
                    }
                }
                break;
        }
    }

    /** La transferencia en sí: comprobar saldo, restar y sumar. Es la SECCIÓN CRÍTICA. */
    private void mover(Cuenta de, Cuenta a, int cantidad) {
        if (de.saldo >= cantidad) {         // comprobar (check)…
            de.saldo -= cantidad;           // … actuar: leer-restar-escribir (no atómico)
            a.saldo += cantidad;            // leer-sumar-escribir (no atómico)
        }
    }

    /** Dinero total del banco. Se llama al final, con todos los cajeros terminados (tras join()). */
    public int total() {
        int suma = 0;
        for (Cuenta c : cuentas) {
            suma += c.saldo;
        }
        return suma;
    }
}
