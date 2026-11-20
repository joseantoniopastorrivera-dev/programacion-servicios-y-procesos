/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · Simulacro de examen UT2 (hilos) · Solución
 * ============================================================
 */

package PSP.c1;

/*
 * C1.3 · Reflexión: ¿funciona si se arrancan las tareas en orden inverso (T7 … T1)?
 *
 * Con join() a secas, casi siempre sí, pero NO está garantizado. join() sobre un hilo que todavía
 * no ha hecho start() (estado NEW, isAlive() = false) vuelve inmediatamente sin esperar. Funciona
 * por casualidad: el main arranca las siete tareas en microsegundos y T7 tarda más en llegar a su
 * join(). Si el main se retrasara entre dos start() (un sleep, un println lento), T7 haría join()
 * sobre un T6 en NEW y empezaría antes de tiempo: es una condición de carrera sobre el orden de
 * arranque. Mi solución lo hace seguro: antes del join() cada tarea espera a que su dependencia
 * deje de estar en NEW (Tarea.esperarA). Otra opción es arrancar siempre en orden de dependencias.
 */

/**
 * Ejercicio C1.2 · Simula el despliegue de una aplicación con siete tareas que dependen unas de otras.
 *
 * <pre>
 * T1 → T2 → T4 ┐
 *    ↘ T3 → T5 ┴→ T6 → T7
 * </pre>
 * Muestra el tiempo total y lo compara con el de una ejecución secuencial (la suma de las duraciones).
 *
 * Argumento opcional: {@code normal} (por defecto) o {@code inverso} (arranca de T7 a T1).
 */
public class Despliegue {

    /**
     * Punto de entrada: comprueba el argumento, crea las tareas, las arranca y espera al final.
     *
     * @param args vacío, "normal" o "inverso"
     */
    public static void main(String[] args) {
        String orden = args.length == 0 ? "normal" : args[0];
        if (args.length > 1 || !(orden.equals("normal") || orden.equals("inverso"))) {
            System.err.println("Uso: Despliegue [normal|inverso]");
            System.exit(1);
        }

        Tarea[] tareas = crearTareas();
        System.out.println("Despliegue en orden " + orden);

        Tarea.marcarInicio();
        long inicio = System.currentTimeMillis();
        arrancar(tareas, orden.equals("inverso"));
        try {
            tareas[tareas.length - 1].join();          // T7 es la última: si ha terminado, han terminado todas
        } catch (InterruptedException e) {
            System.err.println("El main fue interrumpido mientras esperaba el despliegue");
            System.exit(1);
        }
        long total = System.currentTimeMillis() - inicio;

        System.out.printf("Despliegue completado en %d ms (en secuencial serían %d ms)%n", total, sumaDuraciones(tareas));
    }

    /**
     * Crea las siete tareas con sus dependencias. Una tarea solo puede depender de otra ya creada,
     * porque recibe su referencia en el constructor.
     *
     * @return las tareas, de T1 a T7 (sin arrancar)
     */
    static Tarea[] crearTareas() {
        Tarea t1 = new Tarea("T1 · Descargar el código", 1000);
        Tarea t2 = new Tarea("T2 · Compilar el backend", 2000, t1);
        Tarea t3 = new Tarea("T3 · Compilar el frontend", 1500, t1);
        Tarea t4 = new Tarea("T4 · Tests del backend", 1500, t2);
        Tarea t5 = new Tarea("T5 · Tests del frontend", 1000, t3);
        Tarea t6 = new Tarea("T6 · Crear la imagen Docker", 1000, t4, t5);   // depende de DOS
        Tarea t7 = new Tarea("T7 · Desplegar en el servidor", 500, t6);
        return new Tarea[]{t1, t2, t3, t4, t5, t6, t7};
    }

    /**
     * Arranca todas las tareas.
     *
     * @param tareas  tareas a arrancar, de T1 a T7
     * @param inverso true para arrancarlas de T7 a T1
     */
    static void arrancar(Tarea[] tareas, boolean inverso) {
        for (int i = 0; i < tareas.length; i++) {
            tareas[inverso ? tareas.length - 1 - i : i].start();
        }
    }

    /**
     * Suma la duración de todas las tareas: lo que tardaría el despliegue en un solo hilo.
     *
     * @param tareas tareas del despliegue
     * @return suma de sus duraciones en ms
     */
    static int sumaDuraciones(Tarea[] tareas) {
        int suma = 0;
        for (Tarea t : tareas) {
            suma += t.getMs();
        }
        return suma;
    }
}
