public class Presentacion {
    public static void main(String[] args) {
        // Si la longitud del array de argumentos es 0, no se ha pasado nada
        if (args.length == 0) {
            System.out.println("Uso: java Presentacion <nombre> [edad]");
            return; // Detiene la ejecución del programa
        }

        // Si llegamos aquí, hay al menos 1 argumento
        String nombre = args[0];
        System.out.println("Hola, " + nombre + ".");

        // Comprobamos si existe un segundo argumento para la edad
        if (args.length >= 2) {
            try {
                // Convertimos el texto recibido por consola a número entero
                int edad = Integer.parseInt(args[1]);
                System.out.println("Tienes " + edad + " años.");
            } catch (NumberFormatException e) {
                // Prevención de colapso si el usuario escribe letras en lugar de la edad
                System.out.println("Error: La edad introducida no es un número entero válido.");
            }
        }
    }
}