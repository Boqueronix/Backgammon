import java.awt.*;
import java.util.ArrayList;

public class Main {
    private static Turn turn;
    private static boolean running = false;
    private static int[] dice = new int[2];
    private static Piece selected;

    public static void main(String[] args) throws InterruptedException {
        init();
        while (!running) {
            rollOff();
        }
        while (running) {
            run();
        }
    }

    private static void init() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 40));
        ArrayList<Piece> black = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            int point;
            if (i < 5){
                point = 6;
            } else if (i < 8) {
                point = 8;
            } else if (i < 13) {
                point = 13;
            } else {
                point = 24;
            }
            black.add(new Piece(Turn.BLACK, point));
        }
        Piece.blackPieces = black;
        ArrayList<Piece> red = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            int point;
            if (i < 5){
                point = 6;
            } else if (i < 8) {
                point = 8;
            } else if (i < 13) {
                point = 13;
            } else {
                point = 24;
            }
            red.add(new Piece(Turn.RED, point));
        }
        Piece.redPieces = red;
        Piece.all.add(Piece.blackPieces);
        Piece.all.add(Piece.redPieces);
    }

    private static void draw() throws InterruptedException {
//        StdDraw.clear();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j++) {
                double[] pointsX = new double[]{j / 12.0, (j + 1) / 12.0, (j + 0.5) / 12.0};
                double[] pointsY = new double[]{i, i, Math.abs(i - 0.4)};
                StdDraw.setPenColor(((i + j) % 2 == 0) ? new Color(255, 191, 0) : new Color(133, 99, 0));
                StdDraw.filledPolygon(pointsX, pointsY);
            }
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(0.5, 0.5, 0.01, 0.5);
        StdDraw.show();
        Piece.draw();
        StdDraw.show();
    }

    private static void rollOff() throws InterruptedException {
        draw();
        //roll two die (1-6) inclusive
        int dieB = (int) (Math.random() * 6) + 1;
        int dieR = (int) (Math.random() * 6) + 1;
        //roll them again until they aren't the same number
        while (dieB == dieR) {
            dieB = (int) (Math.random() * 6) + 1;
            dieR = (int) (Math.random() * 6) + 1;
        }
        dice = new int[]{dieB, dieR};
        for (int i = 0; i < 2; i++) {
            StdDraw.setPenColor((i == 0) ? Color.BLACK : Color.RED);
            StdDraw.filledSquare(0.5 + (i + 1) / 6.0, 0.5, 0.045);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(0.5 + (i + 1) / 6.0, 0.49, "" + dice[i]);
//            System.out.println(0.5 + (i + 1) / 10.0);
        }
        StdDraw.show();
        turn = (dieB > dieR) ? Turn.BLACK : Turn.RED;
        StdDraw.setTitle((turn == Turn.BLACK) ? "Black Turn" : "Red Turn");
        double[] click = null;
        while (click == null || selected == null) {
            click = getInput();
            select(click);
        }
    }

    private static void run() throws InterruptedException {
        draw();
        roll();
    }

    private static void roll() {
        int die0 = (int) (Math.random() * 6) + 1;
        int die1 = (int) (Math.random() * 6) + 1;
        //(int) (Math.random() * 6) + 1
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius();
        if (die0 == die1) {
//            System.out.println("double");
            dice = new int[]{die0, die1, die0, die1};
            for (int i = 0; i < 4; i++) {
                StdDraw.square(0.5 + (i + 1) / 10.0, 0.5, 0.045);
                StdDraw.text(0.5 + (i + 1) / 10.0, 0.49, "" + dice[i]);
//                System.out.println(0.5 + (i + 1) / 10.0);
            }
            StdDraw.show();
        } else {
            dice = new int[]{die0, die1};
            for (int i = 0; i < 2; i++) {
                StdDraw.square(0.5 + (i + 1) / 6.0, 0.5, 0.045);
                StdDraw.text(0.5 + (i + 1) / 6.0, 0.49, "" + dice[i]);
//                System.out.println(0.5 + (i + 1) / 10.0);
            }
            StdDraw.show();
        }
    }

    private static double[] getInput() {
        while (true) {
            if (StdDraw.isMousePressed()) {
                return new double[]{StdDraw.mouseX, StdDraw.mouseY};
            }
        }
    }

    private static void select(double[] coordinates) {
        int tbs = -1;
        for (int i = 0; i < 24; i++) {

        }
    }
    private static void move(Piece p, int point) throws InterruptedException {
        p.setPoint(point);
        draw();
    }
}