package lorganisation.projectrpg.experiments;

public class AnsiCodes {

    public static void main(String[] args) throws InterruptedException {

        clearConsole();

        System.out.println("Loading ...");
        for (int i = 0; i < 100; i++) {
            System.out.print(getLoadingBar(i));
            Thread.sleep(10);
        }
        System.out.println("DONE.             ");
        System.out.println();

        red("Cleared !");
        System.out.println();

        test();
        System.out.println();

        System.out.println("Some String.");
        System.out.println("Some other.");
        Thread.sleep(2000);
        moveUp(2);
        System.out.println("And it changed !");
        System.out.println();
    }

    static String getLoadingBar(int percent) {

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            if (i < percent / 10) {
                sb.append("=");
            } else {
                sb.append(" ");
            }
        }
        sb.append("]\r");
        return sb.toString();
    }

    static void clearConsole() {

        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void red(String s) {

        System.out.print("\033[31m" + s + "\u001b[0m");
        System.out.flush();
    }

    static void test() {

        System.out.print("\u001b[1m\u001b[4m\u001b[7m BOLD Underline Reversed \u001b[0m");
        System.out.flush();
    }

    static void moveUp(int n) {

        System.out.print("\u001b[" + n + "A");
        System.out.flush();
    }

    static void moveDown(int n) {

        System.out.print("\u001b[" + n + "B");
        System.out.flush();
    }

    static void moveRight(int n) {

        System.out.print("\u001b[" + n + "C");
        System.out.flush();
    }

    static void moveLeft(int n) {

        System.out.print("\u001b" + n + "1D");
        System.out.flush();
    }
}