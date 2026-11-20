/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque4.entradas;

/**
 * UT2 · Relación de ejercicios · Ejercicio 4.2 – Taquillas sin overbooking
 *
 * La versión del 3.4, sin corregir: para comparar tiempos (pregunta 2).
 */
public class SalaSinProteger extends Sala {

    /**
     * Versión del 3.4, sin proteger: comprobar, cobrar y descontar sin cerrojo (vende de más).
     *
     * @return true si «ha vendido» una entrada
     */
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
