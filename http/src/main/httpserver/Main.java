package httpserver;

public class Main {

    public static void main(String[] args) {
        try {
            new ServerInstance().start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
