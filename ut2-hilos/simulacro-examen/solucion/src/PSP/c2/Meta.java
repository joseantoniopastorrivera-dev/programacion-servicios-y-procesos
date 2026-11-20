/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · Simulacro de examen UT2 (hilos) · Solución
 * ============================================================
 */

package PSP.c2;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio C2.1 · La meta de la carrera: un objeto COMPARTIDO por todos los corredores.
 *
 * Asigna las posiciones por orden de llegada y guarda la clasificación. Varios corredores pueden
 * cruzar a la vez, así que los métodos son {@code synchronized}: si no, dos corredores podrían
 * leer el mismo número de llegados y recibir la misma posición (condición de carrera).
 */
public class Meta {

    /** Nombres de los corredores en orden de llegada (la posición es índice + 1). */
    private final List<String> clasificacion = new ArrayList<>();

    /**
     * Registra la llegada de un corredor, le asigna su posición y lo anuncia.
     * Comprobar el tamaño, añadir y anunciar va junto en la sección crítica: es una operación
     * compuesta que no puede intercalarse con la de otro corredor.
     *
     * @param nombre nombre del corredor que llega
     * @return la posición en que ha llegado (1 = primero)
     */
    public synchronized int cruzar(String nombre) {
        clasificacion.add(nombre);
        int posicion = clasificacion.size();
        System.out.println(nombre + " cruza la meta en posición " + posicion);
        return posicion;
    }

    /**
     * Devuelve una COPIA de la clasificación, para que nadie modifique la lista interna desde fuera.
     *
     * @return nombres en orden de llegada
     */
    public synchronized List<String> getClasificacion() {
        return new ArrayList<>(clasificacion);
    }

    /**
     * Número de corredores que han llegado.
     *
     * @return cuántos han cruzado la meta
     */
    public synchronized int getLlegados() {
        return clasificacion.size();
    }
}
