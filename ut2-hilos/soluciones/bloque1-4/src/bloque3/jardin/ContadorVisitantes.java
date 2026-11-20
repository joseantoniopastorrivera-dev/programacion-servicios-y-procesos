/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque3.jardin;

/**
 * UT2 · Relación de ejercicios · Ejercicio 3.2 – El jardín ornamental
 *
 * El único contador de visitantes del jardín.
 *
 * SOLUCIÓN (con el fallo a propósito: el bloque 3 solo pide encontrarlo y explicarlo).
 */
public class ContadorVisitantes {

    private int cuenta = 0;     // EL DATO COMPARTIDO: las dos puertas usan este mismo objeto

    /** Se llama cada vez que entra un visitante por cualquier puerta. */
    public void entrar() {
        cuenta++;
        // ¡No es UNA operación! Son tres: 1) leer cuenta  2) sumarle 1  3) escribir el resultado.
        // Si las dos puertas leen el mismo valor antes de que la otra escriba, se pierde un visitante.
        // Esta es la SECCIÓN CRÍTICA del ejercicio.
    }

    /**
     * Visitantes contados.
     *
     * @return valor del contador
     */
    public int total() {
        return cuenta;
    }
}
