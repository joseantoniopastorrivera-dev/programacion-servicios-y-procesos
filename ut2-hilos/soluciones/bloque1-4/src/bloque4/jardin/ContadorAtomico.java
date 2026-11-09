/*
 * UT2 · Relación de ejercicios · Ejercicio 4.1 – Arregla el jardín
 *
 * SOLUCIÓN 3: variable atómica AtomicInteger (paquete java.util.concurrent.atomic).
 * incrementAndGet() hace leer + sumar + escribir como UNA sola operación indivisible, usando una
 * instrucción especial del procesador (compare-and-swap, CAS). No hay cerrojo: ningún hilo se bloquea.
 */
package bloque4.jardin;

import java.util.concurrent.atomic.AtomicInteger;

public class ContadorAtomico implements Contador {

    private final AtomicInteger cuenta = new AtomicInteger(0);

    @Override
    public void entrar() {
        cuenta.incrementAndGet();   // el "cuenta++" atómico
    }

    @Override
    public int total() {
        return cuenta.get();        // lectura con visibilidad garantizada (como volatile)
    }
}
