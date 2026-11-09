/*
 * UT2 · Relación de ejercicios · Ejercicio 3.2 – El jardín ornamental
 *
 * Una puerta de entrada al jardín.
 *
 * SOLUCIÓN. Forma 1 de compartir: todas las puertas reciben el MISMO objeto ContadorVisitantes en el constructor.
 */
package bloque3.jardin;

public class Puerta extends Thread {

    private final ContadorVisitantes contador;     // referencia al contador común (no es una copia)
    private final int visitantes;                  // cuántos entran por esta puerta

    public Puerta(String nombre, ContadorVisitantes contador, int visitantes) {
        super(nombre);
        this.contador = contador;
        this.visitantes = visitantes;
    }

    public void abrir() {
        for (int i = 0; i < visitantes; i++) {
            contador.entrar();                     // un visitante más
        }
    }

    @Override
    public void run() {
        abrir();
    }
}
