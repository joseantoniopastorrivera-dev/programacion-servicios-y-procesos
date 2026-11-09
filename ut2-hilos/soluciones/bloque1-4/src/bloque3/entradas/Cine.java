/*
 * UT2 · Relación de ejercicios · Ejercicio 3.4 – Venta de entradas
 *
 * Cuatro taquillas venden a la vez.
 */
package bloque3.entradas;

public class Cine {

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
