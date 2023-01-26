import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Main {
    private static Turn turn;
    private static boolean running = false;
    private static int[] dice = new int[2];
    private static Piece selected;
    private static Integer selectedPoint;
    private static ArrayList<Integer> possibleMoves = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        init();
        while (!running) {
            rollOff();
        }
        while (running) {
            run();
        }
    }

    private static void init() throws InterruptedException {
        StdDraw.enableDoubleBuffering();
        Piece.all.add(new ArrayList<>(15));
        Piece.all.add(new ArrayList<>(15));
        for (int i = 0; i < 15; i++) {
            int point;
            if (i < 2) {
                point = 0;
            } else if (i < 7) {
                point = 11;
            } else if (i < 10){
                point = 16;
            } else {
                point = 18;
            }
            new Piece(Turn.RED, point);
        }
        for (int i = 0; i < 15; i++) {
            int point;
            if (i < 2) {
                point = 23;
            } else if (i < 7) {
                point = 12;
            } else if (i < 10) {
                point = 7;
            } else {
                point = 5;
            }
            new Piece(Turn.BLACK, point);
        }
    }

    private static void draw() throws InterruptedException {
        StdDraw.clear();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j++) {
                double[] pointsX = new double[]{j / 12.0, (j + 1) / 12.0, (j + 0.5) / 12.0};
                double[] pointsY = new double[]{i, i, Math.abs(i - 0.4)};
                StdDraw.setPenColor(((i + j) % 2 == 0) ? new Color(255, 191, 0) : new Color(133, 99, 0));
                StdDraw.filledPolygon(pointsX, pointsY);
            }
        }
        if (selected != null) {
            for (Integer possibleMove: possibleMoves) {
                int i = (possibleMove < 12) ? 0 : 1;
                int j = (possibleMove < 12) ? 11 - possibleMove : possibleMove - 12;
                double[] pointsX = new double[]{j / 12.0, (j + 1) / 12.0, (j + 0.5) / 12.0};
                double[] pointsY = new double[]{i, i, Math.abs(i - 0.4)};
                StdDraw.setPenColor(new Color(150, 250, 150));
                StdDraw.setPenRadius(0.005);
                StdDraw.polygon(pointsX, pointsY);
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
        }
        StdDraw.show();
        turn = (dieB > dieR) ? Turn.BLACK : Turn.RED;
        StdDraw.setTitle((turn == Turn.BLACK) ? "Black Turn" : "Red Turn");
        double[] click = null;
        for (int i = 0; i < dice.length; i++) {
            while (click == null || selected == null) {
                click = getInput();
                select(click);
                draw();
            }
            while (selected != null) {
                click = getInput();
                move(selected, click);
                draw();
            }
        }
        draw();
        running = true;
    }

    private static void run() throws InterruptedException {
        StdDraw.setTitle((turn == Turn.BLACK) ? "Black Turn" : "Red Turn");
        double[] click = null;
        for (int i = 0; i < dice.length; i++) {
            while (click == null || selected == null) {
                click = getInput();
                select(click);
            }
        }
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
        if (selected != null) {
            selected.selected = false;
            selected = null;
            selectedPoint = null;
            possibleMoves = new ArrayList<>();
        }
        int tbselected = -1;
        if (coordinates[1] < 0.5) {
            tbselected = (int) ((1 - coordinates[0] - 0.0001) * 12);
        } else if (coordinates[1] > 0.5) {
            tbselected = 12 + (int) ((coordinates[0] - 0.0001) * 12);
        }
        System.out.println(tbselected);
        Piece tbselectedPiece = null;
        Integer tbselectedPoint = null;
        for (ArrayList<Piece> pieces : Piece.all) {
            for (Piece p : pieces) {
                if (p.getPoint() == tbselected) {
                    tbselectedPiece = p;
                    tbselectedPoint = p.getPoint();
                }
            }
        }
        if (tbselectedPiece != null) {
            if (tbselectedPiece.getTurn() == turn) {
                selected = tbselectedPiece;
                selected.selected = true;
                selectedPoint = tbselectedPoint;
            }
        }
        if (selected != null){
            int factor;
            if (turn == Turn.RED) {
                factor = 1;
            } else {
                factor = -1;
            }
            if (dice.length == 2){
                if (Piece.getColor(selectedPoint + factor * dice[0]) != selected.getTurn().getOpposite() || Piece.getNum(selectedPoint + factor * dice[0]) < 2){
                    possibleMoves.add(selectedPoint + factor * dice[0]);
                    if (Piece.getColor(selectedPoint + factor * (dice[0] + dice[1])) != selected.getTurn().getOpposite() || Piece.getNum(selectedPoint + factor * (dice[0] + dice[1])) < 2){
                        possibleMoves.add(selectedPoint + factor * (dice[0] + dice[1]));
                    }
                }
                if (Piece.getColor(selectedPoint + factor * dice[1]) != selected.getTurn().getOpposite() || Piece.getNum(selectedPoint + factor * dice[1]) < 2){
                    possibleMoves.add(selectedPoint + factor * dice[1]);
                    if (Piece.getColor(selectedPoint + factor * (dice[1] + dice[0])) != selected.getTurn().getOpposite() || Piece.getNum(selectedPoint + factor * (dice[1] + dice[0])) < 2){
                        possibleMoves.add(selectedPoint + factor * (dice[1] + dice[0]));
                    }
                }
            } else {
                int die = dice[0];
                for (int i = 0; i < dice.length; i++) {
                    if (Piece.getColor(selectedPoint + die * i) != selected.getTurn().getOpposite() || Piece.getNum(selectedPoint + die * i) < 2){
                        possibleMoves.add(selectedPoint + die * i);
                    }
                }
            }
        }
    }
    private static void move(Piece p, double[] coordinates) throws InterruptedException {
        int moveTo = -1;
        if (coordinates[1] < 0.5) {
            moveTo = (int) ((1 - coordinates[0] + 0.0001) * 12);
        } else if (coordinates[1] > 0.5) {
            moveTo = 12 + (int) ((coordinates[0] - 0.0001) * 12);
        }
        if (possibleMoves.contains(moveTo)) {
            p.setPoint(moveTo);
            draw();
        }
    }
}