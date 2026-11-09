/*
 * UT2 · Relación de ejercicios · Ejercicio 2.2 – El examen
 *
 * Un alumno que contesta un examen de 20 preguntas (200–600 ms cada una).
 */
package bloque2.examen;

public class Alumno {

    static final int PREGUNTAS = 20;

    private final String nombre;
    private int contestadas = 0;
    private boolean aTiempo = false;

    public Alumno(String nombre) {
        this.nombre = nombre;
    }

    /** Contesta las preguntas una a una; si le retiran el examen, deja de escribir en ese momento. */
    public void hacerExamen() {
        // TODO
    }

    public String getNombre() { return nombre; }
    public int getContestadas() { return contestadas; }
    public boolean terminoATiempo() { return aTiempo; }
}
