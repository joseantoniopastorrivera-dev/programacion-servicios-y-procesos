package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Ejercicio 8. Cifrado César (ROT13) con tr, aplicado dos veces.
 *   Linux   : tr A-Za-z N-ZA-Mn-za-m
 *   Windows : no trae tr; se usa el de Git para Windows: C:/Program Files/Git/usr/bin/tr.exe
 * Reflexión: «ñ» y las tildes NO cambian: tr trabaja con los rangos A-Z/a-z del alfabeto inglés
 * y además esas letras ocupan 2 bytes en UTF-8, fuera de los rangos.
 */
public class Ejercicio08 {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Texto: ");
        String texto = sc.nextLine();
        String cifrado = rot13(texto);                 // primer proceso tr
        System.out.println("Cifrado:    " + cifrado);
        String descifrado = rot13(cifrado);            // OTRO proceso tr con el texto cifrado
        System.out.println("Descifrado: " + descifrado);
        System.out.println("¿Coincide con el original?: " + descifrado.equals(texto));
        sc.close();
    }

    /** Envía el texto a tr y devuelve lo que responde. */
    static String rot13(String texto) throws IOException, InterruptedException {
        String tr = SO.WINDOWS ? SO.git("tr") : "tr";
        Process p = new ProcessBuilder(tr, "A-Za-z", "N-ZA-Mn-za-m").start();
        PrintWriter pw = new PrintWriter(p.getOutputStream());
        pw.println(texto);
        pw.close();                                    // tr procesa hasta el final de su entrada
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String resultado = br.readLine();
        br.close();
        p.waitFor();
        return resultado;
    }
}
