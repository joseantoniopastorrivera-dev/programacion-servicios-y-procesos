package PSP.c1;

/**
 * Proceso hijo del ejercicio C1 (YA HECHO, no hay que modificarlo).
 *
 * Simula una tarea que tarda varios segundos: escribe una línea de progreso
 * por segundo y termina con el código de salida indicado.
 *
 * Argumentos: <segundos> <codigoDeSalida>
 * Ejemplo: con los argumentos 3 y 0 tarda 3 s y termina con código 0.
 */
public class Tarea {

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            System.err.println("Uso: Tarea <segundos> <codigoDeSalida>");
            System.exit(1);
        }
        int segundos = Integer.parseInt(args[0]);
        int codigo = Integer.parseInt(args[1]);

        System.out.println("Tarea iniciada: durará " + segundos + " s");
        for (int i = 1; i <= segundos; i++) {
            Thread.sleep(1000);
            System.out.println("Paso " + i + " de " + segundos + " completado");
        }
        System.out.println("Tarea terminada con código " + codigo);
        System.exit(codigo);
    }
}
