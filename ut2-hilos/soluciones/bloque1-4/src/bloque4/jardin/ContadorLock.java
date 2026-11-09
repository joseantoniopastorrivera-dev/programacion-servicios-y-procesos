/*
 * UT2 · Relación de ejercicios · Ejercicio 4.1 – Arregla el jardín
 *
 * SOLUCIÓN 2: cerrojo explícito ReentrantLock (paquete java.util.concurrent.locks).
 * Hace lo mismo que synchronized, pero el cerrojo es un objeto que se coge y se suelta a mano.
 */
package bloque4.jardin;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ContadorLock implements Contador {

    private final Lock cerrojo = new ReentrantLock();   // "reentrante": el mismo hilo puede cogerlo varias veces
    private int cuenta = 0;

    @Override
    public void entrar() {
        cerrojo.lock();             // coge el cerrojo (si lo tiene otro hilo, espera)
        try {
            cuenta++;               // sección crítica
        } finally {
            cerrojo.unlock();       // SIEMPRE en finally: si hubiera una excepción y no se soltara,
        }                           // los demás hilos se quedarían bloqueados para siempre
    }

    @Override
    public int total() {
        cerrojo.lock();
        try {
            return cuenta;
        } finally {
            cerrojo.unlock();
        }
    }
}
