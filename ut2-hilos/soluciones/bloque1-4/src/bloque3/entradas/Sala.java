/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque3.entradas;

/**
 * UT2 · Relación de ejercicios · Ejercicio 3.4 – Venta de entradas
 *
 * La sesión de cine: 100 entradas.
 *
 * SOLUCIÓN (con el fallo a propósito: se corrige en el 4.2).
 */
public class Sala {

    static final int ENTRADAS = 100;

    private int disponibles = ENTRADAS;     // DATO COMPARTIDO por las 4 taquillas

    /** Vende una entrada si queda alguna. Cobrar al cliente lleva un instante. */
    public boolean vender() {
        if (disponibles > 0) {              // 1) COMPRUEBA que queda alguna…
            cobrar();                       // 2) … tarda un instante cobrando (otras taquillas comprueban mientras)…
            disponibles--;                  // 3) … y DESCUENTA. Entre 1 y 3 el dato puede haber cambiado.
            return true;
        }
        return false;
        // Patrón "comprobar y después actuar" (check-then-act): NO es atómico. Si las 4 taquillas ven
        // disponibles == 1 a la vez, las 4 venden: overbooking y disponibles acaba en negativo.
    }

    /** El instante que se tarda en cobrar. */
    private void cobrar() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Entradas que quedan (sin protección: puede ser un valor imposible, ese es el fallo que se estudia).
     *
     * @return entradas disponibles
     */
    public int getDisponibles() {
        return disponibles;
    }
}
