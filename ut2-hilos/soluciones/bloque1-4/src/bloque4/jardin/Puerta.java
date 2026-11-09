/*
 * UT2 · Relación de ejercicios · Ejercicio 4.1 – Arregla el jardín
 *
 * SOLUCIÓN. La Puerta del 3.2, pero recibe un Contador (la interfaz): no sabe ni le importa cuál de
 * las tres correcciones está usando.
 */
package bloque4.jardin;

public class Puerta extends Thread {

    private final Contador contador;
    private final int visitantes;

    public Puerta(String nombre, Contador contador, int visitantes) {
        super(nombre);
        this.contador = contador;
        this.visitantes = visitantes;
    }

    @Override
    public void run() {
        for (int i = 0; i < visitantes; i++) {
            contador.entrar();
        }
    }
}
