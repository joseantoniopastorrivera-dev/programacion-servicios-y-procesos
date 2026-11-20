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
 * Cuatro taquillas venden a la vez.
 */
public class Cine {

    /**
     * Tres sesiones de venta con cuatro taquillas a la vez, sin proteger la sala, para ver el overbooking.
     *
     * @param args no se usan
     * @throws InterruptedException si el main es interrumpido mientras espera a las taquillas
     */
    public static void main(String[] args) throws InterruptedException {
        for (int prueba = 1; prueba <= 3; prueba++) {       // varias ejecuciones: con hilos, una no demuestra nada
            System.out.println("== Sesión " + prueba);
            Sala sala = new Sala();
            Taquilla[] taquillas = new Taquilla[4];
            for (int i = 0; i < taquillas.length; i++) {
                taquillas[i] = new Taquilla("Taquilla " + (i + 1), sala);
                taquillas[i].start();
            }
            int total = 0;
            for (Taquilla t : taquillas) {
                t.join();
                System.out.printf("   %s: %d entradas%n", t.getName(), t.getVendidas());
                total += t.getVendidas();
            }
            System.out.printf("   Total vendido: %d de %d   Quedan: %d   %s%n", total, Sala.ENTRADAS,
                    sala.getDisponibles(), total > Sala.ENTRADAS ? "¡OVERBOOKING!" : "");
        }
    }
}
