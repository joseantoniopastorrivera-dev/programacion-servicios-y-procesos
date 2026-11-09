/*
 * UT2 · Relación de ejercicios · Ejercicio 4.4 – El banco
 *
 * Un cajero que hace 200.000 transferencias de 1 a 100 € entre cuentas al azar.
 */
package bloque4.banco;

import java.util.concurrent.ThreadLocalRandom;

public class Cajero extends Thread {

    static final int TRANSFERENCIAS = 200_000;

    private final Banco banco;          // el MISMO banco para los 4 cajeros

    public Cajero(String nombre, Banco banco) {
        super(nombre);
        this.banco = banco;
    }

    public void hacerTransferencias() {
        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();
        int n = banco.getNumCuentas();
        for (int i = 0; i < TRANSFERENCIAS; i++) {
            int origen = aleatorio.nextInt(n);
            int destino = aleatorio.nextInt(n - 1);     // n-1 posibilidades: todas menos el origen…
            if (destino >= origen) {
                destino++;                              // … saltando el origen: así nunca son la misma cuenta
            }
            int cantidad = aleatorio.nextInt(1, 101);   // de 1 a 100 €
            banco.transferir(origen, destino, cantidad);
        }
    }

    @Override
    public void run() {
        hacerTransferencias();
    }
}
