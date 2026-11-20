/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · Simulacro de examen UT2 (hilos) · Solución
 * ============================================================
 */

package PSP.c3;

/**
 * Ejercicio C3.2 · Hilo que busca UNA vocal en el texto y, cada vez que la encuentra,
 * suma uno al recuento compartido.
 */
public class ContadorVocal implements Runnable {

    private final String texto;
    private final char vocal;
    private final Recuento recuento;

    /**
     * @param texto    texto en el que buscar (ya en minúsculas)
     * @param vocal    vocal que cuenta este hilo
     * @param recuento recuento compartido por todos los hilos
     */
    public ContadorVocal(String texto, char vocal, Recuento recuento) {
        this.texto = texto;
        this.vocal = vocal;
        this.recuento = recuento;
    }

    /** Recorre el texto y suma cada aparición de su vocal en el recuento común. */
    @Override
    public void run() {
        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) == vocal) {
                recuento.sumar(vocal);       // sección crítica dentro de Recuento (synchronized)
            }
        }
    }
}
