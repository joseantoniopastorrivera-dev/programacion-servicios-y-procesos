/*
 * UT2 · Relación de ejercicios · Ejercicio 4.2 – Taquillas sin overbooking
 *
 * La versión del 3.4, sin corregir: para comparar tiempos (pregunta 2).
 */
package bloque4.entradas;

public class SalaSinProteger extends Sala {

    @Override
    public boolean vender() {
        if (disponibles > 0) {      // comprobar…
            cobrar();
            disponibles--;          // … y actuar, sin protección → overbooking
            return true;
        }
        return false;
    }
}
