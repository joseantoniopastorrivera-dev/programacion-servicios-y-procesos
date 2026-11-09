/*
 * UT2 · Relación de ejercicios · Ejercicio 1.1 – Descargas
 *
 * Una descarga de un fichero a 10 MB/s.
 */
package bloque1.descargas;

public class Descarga {

    static final int MB_POR_SEGUNDO = 10;

    private final String fichero;
    private final int megas;

    public Descarga(String fichero, int megas) {
        this.fichero = fichero;
        this.megas = megas;
    }

    /** Simula la descarga e indica, al terminar, cuánto tiempo ha pasado desde que arrancó el programa. */
    public void descargar() {
        // TODO
    }
}
