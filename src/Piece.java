import java.awt.*;
import java.util.ArrayList;

public class Piece {
    public static ArrayList<Piece> blackPieces = new ArrayList<>(15);
    public static ArrayList<Piece> redPieces = new ArrayList<>(15);
    public static ArrayList<ArrayList<Piece>> all = new ArrayList<>(2);
    private final Turn color;
    private int point;
    public boolean selected = false;
    public Piece(Turn color, int point){
        this.color = color;
        this.point = point;
        if (color == Turn.BLACK) {
            blackPieces.add(this);
            all.set(0, blackPieces);
        } else {
            redPieces.add(this);
            all.set(1, redPieces);
        }
    }
    public void setPoint(int point){
        this.point = point;
    }
    public static void draw() {
        for (int i = 0; i < 24; i++) {
            double x;
            if (i < 6){
                x = 1 - (i + 0.5) / 12.0;
            } else if (i < 12) {
                x = 1 - ((i + 0.5) / 12.0);
            } else if (i < 18) {
                x = (i - 11.5) / 12.0;
            } else {
                x = (i - 11.5) / 12.0;
            }
            ArrayList<Piece> column = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                for (Piece piece : all.get(j)) {
                    if (piece.point == i) {
                        column.add(piece);
                    }
                }
            }
            for (int j = 0; j < column.size(); j++) {
                double h = (column.size() < 5)? 0.4 * (j + 0.5) / 4.0: 0.4 * (j + 0.5) / column.size();
                double y = (i < 12)? h: 1 - h;
                StdDraw.setPenColor(column.get(j).color == Turn.BLACK ? new Color(0, 0, 0) : new Color(255, 0, 0));
                StdDraw.filledCircle(x, y, 0.05);
                StdDraw.setPenRadius((column.get(j).selected)? 0.005: 0.002);
                StdDraw.setPenColor((column.get(j).selected)? new Color(150,255,155):new Color(255, 255, 255));
                StdDraw.circle(x, y, 0.05);
                StdDraw.show();
            }
        }
    }

    public int getPoint() {
        return point;
    }

    public Turn getTurn() {
        return color;
    }
    public static Turn getColor(int point) {
        for (Piece piece : blackPieces) {
            if (piece.point == point) {
                return Turn.BLACK;
            }
        }
        for (Piece piece : redPieces) {
            if (piece.point == point) {
                return Turn.RED;
            }
        }
        return null;
    }
    public static int getNum(int point) {
        int num = 0;
        for (ArrayList<Piece> pieces : all) {
            for (Piece piece : pieces) {
                if (piece.point == point) {
                    num++;
                }
            }
        }
        return num;
    }
}
