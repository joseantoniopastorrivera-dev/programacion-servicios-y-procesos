/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque4.jardin;

/**
 * UT2 · Relación de ejercicios · Ejercicio 4.1 – Arregla el jardín
 *
 * SOLUCIÓN. La Puerta del 3.2, pero recibe un Contador (la interfaz): no sabe ni le importa cuál de
 * las tres correcciones está usando.
 */
public class Puerta extends Thread {

    private final Contador contador;
    private final int visitantes;

    /**
     * Crea una puerta que cuenta en un contador compartido (cualquiera de las tres versiones).
     *
     * @param nombre     nombre de la puerta (también será el del hilo)
     * @param contador   contador común a todas las puertas
     * @param visitantes visitantes que entrarán por esta puerta
     */
    public Puerta(String nombre, Contador contador, int visitantes) {
        super(nombre);
        this.contador = contador;
        this.visitantes = visitantes;
    }

    /**
     * Trabajo del hilo: sumar un visitante al contador por cada uno que entra.
     */
    @Override
    public void run() {
        for (int i = 0; i < visitantes; i++) {
            contador.entrar();
        }
    }
}
