package PSP;

/**
 * Ayuda para que cada ejercicio funcione en Linux (examen) y en Windows (casa).
 *
 * En el examen NO hace falta esta clase: se escribe directamente el comando de Linux,
 * por ejemplo new ProcessBuilder("sleep", "3"). Aquí la usamos para que el MISMO programa
 * se pueda probar en los dos sistemas sin tocar el código.
 */
public final class SO {

    /** true si estamos en Windows. os.name vale, por ejemplo, "Windows 11" o "Linux". */
    public static final boolean WINDOWS =
            System.getProperty("os.name").toLowerCase().startsWith("windows");

    /** Carpeta con las herramientas de Linux que instala Git para Windows (tr, sort, wc...). */
    public static final String GIT_BIN = "C:\\Program Files\\Git\\usr\\bin\\";

    private SO() {
        // clase de utilidades: no se crean objetos
    }

    /** Devuelve el comando de Linux o el de Windows según el sistema en el que se ejecuta. */
    public static String[] comando(String[] linux, String[] windows) {
        return WINDOWS ? windows : linux;
    }

    /** Ruta a una herramienta de Git Bash en Windows: tr, sort, wc, uniq, head... */
    public static String git(String herramienta) {
        return GIT_BIN + herramienta + ".exe";
    }

    /** Comando para dormir n segundos: «sleep n» en Linux; PowerShell en Windows (no tiene sleep). */
    public static String[] dormir(int segundos) {
        return comando(new String[]{"sleep", String.valueOf(segundos)},
                new String[]{"powershell", "-NoProfile", "-Command", "Start-Sleep " + segundos});
    }

    /** Nombre del sistema, para mostrarlo en los mensajes. */
    public static String nombre() {
        return System.getProperty("os.name");
    }
}
