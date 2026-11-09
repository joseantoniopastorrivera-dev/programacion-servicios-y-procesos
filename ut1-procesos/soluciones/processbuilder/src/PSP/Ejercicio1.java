package PSP;

import java.io.IOException;

/**
 * Ejercicio 1. Mi primer proceso: abrir la Calculadora y después un editor de texto.
 *
 * El enunciado usa macOS (open -a Calculator). Equivalencias:
 *   macOS   : open -a Calculator          / open -a TextEdit
 *   Linux   : gnome-calculator o kcalc    / gedit, gnome-text-editor o kate (depende del escritorio)
 *   Windows : calc.exe                    / notepad.exe
 */
public class Ejercicio1 {

    // En Linux cada escritorio trae su calculadora: se prueban en orden hasta que una arranque.
    static final String[] CALCULADORAS_LINUX = {"gnome-calculator", "kcalc", "galculator", "xcalc"};
    static final String[] EDITORES_LINUX = {"gnome-text-editor", "gedit", "kate", "mousepad", "xed"};

    public static void main(String[] args) {
        abrir("Calculadora", CALCULADORAS_LINUX, "calc.exe");
        abrir("Editor de texto", EDITORES_LINUX, "notepad.exe");
    }

    /** Lanza el primer programa que exista. start() lanza IOException si el ejecutable no existe. */
    static Process abrir(String que, String[] candidatosLinux, String windows) {
        String[] candidatos = SO.WINDOWS ? new String[]{windows} : candidatosLinux;
        for (String programa : candidatos) {
            try {
                Process p = new ProcessBuilder(programa).start();   // crea el proceso y NO espera a que termine
                System.out.println(que + " lanzada con «" + programa + "» (PID " + p.pid() + ")");
                return p;
            } catch (IOException e) {
                System.out.println("«" + programa + "» no está instalado, pruebo el siguiente...");
            }
        }
        System.out.println("No se ha encontrado ningún programa para: " + que + " (¿Ubuntu Server sin escritorio?)");
        return null;
    }
}
