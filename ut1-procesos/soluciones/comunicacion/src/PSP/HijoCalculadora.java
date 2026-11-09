package PSP;

import java.util.Scanner;

public class HijoCalculadora {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String linea = sc.nextLine().trim();
            if (linea.equals("fin")) {
                break;
            }
            System.out.println(calcular(linea));
        }
        sc.close();
    }

    // Recibe "a op b" y devuelve el resultado o "ERROR"
    private static String calcular(String operacion) {
        String[] partes = operacion.split("\\s+");
        if (partes.length != 3) {
            return "ERROR";
        }
        try {
            double a = Double.parseDouble(partes[0]);
            double b = Double.parseDouble(partes[2]);
            switch (partes[1]) {
                case "+": return String.valueOf(a + b);
                case "-": return String.valueOf(a - b);
                case "*": return String.valueOf(a * b);
                case "/":
                    if (b == 0) {
                        return "ERROR";
                    }
                    return String.valueOf(a / b);
                default: return "ERROR";
            }
        } catch (NumberFormatException e) {
            return "ERROR";
        }
    }
}
