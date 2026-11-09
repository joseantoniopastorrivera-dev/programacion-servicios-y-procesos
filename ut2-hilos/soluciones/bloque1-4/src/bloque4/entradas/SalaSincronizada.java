/*
 * UT2 · Relación de ejercicios · Ejercicio 4.2 – Taquillas sin overbooking
 *
 * SOLUCIÓN PRINCIPAL. La sección crítica es TODO el "comprobar → cobrar → descontar": se hace entero
 * dentro de synchronized. Correcto, pero mientras una taquilla cobra (1 ms), las otras tres esperan:
 * las ventas se hacen de una en una.
 */
package bloque4.entradas;

public class SalaSincronizada extends Sala {

    @Override
    public synchronized boolean vender() {     // un solo hilo dentro a la vez, sobre la misma sala
        if (disponibles > 0) {
            cobrar();                           // ¡se cobra con el cerrojo cogido! → lento
            disponibles--;
            return true;
        }
        return false;
    }
}
