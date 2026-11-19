/*
 * UT2 · Relación de ejercicios · Ejercicio 4.2 – Taquillas sin overbooking
 *
 * La Taquilla del 3.4, pero recibe cualquier Sala (la clase base).
 */
package bloque4.entradas;

public class Taquilla extends Thread {

    static final int CLIENTES = 40;

    private final Sala sala;
    private int vendidas = 0;

    public Taquilla(String nombre, Sala sala) {
        super(nombre);
        this.sala = sala;
    }

    @Override
    public void run() {
        for (int c = 0; c < CLIENTES; c++) {
            if (sala.vender()) {
                vendidas++;
            }
        }
    }

    public int getVendidas() {
        return vendidas;
    }
}
