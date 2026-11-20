/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · Simulacro de examen UT2 (hilos) · Solución
 * ============================================================
 */

package PSP.c3;

/**
 * Ejercicio C3.1 · Recuento COMPARTIDO por los cinco hilos: lleva el total de vocales y
 * el número de cada una.
 *
 * Todos los hilos suman en el mismo total, así que {@code sumar} es {@code synchronized}:
 * {@code total++} son tres pasos (leer, sumar, escribir) y sin protección se perderían vocales.
 */
public class Recuento {

    /** Las vocales que se cuentan, en el orden en que se muestran. */
    public static final char[] VOCALES = {'a', 'e', 'i', 'o', 'u'};

    private int total = 0;
    private final int[] porVocal = new int[VOCALES.length];

    /**
     * Suma una aparición de una vocal al total y a su contador.
     *
     * @param vocal la vocal encontrada ('a', 'e', 'i', 'o' o 'u')
     * @throws IllegalArgumentException si no es una de las cinco vocales
     */
    public synchronized void sumar(char vocal) {
        int i = new String(VOCALES).indexOf(vocal);
        if (i < 0) {
            throw new IllegalArgumentException("No es una vocal: " + vocal);
        }
        porVocal[i]++;
        total++;
    }

    /**
     * Número de veces que ha aparecido una vocal.
     *
     * @param vocal la vocal
     * @return cuántas veces se ha contado (0 si no es una vocal)
     */
    public synchronized int getCantidad(char vocal) {
        int i = new String(VOCALES).indexOf(vocal);
        return i < 0 ? 0 : porVocal[i];
    }

    /**
     * Total de vocales contadas.
     *
     * @return suma de todas las vocales
     */
    public synchronized int getTotal() {
        return total;
    }
}
