/*
 * UT2 · Relación de ejercicios · Ejercicio 4.3 – La máquina de turnos
 *
 * Cuatro clientes usan la máquina a la vez.
 *
 * SOLUCIÓN. Para cada modo de la máquina: 4 clientes × 100.000 números y una comprobación que DEMUESTRA
 * en la salida si hay repetidos: se marca cada número en un array de booleanos; si al marcarlo ya estaba
 * marcado, es un repetido.
 */
package bloque4.turnos;

public class Carniceria {

    static final int CLIENTES = 4;

    public static void main(String[] args) throws InterruptedException {
        for (MaquinaTurnos.Modo modo : MaquinaTurnos.Modo.values()) {   // SIN_PROTECCION, SYNCHRONIZED, CAS
            for (int prueba = 1; prueba <= 3; prueba++) {
                comprobar(modo);
            }
        }
    }

    private static void comprobar(MaquinaTurnos.Modo modo) throws InterruptedException {
        MaquinaTurnos maquina = new MaquinaTurnos(modo);
        Cliente[] clientes = new Cliente[CLIENTES];
        long inicio = System.nanoTime();
        for (int i = 0; i < CLIENTES; i++) {
            clientes[i] = new Cliente("Cliente " + (i + 1), maquina);
            clientes[i].start();
        }
        for (Cliente c : clientes) {
            c.join();
        }
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        int total = CLIENTES * Cliente.TURNOS;              // 400.000 números sacados
        boolean[] visto = new boolean[total + 1];           // posiciones 1..400.000
        int repetidos = 0;
        int mayor = 0;
        for (Cliente c : clientes) {
            for (int n : c.getNumeros()) {
                if (n > total) {                            // con repetidos el mayor nunca supera 400.000,
                    continue;                               // pero se comprueba por seguridad
                }
                if (visto[n]) {
                    repetidos++;                            // este número ya lo tenía otro cliente (o él mismo)
                } else {
                    visto[n] = true;
                }
                mayor = Math.max(mayor, n);
            }
        }
        System.out.printf("%-14s  %,d números, mayor %,7d, repetidos %,6d  → %s  (%d ms)%n", modo, total, mayor,
                repetidos, repetidos == 0 ? "ningún número repetido" : "¡HAY REPETIDOS!", ms);
    }
}
