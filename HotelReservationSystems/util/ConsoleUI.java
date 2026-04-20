package HotelReservationSystems.util;


public class ConsoleUI {


    public static final String RESET  = "\u001B[0m";
    public static final String BOLD   = "\u001B[1m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN   = "\u001B[36m";
    public static final String RED    = "\u001B[31m";
    public static final String WHITE  = "\u001B[37m";

    private static final int WIDTH = 60;


    public static void printBanner(String hotelName) {
        System.out.println(CYAN + BOLD);
        System.out.println(hotelName);
        System.out.println("Hotel Reservation Management System");

        System.out.print(RESET);
    }


    public static void printHeader(String title) {
        System.out.println(YELLOW + BOLD);
        System.out.printf( "%s%n", center("◆  " + title + "  ◆", 60));
        System.out.print(RESET);
    }


    public static void divider() {
        System.out.println(WHITE + "──────────────────────────────────────────────────────────" + RESET);
    }


    public static void success(String msg) {
        System.out.println(GREEN + BOLD + "  ✔  " + msg + RESET);
    }

    public static void error(String msg) {
        System.out.println(RED + BOLD + "  ✘  " + msg + RESET);
    }

    public static void info(String msg) {
        System.out.println(CYAN + "  ℹ  " + msg + RESET);
    }

    public static void warn(String msg) {
        System.out.println(YELLOW + "  ⚠  " + msg + RESET);
    }


    public static void prompt(String label) {
        System.out.print(BOLD + "  » " + label + ": " + RESET);
    }


    public static void menuOption(String num, String text) {
        System.out.printf("   %s[%s]%s %s%n", CYAN, num, RESET, text);
    }


    public static void printBillRow(String label, String value) {
        System.out.printf("  %-30s %s%s%s%n", label, YELLOW, value, RESET);
    }

    public static void printBillTotal(double amount) {
        System.out.println(GREEN + BOLD);
        System.out.printf( "  │  TOTAL AMOUNT DUE: %sRs. %-18.2f%s  │%n", WHITE, amount, GREEN);
        System.out.print(RESET);
    }


    public static String center(String text, int width) {
        // Strip ANSI codes to measure visible length
        String stripped = text.replaceAll("\u001B\\[[;\\d]*m", "");
        int pad = Math.max(0, width - stripped.length());
        int left = pad / 2;
        int right = pad - left;
        return " ".repeat(left) + text + " ".repeat(right);
    }

    public static void pressEnter() {
        System.out.println();
        System.out.print(WHITE + "  [ Press ENTER to continue... ]" + RESET);
        try { System.in.read(); new java.util.Scanner(System.in).nextLine(); }
        catch (Exception ignored) {}
    }
}
