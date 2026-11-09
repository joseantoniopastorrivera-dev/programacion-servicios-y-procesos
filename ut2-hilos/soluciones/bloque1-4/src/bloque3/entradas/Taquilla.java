/*
 * UT2 · Relación de ejercicios · Ejercicio 3.4 – Venta de entradas
 *
 * Una taquilla: le llegan 40 clientes que quieren una entrada.
 */
package bloque3.entradas;

public class Taquilla extends Thread {

    static final int CLIENTES = 40;

    private final Sala sala;        // la MISMA sala para las 4 taquillas
    private int vendidas = 0;       // solo la toca esta taquilla: no es compartido

    public Taquilla(String nombre, Sala sala) {
        super(nombre);
        this.sala = sala;
    }

    public void atenderClientes() {
        for (int c = 0; c < CLIENTES; c++) {
            if (sala.vender()) {    // cada cliente intenta comprar una entrada
                vendidas++;
            }
        }
    }

    @Override
    public void run() {
        atenderClientes();
    }

    public int getVendidas() {
        return vendidas;
    }
}
