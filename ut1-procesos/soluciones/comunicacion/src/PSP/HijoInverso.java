package PSP;

import java.util.Scanner;

public class HijoInverso {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String linea = sc.nextLine();
            String invertida = new StringBuilder(linea).reverse().toString();
            System.out.println(invertida);
        }
        sc.close();
    }
}
