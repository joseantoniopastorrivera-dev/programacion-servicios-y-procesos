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
 * Una puerta de entrada al jardín.
 *
 * SOLUCIÓN. Forma 1 de compartir: todas las puertas reciben el MISMO objeto ContadorVisitantes en el constructor.
 */
public class Puerta extends Thread {

    private final ContadorVisitantes contador;     // referencia al contador común (no es una copia)
    private final int visitantes;                  // cuántos entran por esta puerta

    /**
     * Crea una puerta del jardín.
     *
     * @param nombre     nombre de la puerta (también será el del hilo)
     * @param contador   contador común a todas las puertas
     * @param visitantes visitantes que entrarán por esta puerta
     */
    public Puerta(String nombre, ContadorVisitantes contador, int visitantes) {
        super(nombre);
        this.contador = contador;
        this.visitantes = visitantes;
    }

    /**
     * Deja entrar a sus visitantes, sumando uno al contador común por cada uno.
     */
    public void abrir() {
        for (int i = 0; i < visitantes; i++) {
            contador.entrar();                     // un visitante más
        }
    }

    /**
     * Trabajo del hilo: abrir la puerta.
     */
    @Override
    public void run() {
        abrir();
    }
}
