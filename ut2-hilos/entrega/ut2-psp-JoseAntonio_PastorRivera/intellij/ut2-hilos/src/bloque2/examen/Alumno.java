/*
 * UT2 · Relación de ejercicios · Ejercicio 2.2 – El examen
 *
 * Un alumno que contesta un examen de 20 preguntas (200–600 ms cada una).
 *
 * SOLUCIÓN. El alumno es un Runnable. El profesor (main) le "retira el examen" con interrupt():
 * como el alumno pasa casi todo el tiempo dentro de Thread.sleep(), la interrupción le despierta al
 * momento con una InterruptedException, y ahí deja de escribir.
 */
package bloque2.examen;

import java.util.concurrent.ThreadLocalRandom;

public class Alumno implements Runnable {

    static final int PREGUNTAS = 20;

    private final String nombre;
    private int contestadas = 0;
    private boolean aTiempo = false;

    public Alumno(String nombre) {
        this.nombre = nombre;
    }

    /** Contesta las preguntas una a una; si le retiran el examen, deja de escribir en ese momento. */
    public void hacerExamen() {
        try {
            for (int i = 1; i <= PREGUNTAS; i++) {
                // ThreadLocalRandom: generador de aleatorios propio de cada hilo (más rápido que un Random compartido).
                // nextInt(200, 601) da un número entre 200 y 600, ambos incluidos.
                Thread.sleep(ThreadLocalRandom.current().nextInt(200, 601));   // "pensando y escribiendo" la pregunta
                contestadas++;                                                  // pregunta i contestada
            }
            aTiempo = true;                     // solo se llega aquí si ha contestado las 20 sin que le interrumpan
        } catch (InterruptedException e) {
            // El profesor ha hecho interrupt(): sleep() se despierta lanzando esta excepción.
            // No se contesta nada más: "deja de escribir en ese mismo momento".
            // La pregunta que estaba haciendo NO cuenta (el contestadas++ no llega a ejecutarse).
        }
    }

    @Override
    public void run() {
        hacerExamen();
    }

    public String getNombre() { return nombre; }
    public int getContestadas() { return contestadas; }
    public boolean terminoATiempo() { return aTiempo; }
}
