/*
 * UT2 · Relación de ejercicios · Ejercicio 4.2 – Taquillas sin overbooking (pregunta 3, ampliación)
 *
 * SOLUCIÓN AMPLIADA. Se saca el cobro de la parte protegida:
 *   1) Dentro de synchronized, solo "comprobar y RESERVAR" (descontar ya). Es instantáneo.
 *   2) Fuera, cobrar con calma: las cuatro taquillas pueden cobrar a la vez.
 *   3) Si el cobro fallara, se devolvería la entrada (también protegido).
 * Nunca se vende de más: la entrada se descuenta en el mismo bloque atómico en que se comprueba.
 */
package bloque4.entradas;

public class SalaConReserva extends Sala {

    @Override
    public boolean vender() {
        synchronized (this) {               // sección crítica MÍNIMA
            if (disponibles == 0) {
                return false;               // no queda ninguna
            }
            disponibles--;                  // reservada para este cliente: ya nadie más puede cogerla
        }
        cobrar();                           // fuera del cerrojo: las demás taquillas siguen vendiendo
        // Si el pago pudiera fallar:  if (!pagado) { synchronized (this) { disponibles++; } return false; }
        return true;
    }
}
