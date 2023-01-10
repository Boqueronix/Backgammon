import java.awt.*;
import java.util.ArrayList;

public class Piece {
    public static ArrayList<Piece> blackPieces = new ArrayList<>(15);
    public static ArrayList<Piece> redPieces = new ArrayList<>(15);
    public static ArrayList<ArrayList<Piece>> all = new ArrayList<>(2);
    private final Turn color;
    private int point;
    private boolean selected = false;
    public Piece(Turn color, int point){
        this.color = color;
        this.point = point;
        if (color == Turn.BLACK) { blackPieces.add(this);} else { redPieces.add(this);}
    }
    public void select(){
        selected = true;
    }
    public void deselect(){
        selected = false;
    }
    public void setPoint(int point){
        this.point = point;
    }
    public Turn getColor(){
        return color;
    }
    public int getPoint(){
        return point;
    }
    public static void draw() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            System.out.println(all.get(i).size());
            for (int j = 1; j < 25; j++) {
                ArrayList<Piece> bar = new ArrayList<>();
                for (Piece p: all.get(i)) {
                    if (p.point == j){
                        bar.add(p);
                        bar.get(bar.size() - 1).select();
                    }
                }
                int col = (j > 12)? j - 12: j;
                double x = (j > 12)? (col - 0.5) / 12.0: 1 - (col - 0.5) / 12.0;
                int y = (j > 12 && i == 0 || j <=12 && i == 1)? 0: 1;
                for (int k = 0; k < bar.size(); k++) {
                    System.out.println(j);
                    StdDraw.setPenColor((i == 0)? Color.BLACK: Color.RED);
                    StdDraw.filledCircle(x, Math.abs(y - 0.4 * (k + 0.7) / bar.size()), 0.05);
                    if (bar.get(i).selected){
                        StdDraw.setPenColor(Color.GREEN);
                        StdDraw.setPenRadius(0.005);
                        StdDraw.circle(x, Math.abs(y - 0.4 * (k + 0.7) / bar.size()), 0.05);
                    } else {
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.setPenRadius(0.002);
                        StdDraw.circle(x, Math.abs(y - 0.4 * (k + 0.7) / bar.size()), 0.05);
                    }
                }
            }
        }
        StdDraw.show();
    }
}
