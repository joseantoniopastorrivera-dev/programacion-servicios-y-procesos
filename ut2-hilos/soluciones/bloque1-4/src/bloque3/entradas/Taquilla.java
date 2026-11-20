/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT2 · Relación de ejercicios (bloques 1 a 4)
 * ============================================================
 */

package bloque3.entradas;

/**
 * UT2 · Relación de ejercicios · Ejercicio 3.4 – Venta de entradas
 *
 * Una taquilla: le llegan 40 clientes que quieren una entrada.
 */
public class Taquilla extends Thread {

    static final int CLIENTES = 40;

    private final Sala sala;        // la MISMA sala para las 4 taquillas
    private int vendidas = 0;       // solo la toca esta taquilla: no es compartido

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
     * Atiende a sus clientes: intenta venderle una entrada a cada uno.
     */
    public void atenderClientes() {
        for (int c = 0; c < CLIENTES; c++) {
            if (sala.vender()) {    // cada cliente intenta comprar una entrada
                vendidas++;
            }
        }
    }

    /**
     * Trabajo del hilo: atender a los clientes.
     */
    @Override
    public void run() {
        atenderClientes();
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
