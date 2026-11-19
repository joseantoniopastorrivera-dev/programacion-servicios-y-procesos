/*
 * UT2 · Relación de ejercicios · Ejercicio 3.2 – El jardín ornamental (pregunta 3)
 *
 * SOLUCIÓN. Forma 2 de compartir el contador: un atributo STATIC. Un atributo static pertenece a la CLASE,
 * no a cada objeto: aunque haya dos objetos PuertaContadorEstatico, solo existe UN visitantes.
 * El problema es exactamente el mismo: visitantes++ sigue sin ser atómico.
 */
package bloque3.jardin;

public class PuertaContadorEstatico extends Thread {

    static int visitantes = 0;          // compartido por TODAS las puertas de esta clase

    private final int porEstaPuerta;

    public PuertaContadorEstatico(String nombre, int porEstaPuerta) {
        super(nombre);
        this.porEstaPuerta = porEstaPuerta;
    }

    @Override
    public void run() {
        for (int i = 0; i < porEstaPuerta; i++) {
            visitantes++;               // la misma sección crítica sin proteger
        }
    }
}
