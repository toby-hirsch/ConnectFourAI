import java.io.Serializable;

public class BoardEval implements Serializable {
    private static final long serialVersionUID = 9139792826099066563L;
    private double eval;
    private int depth;

    public BoardEval() {

    }

    public BoardEval(double d, int i) {
        eval = d;
        depth = i;
    }

    public double getEval() {
        return eval;
    }

    public int getDepth() {
        return depth;
    }
}