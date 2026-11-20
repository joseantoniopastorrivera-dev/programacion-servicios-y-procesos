/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

import java.io.IOException;

/**
 * Ayuda del Ejercicio 11 para Windows (donde no hay «{@code sh -c ... &}»).
 * Construye el mismo árbol con procesos Java:  java -cp bin PSP.NodoArbol 2
 *   nivel 2 (padre) → lanza un hijo de nivel 1 y otro de nivel 0, y duerme
 *   nivel 1 (hijo)  → lanza un hijo de nivel 0 (el nieto), y duerme
 *   nivel 0         → solo duerme
 * Resultado: un padre, dos hijos y un nieto, como en el enunciado.
 */
public class NodoArbol {

    /**
     * Crea un nodo del árbol de procesos: lanza a sus hijos según su nivel y queda vivo un rato.
     *
     * @param args el nivel del nodo en el árbol
     * @throws IOException          si no se puede lanzar un hijo
     * @throws InterruptedException si el nodo es interrumpido mientras espera
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        int nivel = Integer.parseInt(args[0]);
        if (nivel == 2) {
            lanzar(1);
            lanzar(0);
        } else if (nivel == 1) {
            lanzar(0);
        }
        Thread.sleep(60000);                     // como «sleep 60»
    }

    /**
     * Lanza un nuevo NodoArbol del nivel indicado.
     *
     * @param nivel nivel del hijo en el árbol
     * @throws IOException si no se puede lanzar el proceso
     */
    private static void lanzar(int nivel) throws IOException {
        new ProcessBuilder("java", "-cp", "bin", "PSP.NodoArbol", String.valueOf(nivel)).start();
    }
}
