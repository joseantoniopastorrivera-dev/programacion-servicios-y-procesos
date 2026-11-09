package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Ejercicio15 {
    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder pb;
        if (args.length > 0) {
            pb = new ProcessBuilder("java", "-cp", "bin", "PSP.HijoAdivina", args[0]);
        } else {
            pb = new ProcessBuilder("java", "-cp", "bin", "PSP.HijoAdivina");
        }
        Process hijo = pb.start();
        PrintWriter haciaHijo = new PrintWriter(hijo.getOutputStream());
        BufferedReader desdeHijo = new BufferedReader(new InputStreamReader(hijo.getInputStream()));

        // Búsqueda binaria: probamos siempre el punto medio del intervalo
        int minimo = 1;
        int maximo = 100;
        int intentos = 0;
        String respuesta = "";

        while (!respuesta.equals("acertado")) {
            int intento = (minimo + maximo) / 2;
            intentos++;

            haciaHijo.println(intento);
            haciaHijo.flush();
            respuesta = desdeHijo.readLine();
            System.out.println("Intento " + intentos + ": " + intento + " -> " + respuesta);

            if (respuesta.equals("mayor")) {
                minimo = intento + 1;
            } else if (respuesta.equals("menor")) {
                maximo = intento - 1;
            }
        }

        haciaHijo.close();
        hijo.waitFor();
        System.out.println("¡Adivinado en " + intentos + " intentos!");
    }
}
