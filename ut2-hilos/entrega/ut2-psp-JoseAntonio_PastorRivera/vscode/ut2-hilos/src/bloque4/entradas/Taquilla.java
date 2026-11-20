/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque4.entradas;

/**
 * UT2 · Relación de ejercicios · Ejercicio 4.2 – Taquillas sin overbooking
 *
 * La Taquilla del 3.4, pero recibe cualquier Sala (la clase base).
 */
public class Taquilla extends Thread {

    static final int CLIENTES = 40;

    private final Sala sala;
    private int vendidas = 0;

    /**
     * Crea una taquilla que vende entradas de una sala compartida.
     *
     * @param nombre nombre de la taquilla (también será el del hilo)
     * @param sala   sala común a todas las taquillas
     */
    public Taquilla(String nombre, Sala sala) {
        super(nombre);
        this.sala = sala;
    }

    /**
     * Trabajo del hilo: intentar vender una entrada a cada uno de sus clientes.
     */
    @Override
    public void run() {
        for (int c = 0; c < CLIENTES; c++) {
            if (sala.vender()) {
                vendidas++;
            }
        }
    }

    /**
     * Entradas que ha vendido esta taquilla.
     *
     * @return número de entradas vendidas
     */
    public int getVendidas() {
        return vendidas;
    }
}
