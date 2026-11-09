/*
 * UT2 · Relación de ejercicios · Ejercicio 4.1 – Arregla el jardín
 *
 * SOLUCIÓN 1: método synchronized (cerrojo intrínseco del objeto, "monitor").
 */
package bloque4.jardin;

public class ContadorSynchronized implements Contador {

    private int cuenta = 0;

    @Override
    public synchronized void entrar() {
        // synchronized en un método = synchronized (this) { … } alrededor de todo el cuerpo.
        // Solo UN hilo a la vez puede estar dentro de un método synchronized del MISMO objeto:
        // el otro se queda BLOCKED en la entrada hasta que el primero sale.
        // Además, al salir se publican los cambios (visibilidad): no hace falta volatile.
        cuenta++;
    }

    @Override
    public synchronized int total() {
        return cuenta;
    }
}
