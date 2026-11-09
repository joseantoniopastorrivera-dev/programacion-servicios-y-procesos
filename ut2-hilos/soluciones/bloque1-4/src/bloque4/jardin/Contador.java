/*
 * UT2 · Relación de ejercicios · Ejercicio 4.1 – Arregla el jardín
 *
 * SOLUCIÓN. Interfaz común para las tres correcciones: así la Puerta y el main son los mismos para todas
 * y solo cambia la clase del contador. Es la misma idea que el ContadorVisitantes del 3.2.
 */
package bloque4.jardin;

public interface Contador {

    /** Entra un visitante. */
    void entrar();

    /** Visitantes contados. */
    int total();
}
