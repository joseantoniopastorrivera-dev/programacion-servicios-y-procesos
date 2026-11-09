/*
 * UT2 · Relación de ejercicios · Ejercicio 4.2 – Taquillas sin overbooking
 *
 * Copia en este paquete tu solución del ejercicio 3.4 y corrígela.
 *
 * SOLUCIÓN. Ejecuta las cuatro versiones de la sala, 5 sesiones cada una, y muestra si alguna vez se vendió
 * de más y el tiempo medio por sesión.
 */
package bloque4.entradas;

import java.util.function.Supplier;

public class CineCorregido {

    static final int TAQUILLAS = 4;
    static final int SESIONES = 5;

    public static void main(String[] args) throws InterruptedException {
        probar("Sin proteger (3.4)       ", SalaSinProteger::new);
        probar("Solo el descuento (P1)   ", SalaSoloDescuento::new);
        probar("vender() synchronized    ", SalaSincronizada::new);
        probar("Reserva + cobro fuera (P3)", SalaConReserva::new);
    }

    private static void probar(String nombre, Supplier<Sala> fabrica) throws InterruptedException {
        int maxVendidas = 0;
        long totalMs = 0;
        StringBuilder detalle = new StringBuilder();
        for (int s = 0; s < SESIONES; s++) {
            Sala sala = fabrica.get();
            Taquilla[] taquillas = new Taquilla[TAQUILLAS];
            long inicio = System.nanoTime();
            for (int i = 0; i < TAQUILLAS; i++) {
                taquillas[i] = new Taquilla("Taquilla " + (i + 1), sala);
                taquillas[i].start();
            }
            int vendidas = 0;
            for (Taquilla t : taquillas) {
                t.join();
                vendidas += t.getVendidas();
            }
            totalMs += (System.nanoTime() - inicio) / 1_000_000;
            maxVendidas = Math.max(maxVendidas, vendidas);
            detalle.append(vendidas).append('/').append(sala.getDisponibles()).append("  ");
        }
        System.out.printf("%s  vendidas/quedan: %s → %s, %3d ms por sesión%n", nombre, detalle,
                maxVendidas > Sala.ENTRADAS ? "OVERBOOKING" : "correcto", totalMs / SESIONES);
    }
}
