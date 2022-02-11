public class MiddleMonteCarlo extends MonteCarloAI {
    private final double k = -0.5;

    public MiddleMonteCarlo(int d, double s) {
        super(d, s);
    }

    public double staticEval(ConnectFour game) {
        double midscore = 0;
        int[][] board = game.getBoard();
        if (game.getWinner() != 2) {
            return (double) game.getWinner();
        }
        for (int i = 0; i < 7; i++) {
            for (int j : board[i]) {
                midscore += 1.0 * j / (Math.abs(3 - i) + 1);
            }
        }
        return sigmoid(midscore);
    }

    public double sigmoid(double d) {
        return 2 / (1 + Math.pow(Math.E, k * d)) - 1;
    }

    public int hashCode() {
        int prime = 11;
        int result = 1;
        result += prime * getDepth();
        result += prime * Double.hashCode(getSmoothing());
        return result;
    }
}
