/*
 * UT2 · Relación de ejercicios · Ejercicio 3.3 – El vigilante
 *
 * El jefe de seguridad avisa al vigilante a los 2 segundos.
 *
 * SOLUCIÓN. Tres pruebas seguidas:
 *   1) Vigilante sin volatile (enunciado): casi siempre NO se entera.
 *   2) VigilanteVolatile (pregunta 2): se entera siempre.
 *   3) Cuatro VigilanteConTotal (pregunta 3): el total común no cuadra con la suma de sus rondas.
 */
package bloque3.vigilante;

public class Vigilancia {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("== 1) Aviso SIN volatile");
        Vigilante vigilante = new Vigilante();
        vigilante.setDaemon(true);      // si no se entera nunca, su bucle es infinito: daemon para que el
        vigilante.start();              // programa pueda terminar igualmente (lo pide el enunciado)

        Thread.sleep(2000);             // el jefe da el aviso a los 2 s
        System.out.println("Jefe: ¡fin de turno!");
        vigilante.avisarFinDeTurno();

        vigilante.join(3000);           // le da 3 s más para enterarse
        if (vigilante.isAlive()) {
            System.out.println("Jefe: pasados 3 s el vigilante NO se ha enterado del aviso.");
        }

        System.out.println();
        System.out.println("== 2) Aviso CON volatile");
        VigilanteVolatile corregido = new VigilanteVolatile("vigilante");
        corregido.setDaemon(true);
        corregido.start();
        Thread.sleep(2000);
        System.out.println("Jefe: ¡fin de turno!");
        corregido.avisarFinDeTurno();
        corregido.join(3000);
        if (corregido.isAlive()) {
            System.out.println("Jefe: pasados 3 s el vigilante NO se ha enterado del aviso.");
        }

        System.out.println();
        System.out.println("== 3) Cuatro vigilantes con un total común volatile");
        VigilanteConTotal[] equipo = new VigilanteConTotal[4];
        for (int i = 0; i < equipo.length; i++) {
            equipo[i] = new VigilanteConTotal("vigilante-" + (i + 1));
            equipo[i].setDaemon(true);
            equipo[i].start();
        }
        Thread.sleep(2000);
        VigilanteConTotal.turnoTerminado = true;        // aviso a todos
        long suma = 0;
        for (VigilanteConTotal v : equipo) {
            v.join(3000);
            suma += v.getMisRondas();                   // la suma de lo que ha contado cada uno (correcta)
        }
        System.out.printf("Suma de las rondas de cada uno: %,d%n", suma);
        System.out.printf("Total común (volatile):         %,d  → %s%n", VigilanteConTotal.totalRondas,
                suma == VigilanteConTotal.totalRondas ? "cuadra" : "NO cuadra: volatile no hace atómico el ++");

        // Al terminar el main solo quedan hilos daemon (el vigilante de la prueba 1 sigue en su bucle infinito):
        // la JVM termina y los corta.
    }
}
