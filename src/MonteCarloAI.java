import java.util.*;
import java.io.*;

public abstract class MonteCarloAI implements ConnectFourAI {
    private int depth;
    private double smoothing;
    private HashMap<KeyWrapper, BoardEval> cache;

    public MonteCarloAI(int d, double s) {
        depth = d;
        smoothing = s;
        try {
            FileInputStream fin = new FileInputStream("cache" + hashCode() + ".ser");
            ObjectInputStream in = new ObjectInputStream(fin);
            cache = (HashMap<KeyWrapper, BoardEval>) in.readObject();
        } catch (Exception e) {
            cache = new HashMap<KeyWrapper, BoardEval>();
        }
    }

    public MonteCarloAI() {
        this(4, 0.5);
    }

    public int findMove(ConnectFour game) {
        Double bestEval = Double.MAX_VALUE * game.getTurn() * -1;
        int bestMove = 0;
        for (int i = 0; i < 7; i++) {
            ConnectFour temp = new ConnectFour(game);
            if (temp.playMove(i)) {
                Double eval = eval(temp, depth, smoothing);
                if (eval.compareTo(bestEval) == game.getTurn()) {
                    bestEval = eval;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }

    /**
     * 
     * @param game      ConnectFour object with the current board state
     * @param d         number of moves into the future to look
     * @param smoothing a decimal from 0 to 1 indicating the weighting of lower
     *                  depth's static evaluation
     * @return an evaluation of the strength of the position, ranging from -1 to 1,
     *         with -1 being strongest for red and 1 being strongest for black
     */

    public double eval(ConnectFour game, int d, double smoothing) {
        int[][] board = game.getBoard();
        KeyWrapper key = new KeyWrapper(board);
        // double cached = 0.0;
        // boolean cacheSol = false;
        if (cache.containsKey(key)) {
            BoardEval cached = cache.get(key);
            if (cached.getDepth() >= d) {
                return cached.getEval();
            }
        }
        if (game.getWinner() != 2) {
            return game.getWinner();
        }
        double inplaceEval = staticEval(game);
        if (d == 0) {
            return inplaceEval;
        }
        ArrayList<Double> evals = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            double smoothingFactor = Math.pow(smoothing, d);
            ConnectFour temp = new ConnectFour(game);
            if (temp.playMove(i)) {
                evals.add(game.getTurn() * (smoothingFactor * inplaceEval + (1 - smoothingFactor)
                        * eval(temp, d - 1, smoothing)));
            }
        }
        Double res = game.getTurn() * Collections.max(evals);
        if (d > depth / 2) {
            cache.put(new KeyWrapper(board), new BoardEval(res, d));
        }
        
        return res;
    }

    public void update() {
        try {
            FileOutputStream file = new FileOutputStream("cache" + hashCode() + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this.cache);
            out.close();
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected int getDepth() {
        return depth;
    }
    
    protected double getSmoothing() {
        return smoothing;
    }

    public abstract double staticEval(ConnectFour game);
}

class KeyWrapper implements Serializable {
    private static final long serialVersionUID = -5489921143589757959L;
    private int[][] board;

    public KeyWrapper(int[][] b) {
        board = b;
    }

    public int[][] getBoard() {
        return board;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public boolean equals(Object o) {
        return Arrays.deepEquals(board, ((KeyWrapper) o).getBoard());
    }
}
