import java.util.ArrayList;
import java.util.Scanner;

public class ListaCompra {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Declaración e inicialización del ArrayList vacío
        ArrayList<String> lista = new ArrayList<>();
        String producto;

        System.out.println("--- CREADOR DE LISTA DE LA COMPRA ---");
        System.out.println("Introduce los productos uno a uno.");
        System.out.println("Escribe 'fin' cuando hayas terminado.\n");

        // Bucle infinito que romperemos con un 'break' cuando el usuario escriba "fin"
        while (true) {
            System.out.print("Añadir producto: ");
            producto = scanner.nextLine().trim();

            // Comprobamos si el usuario quiere salir (ignorando mayúsculas/minúsculas en la palabra "fin")
            if (producto.equalsIgnoreCase("fin")) {
                break;
            }

            // Evitamos que se añadan entradas vacías si el usuario pulsa Intro sin querer
            if (producto.isEmpty()) {
                continue;
            }

            // Comprobamos si el producto ya existe en la lista usando contains()
            if (lista.contains(producto)) {
                System.out.println("  -> Aviso: El producto '" + producto + "' ya está en la lista. No se ha duplicado.");
            } else {
                lista.add(producto);
                System.out.println("  -> Añadido.");
            }
        }

        System.out.println("\n--- ELIMINAR PRODUCTOS ---");
        System.out.print("¿Qué producto deseas eliminar de la lista? (Pulsa Intro para no eliminar nada): ");
        String productoEliminar = scanner.nextLine().trim();

        // Si el usuario ha escrito algo, intentamos eliminarlo
        if (!productoEliminar.isEmpty()) {
            if (lista.contains(productoEliminar)) {
                lista.remove(productoEliminar);
                System.out.println("El producto '" + productoEliminar + "' ha sido eliminado.");
            } else {
                System.out.println("El producto '" + productoEliminar + "' no se ha encontrado en la lista.");
            }
        }

        // Mostramos el resultado final
        System.out.println("\n--- TU LISTA FINAL ---");
        if (lista.isEmpty()) {
            System.out.println("La lista está vacía.");
        } else {
            // Recorremos la lista para imprimirla numerada
            for (int i = 0; i < lista.size(); i++) {
                System.out.println((i + 1) + ". " + lista.get(i));
            }
        }
        
        // Imprimimos el tamaño total de la lista
        System.out.println("\nTotal de productos: " + lista.size());
        
        scanner.close();
    }
}