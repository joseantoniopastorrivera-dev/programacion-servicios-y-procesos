/*
 * UT2 · Relación de ejercicios · Ejercicio 4.2 – Taquillas sin overbooking
 *
 * SOLUCIÓN. Clase base con lo común a todas las versiones (las entradas y el cobro). Cada versión
 * (sin proteger, solo el descuento, sincronizada, con reserva) es una subclase que implementa vender().
 */
package bloque4.entradas;

public abstract class Sala {

    static final int ENTRADAS = 100;

    protected int disponibles = ENTRADAS;      // DATO COMPARTIDO

    /** Vende una entrada si queda alguna. */
    public abstract boolean vender();

    /** El instante que se tarda en cobrar (igual que en el 3.4). */
    protected void cobrar() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized int getDisponibles() {     // se lee al final, pero así es seguro siempre
        return disponibles;
    }
}
